package BaseDatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Godinez Miranda on 24/05/2015.
 */
public class BDD_sqlite extends SQLiteOpenHelper {
    private static CursorFactory factory = null;

    public BDD_sqlite(Context ctx)
    {
        super(ctx, "SchoolTime", factory, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query = "CREATE TABLE HORARIO(HORA TEXT,SALON TEXT,MATERIA TEXT,PROFESOR TEXT,DIA TEXT);";
        db.execSQL(query);

        query = "CREATE TABLE USUARIO(NOMBREAP TEXT,USUARIO TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //Se elimina la version anterior de la tabla

        db.execSQL("DROP TABLE IF EXISTS HORARIO");
        db.execSQL("DROP TABLE IF EXISTS USUARIO");
        //Se crea la nueva version de la tabla
        onCreate(db);
    }
}
