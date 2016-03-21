package com.example.furwin.modfie_login.Errores.Login;

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
    private Button btnlogin;
    private String urlLogin;
    private Login_Class login;
    private GestionaBBDD gestionabbdd = new GestionaBBDD();
    BBDD bbdd;
    private SQLiteDatabase db;
    private String token, username, password;
    private int id;

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
        //Action of the Login BUTTON
        /*
        usuario existente-->token de DB
        usuario no existe o existe y token no funciona-->Pide token nuevo
        */
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edtusername.getText().toString();
                password = edtpassword.getText().toString();
                id = login.search_User(username, password, db);
                if (id != 0) {
                    token = login.getToken(id, Login.this, db);
                    Toast.makeText(Login.this, "Login realizado correctamente.", Toast.LENGTH_SHORT).show();
                } else {
                    token = login.request_Token(username, password, Login.this, db);
                    Toast.makeText(Login.this, "Login realizado correctamente.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
