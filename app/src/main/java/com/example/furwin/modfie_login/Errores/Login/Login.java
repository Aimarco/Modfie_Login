package com.example.furwin.modfie_login.Errores.Login;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.furwin.modfie_login.Errores.BBDD.BBDD;
import com.example.furwin.modfie_login.Errores.BBDD.GestionaBBDD;
import com.example.furwin.modfie_login.Errores.Errores.ControlErroresJson;
import com.example.furwin.modfie_login.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private EditText edtusername, edtpassword;
    private Button btnlogin;
    private String urlLogin = "****";
    private GestionaBBDD gestionabbdd = new GestionaBBDD();
    BBDD bbdd;
    private SQLiteDatabase db;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtusername = (EditText) findViewById(R.id.edtusername);
        edtpassword = (EditText) findViewById(R.id.edtpassword);
        btnlogin = (Button) findViewById(R.id.btnLogin);
        bbdd = new BBDD(this, "modfiedb", null, 1);
        db = bbdd.getWritableDatabase();
        //Action of the Login BUTTON
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLogin();
            }
        });
    }

//Method that will execute when the button login is pressed
    protected void requestLogin() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, urlLogin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jtoken = new JSONObject(response);
                    token = jtoken.getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String[] userdata = {edtusername.getText().toString(), edtpassword.getText().toString(), token};
                gestionabbdd.insertaDatos(db, "Usuarios", userdata);
                Toast.makeText(Login.this, "Login realizado con exito", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse respuesta = error.networkResponse;
                String jsonerror = new String(respuesta.data);
                Log.e("Error", "" + error.networkResponse.statusCode);
                try {
                    JSONObject errorjson = new JSONObject(jsonerror);
                    //class that treat the error and build the response
                    ControlErroresJson jsonerror2 = new ControlErroresJson(error.networkResponse.statusCode, errorjson, Login.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            public byte[] getBody() {
                String httpPostBody = "username=" + edtusername.getText() + "&password=" + edtpassword.getText();
                return httpPostBody.getBytes();
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
        db.close();

    }


}
