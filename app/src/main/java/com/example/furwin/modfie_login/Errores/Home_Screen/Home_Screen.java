package com.example.furwin.modfie_login.Errores.Home_Screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.furwin.modfie_login.Errores.Album.Album_Screen;
import com.example.furwin.modfie_login.Errores.Login_class.Login_Class;
import com.example.furwin.modfie_login.Errores.Sliding_menu_dep;
import com.example.furwin.modfie_login.Errores.Sounds.Sounds_Screen;
import com.example.furwin.modfie_login.Errores.Videos.Video_Screen;
import com.example.furwin.modfie_login.R;
import com.squareup.picasso.Picasso;

/**
 * Created by FurWin on 22/03/2016.
 */
public class Home_Screen extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ListView menu;
    private ArrayAdapter<String> mAdapter;
    private TextView nombre;
    private ImageView imagen;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private Intent intent, albumintent,soundintent,videointent;
    private Login_Class login;
    private int cont=1;
    //Sliding_menu_dep slidemenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        intent=getIntent();
        login=(Login_Class) intent.getSerializableExtra("Datos");
        mDrawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        setTitle("Albums");
        //slidemenu=new Sliding_menu_dep();
        menu=(ListView) findViewById(R.id.opciones);
        createSlide(mDrawerLayout);
    }
/*
************START IMPLEMENTING LATERAL MENU**********************
 */
    public void createSlide(DrawerLayout drawer) {
        mDrawerLayout=drawer;
        addDrawerItems();
        setupDrawer();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void addDrawerItems() {

        String[] osArray = {"Albums", "Sound","Videos"};
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,osArray);
        menu.setAdapter(mAdapter);
        nombre=(TextView) findViewById(R.id.nombre);
        imagen=(ImageView) findViewById(R.id.Imagen);
        Picasso.with(this).load("https://untiempoparaamonita.files.wordpress.com/2014/11/loto-rosa-loto-sagrado-nelumbo-nucifera1.jpg").into(imagen);
        nombre.setText("Aimar Correyero");

        soundintent=new Intent(this, Sounds_Screen.class);
        albumintent = new Intent(this, Album_Screen.class);
        videointent=new Intent(this,Video_Screen.class);
        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        albumintent.putExtra("Datos", login);
                        startActivity(albumintent);
                        break;
                    case 1:
                        soundintent.putExtra("Datos", login);
                        startActivity(soundintent);break;
                    case 2:
                        videointent.putExtra("Datos",login);
                        startActivity(videointent);


                }

            }
        });
    }
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout , R.string.abrir, R.string.cerrar) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getSupportActionBar().setTitle("Home");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }
    @Override
    public void onBackPressed()
    {
        if(cont<2){
            Toast.makeText(Home_Screen.this, "Presiona atras otra vez para salir", Toast.LENGTH_SHORT).show();
            cont++;}
        else
            System.exit(0);
    }


}
