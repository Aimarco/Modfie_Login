package com.example.furwin.modfie_login.Errores.Photos;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import java.io.File;

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
public class ServiceGenerator extends AppCompatActivity{
    private Context context;
    private int albumid;
    public static final String API_BASE_URL = "https://modfie.com/api/v1/albums/";
    public static final String AUDIO_URL="https://modfie.com/api/v1/modfies/iieVmdVv/voices/";
    private String token;
    private ProgressDialog pd;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static OkHttpClient.Builder httpClient2 = new OkHttpClient.Builder();
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
    private static Retrofit.Builder audiobuilder =
            new Retrofit.Builder()
                    .baseUrl(AUDIO_URL)
                    .addConverterFactory(GsonConverterFactory.create());



    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
    public static <S> S createServiceAudio(Class<S> serviceClass) {
        Retrofit retrofit = audiobuilder.client(httpClient2.build()).build();
        return retrofit.create(serviceClass);
    }


    public void uploadFile(File file) {
        pd=new ProgressDialog(getContext());
        // create upload service client
        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class);

        // use the FileUtils to get the actual file by uri


        // create RequestBody instance from file


        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);


        //HEADERS DE LA PETICIONOkHttpClient client = new OkHttpClient.Builder()





        // MultipartBody.Part is used to send also the actual file name
        String fname=file.getName();
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file_path", file.getName(), requestFile);




        // finally, execute the request
        String url=formUrl();
        Call<ResponseBody> call = service.upload(fname, body,url,"Bearer "+getToken());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                pd.dismiss();
                Toast.makeText(getContext(), "Photo uploaded succesfully", Toast.LENGTH_SHORT).show();
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Photo not uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }



    //AUDIO PART
    public void uploadAudio(File audiofile) {
        //pd=new ProgressDialog(getContext());
        FileUploadService service2 =
                ServiceGenerator.createServiceAudio(FileUploadService.class);
        RequestBody requestFile2 =
                RequestBody.create(MediaType.parse("application/json"), audiofile);


        MultipartBody.Part body2 =
                MultipartBody.Part.createFormData("file_path", audiofile.getName(), requestFile2);

        String url=AUDIO_URL;
        Call<ResponseBody> call2 = service2.uploadAudio(audiofile.getName(),body2,url,"Bearer "+getToken());
        Log.v("name",audiofile.getName());
        //pd.setMessage("Uploading Audio...");
        //pd.show();
        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Toast.makeText(getContext(), "Audio uploaded succesfully", Toast.LENGTH_SHORT).show();
                Log.v("Upload", "success");
                Log.v("cod resp",""+response.isSuccessful());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("resp_error",t.getMessage());
                Toast.makeText(getContext(), "Audio not uploaded", Toast.LENGTH_SHORT).show();
            }

        });
        //pd.dismiss();
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}