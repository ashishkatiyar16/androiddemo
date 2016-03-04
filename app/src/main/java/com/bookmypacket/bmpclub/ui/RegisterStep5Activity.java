package com.bookmypacket.bmpclub.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookmypacket.bmpclub.R;
import com.bookmypacket.bmpclub.dto.RegistrationResponse;
import com.bookmypacket.bmpclub.utils.retro.ImageUploadService;
import com.bookmypacket.bmpclub.utils.retro.ServiceGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

import static android.content.Intent.ACTION_PICK;
import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.bookmypacket.bmpclub.utils.AppConstants.BundleExtraKeys.REGISTRATION_DTO;
import static com.bookmypacket.bmpclub.utils.AppConstants.BundleExtraKeys.REGISTRATION_RESPONSE_DTO;

public class RegisterStep5Activity extends BaseActivity
{
    private static final int SELFIE_REQUET         = 2000;
    private static final int ADDRESS_REQUET        = 2001;
    private static final int TAX_REQUET            = 2002;
    private static final int ID_REQUET             = 2003;
    private static final int CHEQUE_REQUEST        = 2004;
    private static final int SELECT_SELFIE_REQUET  = 3000;
    private static final int SELECT_ADDRESS_REQUET = 3001;
    private static final int SELECT_TAX_REQUET     = 3002;
    private static final int SELECT_ID_REQUET      = 3003;
    private static final int SELECT_CHEQUE_REQUEST = 3004;
    private TextView  btnCongtinue;
    private TextView  btnTaxId;
    private TextView  btnAddressProof;
    private TextView  btnIdProof;
    private TextView  btnSelfie;
    private TextView  btnCheque;
    private ImageView ivTaxId;
    private ImageView ivAddressProof;
    private ImageView ivIdProof;
    private ImageView ivSelfie;
    private ImageView ivCheque;
    private String    currentPhotoPath;
    private Uri       idProofURI;
    private Uri       taxIdUri;
    private Uri       selfieUri;
    private Uri       addressUri;
    private Uri       chequeUri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step5);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        registerFab();
        initIds();
        setActions();
    }

    private void setActions()
    {
        btnTaxId.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                bindImageAction(R.string.title_taxId, TAX_REQUET, SELECT_TAX_REQUET);
                //startCamera(TAX_REQUET);
            }
        });
        btnSelfie.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                bindImageAction(R.string.title_selfie, SELFIE_REQUET, SELECT_SELFIE_REQUET);
                //startCamera(SELFIE_REQUET);
            }
        });
        btnAddressProof.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                bindImageAction(R.string.title_address_proof, ADDRESS_REQUET,
                                SELECT_ADDRESS_REQUET);
                //startCamera(ADDRESS_REQUET);
            }
        });
        btnIdProof.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                bindImageAction(R.string.title_idproof, ID_REQUET, SELECT_ID_REQUET);
                //startCamera(ID_REQUET);
            }
        });
        btnCheque.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                bindImageAction(R.string.title_cheque, CHEQUE_REQUEST, SELECT_CHEQUE_REQUEST);
                // startCamera(CHEQUE_REQUEST);
            }
        });
       ivTaxId.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               bindImageAction(R.string.title_taxId, TAX_REQUET, SELECT_TAX_REQUET);
           }
       });
        ivAddressProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindImageAction(R.string.title_address_proof, ADDRESS_REQUET,
                        SELECT_ADDRESS_REQUET);
            }
        });
        ivIdProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindImageAction(R.string.title_idproof, ID_REQUET, SELECT_ID_REQUET);
            }
        });
        ivSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindImageAction(R.string.title_selfie, SELFIE_REQUET, SELECT_SELFIE_REQUET);
            }
        });
        ivCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindImageAction(R.string.title_cheque, CHEQUE_REQUEST, SELECT_CHEQUE_REQUEST);
            }
        });
        btnCongtinue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (validForm())
                {
                    uploadImageWithRetro();
                } else
                {
                    makeText(RegisterStep5Activity.this, R.string.toast_documents, LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private void initIds()
    {
        btnCongtinue = (TextView) findViewById(R.id.btn_continue);
        btnAddressProof = (TextView) findViewById(R.id.btn_addressProof);
        btnIdProof = (TextView) findViewById(R.id.btn_idProof);
        btnSelfie = (TextView) findViewById(R.id.btn_selfie);
        btnTaxId = (TextView) findViewById(R.id.btn_idTax);
        btnCheque = (TextView) findViewById(R.id.btn_id_cheque);
        ivAddressProof = (ImageView) findViewById(R.id.iv_addressProof);
        ivIdProof = (ImageView) findViewById(R.id.iv_idProof);
        ivSelfie = (ImageView) findViewById(R.id.iv_selfie);
        ivTaxId = (ImageView) findViewById(R.id.iv_idTax);
        ivCheque = (ImageView) findViewById(R.id.iv_id_cheque);
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
            // cameraIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
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
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            photo.recycle();

            switch (requestCode)
            {
                case ADDRESS_REQUET:
                    addressUri = setImageRes(ivAddressProof);//.setImageURI(image);
                    break;
                case ID_REQUET:
                    idProofURI = setImageRes(ivIdProof);//, image);//.setImageURI(image);
                    // idProofURI = image;
                    break;
                case TAX_REQUET:
                    taxIdUri = setImageRes(ivTaxId);//, image);//.setImageURI(image);
                    //taxIdUri = image;
                    break;
                case SELFIE_REQUET:
                    selfieUri = setImageRes(ivSelfie);//, image);//.setImageURI(image);
                    //selfieUri = image;
                    break;
                case CHEQUE_REQUEST:
                    //setImageRes(ivCheque, image);//.setImageURI(image);
                    chequeUri = setImageRes(ivCheque);
                    break;
                case SELECT_ADDRESS_REQUET:
                    addressUri = getSelectedImage(ivAddressProof, data);
                    break;
                case SELECT_CHEQUE_REQUEST:
                    chequeUri = getSelectedImage(ivCheque, data);
                    break;
                case SELECT_ID_REQUET:
                    idProofURI = getSelectedImage(ivIdProof, data);
                    break;
                case SELECT_SELFIE_REQUET:
                    selfieUri = getSelectedImage(ivSelfie, data);
                    break;
                case SELECT_TAX_REQUET:
                    taxIdUri = getSelectedImage(ivTaxId, data);
                    break;
            }
        }
    }

    private Uri getSelectedImage(ImageView iv, Intent data)
    {
        Uri      selectedImageUri = data.getData();
        String[] projection       = {DATA};
        CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                                                     null);
        Cursor cursor       = cursorLoader.loadInBackground();
        int    column_index = cursor.getColumnIndexOrThrow(DATA);
        cursor.moveToFirst();
        String selectedImagePath = cursor.getString(column_index);
        Uri    image             = Uri.parse(selectedImagePath);
        setImageRes(iv, image);
        return image;

    }

    private Uri setImageRes(final ImageView iv)
    {
        final Uri image = Uri.parse(currentPhotoPath);
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Glide.with(RegisterStep5Activity.this).load(image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(iv);
                //  Picasso.with(RegisterStep5Activity.this).load(currentPhotoPath).into(iv);
                //  iv.setImageURI(Uri.parse(currentPhotoPath));

            }
        });
        return image;
    }

    private Uri setImageRes(final ImageView iv, final Uri image)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                iv.setImageURI(image);
                //  Glide.with(RegisterStep5Activity.this).load(image).into(iv);
                //     .diskCacheStrategy(DiskCacheStrategy.ALL).into(iv);
                //  Picasso.with(RegisterStep5Activity.this).load(currentPhotoPath).into(iv);
                //  iv.setImageURI(Uri.parse(currentPhotoPath));

            }
        });
        return image;
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


    private void uploadImageWithRetro()
    {
        RegistrationResponse resp = (RegistrationResponse) getIntent().getSerializableExtra(
                REGISTRATION_RESPONSE_DTO);

        final ImageUploadService service = ServiceGenerator.createService(ImageUploadService.class);
        progressDialog.setMessage(getString(R.string.pd_registration_images_message));
        progressDialog.show();
        Call<RegistrationResponse> call =
                service.upload(createImageRequest(chequeUri), createImageRequest(addressUri),
                               createImageRequest(idProofURI), createImageRequest(selfieUri),
                               createImageRequest(taxIdUri), resp.getProfileId().trim());
        call.enqueue(new Callback<RegistrationResponse>()
        {
            @Override
            public void onResponse(retrofit.Response<RegistrationResponse> response,
                                   Retrofit retrofit1)
            {
                progressDialog.dismiss();
                RegistrationResponse resp = response.body();
                if (resp.getSuccess())
                {
                    Intent i = new Intent(RegisterStep5Activity.this, MobileNumberVerifyActivity
                            .class);
                    i.putExtra(REGISTRATION_RESPONSE_DTO, resp);
                    i.putExtra(REGISTRATION_DTO, getIntent()
                            .getSerializableExtra(REGISTRATION_DTO));
                    startActivity(i);
                } else
                {
                    makeText(RegisterStep5Activity.this, resp.getErrorMessage(),
                             LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t)
            {
                t.printStackTrace();
                progressDialog.dismiss();
                makeText(RegisterStep5Activity.this, R.string.json_error,
                         LENGTH_LONG).show();
            }
        });

    }


    private RequestBody createImageRequest(Uri bmpUri)
    {
        RequestBody req = RequestBody.create(MediaType.parse("multipart/form-data"), new File
                (bmpUri.getPath()));
        return req;
    }


    private boolean validForm()
    {
        return !(idProofURI == null || taxIdUri == null || addressUri == null ||
                         selfieUri == null ||
                         chequeUri == null);
    }

    private void selectExistingImage(int title, int requetCode)
    {
        Intent i = new Intent(ACTION_PICK, EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, getString(title)), requetCode);
    }

    private void bindImageAction(final int title, final int cameraRequest, final int libraryRequest)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setItems(R.array.array_image_actions,
                                         new DialogInterface.OnClickListener()
                                         {
                                             @Override
                                             public void onClick(DialogInterface dialogInterface,
                                                                 int i)
                                             {
                                                 dialogInterface.dismiss();
                                                 if (i == 0)
                                                 {
                                                     startCamera(cameraRequest);
                                                 } else if (i == 1)
                                                 {
                                                     selectExistingImage(title, libraryRequest);
                                                 }
                                             }
                                         });
        builder.setNegativeButton(R.string.alert_img_btn_cancel,
                                  new DialogInterface.OnClickListener()
                                  {
                                      @Override
                                      public void onClick(DialogInterface dialogInterface, int i)
                                      {
                                          dialogInterface.dismiss();
                                      }
                                  });
        builder.create().show();
    }
}
