package gt.com.kinal.juanlopez.schooltime.Activitys;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import BaseDatos.BDD_sqlite;
import Beans.Horario;
import Beans.Usuario;
import Sincro.cls_sincro;
import fragment.HorarioFragment;
import fragment.NavigationDrawerFragment;
import fragment.NotificacionFragment;
import fragment.TareasFragment;
import gt.com.kinal.juanlopez.schooltime.R;


public class Activity_Menu extends ActionBarActivity {
    private Toolbar mToolbar;
    private ViewPager mPager;
    private NavigationDrawerFragment mDrawerFragment;
    private LinearLayout linearNotifi,linerarTare,linearHora;
    private String resultado_proceso;

    public SQLiteDatabase db;
    public BDD_sqlite sqlite;

    String Usuario;

    public ListView listView;
    private SimpleDateFormat dateFormat;

    ArrayList<Horario> listaHorario = new ArrayList<Horario>();
    ArrayList<Horario> listBackupDatas = new ArrayList<Horario>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity__menu);

        llenarLista();
        setupDrawer();

    }
    private void setupDrawer() {
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout)findViewById(R.id.drawer_layout), mToolbar);
    }
    public void llenarLista() {
        BDD_sqlite usdbh = new BDD_sqlite(getBaseContext());
        SQLiteDatabase db = usdbh.getWritableDatabase();

        usdbh = new BDD_sqlite(getBaseContext());
        db = usdbh.getReadableDatabase();

        String Sql = "SELECT * FROM USUARIO";

        Cursor cc = db.rawQuery(Sql, null);
        Usuario obj;
        ArrayList<Usuario> listaUsuario = new ArrayList<>();
        if (cc.moveToFirst()) {
            do {
                obj = new Usuario();
                obj.setNombre(cc.getString(0));
                obj.setUsuario(cc.getString(1));
                listaUsuario.add(obj);
            } while (cc.moveToNext());
        }
        if (listaUsuario.size() > 0){
            for (int x = 0 ; x < listaUsuario.size(); x++){
                Usuario = listaUsuario.get(x).getUsuario().toString();
                new DownloadTask().execute("Tarea");
            }
        }
    }
    public void Messagebox(String mensaje) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Horario");

        builder.setMessage(mensaje);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    @SuppressLint("SimpleDateFormat") public String fechaActual()
    {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String nodocto = dateFormat.format(new Date());
        return nodocto;
    }

    private class DownloadTask extends AsyncTask<Object, Object, Object> {
        protected void onPostExecute(Object result) {


            if (resultado_proceso.equals("1")) {

            } else {

                String invalido = resultado_proceso;
                new DownloadTask1().execute("Tarea");
                Messagebox("-" + invalido);
            }

        }

        @Override
        protected Object doInBackground(Object... params) {
            cls_sincro sincronizacion = new cls_sincro();

            resultado_proceso = sincronizacion.GetCodigo(Usuario);

            if (resultado_proceso.substring(0, 1).equals("1")) {
                resultado_proceso = "1";
            } else {
            }
            return null;
        }
    }
    private class DownloadTask1 extends AsyncTask<Object, Object, Object> {
        protected void onPostExecute(Object result) {


            if (resultado_proceso.equals("1")) {

            } else {
                String invalido = resultado_proceso;
                Messagebox("-" + invalido);
            }

        }

        @Override
        protected Object doInBackground(Object... params) {
            cls_sincro sincronizacion = new cls_sincro();

            resultado_proceso = sincronizacion.GetHorario(getApplicationContext(), resultado_proceso, fechaActual());

            if (resultado_proceso.substring(0, 1).equals("1")) {
                resultado_proceso = "1";
            } else {
                String invalido = "No hay horario";
                resultado_proceso = invalido + " " + resultado_proceso;
            }
            return null;
        }
    }

    public void Nav(int index){
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (index){
            case 0:
                fragment = new NotificacionFragment();
                title = "Notificaciones";
                break;
            case 1:
                fragment = new TareasFragment();
                title = "Tareas";
                break;
            case 2:
                fragment = new HorarioFragment();
                title = "Horarios";
                break;

        }
        if (fragment != null) {
            ((RelativeLayout) findViewById(R.id.ContainerLayout)).removeAllViewsInLayout();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.ContainerLayout, fragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(title);
        }
    }

    public void onDrawerItemClicked(int index) {
        switch (index) {
            case 0:
                Nav(0);
                break;
            case 1:
                Nav(1);
                break;
            case 2:
                Nav(2);
                break;
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity__menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
