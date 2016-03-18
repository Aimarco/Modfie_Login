package com.example.furwin.modfie_login.Errores.Errores;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by FurWin on 18/03/2016.
 */
public class JsonError401 {

    public String getErrors(JSONObject errores,Context activity){
        String mensajeError="";

        try {
            Toast.makeText(activity, errores.getString("message"), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mensajeError;
    }


}
