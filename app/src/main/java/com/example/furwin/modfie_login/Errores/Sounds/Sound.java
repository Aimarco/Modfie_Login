package com.example.furwin.modfie_login.Errores.Sounds;

import android.content.Context;
import android.util.StringBuilderPrinter;

/**
 * Created by Aimar on 14/04/2016.
 */
public class Sound {
    private Context context;
    private String id,source;
    private String urlsound,urlsounddelete;


    public String getUrlsound() {
        return urlsound;
    }

    public void setUrlsound(String id) {
        this.urlsound = "https://modfie.com/api/v1/modfies/"+id+"/voices";
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setUrlsounddelete(String id) {
        this.urlsounddelete = "https://www.modfie.com/api/v1/voices/"+id;
    }

    public String geturldelete(){
        return urlsounddelete;
    }


}
