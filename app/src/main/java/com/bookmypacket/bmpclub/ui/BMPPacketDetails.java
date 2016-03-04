package com.bookmypacket.bmpclub.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bookmypacket.bmpclub.R;
import com.bookmypacket.bmpclub.dto.AcceptPacketRequest;
import com.bookmypacket.bmpclub.dto.AcceptPacketResponse;
import com.bookmypacket.bmpclub.dto.BMPPacket;
import com.bookmypacket.bmpclub.utils.SharedPrefrenceManager;
import com.bookmypacket.bmpclub.utils.retro.PacketDeliveryAccept;
import com.bookmypacket.bmpclub.utils.retro.ServiceGenerator;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.text.TextUtils.isEmpty;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.bookmypacket.bmpclub.utils.AppConstants.BundleExtraKeys.PACKET;
import static com.bookmypacket.bmpclub.utils.AppConstants.RS_SYMBOL;

public class BMPPacketDetails extends BaseActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmppacket_details);
        BMPPacket packet = (BMPPacket) getIntent().getSerializableExtra(PACKET);
        getSupportActionBar().setTitle(getString(R.string.title_awb) + packet.getaWBNo());
        popolateFields(packet);
    }

    private void popolateFields(final BMPPacket packet)
    {
        TextView earning = (TextView) findViewById(R.id.tv_earning_amount);
        earning.setText(RS_SYMBOL + packet.getEarnings());
        TextView sizeView = (TextView) findViewById(R.id.tv_dimensions);
        sizeView.setText(packet.getLength() + " X " + packet.getWidth() + " X " + packet
                .getHeight());
        TextView weightView = (TextView) findViewById(R.id.tv_weight);
        weightView.setText(packet.getWeight());
        TextView addressView = (TextView) findViewById(R.id.tv_address);
        addressView.setText(packet.getDeliveryAddress());
        TextView pinCodeTxt = (TextView) findViewById(R.id.tv_pincode);
        pinCodeTxt.setText(packet.getToCityPin());
        View cod = findViewById(R.id.ll_cod);
        if (packet.isCod())
        {
            cod.setVisibility(VISIBLE);
            TextView codView = (TextView) findViewById(R.id.tv_cod);
            codView.setText(RS_SYMBOL + packet.getCodAmount());
        } else
        {
            cod.setVisibility(GONE);
        }
        TextView btnPick    = (TextView) findViewById(R.id.btn_pick);
        TextView btnRelease = (TextView) findViewById(R.id.btn_release);
        TextView btnCallCustomer = (TextView) findViewById(R.id.btn_call);
        btnCallCustomer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                callCustomer(packet);
            }
        });
        TextView btnDeliver = (TextView) findViewById(R.id.btn_deliver);
        btnDeliver.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(BMPPacketDetails.this, BMPDeliveryActivity.class);
                i.putExtra(PACKET, packet);
                startActivity(i);
                finish();
            }
        });
        TextView btnAccept = (TextView) findViewById(R.id.btn_accept);
        if ("PACKET_DELIVERED".equalsIgnoreCase(packet.getStatus()))
        {
            btnAccept.setVisibility(GONE);
            btnCallCustomer.setVisibility(GONE);
            btnDeliver.setVisibility(GONE);
            btnPick.setVisibility(GONE);
            btnRelease.setVisibility(GONE);
        } else if ("PACKET_DELIVERY_ACCEPTED".equalsIgnoreCase(packet.getStatus()))
        {
            btnAccept.setVisibility(GONE);
            btnCallCustomer.setVisibility(GONE);
            btnDeliver.setVisibility(GONE);
        } else if ("PACKET_PICKED_FOR_DELIVERY".equalsIgnoreCase(packet.getStatus()))
        {
            btnAccept.setVisibility(GONE);
            btnPick.setVisibility(GONE);
            btnRelease.setVisibility(GONE);
        } else
        {
            btnCallCustomer.setVisibility(GONE);
            btnDeliver.setVisibility(GONE);
            btnPick.setVisibility(GONE);
            btnRelease.setVisibility(GONE);
        }
        btnAccept.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                acceptPacket(packet, "PACKET_DELIVERY_ACCEPTED");
            }
        });
        btnPick.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                acceptPacket(packet, "PACKET_PICKED_FOR_DELIVERY");
            }
        });
        btnRelease.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                acceptPacket(packet, "PACKET_DELIVERY_RELEASED");
            }
        });


    }

    private void callCustomer(BMPPacket packet)
    {
        if (!isEmpty(packet.getDeliveryCustomerMobileNo()))
        {
            Intent in = new Intent(Intent.ACTION_CALL,
                                   Uri.parse("tel:" + packet.getDeliveryCustomerMobileNo()));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=
                    PackageManager.PERMISSION_GRANTED)
            {
                makeText(this, R.string.phone_permission, Toast.LENGTH_LONG).show();
                return;
            }
            try
            {
                startActivity(in);
            }

            catch (android.content.ActivityNotFoundException ex)
            {
                makeText(getApplicationContext(), R.string.phone_error, LENGTH_LONG).show();
            }
        } else
        {
            makeText(this, R.string.phone_not_available, LENGTH_LONG).show();
        }
    }

    private void acceptPacket(BMPPacket packet, String status)
    {
        progressDialog.show();
        String              autHeader = SharedPrefrenceManager.getAuthHeader(this);
        String              mobile    = SharedPrefrenceManager.getMobileNo(this);
        AcceptPacketRequest request   = new AcceptPacketRequest();
        request.setAssignedTo(mobile);
        request.setPacketId(packet.getPacketId());
        request.setAcceptanceStatus(status);
        PacketDeliveryAccept service =
                ServiceGenerator.createService(PacketDeliveryAccept.class);
        Call<AcceptPacketResponse> call = service.acceptPacket(autHeader, request);
        call.enqueue(new Callback<AcceptPacketResponse>()
        {
            @Override
            public void onResponse(Response<AcceptPacketResponse> response, Retrofit retrofit)
            {
                progressDialog.dismiss();
                if (response.body().getSuccess())
                {
                    makeText(BMPPacketDetails.this, R.string.packet_status_update,
                             LENGTH_LONG).show();
                    finish();
                } else
                {
                    makeText(BMPPacketDetails.this, response.body().getErrorMessage(),
                             LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t)
            {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bmp, menu);
        return true;
    }
}
