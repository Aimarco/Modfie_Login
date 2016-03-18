package com.example.furwin.modfie_login.Errores.BBDD;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by FurWin on 18/03/2016.
 */
public class GestionaBBDD  {

   /* public GestionaBBDD(SQLiteDatabase bbdd,String operacion,String tabla){
        switch(operacion.toString().toLowerCase()){
            case "select":
                String respuesta=gestionaSelect(bbdd,tabla,"*");break;
            case "insert":
                insertaDatos(bbdd,tabla,);



        }*/




    public String gestionaSelect(SQLiteDatabase db,String tabla,String params){
        String datos="";
        String query="Select "+params+" from "+tabla;
        Cursor c=db.rawQuery(query,null);
        while(c.moveToNext()) {
            datos += c.getString(1)+"\n"+c.getString(2)+"\n"+c.getString(3);

        }
        return datos;
    }

    public void insertaDatos(SQLiteDatabase db,String tabla,String[] params){

        ContentValues values = new ContentValues();

//Seteando body y author
        values.put("username",params[0]);
        values.put("password",params[1]);
        values.put("token",params[2]);

//Insertando en la base de datos
        db.insert(tabla,null,values);
    }



}
