package com.example.furwin.modfie_login.Errores.Videos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.example.furwin.modfie_login.Errores.Album.AddAlbum;
import com.example.furwin.modfie_login.Errores.Errores.ControlErroresJson;
import com.example.furwin.modfie_login.Errores.Home_Screen.Home_Screen;
import com.example.furwin.modfie_login.Errores.Login_class.Login_Class;
import com.example.furwin.modfie_login.Errores.Sounds.AudioOnTouchActivity;
import com.example.furwin.modfie_login.Errores.Sounds.Sound;
import com.example.furwin.modfie_login.Errores.Sounds.Sound_Adapter;
import com.example.furwin.modfie_login.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aimar on 19/05/2016.
 */
public class Video_Screen extends AppCompatActivity {
    private Login_Class login;
    private Intent intent,newVideo;
    private ProgressBar pb;
    private ArrayList<Video> videolist=new ArrayList<>();
    private VideoAdapter video_adapt;
    private GridView gridView;
    private ImageButton addvideo;
    private Video video=new Video();
    private RequestQueue requestQueue;
    private String urlvideos;
    private ImageView imgvideo;
    private String idvideo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_screen);
        intent=getIntent();
        pb=(ProgressBar) findViewById(R.id.pbVideos);
        login=(Login_Class) intent.getSerializableExtra("Datos");
        gridView=(GridView) findViewById(R.id.gridvideos);
        registerForContextMenu(gridView);
        addvideo=(ImageButton) findViewById(R.id.addVideo);


        video.setGeturl(login.getUnique_id());
        urlvideos=video.getGeturl();


        //request de video
        pb.setVisibility(View.VISIBLE);
        videoRequest();



        //elegir video
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playVideo(videolist.get(position).getPath());
            }
        });

        newVideo=new Intent(Video_Screen.this,AddVideo.class);
        addvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newVideo.putExtra("Datos",login);
                startActivity(newVideo);
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                idvideo=videolist.get(position).getVidid();
                return false;
            }
        });



    }

    //context menu
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        switch (v.getId()) {
            case R.id.gridvideos:
                inflater.inflate(R.menu.delete_photo_menu, menu);
                break;
        }

    }
    //action on context menu selected

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete: {
                deleteVideo(idvideo);
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }




    public void playVideo(String path){
        Intent videoClient = new Intent(Intent.ACTION_VIEW);
        videoClient.setData(Uri.parse(path));
        startActivityForResult(videoClient, 1234);

    }
    public void videoRequest(){
        StringRequest request = new StringRequest(Request.Method.GET, urlvideos, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsondata = new JSONObject(response).getJSONArray("data");
                    Log.e("longi",""+jsondata.length());
                    if(jsondata.length()<1){
                        Toast.makeText(Video_Screen.this, "No hay videos que mostrar", Toast.LENGTH_SHORT).show();
                        pb.setVisibility(View.INVISIBLE);
                    }
                    //Falta hacer un if con la clave Icon para cambiar la imagen a la de youtube o vimeo
                    else {
                        for (int i = 0; i < jsondata.length(); i++) {
                            Video video=new Video();
                            video.setIcon(jsondata.getJSONObject(i).getString("icon"));
                            video.setTitle(jsondata.getJSONObject(i).getString("title"));
                            video.setPath(jsondata.getJSONObject(i).getString("url"));
                            video.setVidid(jsondata.getJSONObject(i).getString("unique_id"));
                            videolist.add(video);
                            video_adapt = new VideoAdapter(Video_Screen.this);
                            video_adapt.setVideos(videolist);
                            gridView.setAdapter(video_adapt);
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
                    ControlErroresJson jsonerror2 = new ControlErroresJson(error.networkResponse.statusCode, errorjson, Video_Screen.this);
                } catch (Exception n) {
                    n.printStackTrace();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + login.getToken());

                return params;
            }
        };
        requestQueue= Volley.newRequestQueue(Video_Screen.this);
        requestQueue.add(request);
    }


    public void deleteVideo(String idvideo){
        video.seturldelete(idvideo);
        String urldelete=video.geturldelete();
        intent=new Intent(this,Video_Screen.class);
        StringRequest request = new StringRequest(Request.Method.DELETE,urldelete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Video_Screen.this, "Video deleted succesfully", Toast.LENGTH_SHORT).show();
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
                    ControlErroresJson jsonerror2 = new ControlErroresJson(error.networkResponse.statusCode, errorjson, Video_Screen.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization","Bearer "+ login.getToken());

                return headers;
            }

        };
        requestQueue.add(request);

    }
    public void onBackPressed() {
        Intent a = new Intent(this,Home_Screen.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        a.putExtra("Datos", login);
        startActivity(a);
    }

}
