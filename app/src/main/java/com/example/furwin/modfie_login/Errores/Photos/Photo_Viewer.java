package com.example.furwin.modfie_login.Errores.Photos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.furwin.modfie_login.Errores.Errores.ControlErroresJson;
import com.example.furwin.modfie_login.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Aimar on 08/04/2016.
 */
public class Photo_Viewer extends Activity {
    private SubsamplingScaleImageView imgviewer;
    private Intent intent;
    private Uri Urimg;
    private Bitmap foto;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_viewer);
        intent = getIntent();
        imgviewer = (SubsamplingScaleImageView) findViewById(R.id.imagenVista);
        Urimg = Uri.parse(intent.getStringExtra("Uriimagen"));


        new DownloadImageTask(imgviewer).execute(Urimg.toString());
    }


    }






