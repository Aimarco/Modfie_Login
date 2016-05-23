package com.example.furwin.modfie_login.Errores.Errores;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by FurWin on 17/03/2016.
 */
public class JsonError422 {
    private Error error;
    private ArrayList<Error> errorlist=new ArrayList<>();


    public void getErrors(JSONObject errores,Context activity){
        JSONObject tiposerror=new JSONObject();

        try {

           tiposerror = errores.getJSONObject("errors");
        }catch(JSONException e){
            e.printStackTrace();
        }
        errorlist=showErrors(tiposerror);
        printErrors(errorlist,activity);
    }

    public ArrayList<Error> showErrors(JSONObject errors){
        ArrayList<Error> listaerrores=new ArrayList<>();
        Iterator<?> keys = errors.keys();
        while (keys.hasNext()) {
            error=new Error();
            String key = (String) keys.next();
            try {
                error.setKey(key);
                error.setDescription(errors.getJSONArray(key).getString(0).toString());
                listaerrores.add(error);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return listaerrores;
    }

    public void printErrors(ArrayList<Error> errorlist,Context activity){
        String errormsg="";
        for(int x=0;x<errorlist.size();x++){
            errormsg+=errorlist.get(x).getDescription()+"\n";
        }
        Toast.makeText(activity, errormsg, Toast.LENGTH_SHORT).show();
    }

}
