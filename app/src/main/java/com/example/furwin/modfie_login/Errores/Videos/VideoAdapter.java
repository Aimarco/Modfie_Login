package com.example.furwin.modfie_login.Errores.Videos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.furwin.modfie_login.Errores.Sounds.Sound;
import com.example.furwin.modfie_login.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Aimar on 19/05/2016.
 */
public class VideoAdapter extends BaseAdapter {
    private Context context;
    private Video video;
    private ArrayList<Video> videolist=new ArrayList<>();



    public VideoAdapter(Context context){
        this.context=context;
    }


    @Override
    public int getCount() {
        return videolist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public void setVideos(ArrayList<Video> videos) {
        this.videolist = videos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= inflater.inflate(R.layout.vidobject, parent, false);
        }

        ImageView foto = (ImageView) convertView.findViewById(R.id.vid_object);
        if(videolist.get(position).getIcon().equals("youtube"))
        Picasso.with(context).load(R.drawable.youtube).into(foto);
        else if(videolist.get(position).getIcon().equals("vimeo"))
            Picasso.with(context).load(R.drawable.vimeo).into(foto);
        TextView title=(TextView) convertView.findViewById(R.id.vidTitle);
        title.setText(videolist.get(position).getTitle());
        return convertView;
    }
}
