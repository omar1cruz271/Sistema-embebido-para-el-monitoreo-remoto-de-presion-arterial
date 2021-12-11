package com.example.materialdesign.activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbmsSQLiteHelper extends SQLiteOpenHelper {
    String sqlCreate = "CREATE TABLE Pacientes (id INTEGER PRIMARY KEY AUTOINCREMENT, presionS TEXT NOT NULL, presionD TEXT NOT NULL,fecha TEXT NOT NULL, hora TEXT NOT NULL ,numero TEXT NOT NULL)";
    String CreateP = "CREATE TABLE Pacientes_Registrados (curp TEXT PRIMARY KEY, nombre TEXT NOT NULL, apellidoP TEXT NOT NULL,apellidoM TEXT NOT NULL, nacimiento TEXT NOT NULL ,sexo TEXT NOT NULL ,numero TEXT NOT NULL)";
    public DbmsSQLiteHelper(Context c, String s, SQLiteDatabase.CursorFactory cf, int v){
        super(c, s, cf, v);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CreateP);
        db.execSQL(sqlCreate);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqld, int ov, int nv) {
        sqld.execSQL("DROP TABLE IF EXISTS Pacientes");
        sqld.execSQL("DROP TABLE IF EXISTS Pacientes_Registrados");
        sqld.execSQL(sqlCreate);
        sqld.execSQL(CreateP);
    }
}
