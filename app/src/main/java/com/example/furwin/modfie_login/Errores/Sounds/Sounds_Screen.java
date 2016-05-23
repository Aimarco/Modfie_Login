package com.example.furwin.modfie_login.Errores.Sounds;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aimar on 14/04/2016.
 */
public class Sounds_Screen extends AppCompatActivity{
    private String urlsounds;
    private Sound sound;
    private Intent intent,soundplayer,soundupload;
    private Login_Class login;
    private GridView gridView;
    private ArrayList<Sound> sounds;
    private Sound_Adapter sound_adapt;
    private String token;
    private ProgressBar pb;
    private ImageButton btnaniadir;
    private MediaPlayer player = new MediaPlayer();
    private boolean isPlaying;
    private String idsound;
    private RequestQueue requestQueue;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_sounds);

        intent=getIntent();
        login=(Login_Class) intent.getSerializableExtra("Datos");
        gridView=(GridView) findViewById(R.id.gridvideos);
        //esto ahce que se abra el menu de eliminar
        registerForContextMenu(gridView);
        btnaniadir=(ImageButton) findViewById(R.id.addSound);
        sound=new Sound();
        sound.setUrlsound(login.getUnique_id());
        sounds=new ArrayList<>();
        token=login.getToken();
        pb=(ProgressBar) findViewById(R.id.pbSounds);
        urlsounds=sound.getUrlsound();
        pb.setVisibility(View.VISIBLE);
        SoundRequest();

        soundplayer=new Intent(this,AudioPlayer.class);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playSound(sounds.get(position).getSource());
            }
        });
            soundupload=new Intent(this,AudioOnTouchActivity.class);
            btnaniadir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    soundupload.putExtra("Datos",login);
                    startActivity(soundupload);
                }
            });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                idsound=sounds.get(position).getId();
                return false;
            }
        });






    }

        //ContextMenu
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        switch (v.getId()) {
            case R.id.gridvideos:
                inflater.inflate(R.menu.delete_photo_menu, menu);
                break;
        }

    }



    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.delete:{
                deletePhoto(idsound);
                return true;}
            default:
                return super.onContextItemSelected(item);
        }
    }



    public void SoundRequest(){
        StringRequest request = new StringRequest(Request.Method.GET, urlsounds, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsondata = new JSONObject(response).getJSONArray("data");
                    Log.e("longi",""+jsondata.length());
                    if(jsondata.length()<1){
                        Toast.makeText(Sounds_Screen.this, "No hay sonidos que mostrar", Toast.LENGTH_SHORT).show();
                        pb.setVisibility(View.INVISIBLE);
                    }

                    else {
                        for (int i = 0; i < jsondata.length(); i++) {
                           Sound sound=new Sound();
                            sound.setSource(jsondata.getJSONObject(i).getJSONArray("sources").getJSONObject(0).getString("src"));
                            sound.setId(jsondata.getJSONObject(i).getString("unique_id"));
                            sounds.add(sound);

                            sound_adapt = new Sound_Adapter(Sounds_Screen.this);
                            sound_adapt.setSounds(sounds);
                            gridView.setAdapter(sound_adapt);

                            pb.setVisibility(View.INVISIBLE);
                        }
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
                    ControlErroresJson jsonerror2 = new ControlErroresJson(error.networkResponse.statusCode, errorjson, Sounds_Screen.this);
                } catch (Exception n) {
                    n.printStackTrace();
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
        requestQueue= Volley.newRequestQueue(Sounds_Screen.this);
        requestQueue.add(request);
    }
    public void playSound(String src) {
        soundplayer.putExtra("source",src);
        startActivity(soundplayer);

    }

    public void deletePhoto(String idsound){
        sound.setUrlsounddelete(idsound);
        String urldelete=sound.geturldelete();
        intent=new Intent(this,Sounds_Screen.class);
        StringRequest request = new StringRequest(Request.Method.DELETE,urldelete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Sounds_Screen.this, "el audio se ha borrado.", Toast.LENGTH_SHORT).show();
                Intent intent = getIntent();
                finish();
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
                    ControlErroresJson jsonerror2 = new ControlErroresJson(error.networkResponse.statusCode, errorjson, Sounds_Screen.this);
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

        };
        requestQueue.add(request);
    }




}
