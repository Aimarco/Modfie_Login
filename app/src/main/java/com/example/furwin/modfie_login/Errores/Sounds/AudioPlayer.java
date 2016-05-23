package com.example.furwin.modfie_login.Errores.Sounds;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;


import com.example.furwin.modfie_login.Errores.Album.Album_Screen;
import com.example.furwin.modfie_login.Errores.Login_class.Login_Class;
import com.example.furwin.modfie_login.R;

import java.io.IOException;

/**
 * Created by Aimar on 14/04/2016.
 */
public class AudioPlayer extends Activity{
    private ImageButton btnplay, btnpause, btnstop;
    private ProgressBar pbplayer,pbload;
    private String source;
    private Intent intent;
    private MediaPlayer player = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_player_layout);
        pbload=(ProgressBar) findViewById(R.id.pbload);
        pbload.setVisibility(View.VISIBLE);
        intent=getIntent();
        btnplay= (ImageButton) findViewById(R.id.imgbplay);
        btnpause= (ImageButton) findViewById(R.id.imgbpause);
        btnstop= (ImageButton) findViewById(R.id.imgbstop);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int heigth = dm.heightPixels;
        source=intent.getStringExtra("source");
        getWindow().setLayout((int) (width * .9), (int) (heigth * .2));

        initplayer();


        btnpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.pause();
            }
        });

        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.release();
                finish();
            }
        });

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.start();
            }
        });

    }

public void initplayer(){
    try {
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setDataSource(source);
        player.prepare();
        player.start();
        pbload.setVisibility(View.INVISIBLE);
    } catch (IllegalArgumentException e) {
        e.printStackTrace();
    } catch (IllegalStateException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
}



    @Override
    protected void onDestroy(){
        super.onDestroy();
        finish();
    }
    @Override
    public void onBackPressed() {
        player.stop();
        finish();
    }

}

