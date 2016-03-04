package com.bookmypacket.bmpclub.ui.frags;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bookmypacket.bmpclub.R;
import com.bookmypacket.bmpclub.dto.AcceptPacketRequest;
import com.bookmypacket.bmpclub.dto.AcceptPacketResponse;
import com.bookmypacket.bmpclub.dto.BMPPacket;
import com.bookmypacket.bmpclub.dto.UpdatePacketStatusRequest;
import com.bookmypacket.bmpclub.dto.UpdatePacketStatusResponse;
import com.bookmypacket.bmpclub.ui.BMPDeliveryActivity;
import com.bookmypacket.bmpclub.ui.BaseActivity;
import com.bookmypacket.bmpclub.utils.SharedPrefrenceManager;
import com.bookmypacket.bmpclub.utils.retro.PacketDeliveryAccept;
import com.bookmypacket.bmpclub.utils.retro.ServiceGenerator;
import com.bookmypacket.bmpclub.utils.retro.UpdatePacketStatusService;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.bookmypacket.bmpclub.utils.AppConstants.BundleExtraKeys.PACKET;
import static com.bookmypacket.bmpclub.utils.AppConstants.RS_SYMBOL;

/**
 * {@link RecyclerView.Adapter} that can display a {} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BMPPacketProgressRecyclerViewAdapter extends BMPRecyclerViewAdapter
{


    public BMPPacketProgressRecyclerViewAdapter(
            OnListFragmentInteractionListener mListener,
            List<BMPPacket> mValues)
    {
        super(mListener, mValues);
    }

    @Override
    public RecyclerView.ViewHolder customViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_bmppacketprogress, parent, false);
        return new BMPProgressViewHolder(view);
    }

    @Override
    protected int emptyString()
    {
        return R.string.empty_bmp_packets;
    }

    @Override
    public void customBindViewHolder(final RecyclerView.ViewHolder h, int position)
    {
        if (mValues.size() > 0)
        {
            final BMPProgressViewHolder holder = (BMPProgressViewHolder) h;
            holder.mItem = mValues.get(position);
            final BMPPacket packet = mValues.get(position);
            holder.mIdView.setText(RS_SYMBOL + packet.getEarnings());
            holder.mContentView.setText(
                    packet.getLength() + " X " + packet.getWidth() + " X " + packet
                            .getHeight());
            holder.mDelivery.setText(packet.getDeliveryAddress() + " " + packet.getToCityPin());
            holder.mWeight.setText(holder.mItem.getWeight());
            if (packet.isCod())
            {
                holder.mRootView.setBackgroundResource(R.color.colorCOD);
            }

            holder.mStatus.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    acceptPacket(packet);
                }
            });


            holder.mDeliverBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    BaseActivity ctx = (BaseActivity) mListener;
                    Intent       i   = new Intent(ctx, BMPDeliveryActivity.class);
                    i.putExtra(PACKET, packet);
                    ctx.startActivity(i);
                }
            });
            holder.mRootView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            });
            if (packet.getStatus().equalsIgnoreCase("PACKET_DELIVERY_ACCEPTED"))
            {
                holder.releaseView.setVisibility(VISIBLE);
                holder.deliverView.setVisibility(GONE);
            } else if (packet.getStatus().equalsIgnoreCase("PACKET_PICKED_FOR_DELIVERY"))
            {
                holder.releaseView.setVisibility(GONE);
                holder.deliverView.setVisibility(VISIBLE);
            }
            if (packet.getStatus().equalsIgnoreCase("PACKET_UNDELIVERED"))
            {
                holder.mStatus.setText(R.string.btn_attempt_text);
            }
            holder.btnPick.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    pickPacket(packet, "PACKET_PICKED_FOR_DELIVERY");
                }
            });
            holder.btnRelease.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    releasePacket(packet);
                }
            });

        }
    }

    private void acceptPacket(final BMPPacket packet)
    {
        final BaseActivity ctx = (BaseActivity) mListener;
        // ctx.getProgressDialog().show();
        String              autHeader = SharedPrefrenceManager.getAuthHeader(ctx);
        String              mobile    = SharedPrefrenceManager.getMobileNo(ctx);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(R.string.select_status)
                .setItems(R.array.packt_status, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(
                            DialogInterface dialogInterface,
                            int i)
                    {
                        dialogInterface.dismiss();
                        String[] statuses = ctx.getResources().getStringArray(R.array.packt_status);
                        String   value    = statuses[i];
                        updateStatus(packet, value);

                    }
                });
        builder.create().show();
    }

    private void releasePacket(final BMPPacket packet)
    {
        pickPacket(packet, "PACKET_DELIVERY_RELEASED");
    }

    private void pickPacket(final BMPPacket packet, String status)
    {
        final BaseActivity ctx = (BaseActivity) mListener;
        ctx.getProgressDialog().show();
        String              autHeader = SharedPrefrenceManager.getAuthHeader(ctx);
        String              mobile    = SharedPrefrenceManager.getMobileNo(ctx);
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
                ctx.getProgressDialog().dismiss();
                if (response.body() != null && response.body().getSuccess())
                {
                    makeText(ctx, R.string.packet_accepted, LENGTH_LONG).show();
                } else
                {
                    makeText(ctx, response.body().getErrorMessage(), LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t)
            {
                ctx.getProgressDialog().dismiss();
            }
        });
    }

    private void updateStatus(BMPPacket p, String value)
    {
        final BaseActivity ctx = (BaseActivity) mListener;
        Location           l   = ctx.getLocation();
        if (l == null)
        {
            return;
        }
        ctx.getProgressDialog().show();
        String                    autHeader = SharedPrefrenceManager.getAuthHeader(ctx);
        String                    mobile    = SharedPrefrenceManager.getMobileNo(ctx);
        UpdatePacketStatusRequest request   = new UpdatePacketStatusRequest();
        request.setAssignedTo(mobile);
        request.setPacketSubStatus(value);
        request.setPacketId(p.getPacketId());
        request.setLattitude("" + l.getLatitude());
        request.setLongitute("" + l.getLongitude());
        UpdatePacketStatusService service = ServiceGenerator.createService
                (UpdatePacketStatusService.class);
        Call<UpdatePacketStatusResponse> call = service.update(autHeader, request);
        call.enqueue(new Callback<UpdatePacketStatusResponse>()
        {
            @Override
            public void onResponse(Response<UpdatePacketStatusResponse> response, Retrofit retrofit)
            {
                ctx.getProgressDialog().dismiss();
                if (response.body() != null && response.body().isSuccess())
                {
                    makeText(ctx, R.string.status_marked, LENGTH_LONG).show();
                } else if (response.body() != null)
                {
                    makeText(ctx, response.body().getErrorMessage(), LENGTH_LONG).show();
                } else
                {
                    makeText(ctx, R.string.json_error, LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t)
            {
                ctx.getProgressDialog().dismiss();
            }
        });
    }

    public class BMPProgressViewHolder extends RecyclerView.ViewHolder
    {
        public final View      mView;
        public final TextView  mIdView;
        public final TextView  mContentView;
        public final TextView  mDelivery;
        public final TextView  mStatus;
        public final TextView  mDeliverBtn;
        public final TextView mWeight;
        public final View     mRootView;
        public final View     releaseView;
        public final View     deliverView;
        public final View     btnRelease;
        public final View     btnPick;
        public       BMPPacket mItem;

        public BMPProgressViewHolder(View view)
        {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.packet_size);
            mDelivery = (TextView) view.findViewById(R.id.tv_delivery);
            mStatus = (TextView) view.findViewById(R.id.btn_update);
            mDeliverBtn = (TextView) view.findViewById(R.id.btn_deliver);
            mWeight = (TextView) view.findViewById(R.id.packet_weight);
            mRootView = view.findViewById(R.id.card_view);
            releaseView = view.findViewById(R.id.ll_tobepicked);
            btnRelease = view.findViewById(R.id.btn_release);
            btnPick = view.findViewById(R.id.btn_pick);
            deliverView = view.findViewById(R.id.ll_picked);
        }

        @Override
        public String toString()
        {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
