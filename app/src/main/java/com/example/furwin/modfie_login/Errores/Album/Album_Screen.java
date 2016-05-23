package com.example.furwin.modfie_login.Errores.Album;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.furwin.modfie_login.Errores.BBDD.BBDD;
import com.example.furwin.modfie_login.Errores.BBDD.GestionaBBDD;
import com.example.furwin.modfie_login.Errores.Errores.ControlErroresJson;
import com.example.furwin.modfie_login.Errores.Photos.ScreenFotos;
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
 * Created by Aimar on 05/04/2016.
 */
public class Album_Screen extends AppCompatActivity {
    private GestionaBBDD gestionabbdd = new GestionaBBDD();
    BBDD bbdd;
    private ImageButton imgadd;
    private SQLiteDatabase db;
    private String token,url;
    private int idalbum;
    private Login_Class login;
    private Intent intent,intentfotos,newAlbum;
    private Album album;
    private Bundle bundle;
    private GridView gridView;
    private AlbumAdapter adaptador;
    private ArrayList<Album> albums;
    private ProgressBar pb;
    private  RequestQueue requestQueue;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_screen);
        setTitle("Albums");
        intent = getIntent();
        login = (Login_Class) intent.getSerializableExtra("Datos");
        album = new Album();
        album.setUrl(login.getUnique_id());
        url=album.getUrl();
        intentfotos = new Intent(this, ScreenFotos.class);
        token = login.getToken();
        albums = new ArrayList<>();
        //albums = RequestAlbums(bundle.getString("token"), album.getUrl(), this);
        gridView = (GridView) findViewById(R.id.gridvideos);
        adaptador = new AlbumAdapter(this);
        gridView.setAdapter(adaptador);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        imgadd=(ImageButton) findViewById(R.id.Albumadd);
        registerForContextMenu(gridView);
        requestQueue = Volley.newRequestQueue(Album_Screen.this);

        /*
*Request to the API saving the important data
* for each album save it in an arrayList
* Set the View as an adapter for the grid taking the photos from de AL
*
* */
        getAlbums();


        //ADDAlbum performance
       newAlbum=new Intent(Album_Screen.this,AddAlbum.class);
        imgadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAlbum.putExtra("Datos",login);
                newAlbum.putExtra("token",token);
                newAlbum.putExtra("url",url);
                startActivity(newAlbum);
            }
        });




    //ALBUM SELECTION ACTION
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                finish();
                intentfotos.putExtra("idalbum", albums.get(position).getId_album());
                intentfotos.putExtra("Datos", login);
                intentfotos.putExtra("idfrom", "Home");
                startActivity(intentfotos);
            }
        });

        //ALBUM LONG CLICK
gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        idalbum=albums.get(position).getId_album();
        return false;
    }
});




    }



        //action on context menu selected

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.delete:{
                 deleteAlbum(idalbum);
                return true;}
            case R.id.seealbum:{
                finish();
                intentfotos.putExtra("idalbum",idalbum);
                intentfotos.putExtra("Datos", login);
                intentfotos.putExtra("idfrom", "Home");
                startActivity(intentfotos);
                return true;}
            default:
                return super.onContextItemSelected(item);
        }
    }
//ContextMenu
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            super.onCreateContextMenu(menu, v, menuInfo);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.contextmenu, menu);
        }

    public void getAlbums(){
        pb.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET, album.getUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsondata = new JSONObject(response).getJSONArray("data");
                    if(jsondata.length()>0){
                        for (int i = 0; i < jsondata.length(); i++) {
                            Album album = new Album();
                            album.setTitulo(jsondata.getJSONObject(i).getString("title"));
                            album.setId_album(jsondata.getJSONObject(i).getInt("id"));
                            album.setCont_fotos(jsondata.getJSONObject(i).getInt("count_photos"));
                            JSONArray photos = jsondata.getJSONObject(i).getJSONObject("photos").getJSONArray("data");
                            if(photos.length()<1){
                                album.setThumb("");
                                albums.add(album);
                                //Log.e("THUMB", "" + albums.get(i).getThumb());
                                gridView = (GridView) findViewById(R.id.gridvideos);
                                adaptador = new AlbumAdapter(Album_Screen.this);
                                adaptador.setAlbums(albums);
                                gridView.setAdapter(adaptador);
                            }else{
                                for (int j = 0; j < photos.length(); j++) {
                                    album.setThumb(photos.getJSONObject(0).getJSONObject("file_path").getJSONObject("thumbnails").getString("lg"));
                                    albums.add(album);
                                    //Log.e("THUMB", "" + albums.get(i).getThumb());
                                    gridView = (GridView) findViewById(R.id.gridvideos);
                                    adaptador = new AlbumAdapter(Album_Screen.this);
                                    adaptador.setAlbums(albums);
                                    gridView.setAdapter(adaptador);
                                }
                            }
                            pb.setVisibility(View.INVISIBLE);
                        }
                    }else{
                        Toast.makeText(Album_Screen.this, "Debe crear un album para ver sus fotos.", Toast.LENGTH_SHORT).show();
                        pb.setVisibility(View.INVISIBLE);
                    }

                } catch (JSONException e) {
                    Log.e("ERROR", e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse respuesta = error.networkResponse;

                try {
                    String jsonerror = new String(respuesta.data);
                    JSONObject errorjson = new JSONObject(jsonerror);
                    //class that treat the error and build the response
                    ControlErroresJson jsonerror2 = new ControlErroresJson(error.networkResponse.statusCode, errorjson, Album_Screen.this);
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

        requestQueue.add(request);

    }


    public void deleteAlbum(int albumid){
        album.setUrldelete(albumid);
        String urldelete=album.getUrldelete();
        intent=new Intent(this,Album_Screen.class);
            StringRequest request = new StringRequest(Request.Method.DELETE,urldelete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(Album_Screen.this, "Album deleted succesfully", Toast.LENGTH_SHORT).show();
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
                        ControlErroresJson jsonerror2 = new ControlErroresJson(error.networkResponse.statusCode, errorjson, Album_Screen.this);
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

    //Go to Home Screen on pressing back btn
    @Override
    public void onBackPressed() {
            Intent a = new Intent(this,Home_Screen.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            a.putExtra("Datos", login);
            startActivity(a);
        }


}


