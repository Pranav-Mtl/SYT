package com.syt.balram.syt;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.syt.constant.Constant;
import com.syt.util.Configuration;
import com.google.android.gms.analytics.HitBuilders;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class UploadPhoto extends ActionBarActivity {
    Uri imageUri = null;
    ProgressDialog prgDialog;
    String encodedString;
    RequestParams params = new RequestParams();
    String imgPath, fileName;
    Bitmap bitmap;
    private static int RESULT_LOAD_IMG = 1;
    private static int RESULT_camera_image = 100;
    private static int PIC_CROP=10;
    Button cameraBtn;
    Button loadBtn;
    ImageView imgView;
    ImageButton back;
    private Uri fileUri;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);
        cameraBtn= (Button) findViewById(R.id.upload_camera);
        loadBtn= (Button) findViewById(R.id.upload_load_pic);
        imgView = (ImageView) findViewById(R.id.upload_image);
        back= (ImageButton) findViewById(R.id.upload_back);
        prgDialog = new ProgressDialog(this);
        prgDialog.setCancelable(false);

        email= Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.SHARED_PREFERENCE_UserID);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // String fileName = "Camera_Example.jpg";



                //Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
              /* intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 111);*/
               // startActivityForResult(intent,RESULT_camera_image);

                if (getApplicationContext().getPackageManager().hasSystemFeature(
                        PackageManager.FEATURE_CAMERA)) {

                    Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                    File file=getOutputMediaFile(1);
                    imageUri = Uri.fromFile(file); // create
                    i.putExtra(MediaStore.EXTRA_OUTPUT,imageUri); // set the image file

                    startActivityForResult(i,RESULT_camera_image);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_upload_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    // When Image is selected from Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("REQUEST CODE" + requestCode + "RESULT CODE" + resultCode);
        try {
            // When an Image is picked
            if (resultCode == RESULT_OK  && requestCode==RESULT_LOAD_IMG ) {
                // Get the Image from data

                loadBtn.setVisibility(View.VISIBLE);
                imgView.setVisibility(View.VISIBLE);

                System.out.println("Data"+data);


                Uri selectedImage = data.getData();
                System.out.println("GALLERY SELECTED"+selectedImage);
                System.out.println("CROPPED URI"+selectedImage);

                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();

                System.out.println("IMAGE PATH" + imgPath);

                // Set the Image in ImageView
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgPath));


                // Get the Image's file name
                String fileNameSegments[] = imgPath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];
                // Put file name in Async Http Post Param which will used in Php web app
                params.put("filename", fileName);
                params.put("email",email);
                performCrop(selectedImage);

            }

            else if(requestCode==RESULT_camera_image && resultCode == RESULT_OK){
              /*  Toast.makeText(this, "You  picked Image from camera",
                        Toast.LENGTH_LONG).show();*/
                loadBtn.setVisibility(View.VISIBLE);
                imgView.setVisibility(View.VISIBLE);
                System.out.println(imageUri);
               /* String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(imageUri,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();*/
                imgPath=imageUri.toString();
                imgPath=imgPath.replace(imgPath.substring(0,7),"");

                System.out.println("IMAGE PATH" + imgPath);

                // Set the Image in ImageView
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgPath));


                // Get the Image's file name
                String fileNameSegments[] = imgPath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];
                // Put file name in Async Http Post Param which will used in Php web app
                params.put("filename", fileName);
                params.put("email",email);                performCrop(imageUri);

            }
            else if (requestCode == PIC_CROP) {
                if (data != null) {
                    // get the returned data
                    //Uri output=data.getData();

                    Bundle extras = data.getExtras();
                    // get the cropped bitmap
                    Bitmap selectedBitmap = extras.getParcelable("data");

                    //imgView.setImageBitmap(selectedBitmap);
                    Uri uriSelected=getImageUri(UploadPhoto.this,selectedBitmap);
                    System.out.println("CROPPED URI"+uriSelected);

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(uriSelected,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgPath = cursor.getString(columnIndex);
                    cursor.close();

                    System.out.println("IMAGE PATH" + imgPath);

                    // Set the Image in ImageView
                    imgView.setImageBitmap(BitmapFactory
                            .decodeFile(imgPath));


                    // Get the Image's file name
                    String fileNameSegments[] = imgPath.split("/");
                    fileName = fileNameSegments[fileNameSegments.length - 1];
                    // Put file name in Async Http Post Param which will used in Php web app
                    params.put("filename", fileName);
                    params.put("email",email);


                    /*imgPath=uriSelected.toString();
                   // imgPath=imgPath.replace(imgPath.substring(0,7),"");
                    System.out.println("ss------>" + imgPath);
                    imgView.setImageBitmap(BitmapFactory
                            .decodeFile(imgPath));
                    String fileNameSegments[] = imgPath.split("/");
                    fileName = fileNameSegments[fileNameSegments.length - 1];
                    // Put file name in Async Http Post Param which will used in Php web app
                    params.put("filename", fileName);
                    params.put("email", email);*/
                }
            }

            else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    // When Upload button is clicked
    public void uploadImage(View v) {
        // When Image is selected from Gallery
        MyApp.tracker().setScreenName("Upload Image");
        MyApp.tracker().send(new HitBuilders.EventBuilder("Button", "Click")
                .setCategory("UI")
                .setAction("Click")
                .setLabel("Submit")
                .build());

        if (imgPath != null && !imgPath.isEmpty()) {
            prgDialog.setMessage("Loading...");
            prgDialog.show();
            // Convert image to String using Base64
            encodeImagetoString();
            // When Image is not selected from Gallery
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "You must select image from gallery before you try to upload",
                    Toast.LENGTH_LONG).show();
        }
    }

    // AsyncTask - To convert Image to String
    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            }

            ;

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imgPath,
                        options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                prgDialog.setMessage("loading...");
                // Put converted Image string into Async Http Post param
                params.put("image", encodedString);
                // Trigger Image upload
                System.out.println("tttttttttttttttttt" + encodedString);
                triggerImageUpload();
            }
        }.execute(null, null, null);
    }

    public void triggerImageUpload() {
        makeHTTPCall();
    }

    // http://192.168.2.4:9000/imgupload/upload_image.php
    // http://192.168.2.4:9999/ImageUploadWebApp/uploadimg.jsp
    // Make Http call to upload Image to Php server
    public void makeHTTPCall() {
        prgDialog.setMessage("Loading...");
        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.
        client.post("https://sellyourtime.in/api/upload_image.php",
                params, new AsyncHttpResponseHandler() {

                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        prgDialog.hide();
                        startActivity(new Intent(getApplicationContext(),HomeScreen.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        Toast.makeText(getApplicationContext(),"Image uploaded successfully",
                                Toast.LENGTH_LONG).show();
                    }

                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        // Hide Progress Dialog
                        prgDialog.hide();
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(),
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
                                            + statusCode, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // Dismiss the progress bar when application is closed
        if (prgDialog != null) {
            prgDialog.dismiss();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (imageUri != null) {
            outState.putString("cameraImageUri", imageUri.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("cameraImageUri")) {
            imageUri = Uri.parse(savedInstanceState.getString("cameraImageUri"));
        }
    }

    /** Create a File for saving an image */
    private File getOutputMediaFile(int type){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "SellYourTime");

        /**Create the storage directory if it does not exist*/
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        /**Create a media file name*/
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    private void performCrop(Uri picUri) {
        try {

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 150);
            cropIntent.putExtra("outputY", 150);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
