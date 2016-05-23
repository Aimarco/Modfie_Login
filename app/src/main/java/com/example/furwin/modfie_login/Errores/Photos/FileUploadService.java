package com.example.furwin.modfie_login.Errores.Photos;


import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.*;

/**
 * Created by FurWin on 01/04/2016.
 */
public interface FileUploadService {
    @Multipart
    @POST
    Call<ResponseBody> upload(@Part("file_path") String fname,
                              @Part MultipartBody.Part file,
                              @Url String url,
                              @Header("Authorization") String token);
    @Multipart
    @POST
    Call<ResponseBody> uploadAudio(@Part("file_path") String aname,
                                   @Part MultipartBody.Part file,
                                   @Url String url,
                                   @Header("Authorization") String token);
}