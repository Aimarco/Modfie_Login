package com.example.furwin.modfie_login.Errores.Fotos;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;


import java.io.File;
import java.net.URI;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by FurWin on 01/04/2016.
 */
public class ServiceGenerator {
    private Context context;
    private int albumid;
    public static final String API_BASE_URL = "https://modfie.com/api/v1/albums/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
    public void uploadFile(File file) {
        // create upload service client
        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class);

        // use the FileUtils to get the actual file by uri


        // create RequestBody instance from file


        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);


        // MultipartBody.Part is used to send also the actual file name
        String fname=file.getName();
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file_path", file.getName(), requestFile);
        // finally, execute the request
        String url=formUrl();
        Call<ResponseBody> call = service.upload(fname, body,url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Toast.makeText(getContext(), "Photo uploaded succesfully", Toast.LENGTH_SHORT).show();
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Photo not uploaded", Toast.LENGTH_SHORT).show();
                Log.e("Upload error:", t.getMessage());
            }
        });
    }
    public String formUrl(){
        String url=getAlbumid()+"/photos";
        return url;
    }

    public int getAlbumid() {
        return albumid;
    }

    public void setAlbumid(int albumid) {
        this.albumid = albumid;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}