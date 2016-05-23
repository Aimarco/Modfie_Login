package com.example.furwin.modfie_login.Errores.Sounds;

/**
 * Created by Aimar on 19/04/2016.
 */
import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.furwin.modfie_login.Errores.Login_class.Login_Class;
import com.example.furwin.modfie_login.Errores.Photos.ServiceGenerator;
import com.example.furwin.modfie_login.Errores.UriHandler;
import com.example.furwin.modfie_login.Manifest;
import com.example.furwin.modfie_login.R;

public class AudioOnTouchActivity extends Activity {
    ImageButton mic,folder;
    private Login_Class login;
    ImageView recording;
    private TextView timertext;
    private Button btnupload;
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FILE_EXT_MP3 = ".mp3";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private MediaRecorder recorder = null;
    long updatedTime = 0L;
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    private Handler customHandler = new Handler();
    private ServiceGenerator svg;
    private File file,audio;
    private EditText fname;
    private UriHandler urihandler=new UriHandler();
    private int PICK_AUDIO_REQUEST=1;
    private String token;
    private Intent intento;
    private  byte[] barray;
    private ProgressDialog pd;
    private VolleyMultipartRequest multipartRequest;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_sounds);
        mic=(ImageButton)findViewById(R.id.mic);
        intento=getIntent();
        token=intento.getExtras().getString("token");
        login=new Login_Class();
        login=(Login_Class) intento.getExtras().getSerializable("Datos");
        recording=(ImageView) findViewById(R.id.recording);
        folder=(ImageButton) findViewById(R.id.folder);
        timertext=(TextView) findViewById(R.id.texttimer);
        btnupload=(Button) findViewById(R.id.btnsubiraudio);
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int heigth=dm.heightPixels;
        getWindow().setLayout((int)(width*.9),(int)(heigth*.2));
        svg=new ServiceGenerator();
        fname=(EditText) findViewById(R.id.txtfname) ;
        login=(Login_Class) getIntent().getSerializableExtra("Datos");
        pd=new ProgressDialog(AudioOnTouchActivity.this);



        mic.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(!fname.getText().toString().equals("")){
                        AppLog.logString("Start Recording");
                        startRecording();
                        startTime = SystemClock.uptimeMillis();
                        customHandler.postDelayed(updateTimerThread, 0);
                        recording.setImageDrawable(getDrawable(R.drawable.recred));}
                        else
                            Toast.makeText(AudioOnTouchActivity.this, "Introduce nombre para el archivo.", Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_UP:
                        AppLog.logString("stop Recording");
                        recording.setImageDrawable(getDrawable(R.drawable.recgrey));
                        customHandler.removeCallbacks(updateTimerThread);
                        stopRecording();
                        break;
                }
                return false;
            }
        });

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fname.getText().toString().equals("")) {
                    Toast.makeText(AudioOnTouchActivity.this, "Introduce nombre para el archivo.", Toast.LENGTH_SHORT).show();
                } else {
                    svg.setContext(AudioOnTouchActivity.this);
                    svg.setToken(login.getToken());
                    Log.v("clik subir", login.getToken());
                    Log.v("audio",audio.getAbsoluteFile().getName());
                    if (!audio.isFile())
                        Log.v("ES UN ARCHIVO", "NO");
                    else{
                        Log.v("ES UN ARCHIVO", "SI");
                        //svg.uploadAudio(audio);
                        uploadAudio(audio);
                    }
                }
            }
        });

        //boton Carpeta
        folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStoragePermissionGranted();

            }
        });





    }


    private String getFilename(){
        String filepath = Environment.getExternalStorageDirectory().getPath();
         file= new File(filepath,AUDIO_RECORDER_FOLDER);

        if(!file.exists()){
            file.mkdirs();
        }
        AppLog.logString(file.getAbsolutePath());
        return (file.getAbsolutePath() + "/" + fname.getText()+ AUDIO_RECORDER_FILE_EXT_MP3);
    }
    private void startRecording(){
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(getFilename());

        audio=new File(getFilename());


        recorder.setOnErrorListener(errorListener);
        recorder.setOnInfoListener(infoListener);

        try {
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private MediaRecorder.OnErrorListener errorListener = new        MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            AppLog.logString("Error: " + what + ", " + extra);
        }
    };

    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            AppLog.logString("Warning: " + what + ", " + extra);
        }
    };
    private void stopRecording(){
        if(null != recorder){
            recorder.stop();
            recorder.reset();
            recorder.release();

            recorder = null;
        }
    }



    //TIMER FOR RECORDING

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timertext.setText("" + mins + ":"
                    + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
        }

    };

    public  void isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                showFileChooser();
                Log.v("PERM","Permission is granted");
            } else {
                ActivityCompat.requestPermissions(AudioOnTouchActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);

            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
        }

    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/mpeg");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Audio file"), PICK_AUDIO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_AUDIO_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String path=urihandler.getPath(AudioOnTouchActivity.this,uri);
            audio=new File(path);
            audio= FileUtils.getFile(audio);
            fname.setText(audio.getName());

        }
    }


    public void uploadAudio(final File passfile) {
        pd.setTitle("Subiendo Audio...");
        pd.setMessage("Subiendo el audio a tu espacio personal");
        pd.show();
        multipartRequest = new VolleyMultipartRequest(Request.Method.POST, "https://modfie.com/api/v1/modfies/iieVmdVv/voices", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Log.v("nuevo metodo","success");
                pd.dismiss();
                Toast.makeText(AudioOnTouchActivity.this, "El audio se ha subido con exito.", Toast.LENGTH_SHORT).show();
              volver();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    if (error.getClass().equals(SocketTimeoutException.class)) {
                        // Show timeout error message
                        Toast.makeText(AudioOnTouchActivity.this,
                                "Oops. Timeout error!",
                                Toast.LENGTH_LONG).show();
                    }
                Toast.makeText(AudioOnTouchActivity.this,
                        "No se pudo subir el audio",
                        Toast.LENGTH_LONG).show();
                pd.dismiss();
                Log.v("Timeout","Error:"+error.getClass()+":"+error.getMessage());
               volver();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization","Bearer "+ login.getToken());


                return headers;

            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                try {
                   barray=FileUtils.readFileToByteArray(passfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                params.put("file_path", new DataPart(passfile.getName(),barray));

                return params;
            }
        };
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(AudioOnTouchActivity.this);
        requestQueue.add(multipartRequest);

    }

    public void volver(){
        Intent retur=new Intent(AudioOnTouchActivity.this,Sounds_Screen.class);
        retur.putExtra("Datos",login);
        finish();
        startActivity(retur);
    }

}
