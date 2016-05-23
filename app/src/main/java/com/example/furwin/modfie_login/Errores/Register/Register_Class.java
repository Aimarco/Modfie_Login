package com.example.furwin.modfie_login.Errores.Register;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.furwin.modfie_login.Errores.Errores.ControlErroresJson;
import com.example.furwin.modfie_login.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aimar on 09/05/2016.
 */
public class Register_Class extends AppCompatActivity implements View.OnClickListener {
    private EditText username,pass1,pass2,email,birthdate;
    private RadioButton male,female;
    private CheckBox politics;
    private RadioGroup genre;
    private Button register;
    private ImageButton back;
    private String genero,terminos;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private ControlErroresJson errores;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        username=(EditText) findViewById(R.id.edtusername);
        pass1=(EditText) findViewById(R.id.npassword1);
        pass2=(EditText) findViewById(R.id.npassword2);
        email=(EditText) findViewById(R.id.nemail);
        male=(RadioButton) findViewById(R.id.rdbmale);
        female=(RadioButton) findViewById(R.id.rdbmujer);
        politics=(CheckBox)findViewById(R.id.cblicense);
        register=(Button) findViewById(R.id.btnregistrarse);
        back=(ImageButton) findViewById(R.id.imgbtnback);
        genre=(RadioGroup)findViewById(R.id.radioGroup);
        birthdate = (EditText) findViewById(R.id.birthdate);
        birthdate.setInputType(InputType.TYPE_NULL);
        birthdate.requestFocus();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(RellenaCampos()) {
                    if (pass1.getText().toString().trim().equals(pass2.getText().toString().trim()))
                        peticionRegistro();
                    else
                        Toast.makeText(Register_Class.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        genre.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                genre.setBackgroundColor(00000000);
            }
        });
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");


        setDateTimeField();

    }
    @Override
    public void onClick(View view) {
        if(view == birthdate) {
            fromDatePickerDialog.show();
        }
    }

    public boolean RellenaCampos(){
        if(username.getText().toString().trim().length()>0) {
            if (pass1.getText().toString().trim().length() > 0) {

                if (email.getText().toString().trim().length() > 0) {

                    if (birthdate.getText().toString().trim().length() > 0) {

                        if (male.isChecked() || female.isChecked()) {
                                if(male.isChecked())
                                    genero="M";
                                else if(female.isChecked())
                                    genero="F";
                            if (politics.isChecked()) {
                                terminos="1";
                                return true;
                            } else
                                terminos="0";
                                politics.setTextColor(getResources().getColor(R.color.fondoError));
                        } else {
                            male.setTextColor(getResources().getColor(R.color.fondoError));
                            female.setTextColor(getResources().getColor(R.color.fondoError));
                        }
                    } else
                        birthdate.setError("El campo es obligatorio");

                } else
                    email.setError("El campo es obligatorio");
            } else
                pass1.setError("El campo es obligatorio");
        }else
            username.setError("El campo es obligatorio");

        return false;
    }


    public void peticionRegistro(){
        StringRequest request = new StringRequest(Request.Method.POST, "https://modfie.com/api/v1/modfies/register", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Register_Class.this, "Registro completado,inicia sesion.", Toast.LENGTH_SHORT).show();
               finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse respuesta = error.networkResponse;
                String jsonerror = new String(respuesta.data);
                try {
                    JSONObject errorjson = new JSONObject(jsonerror);
                    //class that treat the error and build the response
                    ControlErroresJson jsonerror2 = new ControlErroresJson(error.networkResponse.statusCode,errorjson,Register_Class.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        })

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username.getText().toString().trim());
                params.put("email", email.getText().toString().trim());
                params.put("password", pass1.getText().toString().trim());
                params.put("birth_date", birthdate.getText().toString().trim());
                params.put("genre", genero);
                params.put("terms",terminos);
                params.put("password_confirmation",pass2.getText().toString().trim());
                return params;
            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(Register_Class.this);
        requestQueue.add(request);
    }
    private void setDateTimeField() {
        birthdate.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                birthdate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

}
