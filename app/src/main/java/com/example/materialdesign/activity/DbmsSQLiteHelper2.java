package com.example.materialdesign.activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbmsSQLiteHelper2 extends SQLiteOpenHelper {
    String sqlCreate = "CREATE TABLE Pacientes_Registrados (curp TEXT PRIMARY KEY, nombre TEXT NOT NULL, apellidoP TEXT NOT NULL,apellidoM TEXT NOT NULL, nacimiento TEXT NOT NULL ,sexo TEXT NOT NULL ,numero TEXT NOT NULL)";
    public DbmsSQLiteHelper2(Context c, String s, SQLiteDatabase.CursorFactory cf, int v){
        super(c, s, cf, v);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(sqlCreate);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqld, int ov, int nv) {
        sqld.execSQL("DROP TABLE IF EXISTS Pacientes_Registrados");
        sqld.execSQL(sqlCreate);
    }
}
