package com.example.furwin.modfie_login.Errores.Photos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.furwin.modfie_login.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by FurWin on 29/03/2016.
 */
public class FotoAdapter extends BaseAdapter {
    private Context context;
    private Foto foto;
    private ArrayList<Foto> fotos=new ArrayList<>();



    public FotoAdapter(Context context){
        this.context=context;
    }



    @Override
    public int getCount() {
        return getFotos().size();
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }

    public ArrayList<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(ArrayList<Foto> fotos) {
        this.fotos = fotos;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.objetofoto, viewGroup, false);
        }

        ImageView foto = (ImageView) view.findViewById(R.id.imagen_foto);
        Picasso.with(context).load(fotos.get(position).getSource()).into(foto);
        return view;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}

