package com.example.furwin.modfie_login.Errores.Errores;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import org.json.JSONObject;

/**
 * Created by FurWin on 17/03/2016.
 */
public class ControlErroresJson{
    private String message;
    private JSONObject menserror;

    public ControlErroresJson(int errorcod,JSONObject jsonrespuesta,Context activity){
        switch (errorcod){
            case 422:
                JsonError422 error422=new JsonError422();
                error422.getErrors(jsonrespuesta,activity);break;
            case 401:
                JsonError401 error401=new JsonError401();
                error401.getErrors(jsonrespuesta,activity);break;
                //textbox.setText(message);
        }


    }

}
