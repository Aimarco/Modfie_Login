package com.example.furwin.modfie_login.Errores.Photos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.furwin.modfie_login.Errores.Login_class.Login_Class;
import com.example.furwin.modfie_login.Errores.UriHandler;
import com.example.furwin.modfie_login.Manifest;
import com.example.furwin.modfie_login.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Handler;

/**
 * Created by FurWin on 30/03/2016.
 */

//IMPROVE CON MULTIPARVOLLEY


public class Photo_Upload extends AppCompatActivity implements View.OnClickListener,ActivityCompat.OnRequestPermissionsResultCallback {
    private Button buttonChoose;
    private Button buttonUpload;
    private ImageView imageView;
    private int idalbum;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private String Path,token;
    private Intent intent,rtrnalbum;
    private ServiceGenerator svg;
    private Login_Class login;
    ProgressDialog pd;
    private Bitmap bm;
    private UriHandler urihandler=new UriHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image);
        rtrnalbum=new Intent(this,ScreenFotos.class);
        svg=new ServiceGenerator();
        pd=new ProgressDialog(Photo_Upload.this);
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        intent=getIntent();
        token=intent.getExtras().getString("token");
        idalbum=intent.getExtras().getInt("album");
        login=new Login_Class();
        login=(Login_Class) intent.getExtras().getSerializable("Datos");

        imageView = (ImageView) findViewById(R.id.UploadPhoto);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

        if(intent.getExtras().getString("from").equals("camera")){
           bm=StringToBitMap(intent.getExtras().getString("imagen"));
            imageView.setImageBitmap(bm);
        }else{
            //nothing
        }

    }



    public File BitmapToFile(Bitmap bm){
        File f = new File(this.getCacheDir(), "image");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

//Convert bitmap to byte array
        Bitmap bitmap =bm;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();


        try {
//write the byts in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return f;
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK) {
            ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Fetching image");
            pd.setTitle("Fetching...");
            pd.show();
            handleGalleryResult(data);
            pd.dismiss();
        }
    }
    @Override
    public void onClick(View v) {
        if (v == buttonChoose)
            isStoragePermissionGranted();
        if (v == buttonUpload) {
            v.setEnabled(false);
                uploadImage();
        }
    }
    public void uploadImage(){
        if(imageView.getDrawable()==null)
            Toast.makeText(Photo_Upload.this, "Debes seleccionar una foto antes.", Toast.LENGTH_SHORT).show();
        else{
            svg.setAlbumid(idalbum);
            svg.setToken(token);
            svg.setContext(this);
        svg.setContext(Photo_Upload.this);
        if(intent.getExtras().getString("from").equals("camera")){
        File fcam=BitmapToFile(bm);
            svg.setAlbumid(idalbum);
            svg.setToken(token);
            svg.setContext(this);
            svg.uploadFile(fcam);
            rtrnalbum.putExtra("idalbum", idalbum);
            rtrnalbum.putExtra("idfrom", "Upload");
            rtrnalbum.putExtra("token", token);
            rtrnalbum.putExtra("Datos",login);

            int secs=1;

            Utils.delay(secs, new Utils.DelayCallback() {
                @Override
                public void afterDelay() {
                    startActivity(rtrnalbum);
                    finish();

                }
            });
        }
            else{
            pd.setMessage("Uploading photo...");
            pd.show();
            File ftlf=BitmapToFile(bitmap);
            svg.uploadFile(ftlf);
            rtrnalbum.putExtra("idalbum", idalbum);
            rtrnalbum.putExtra("idfrom", "Upload");
            rtrnalbum.putExtra("token", token);
            rtrnalbum.putExtra("Datos",login);

            int secs=1;

            Utils.delay(secs, new Utils.DelayCallback() {
                @Override
                public void afterDelay() {
                    pd.dismiss();
                    startActivity(rtrnalbum);
                    finish();

                }
            });
        }




}
    }
    //ANDROID >19
    private void handleGalleryResult(Intent data)
    {

        Uri selectedImage = data.getData();
        if(selectedImage.getScheme().equals("content"))
            Path = urihandler.getPath(Photo_Upload.this,selectedImage);

        else
            Path=getPath(selectedImage);


        imageView.setImageBitmap(decodeSampledBitmapFromResource(Path,R.id.UploadPhoto,250,250));
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public static Bitmap decodeSampledBitmapFromResource(String res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(res,options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(res,options);
    }




    @SuppressLint("NewApi")
    private String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }

        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor;
        if(Build.VERSION.SDK_INT >19)
        {
            // Will return "image:x*"
            String wholeID = DocumentsContract.getDocumentId(uri);
            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];
            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";
            cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection, sel, new String[]{ id }, null);
        }
        else
        {
            cursor = getContentResolver().query(uri, projection, null, null, null);
        }
        String path = null;
        try
        {
            int column_index = cursor
                    .getColumnIndex(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index).toString();
            cursor.close();
        }
        catch(Exception e) {
            Toast.makeText(Photo_Upload.this, "Unable to open selected picture.", Toast.LENGTH_SHORT).show();

        }
        return path;
    }


    public  void isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                showFileChooser();
                Log.v("PERM","Permission is granted");
            } else {
                ActivityCompat.requestPermissions(Photo_Upload.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);

            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("PERMISSION","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }
    //Volley para imagenes


}
