package com.example.furwin.modfie_login.Errores;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.furwin.modfie_login.Errores.Errores.ControlErroresJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by FurWin on 22/03/2016.
 */
public class Album {

    private int cont_fotos, idalbum;
    private String titulo;
    private String url;
    private String thumb;
    private int id_album;
    private ArrayList<Album> albums = new ArrayList<>();

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setIdalbum(int idalbum) {
        this.idalbum = idalbum;
    }

    public void setCont_fotos(int cont_fotos) {
        this.cont_fotos = cont_fotos;
    }

    public int getCont_fotos() {
        return cont_fotos;
    }

    public int getIdalbum() {
        return idalbum;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String parametro) {
        this.url = "https://modfie.com/api/v1/modfies/" + parametro + "/albums";
    }

    public int getId_album() {
        return id_album;
    }

    public void setId_album(int id_album) {
        this.id_album = id_album;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }
}
