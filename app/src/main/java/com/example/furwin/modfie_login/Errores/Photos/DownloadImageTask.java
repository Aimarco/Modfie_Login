package com.example.furwin.modfie_login.Errores.Photos;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.InputStream;

/**
 * Created by Aimar on 22/04/2016.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private SubsamplingScaleImageView imageView;
    private Bitmap image;

    public DownloadImageTask(SubsamplingScaleImageView imageView) {
        this.imageView = imageView;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            image = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            image = null;
        }
        return image;
    }

    @SuppressLint("NewApi")
    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            imageView.setImage(ImageSource.bitmap(result));
        }
    }
}
