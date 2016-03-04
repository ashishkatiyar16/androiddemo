package com.bookmypacket.bmpclub.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bookmypacket.bmpclub.R;
import com.bookmypacket.bmpclub.dto.BMPPacket;
import com.bookmypacket.bmpclub.utils.SharedPrefrenceManager;
import com.bookmypacket.bmpclub.utils.retro.DeliverPacketService;
import com.bookmypacket.bmpclub.utils.retro.ServiceGenerator;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.text.TextUtils.isEmpty;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_LONG;
import static com.bookmypacket.bmpclub.R.string.delivery_cod;
import static com.bookmypacket.bmpclub.utils.AppConstants.BundleExtraKeys.PACKET;
import static com.bookmypacket.bmpclub.utils.AppConstants.RS_SYMBOL;

public class BMPDeliveryActivity extends BaseActivity
{
    private final int ID_REQUEST = 1000;
    private BMPPacket          bmpPacket;
    private AppCompatImageView imageView;
    private TextView           btnSubmit;
    private EditText           etIdProof;
    private TextView           btnTakePicture;
    private Spinner            idProofType;
    private String             currentPhotoPath;
    private EditText codeAmount;
    private EditText etPIN;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmpdelivery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bmpPacket = (BMPPacket) getIntent().getSerializableExtra(PACKET);
        getSupportActionBar().setTitle("AWB: " + bmpPacket.getaWBNo());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intViews();
        addActions();
        ArrayAdapter<CharSequence> taxtTypeAdapter = ArrayAdapter.createFromResource(this,
                                                                                     R.array.id_proof_types,
                                                                                     android.R
                                                                                             .layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        taxtTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        idProofType.setAdapter(taxtTypeAdapter);
    }

    private void intViews()
    {
        btnSubmit = (TextView) findViewById(R.id.btn_deliver);
        btnTakePicture = (TextView) findViewById(R.id.btn_document);
        idProofType = (Spinner) findViewById(R.id.sp_taxType);
        imageView = (AppCompatImageView) findViewById(R.id.iv_idTax);
        etIdProof = (EditText) findViewById(R.id.et_idProofNo);

        codeAmount = (EditText) findViewById(R.id.et_cod);
        etPIN = (EditText) findViewById(R.id.et_otp);
        TextInputLayout til = (TextInputLayout) findViewById(R.id.til_cod);
        if (bmpPacket.isCod())
        {
            til.setHint(getString(delivery_cod) + RS_SYMBOL + bmpPacket.getCodAmount());
            til.setVisibility(VISIBLE);
        }
    }

    private RequestBody createImageRequest(Uri bmpUri)
    {
        RequestBody req = RequestBody.create(MediaType.parse("multipart/form-data"), new File
                (bmpUri.getPath()));
        return req;
    }

    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp     = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
                                        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void addActions()
    {
        btnTakePicture.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startCamera(ID_REQUEST);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (validateForm())
                {
                    markDeliver();
                }
            }
        });
    }

    private void markDeliver()
    {
        Location l = getLocation();
        if (l == null)
        {
            return;
        }
        progressDialog.show();
        String authHeader =
                SharedPrefrenceManager.getAuthHeader(BMPDeliveryActivity.this);
        String mobileNo =
                SharedPrefrenceManager.getMobileNo(BMPDeliveryActivity.this);
        Map<String, RequestBody> partMap = new HashMap<>();
        if (currentPhotoPath != null)
        {
            RequestBody requestBody = createImageRequest(Uri.parse(currentPhotoPath));
            partMap.put("file\"; filename=\"idProof.jpg", requestBody);
        }
        DeliverPacketService service =
                ServiceGenerator.createService(DeliverPacketService.class);
        Call<JSONObject> call = service.deliver(authHeader, partMap, bmpPacket.getPacketId(),
                                                idProofType.getSelectedItem().toString(),
                                                etIdProof.getText().toString(),
                                                "" + l.getLatitude(),
                                                "" + l.getLongitude(),
                                                "PACKET_DELIVERED", mobileNo);
        call.enqueue(new Callback<JSONObject>()
        {
            @Override
            public void onResponse(Response<JSONObject> response, Retrofit retrofit)
            {
                progressDialog.dismiss();
                Log.d("BMPDelivery", response.body().toString());
                finish();
            }

            @Override
            public void onFailure(Throwable t)
            {
                progressDialog.dismiss();
                Toast.makeText(BMPDeliveryActivity.this, "Unknown Error", LENGTH_LONG).show();
            }
        });
    }

    private void startCamera(int requestId)
    {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File   photoFile    = null;
        try
        {
            photoFile = createImageFile();
        }
        catch (IOException ex)
        {
            // Error occurred while creating the File
        }
        // Continue only if the File was successfully created
        if (photoFile != null)
        {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                  Uri.fromFile(photoFile));
            cameraIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024 * 128);
            startActivityForResult(cameraIntent, requestId);
        }
        //startActivityForResult(cameraIntent, requestId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            //  Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri image = Uri.parse(currentPhotoPath);
            switch (requestCode)
            {
                case ID_REQUEST:
                    imageView.setImageURI(image);
                    break;
            }
        }
    }

    private boolean validateForm()
    {
        boolean allowed = true;
        if (bmpPacket.isCod())
        {
            if (isEmpty(codeAmount.getText()))
            {
                allowed = false;
                String codToast = getString(R.string.cod_required) + bmpPacket.getCodAmount();
                renderToast(codToast);
                return allowed;
            } else
            {
                double totalCOD = isEmpty(bmpPacket.getCodAmount()) ? 0.00 : Double.parseDouble
                        (bmpPacket.getCodAmount());
                double collectedAmount = Double.parseDouble(codeAmount.getText().toString());
                if (totalCOD > collectedAmount)
                {
                    allowed = false;
                    String toastStr =
                            getString(R.string.cod_required) + totalCOD;
                    renderToast(toastStr);
                    return allowed;
                }
            }
        }
        if (isEmpty(etIdProof.getText()))
        {
            allowed = false;
            renderToast(getString(R.string.customer_id_required));
            return allowed;

        }
        if (idProofType.getSelectedItemId() < 1)
        {
            allowed = false;
            renderToast(getString(R.string.customer_id_type_required));
            return allowed;
        }
        if (isEmpty(currentPhotoPath))
        {
            allowed = false;
            renderToast(getString(R.string.customer_photo_required));
            return allowed;
        }
        if (isEmpty(etPIN.getText()) && etPIN.getText().length() < 5)
        {
            allowed = false;
            showCODDialog(R.string.alert_submit_without_otp);
            return allowed;
        }
        return allowed;
    }

    private void renderToast(String res)
    {
        Toast.makeText(this, res, LENGTH_LONG).show();
    }

    private void showCODDialog(int message)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.title_cod_alert);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setNegativeButton(R.string.btn_cancel_text,
                                             new DialogInterface.OnClickListener()
                                             {
                                                 @Override
                                                 public void onClick(
                                                         DialogInterface dialogInterface,
                                                         int i)
                                                 {
                                                     dialogInterface.dismiss();
                                                 }
                                             });
        alertDialogBuilder.setPositiveButton(R.string.btn_ok_text,
                                             new DialogInterface.OnClickListener()
                                             {
                                                 @Override
                                                 public void onClick(
                                                         DialogInterface dialogInterface,
                                                         int i)
                                                 {
                                                     dialogInterface.dismiss();
                                                     markDeliver();
                                                 }
                                             });
        alertDialogBuilder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bmp, menu);
        return true;
    }
}
