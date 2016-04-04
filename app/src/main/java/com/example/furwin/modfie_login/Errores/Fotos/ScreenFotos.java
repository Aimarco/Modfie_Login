package com.example.furwin.modfie_login.Errores.Fotos;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.furwin.modfie_login.Errores.Album;
import com.example.furwin.modfie_login.Errores.Errores.ControlErroresJson;
import com.example.furwin.modfie_login.Errores.Home_Screen.AlbumAdapter;
import com.example.furwin.modfie_login.Errores.Home_Screen.Home_Screen;
import com.example.furwin.modfie_login.Errores.Login_class.Login_Class;
import com.example.furwin.modfie_login.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by FurWin on 29/03/2016.
 */
public class ScreenFotos extends AppCompatActivity{
private String urlfotos;
    private int idalbum;
    private Foto foto;
    private Intent intent,camintent,upphotos;
    private Bundle bundle;
    private Login_Class login;
    private GridView gridView;
    private ArrayList<Foto> fotos;
    private FotoAdapter adaptadorFotos;
    private String token;
    private ProgressBar pb;
    ImageButton btnaniadir;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fotos);
        pb=(ProgressBar) findViewById(R.id.progressBar);
        intent=getIntent();
        bundle=intent.getExtras();
        idalbum=bundle.getInt("idalbum");
        urlfotos="https://modfie.com/api/v1/albums/"+idalbum+"?include=photos";
        login=(Login_Class) intent.getSerializableExtra("Datos");
        Log.e("Intent",""+bundle.getString("idfrom"));
        if(bundle.getString("idfrom").equalsIgnoreCase("Home"))
        token=login.getToken();
        else
        token=bundle.getString("token");



        fotos=new ArrayList<>();
        btnaniadir=(ImageButton) findViewById(R.id.imgbtnaniadir);


        //Rquest for the images


        pb.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET, urlfotos, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsondata = new JSONObject(response).getJSONObject("data").getJSONObject("photos").getJSONArray("data");
                    for (int i = 0; i < jsondata.length(); i++) {
                        Foto foto=new Foto();
                        foto.setSource(jsondata.getJSONObject(i).getJSONObject("file_path").getString("src"));
                        fotos.add(foto);
                        gridView = (GridView) findViewById(R.id.gridViewFotos);
                        adaptadorFotos = new FotoAdapter(ScreenFotos.this);
                        adaptadorFotos.setFotos(fotos);
                        gridView.setAdapter(adaptadorFotos);

                        pb.setVisibility(View.INVISIBLE);
                    }
                } catch (JSONException e) {
                    Log.e("ERROR", e.getMessage());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse respuesta = error.networkResponse;
                String jsonerror = new String(respuesta.data);
                try {

                    JSONObject errorjson = new JSONObject(jsonerror);
                    //class that treat the error and build the response
                    ControlErroresJson jsonerror2 = new ControlErroresJson(error.networkResponse.statusCode, errorjson,ScreenFotos.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ScreenFotos.this);
        requestQueue.add(request);



        camintent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        upphotos=new Intent(this,Photo_Upload.class);
        btnaniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upphotos.putExtra("album",idalbum);
                upphotos.putExtra("token",token);
                startActivity(upphotos);
                finish();
            }
        });

    }


}
