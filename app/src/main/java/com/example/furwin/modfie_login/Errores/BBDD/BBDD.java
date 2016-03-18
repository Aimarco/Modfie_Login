package com.example.furwin.modfie_login.Errores.BBDD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by FurWin on 18/03/2016.
 */
public class BBDD extends SQLiteOpenHelper {
    String SqlCreate="CREATE TABLE Usuarios(idusuario PRIMARY KEY,username TEXT,password TEXT,token TEXT)";

    public BBDD(Context contexto, String nombre,
                SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    public void onCreate(SQLiteDatabase db){
        //exec the table creation command
        db.execSQL(SqlCreate);
    }

    public void onUpgrade(SQLiteDatabase db,int oldversion,int newversion){
        //Por Implementar


    }


}
