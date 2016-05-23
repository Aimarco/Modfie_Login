package com.example.furwin.modfie_login.Errores.Photos;

/**
 * Created by FurWin on 29/03/2016.
 */
public class Foto {
    private int id;
    private String source;
    private String descripcion;
    private String url,urldelete;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String param) {
        this.url = "https://modfie.com/api/v1/albums/"+param+"?include=photos";
    }

    public String getUrldelete() {
        return urldelete;
    }

    public void setUrldelete(int param) {
        this.urldelete = "https://modfie.com/api/v1/photos/"+param;
    }
}


