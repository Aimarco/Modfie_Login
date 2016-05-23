package com.example.furwin.modfie_login.Errores;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.furwin.modfie_login.Errores.Album.Album_Screen;
import com.example.furwin.modfie_login.Errores.Login_class.Login_Class;
import com.example.furwin.modfie_login.R;
import com.squareup.picasso.Picasso;


/**
 * Created by Aimar on 05/04/2016.
 */
public class Sliding_menu_dep extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ListView menu;
    private ArrayAdapter<String> mAdapter;
    private TextView nombre;
    private ImageView imagen;
    private LinearLayout optalbum;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private Intent albumintent;
    private Login_Class login;

    public void createSlide(DrawerLayout drawer) {
        mDrawerLayout=drawer;
        addDrawerItems();
        setupDrawer();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void addDrawerItems() {

        String[] osArray = {"Albums", "Sonidos","Videos"};
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,osArray);
        menu.setAdapter(mAdapter);
        nombre=(TextView) findViewById(R.id.nombre);
        imagen=(ImageView) findViewById(R.id.Imagen);
        Picasso.with(this).load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSZGR8SRBGo2dO-8zNgl-tHfKqQ9MF8XxeLLKRNU_ioajtMWkC").into(imagen);
        nombre.setText("Aimar Correyero");


        albumintent = new Intent(this, Album_Screen.class);
        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        albumintent.putExtra("Datos", login);
                        startActivity(albumintent);
                        break;
                    case 1:

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

}



