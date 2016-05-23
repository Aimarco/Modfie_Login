package com.example.furwin.modfie_login.Errores.Login_class;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
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
import com.example.furwin.modfie_login.Errores.BBDD.BBDD;
import com.example.furwin.modfie_login.Errores.BBDD.GestionaBBDD;
import com.example.furwin.modfie_login.Errores.Errores.ControlErroresJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by FurWin on 21/03/2016.
 */
public class Login_Class implements Serializable {
    private String urlpost = "https://modfie.com/api/v1/modfies/attempt";
    private GestionaBBDD gestionabbdd = new GestionaBBDD();

    private String token, username, password,unique_id;
    private Context context;
    private SQLiteDatabase db;
    private int errcod;
    private boolean test;


    public boolean search_User(String user, String pass, SQLiteDatabase db) {
        boolean encontrado;
        String query = "Select idusuario from Usuarios where username='" + user + "' and password='" + pass + "'";
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            encontrado=true;
        } else
            encontrado=false;
        return encontrado;
    }
    public int search_id(String user, String pass, SQLiteDatabase db) {
        int id;
        String query = "Select idusuario from Usuarios where username='" + user + "' and password='" + pass + "'";
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            id=c.getInt(0);
        } else
            id=0;
        return id;
    }


    public String getToken(int id, Context context, SQLiteDatabase db) {
        String query = "Select token from Usuarios where idusuario=" + id;
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            setToken(c.getString(0));
        }
        return getToken();
    }


    public String request_Token(final String user, final String pass, final Context cont, final SQLiteDatabase db) {
        StringRequest request = new StringRequest(Request.Method.POST, urlpost, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                db.execSQL("delete from Usuarios");
                try {
                    JSONObject jtoken = new JSONObject(response);
                   setToken(jtoken.getString("token"));
                    setUnique_id(jtoken.getString("unique_id"));
                    String[] userdata = {user, pass, getToken(),getUnique_id()};
                    gestionabbdd.insertaDatos(db, "Usuarios", userdata);
                    } catch (JSONException e) {
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
                   // ControlErroresJson jsonerror2 = new ControlErroresJson(error.networkResponse.statusCode, errorjson,cont);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            public byte[] getBody() {
                String httpPostBody = "username=" + user + "&password=" + pass;
                return httpPostBody.getBytes();
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(cont);
        requestQueue.add(request);


        return token;
    }
    public String getunique(int id, Context context, SQLiteDatabase db) {
        String query = "Select idunica from Usuarios where idusuario=" + id;
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            setUnique_id(c.getString(0));
        }
        return getUnique_id();
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }



    public void request_new_token(final String idunica,final Context cont){
        StringRequest request = new StringRequest(Request.Method.POST, urlpost, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jtoken = new JSONObject(response);
                    setToken(jtoken.getString("token"));
                    Log.v("en rnewtoken",jtoken.getString("token"));

                    ContentValues valores = new ContentValues();
                    valores.put("token", getToken());

                    String[] args = new String[]{idunica};
                    db.update("Usuarios", valores, "idunica=?", args);

                } catch (JSONException e) {
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
                    ControlErroresJson jsonerror2 = new ControlErroresJson(error.networkResponse.statusCode, errorjson, cont);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username",getUsername());
                params.put("password",getPassword());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(cont);
        requestQueue.add(request);
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
