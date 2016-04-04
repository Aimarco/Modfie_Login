package com.example.furwin.modfie_login.Errores.Login;

import android.content.Intent;
import android.database.Cursor;
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
import com.example.furwin.modfie_login.Errores.Home_Screen.Home_Screen;
import com.example.furwin.modfie_login.Errores.Login_class.Login_Class;
import com.example.furwin.modfie_login.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private EditText edtusername, edtpassword;
    private Button btnlogin,saltar;
    private Login_Class login;
    private GestionaBBDD gestionabbdd = new GestionaBBDD();
    BBDD bbdd;
    private SQLiteDatabase db;
    private String token, username, password,idunica;
    private int id;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = new Login_Class();
        edtusername = (EditText) findViewById(R.id.edtusername);
        edtpassword = (EditText) findViewById(R.id.edtpassword);
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
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edtusername.getText().toString();
                password = edtpassword.getText().toString();
                id = login.search_User("aimar1993", "modfie1234", db);
                if (id != 0) {
                    token = login.getToken(id, Login.this, db);
                    idunica=login.getunique(id, Login.this, db);
                    Toast.makeText(Login.this, "Login realizado correctamente.", Toast.LENGTH_SHORT).show();
                    login.setToken(token);
                    login.setUnique_id(idunica);
                    intent.putExtra("Datos", login);
                    intent.putExtra("idfrom","login");
                    startActivity(intent);
                } else {
                    token = login.request_Token(username, password, Login.this, db);
                    //Toast.makeText(Login.this, "Login realizado correctamente.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        saltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = login.search_User("aimar1993", "modfie1234", db);
                token = login.getToken(id, Login.this, db);
                idunica=login.getunique(id, Login.this, db);
                login.setToken(token);
                login.setUnique_id(idunica);
                intent.putExtra("Datos",login);
                intent.putExtra("idfrom","login");
                startActivity(intent);
            }
        });
    }
}
