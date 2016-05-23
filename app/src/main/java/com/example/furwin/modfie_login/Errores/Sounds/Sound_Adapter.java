package com.example.furwin.modfie_login.Errores.Sounds;

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
 * Created by Aimar on 13/04/2016.
 */
public class Sound_Adapter extends BaseAdapter {
    private Context context;
    private Sound sound;
    private ArrayList<Sound> sounds=new ArrayList<>();



    public Sound_Adapter(Context context){
        this.context=context;
    }

    @Override
    public int getCount() {
        return sounds.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public ArrayList<Sound> getSounds() {
        return sounds;
    }

    public void setSounds(ArrayList<Sound> sounds) {
        this.sounds = sounds;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.soundobject, parent, false);
        }

        ImageView foto = (ImageView) view.findViewById(R.id.sound_photo);
        Picasso.with(context).load(R.drawable.soundicon).into(foto);
        return view;
    }

}
