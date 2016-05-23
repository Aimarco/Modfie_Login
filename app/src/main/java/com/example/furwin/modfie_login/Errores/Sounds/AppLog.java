package com.example.furwin.modfie_login.Errores.Sounds;

/**
 * Created by Aimar on 19/04/2016.
 */
import android.util.Log;

public class AppLog {
    private static final String APP_TAG = "AudioRecorder";

    public static int logString(String message) {
        return Log.i(APP_TAG, message);
    }
}