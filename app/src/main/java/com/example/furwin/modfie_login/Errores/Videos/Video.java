package com.example.furwin.modfie_login.Errores.Videos;

import android.content.Context;

/**
 * Created by Aimar on 19/05/2016.
 */
public class Video {
    private String title,path;
    private String geturl,posturl,icon,urldelete;
    private Context context;
    private String vidid;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGeturl() {
        return geturl;
    }

    public void setGeturl(String id) {
        this.geturl = "https://modfie.com/api/v1/modfies/"+id+"/videos";
    }

    public String getPosturl() {
        return posturl;
    }

    public void setPosturl(String posturl) {
        this.posturl = posturl;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getVidid() {
        return vidid;
    }

    public void setVidid(String vidid) {
        this.vidid = vidid;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String geturldelete() {
        return urldelete;
    }

    public void seturldelete(String idvideo) {
        this.urldelete = "https://modfie.com/api/v1/videos/"+idvideo;
    }
}
