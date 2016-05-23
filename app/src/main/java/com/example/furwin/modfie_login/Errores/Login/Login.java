package com.example.furwin.modfie_login.Errores.Login;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.furwin.modfie_login.Errores.BBDD.BBDD;
import com.example.furwin.modfie_login.Errores.BBDD.GestionaBBDD;
import com.example.furwin.modfie_login.Errores.Photos.Utils;
import com.example.furwin.modfie_login.Errores.Home_Screen.Home_Screen;
import com.example.furwin.modfie_login.Errores.Login_class.Login_Class;
import com.example.furwin.modfie_login.Errores.Register.Register_Class;
import com.example.furwin.modfie_login.R;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private EditText edtusername, edtpassword;
    private Button btnlogin,saltar,btnregistro;
    private Login_Class login;
    private GestionaBBDD gestionabbdd = new GestionaBBDD();
    BBDD bbdd;
    private SQLiteDatabase db;
    private String token, username, password,idunica,identificador;
    private int id;
    private Intent intent,logagain,registro;
    private Utils timer;
    private boolean test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        login = new Login_Class();
        edtusername = (EditText) findViewById(R.id.edtusername);
        edtpassword = (EditText) findViewById(R.id.edtpassword);
        btnregistro=(Button) findViewById(R.id.Registrarse);
        btnlogin = (Button) findViewById(R.id.btnLogin);
        bbdd = new BBDD(this, "modfiedb", null, 1);
        db = bbdd.getWritableDatabase();
        saltar=(Button) findViewById(R.id.Saltar);
        //Action of the Login BUTTON
        /*
        usuario existente-->token de DB
        usuario no existe o existe y token no funciona-->Pide token nuevo
        */










        intent=new Intent(this,Home_Screen.class);
        logagain=new Intent(this,Login.class);
        btnlogin.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            username = edtusername.getText().toString().trim();
                                            password = edtpassword.getText().toString().trim();
                                            if (username.trim().length() < 1 || password.trim().length() < 1)
                                                edtpassword.setError("Rellene todos los campos requeridos.");
                                            else {
                                                if (login.search_User(username, password, db)) {
                                                    id = login.search_id(username, password, db);
                                                    idunica = login.getunique(id, Login.this, db);
                                                    token = login.getToken(id, Login.this, db);
                                                    setters();
                                                } else {
                                                   String respuesta= login.request_Token(username, password, Login.this, db);
                                                    if(respuesta!=null){
                                                    Log.v("rtoken","datos: "+id+","+idunica);
                                                    setters();}
                                                    else
                                                        Toast.makeText(Login.this, "Usuario o Contraseña invalidos", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    });


        saltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = login.search_id("aimar1993", "modfie1234", db);
                if (id != 0) {
                    token = login.getToken(id, Login.this, db);
                    idunica = login.getunique(id, Login.this, db);
                    login.setUsername("aimar1993");
                    login.setPassword("modfie1234");
                    login.setToken(token);
                    login.setUnique_id(idunica);
                    intent.putExtra("Datos", login);
                    intent.putExtra("idfrom", "login");
                    finish();
                    startActivity(intent);
                } else
                    Toast.makeText(Login.this, "BD VACIA", Toast.LENGTH_SHORT).show();
            }
        });

        registro=new Intent(this,Register_Class.class);
        btnregistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(registro);
            }
        });
    }

public void setters(){
    token = login.getToken(id, Login.this, db);
    idunica=login.getunique(id, Login.this, db);
    login.setUsername(username);
    login.setPassword(password);
    login.setToken(token);
    login.setUnique_id(idunica);
    intent.putExtra("Datos", login);
    intent.putExtra("idfrom", "login");
    finish();
    startActivity(intent);
}

    public boolean testToken(final String idunica,final String token,final Context cont) {
        String url="https://modfie.com/api/v1/modfies/"+idunica+"/albums";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                test=true;
                Log.v("test",""+test);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("test",""+test);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(cont);
        requestQueue.add(request);
        return test;
    }



}
