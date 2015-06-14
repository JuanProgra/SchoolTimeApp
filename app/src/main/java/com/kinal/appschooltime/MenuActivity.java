package com.kinal.appschooltime;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kinal.appschooltime.BaseDatos.BDD_sqlite;
import com.kinal.appschooltime.Fragment.HorarioFragment;
import com.kinal.appschooltime.Fragment.NotificaFragment;
import com.kinal.appschooltime.Fragment.TareaFragment;
import com.kinal.appschooltime.Sincronizacion.cls_sincro;
import com.kinal.appschooltime.beans.Usuario;

import java.util.ArrayList;
import java.util.List;


public class MenuActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private TextView userName;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mSelectedItemId;

    private String resultado_proceso;
    String Usuario;
    String Nombre;
    String Codigo;

    private final String KEY_NAV_SELECTED_ITEM = "NavigationSelectedItem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        llenarLista();
        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        setupDrawerToggle();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nvView);
        if (navigationView != null){
            userName = (TextView)navigationView.findViewById(R.id.userTe);
            userName.setText(Usuario);
            setupDrawerContent(navigationView, savedInstanceState);
        }

    }

    private void setupDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.drawer_open, R.string.drawer_close);
        mDrawer.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    private void setupDrawerContent(final NavigationView navigationView, Bundle savedInstanceState) {
        if (savedInstanceState != null){
            mSelectedItemId = savedInstanceState.getInt(KEY_NAV_SELECTED_ITEM, R.id.nav_first_fragment);
            fragmenNav(0);
        }
        else {
            mSelectedItemId = R.id.nav_first_fragment;
            fragmenNav(0);
        }

        navigationView.getMenu().findItem(mSelectedItemId).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (mSelectedItemId == menuItem.getItemId()) {
                    return true;
                }
                switch (menuItem.getItemId()) {
                    case R.id.nav_first_fragment:
                        fragmenNav(0);
                        break;
                    case R.id.nav_second_fragment:
                        fragmenNav(1);
                        break;
                }

                menuItem.setChecked(true);
                mSelectedItemId = menuItem.getItemId();
                mDrawer.closeDrawer(Gravity.LEFT);
                return true;
            }
        });
    }
    public void fragmenNav (int nav) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (nav) {
            case 0:
                fragment = new HorarioFragment();
                title = "Horario";
                break;
            case 1:
                fragment = new TareaFragment();
                title = "Tarea";
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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        switch (item.getItemId()){
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_NAV_SELECTED_ITEM, mSelectedItemId);
    }

    @Override
    public void onBackPressed() {
        if(mDrawer.isDrawerOpen(Gravity.LEFT)){
            mDrawer.closeDrawer(Gravity.LEFT);
        }
        else{
            super.onBackPressed();
        }
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
                Usuario = listaUsuario.get(x).getNombre().toString();
                Nombre = listaUsuario.get(x).getUsuario().toString();
                new DownloadTask().execute("Tarea");
            }
        }
    }
    private class DownloadTask extends AsyncTask<Object, Object, Object> {
        protected void onPostExecute(Object result) {


            if (resultado_proceso.equals("1")) {

            } else {
                new DownloadTask1().execute("Tarea");

            }

        }

        @Override
        protected Object doInBackground(Object... params) {
            cls_sincro sincronizacion = new cls_sincro();

            resultado_proceso = sincronizacion.GetCodigo(Nombre);
            Codigo = resultado_proceso;
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
                new DownloadTask2().execute("Tarea");
            } else {
                String invalido = resultado_proceso;
                Messagebox("-" + invalido);
            }

        }

        @Override
        protected Object doInBackground(Object... params) {
            cls_sincro sincronizacion = new cls_sincro();

            resultado_proceso = sincronizacion.GetHorario(getApplicationContext(),Codigo);

            if (resultado_proceso.substring(0, 1).equals("1")) {
                resultado_proceso = "1";
            } else {
                String invalido = "No hay horario";
                resultado_proceso = invalido + " " + resultado_proceso;
            }
            return null;
        }
    }
    private class DownloadTask2 extends AsyncTask<Object, Object, Object> {
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

            resultado_proceso = sincronizacion.GetMateria(getApplicationContext(),Codigo);

            if (resultado_proceso.substring(0, 1).equals("1")) {
                resultado_proceso = "1";
            } else {
                String invalido = "No hay horario";
                resultado_proceso = invalido + " " + resultado_proceso;
            }
            return null;
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
    
}
