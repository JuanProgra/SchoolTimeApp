package com.kinal.appschooltime;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kinal.appschooltime.BaseDatos.BDD_sqlite;
import com.kinal.appschooltime.beans.Tarea;
import com.kinal.appschooltime.beans.Tarea;

import java.util.ArrayList;


public class DetalleActivity extends ActionBarActivity {
    String codigo;

    private ListView listView;

    public TareaAdapter adapter;

    public SQLiteDatabase db;
    public BDD_sqlite sqlite;

    ArrayList<Tarea> listaTarea = new ArrayList<Tarea>();
    ArrayList<Tarea> listBackupDatas = new ArrayList<Tarea>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        listView = (ListView) findViewById(R.id.listView);

        Bundle bun = getIntent().getExtras();
        codigo= bun.getString("codigo");

        llenarLista(codigo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        collapsingToolbar.setTitle(codigo);
    }
    public void llenarLista(String codigo) {
        listaTarea.clear();
        listBackupDatas.clear();

        sqlite = new BDD_sqlite(this.getBaseContext());
        db = sqlite.getReadableDatabase();

        String Sql = "SELECT * FROM TAREA WHERE NOMBRETARE='"+codigo+"'";

        Cursor cc = db.rawQuery(Sql, null);
        Tarea obj;

        if (cc.moveToFirst()) {
            do {
                obj = new Tarea();
                obj.setNombreTare(cc.getString(0));
                obj.setNombre(cc.getString(1));
                obj.setDetalle(cc.getString(2));
                obj.setFecha(cc.getString(3));
                obj.setPunteo(cc.getString(4));

                listaTarea.add(obj);
                listBackupDatas.add(obj);

            } while (cc.moveToNext());
        }
        adapter = new TareaAdapter();
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalle, menu);
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
        if (id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class TareaAdapter extends BaseAdapter implements
            Filterable {
        private MateriaFilter materiaFilter;

        @Override
        public int getCount() {
            return listaTarea.size();
        }

        @Override
        public Filter getFilter() {
            if (materiaFilter == null)
                materiaFilter = new MateriaFilter();
            return materiaFilter;
        }

        @Override
        public Tarea getItem(int position) {
            return listaTarea.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PlayerViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(
                        getApplicationContext()).inflate(
                        R.layout.item_tarea, null);
                holder = new PlayerViewHolder();

                holder.Nombre = (TextView) convertView.findViewById(R.id.textNombreT);
                holder.Detalle = (TextView) convertView.findViewById(R.id.textDetalleT);
                holder.Fecha = (TextView) convertView.findViewById(R.id.textFechaT);
                holder.Punteo = (TextView) convertView.findViewById(R.id.textPunteoT);

                convertView.setTag(holder);
            } else {
                holder = (PlayerViewHolder) convertView.getTag();
            }
            holder.Nombre.setText(getItem(position).getNombre());
            holder.Detalle.setText(getItem(position).getDetalle());
            holder.Fecha.setText(getItem(position).getFecha().substring(0,10));
            holder.Punteo.setText(getItem(position).getPunteo());

            return convertView;
        }

    }

    private class MateriaFilter extends Filter {
        @SuppressLint("DefaultLocale")
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // 	We implement here the filter logic
            ArrayList<Tarea> filters = new ArrayList<Tarea>();
            if (constraint == null || constraint.length() == 0) {
                // 	No filter implemented we return all the list
                for (Tarea player : listBackupDatas) {
                    filters.add(player);
                }
                results.values = filters;
                results.count = filters.size();
            } else {
                //We perform filtering operation
                for (Tarea row : listBackupDatas) {
                    if (row.Nombre.toUpperCase().startsWith(
                            constraint.toString().toUpperCase())) {
                        filters.add(row);
                    }
                }
                results.values = filters;
                results.count = filters.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            if (results.count == 0) {
                listaTarea.clear();
                adapter.notifyDataSetInvalidated();
            } else {
                listaTarea.clear();
                @SuppressWarnings("unchecked")
                ArrayList<Tarea> resultList = (ArrayList<Tarea>) results.values;
                for (Tarea row : resultList) {
                    listaTarea.add(row);
                    adapter.notifyDataSetChanged();
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    private static class PlayerViewHolder {
        public ImageView thumb;
        public TextView Nombre;
        public TextView Detalle;
        public TextView Fecha;
        public TextView Punteo;

    }
}
