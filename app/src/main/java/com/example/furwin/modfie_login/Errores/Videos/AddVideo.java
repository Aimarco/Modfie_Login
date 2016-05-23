package com.example.furwin.modfie_login.Errores.Videos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.furwin.modfie_login.Errores.Album.Album_Screen;
import com.example.furwin.modfie_login.Errores.Errores.ControlErroresJson;
import com.example.furwin.modfie_login.Errores.Login_class.Login_Class;
import com.example.furwin.modfie_login.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aimar on 20/05/2016.
 */
public class AddVideo extends Activity {
private EditText url,title;
    private Button addvideo;
    private Intent returnintent,vidintent;
    private String token;
    private Login_Class logindata;
    private String posturl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_video);
        url=(EditText) findViewById(R.id.txturl);
        title=(EditText) findViewById(R.id.vidtitle);
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        vidintent=getIntent();
        token=vidintent.getExtras().getString("token");
        logindata=(Login_Class) vidintent.getSerializableExtra("Datos");
        int width=dm.widthPixels;
        int heigth=dm.heightPixels;
        Video video=new Video();
        video.setGeturl(logindata.getUnique_id());
        posturl=video.getGeturl();
        getWindow().setLayout((int)(width*.9),(int)(heigth*.2));
        addvideo=(Button) findViewById(R.id.addvideo);

        returnintent=new Intent(this,Album_Screen.class);
        addvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(url.length()<1 || title.length()<1 )
                    Toast.makeText(AddVideo.this, "Por favor rellena los campos.", Toast.LENGTH_SHORT).show();
                /*else if(!url.getText().toString().trim().contains("youtube.com") || !url.getText().toString().trim().contains("youtu.be") || !url.getText().toString().trim().contains("vimeo.com"))
                    Toast.makeText(AddVideo.this, "Por favor introduzca una url de YouTube o Vimeo.", Toast.LENGTH_SHORT).show();*/
                else
                    addVideo();
            }
        });
    }



    public void addVideo(){

        StringRequest request = new StringRequest(Request.Method.POST,posturl , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AddVideo.this, "Video added succesfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddVideo.this,Video_Screen.class);
                intent.putExtra("token",token);
                intent.putExtra("Datos",logindata);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse respuesta = error.networkResponse;
                String jsonerror = new String(respuesta.data);
                try {

                    JSONObject errorjson = new JSONObject(jsonerror);
                    //class that treat the error and build the response
                    ControlErroresJson jsonerror2 = new ControlErroresJson(error.networkResponse.statusCode, errorjson, AddVideo.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization","Bearer "+ logindata.getToken());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("title", title.getText().toString().trim());
                params.put("url",url.getText().toString().trim());
                return params;
            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(AddVideo.this);
        requestQueue.add(request);
        finish();


    }
}
