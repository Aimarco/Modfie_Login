package com.example.furwin.modfie_login.Errores.Errores;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by FurWin on 17/03/2016.
 */
public class JsonError422 {

    public String getErrors(JSONObject errores,Context activity){
        String mensajeError="";

        try {
            Toast.makeText(activity,errores.getString("message"), Toast.LENGTH_SHORT).show();
            JSONObject tiposerror = errores.getJSONObject("errors");
            Iterator<?> keys = tiposerror.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                mensajeError += key + ":" + tiposerror.getJSONArray(key).getString(0).toString() + "\n";
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
            return mensajeError;
    }
}
