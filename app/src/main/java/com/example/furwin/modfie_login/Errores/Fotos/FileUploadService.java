package com.example.furwin.modfie_login.Errores.Fotos;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.*;

/**
 * Created by FurWin on 01/04/2016.
 */
public interface FileUploadService {
    @Multipart
    @POST
    @Headers({
           /* "Accept: application/xml",
            "Accept-Encoding: gzip",*/
            "Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjE5NywiaXNzIjoiaHR0cHM6XC9cL21vZGZpZS5jb21cL2Rhc2hib2FyZCIsImlhdCI6MTQ1OTQ5NjQzMywiZXhwIjoxNDYwNzA2MDMzLCJuYmYiOjE0NTk0OTY0MzMsImp0aSI6ImMwYjhmNjRhNjA5MjM3OGY0NTE4ZmI1MmU4ZDZlOGUyIn0.QJ7trkYSchz4OeBTAI6mZTWbxJMbBwaSm1nhZ5s9wLc"
    })
    Call<ResponseBody> upload(@Part("file_path") String fname,
                              @Part MultipartBody.Part file,
                              @Url String url);
}