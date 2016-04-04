package com.example.furwin.modfie_login.Errores.Fotos;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.furwin.modfie_login.Errores.Errores.ControlErroresJson;
import com.example.furwin.modfie_login.Manifest;
import com.example.furwin.modfie_login.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Handler;

/**
 * Created by FurWin on 30/03/2016.
 */
public class Photo_Upload extends AppCompatActivity implements View.OnClickListener,ActivityCompat.OnRequestPermissionsResultCallback {
    private Button buttonChoose;
    private Button buttonUpload;
    private ImageView imageView;
    private EditText editTextName;
    private int idalbum;
    private Bundle bundle;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    private String Path,token;
    private File imagen;
    private Intent intent,rtrnalbum;
    private ServiceGenerator svg;
    private Bitmap yourSelectedImage;
    private byte[] imageBytes;
    private  String encodedImage;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image);
        rtrnalbum=new Intent(this,ScreenFotos.class);
        svg=new ServiceGenerator();

        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        intent=getIntent();
        bundle=intent.getExtras();
        token=bundle.getString("token");
        idalbum=bundle.getInt("album");
        editTextName = (EditText) findViewById(R.id.editText);

        imageView = (ImageView) findViewById(R.id.imageView);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
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
        handleGalleryResult(data);
    }
    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            isStoragePermissionGranted();
            showFileChooser();

        }
        if (v == buttonUpload) {
            uploadImage();
        }
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    public void uploadImage(){
        if(imageView.getDrawable()==null)
            Toast.makeText(Photo_Upload.this, "Debes seleccionar una foto antes.", Toast.LENGTH_SHORT).show();
        else{
        svg.setAlbumid(idalbum);
        svg.setContext(Photo_Upload.this);
        svg.uploadFile(imagen);
            rtrnalbum.putExtra("idalbum", idalbum);
            rtrnalbum.putExtra("idfrom", "Upload");
            rtrnalbum.putExtra("token", token);
           int secs=1;

            Utils.delay(secs, new Utils.DelayCallback() {
                @Override
                public void afterDelay() {
                    startActivity(rtrnalbum);
                    finish();
                }
            });


}
    }
    //ANDROID >19
    private void handleGalleryResult(Intent data)
    {
        Uri selectedImage = data.getData();
        Path = getPath(selectedImage);
        if(Path!=null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            } catch (IOException io) {
                Log.e("IO", "IO except");
            }
            getStringImage(bitmap);
            imagen=new File(Path);
            //Setting the Bitmap to ImageView
            imageView.setImageBitmap(bitmap);
        }else
        {
            try {
                InputStream is = getContentResolver().openInputStream(selectedImage);
                imageView.setImageBitmap(BitmapFactory.decodeStream(is));
                Path = selectedImage.getPath();
                imagen=new File(Path);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
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
        catch(NullPointerException e) {

        }
        return path;
    }


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERM","Permission is granted");
                return true;
            } else {

                Log.v("PERM","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("PERM","Permission is granted");
            return true;
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
