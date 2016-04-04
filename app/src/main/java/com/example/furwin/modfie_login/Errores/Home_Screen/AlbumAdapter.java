package com.example.furwin.modfie_login.Errores.Home_Screen;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.furwin.modfie_login.Errores.Album;
import com.example.furwin.modfie_login.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Created by FurWin on 22/03/2016.
 */
public class AlbumAdapter extends BaseAdapter {
private Context context;
    private Album album;
    private ArrayList<Album> albums=new ArrayList<>();



public AlbumAdapter(Context context){
    this.context=context;
}



    @Override
    public int getCount() {
       return getAlbums().size();
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.albums, viewGroup, false);
        }

        ImageView imagenAlbum = (ImageView) view.findViewById(R.id.imagen_album);
        TextView nombreAlbum = (TextView) view.findViewById(R.id.nombre_album);
        TextView photocont=(TextView) view.findViewById(R.id.PhotoCount);
        Picasso.with(context).load(albums.get(position).getThumb()).into(imagenAlbum);
        nombreAlbum.setText(albums.get(position).getTitulo());
        photocont.setText(""+albums.get(position).getCont_fotos());
        return view;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
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
