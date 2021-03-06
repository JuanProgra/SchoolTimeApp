package com.kinal.appschooltime.BaseDatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Godinez Miranda on 13/06/2015.
 */
public class BDD_sqlite extends SQLiteOpenHelper {
    private static CursorFactory factory = null;

    public BDD_sqlite(Context ctx)
    {
        super(ctx, "SchoolTime", factory, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query = "CREATE TABLE HORARIO(HORA TEXT,SALON TEXT,MATERIA TEXT,DIA TEXT);";
        db.execSQL(query);

        query = "CREATE TABLE USUARIO(NOMBREAP TEXT,USUARIO TEXT);";
        db.execSQL(query);

        query = "CREATE TABLE MATERIA(NOMBRE TEXT);";
        db.execSQL(query);

        query = "CREATE TABLE TAREA(NOMBRETARE TEXT,NOMBRE TEXT,DETALLE TEXT,FECHA TEXT,PUNTEO TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //Se elimina la version anterior de la tabla

        db.execSQL("DROP TABLE IF EXISTS HORARIO");
        db.execSQL("DROP TABLE IF EXISTS USUARIO");
        db.execSQL("DROP TABLE IF EXISTS MATERIA");
        //Se crea la nueva version de la tabla
        onCreate(db);
    }
}
