package com.example.furwin.modfie_login.Errores.Photos;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import android.widget.PopupMenu;
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
import com.example.furwin.modfie_login.Errores.Album.Album_Screen;
import com.example.furwin.modfie_login.Errores.Errores.ControlErroresJson;
import com.example.furwin.modfie_login.Errores.Login_class.Login_Class;
import com.example.furwin.modfie_login.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by FurWin on 29/03/2016.
 */
public class ScreenFotos extends AppCompatActivity implements android.widget.PopupMenu.OnMenuItemClickListener{
    private String urlfotos;
    private int idalbum,idphoto;
    private Foto foto;
    private Intent intent,intentver,upphotos;
    private Login_Class login;
    private GridView gridView;
    private ArrayList<Foto> fotos;
    private FotoAdapter adaptadorFotos;
    private String token;
    private ProgressBar pb;
    ImageButton btnaniadir;
    private RequestQueue requestQueue;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fotos);
        pb=(ProgressBar) findViewById(R.id.progressBar);
        intent=getIntent();
        idalbum=intent.getExtras().getInt("idalbum");
        gridView = (GridView) findViewById(R.id.gridViewFotos);
        registerForContextMenu(gridView);
        urlfotos="https://modfie.com/api/v1/albums/"+idalbum+"?include=photos";
        login=(Login_Class) intent.getExtras().getSerializable("Datos");
        if(intent.getExtras().getString("idfrom").equalsIgnoreCase("Home"))
        token=login.getToken();
        else
        token=intent.getExtras().getString("token");
        fotos=new ArrayList<>();
        btnaniadir=(ImageButton) findViewById(R.id.imgbtnaniadir);
        registerForContextMenu(btnaniadir);
        foto=new Foto();

        //Rquest for the images
        pb.setVisibility(View.VISIBLE);
        getPhotos();
        //END OF REQUEST

        upphotos=new Intent(this,Photo_Upload.class);
        btnaniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
/*
                upphotos.putExtra("album",idalbum);
                upphotos.putExtra("token",token);
                upphotos.putExtra("Datos",login);
                startActivity(upphotos);*/


            }
        });




        intentver = new Intent(this,Photo_Viewer.class);
       gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               intentver.putExtra("Uriimagen", fotos.get(position).getSource());
               startActivity(intentver);
           }
       });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                idphoto=fotos.get(position).getId();
                return false;
            }
        });






    }
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        inflater.inflate(R.menu.image_upload_menu, popup.getMenu());
        popup.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item){
        switch (item.getItemId()) {
            case R.id.phone:{
                uploadPhotoActivity();
                return true;}
            case R.id.camera:{
                camactivity();
                return true;}
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void uploadPhotoActivity(){
        Log.e("telefono","telefono");
        upphotos.putExtra("album",idalbum);
        upphotos.putExtra("token",token);
        upphotos.putExtra("Datos",login);
        upphotos.putExtra("from","device");
        startActivity(upphotos);
    }
    public void camactivity(){
        Log.e("camara","camara");
        Intent camintent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(camintent, 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            upphotos.putExtra("album", idalbum);
            upphotos.putExtra("token", token);
            upphotos.putExtra("Datos", login);
            upphotos.putExtra("from", "camera");
            Uri selectedImage = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
       /* Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();*/

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            String enviarimg = BitMapToString(photo);
            upphotos.putExtra("imagen", enviarimg);

            startActivity(upphotos);
        }else
            Toast.makeText(ScreenFotos.this, "Photo not taken.", Toast.LENGTH_SHORT).show();
    }


    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }


   /* public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }*/







    @Override
    public void onBackPressed() {
            Intent a = new Intent(this,Album_Screen.class);
            a.putExtra("Datos", login);
            startActivity(a);
            finish();
    }

    //ContextMenu
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        switch (v.getId()) {
            case R.id.gridViewFotos:
                inflater.inflate(R.menu.delete_photo_menu, menu);
                break;
        }

    }



    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.delete:{
                deletePhoto(idphoto);
                return true;}
            default:
                return super.onContextItemSelected(item);
        }
    }

    //REQUEST for Photos
    public void getPhotos(){
        StringRequest request = new StringRequest(Request.Method.GET, urlfotos, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsondata = new JSONObject(response).getJSONObject("data").getJSONObject("photos").getJSONArray("data");
                    if(jsondata.length()<1){
                        Toast.makeText(ScreenFotos.this, "No hay fotos que mostrar", Toast.LENGTH_SHORT).show();
                        pb.setVisibility(View.INVISIBLE);
                    }

                    else {
                        for (int i = 0; i < jsondata.length(); i++) {
                            Foto foto = new Foto();
                            foto.setSource(jsondata.getJSONObject(i).getJSONObject("file_path").getString("src"));
                            foto.setId(jsondata.getJSONObject(i).getInt("id"));
                            fotos.add(foto);

                            adaptadorFotos = new FotoAdapter(ScreenFotos.this);
                            adaptadorFotos.setFotos(fotos);
                            gridView.setAdapter(adaptadorFotos);

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
        requestQueue = Volley.newRequestQueue(ScreenFotos.this);
        requestQueue.add(request);

    }

    public void deletePhoto(int fotoid){
        foto.setUrldelete(fotoid);
        String urldelete=foto.getUrldelete();
        intent=new Intent(this,ScreenFotos.class);
        StringRequest request = new StringRequest(Request.Method.DELETE,urldelete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ScreenFotos.this, "La foto se ha borrado.", Toast.LENGTH_SHORT).show();
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
                    ControlErroresJson jsonerror2 = new ControlErroresJson(error.networkResponse.statusCode, errorjson, ScreenFotos.this);
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
