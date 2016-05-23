package com.example.furwin.modfie_login.Errores.Album;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
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
import com.example.furwin.modfie_login.Errores.Errores.ControlErroresJson;
import com.example.furwin.modfie_login.Errores.Login_class.Login_Class;
import com.example.furwin.modfie_login.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aimar on 11/04/2016.
 */
public class AddAlbum extends Activity {
private TextView title;
    private Login_Class logindata;
    private EditText edtTitle;
    private Intent albumintent,returnintent;
    private String token,url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_album);
        title=(TextView) findViewById(R.id.txttitle);
        edtTitle=(EditText) findViewById(R.id.edttitle);
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        albumintent=getIntent();
        token=albumintent.getExtras().getString("token");
        url=albumintent.getExtras().getString("url");
        logindata=(Login_Class) albumintent.getSerializableExtra("Datos");
        int width=dm.widthPixels;
        int heigth=dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(heigth*.1));
        final Button addalbum=(Button) findViewById(R.id.btnaddalbum);

        returnintent=new Intent(this,Album_Screen.class);
        addalbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtTitle.length()<1)
                    Toast.makeText(AddAlbum.this, "Por favor introduzca un titulo.", Toast.LENGTH_SHORT).show();
                else
                addAlbum();
            }
        });
    }
    public void addAlbum(){

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AddAlbum.this, "Album added succesfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddAlbum.this,Album_Screen.class);
                intent.putExtra("idfrom","addalbum");
                intent.putExtra("token",token);
                intent.putExtra("Datos",logindata);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
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
                    ControlErroresJson jsonerror2 = new ControlErroresJson(error.networkResponse.statusCode, errorjson, AddAlbum.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization","Bearer "+ token);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("title", edtTitle.getText().toString().trim());
                return params;
            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(AddAlbum.this);
        requestQueue.add(request);
        finish();
    }
}