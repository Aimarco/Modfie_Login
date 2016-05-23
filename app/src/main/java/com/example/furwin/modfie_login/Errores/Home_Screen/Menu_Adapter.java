package com.example.furwin.modfie_login.Errores.Home_Screen;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.furwin.modfie_login.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Aimar on 06/04/2016.
 */
public class Menu_Adapter extends BaseAdapter {
    private String[]items=new String[]{"Albums","Sonidos","Videos"};
    private Card card;
    private Context context;
    private String nombre;


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.menu_items, viewGroup, false);
        }
        ArrayAdapter<String>  adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items);

        ImageView imagen = (ImageView) view.findViewById(R.id.imagen);
        TextView nombre = (TextView) view.findViewById(R.id.nombre);
        ListView opciones=(ListView) view.findViewById(R.id.listView);
        opciones.setAdapter(adapter);


        Picasso.with(context).load("https://untiempoparaamonita.files.wordpress.com/2014/11/loto-rosa-loto-sagrado-nelumbo-nucifera1.jpg").into(imagen);
        nombre.setText("Aimar Correyero");
                return view;
    }


    public Object getItem(int position) {
        return null;
    }
    public long getItemId(int position) {
        return 0;
    }
    public int getCount() {
        return 0;
    }




}