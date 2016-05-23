package com.example.furwin.modfie_login.Errores.Album;

import java.util.ArrayList;

/**
 * Created by FurWin on 22/03/2016.
 */
public class Album {

    private int cont_fotos, idalbum;
    private String titulo;
    private String url,urldelete;
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
    public void setUrldelete(int parametro) {
        this.urldelete = "https://modfie.com/api/v1/albums/" + parametro;
    }
    public String getUrldelete() {
        return urldelete;
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
