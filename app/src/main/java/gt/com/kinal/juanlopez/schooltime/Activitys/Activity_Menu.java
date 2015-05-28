package gt.com.kinal.juanlopez.schooltime.Activitys;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import BaseDatos.BDD_sqlite;
import Beans.Horario;
import fragment.NavigationDrawerFragment;
import gt.com.kinal.juanlopez.schooltime.R;


public class Activity_Menu extends ActionBarActivity {
    private Toolbar mToolbar;
    private ViewPager mPager;
    private NavigationDrawerFragment mDrawerFragment;
    private LinearLayout linearNotifi,linerarTare,linearHora;

    public SQLiteDatabase db;
    public BDD_sqlite sqlite;

    public HorarioAdapter adapter;
    public ListView listView;

    ArrayList<Horario> listaHorario = new ArrayList<Horario>();
    ArrayList<Horario> listBackupDatas = new ArrayList<Horario>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity__menu);

        linearNotifi = (LinearLayout)findViewById(R.id.LinearNotifi);
        linerarTare = (LinearLayout)findViewById(R.id.LinearTarea);
        linearHora = (LinearLayout)findViewById(R.id.LinearHorario);
        listView = (ListView)findViewById(R.id.listViewT);

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

    public void Nav(int index){
        switch (index){
            case 0:
                linearNotifi.setVisibility(View.VISIBLE);
                linerarTare.setVisibility(View.INVISIBLE);
                linearHora.setVisibility(View.INVISIBLE);
                break;
            case 1:
                linearNotifi.setVisibility(View.INVISIBLE);
                linerarTare.setVisibility(View.VISIBLE);
                linearHora.setVisibility(View.INVISIBLE);
                break;
            case 2:
                linearNotifi.setVisibility(View.INVISIBLE);
                linerarTare.setVisibility(View.INVISIBLE);
                linearHora.setVisibility(View.VISIBLE);
                break;

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
                llenarHorario();
                llenarLista();
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
    public void llenarHorario() {
        try {
            sqlite = new BDD_sqlite(getBaseContext());
            db = sqlite.getReadableDatabase();

            ContentValues horarioConte = new ContentValues();

            String sql1 = "DELETE FROM HORARIO";
            db.execSQL(sql1);
            db.close();

            db = sqlite.getReadableDatabase();

            horarioConte.put("HORA", "12:40");
            horarioConte.put("SALON", "C-39");
            horarioConte.put("MATERIA", "Matematicas");
            horarioConte.put("PROFESOR", "Jose Francisco Noj");

            db.insert("HORARIO", null, horarioConte);
            db.close();

            db = sqlite.getReadableDatabase();

            horarioConte.put("HORA", "13:15");
            horarioConte.put("SALON", "C-35");
            horarioConte.put("MATERIA", "Lenguaje");
            horarioConte.put("PROFESOR", "Alexander Mayen");

            db.insert("HORARIO", null, horarioConte);
            db.close();

            db = sqlite.getReadableDatabase();

            horarioConte.put("HORA", "13:50");
            horarioConte.put("SALON", "C-22");
            horarioConte.put("MATERIA", "Idioma Ingles");
            horarioConte.put("PROFESOR", "Junior Flores");

            db.insert("HORARIO", null, horarioConte);
            db.close();

            db = sqlite.getReadableDatabase();

            horarioConte.put("HORA", "14:25");
            horarioConte.put("SALON", "C-38");
            horarioConte.put("MATERIA", "Estadistica");
            horarioConte.put("PROFESOR", "Jose Francisco Noj");

            db.insert("HORARIO", null, horarioConte);
            db.close();
        } catch (SQLException e) {
            db.close();
            String message = e.toString();
            Toast.makeText(this, "0)" + message, Toast.LENGTH_LONG).show();
        }

    }
    public void llenarLista() {
        listaHorario.clear();
        listBackupDatas.clear();

        sqlite = new BDD_sqlite(getBaseContext());
        db = sqlite.getReadableDatabase();

        String Sql = "SELECT * FROM HORARIO";

        Cursor cc = db.rawQuery(Sql, null);
        Horario obj;

        if (cc.moveToFirst()) {
            do {
                obj = new Horario();
                obj.setHora(cc.getString(0));
                obj.setSalon(cc.getString(1));
                obj.setMateria(cc.getString(2));
                obj.setProfesor(cc.getString(3));


                listaHorario.add(obj);
                listBackupDatas.add(obj);

            } while (cc.moveToNext());
        }
        adapter = new HorarioAdapter();
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private class HorarioAdapter extends BaseAdapter implements
            Filterable {
        private HorarioFilter horarioFilter;

        @Override
        public int getCount() {
            return listaHorario.size();
        }

        @Override
        public Filter getFilter() {
            if (horarioFilter == null)
                horarioFilter = new HorarioFilter();
            return horarioFilter;
        }

        @Override
        public Horario getItem(int position) {
            return listaHorario.get(position);
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
                        R.layout.item_horario, null);
                holder = new PlayerViewHolder();

                holder.Materia = (TextView) convertView.findViewById(R.id.textMateria);
                holder.Salon = (TextView) convertView.findViewById(R.id.textSalon);
                holder.Profesor = (TextView) convertView.findViewById(R.id.textProfesor);
                holder.Hora = (TextView) convertView.findViewById(R.id.textHora);
                holder.thumb = (ImageView) convertView.findViewById(R.id.imageView);

                convertView.setTag(holder);
            } else {
                holder = (PlayerViewHolder) convertView.getTag();
            }
            holder.Materia.setTextColor(R.color.colorSecondaryText);
            holder.Materia.setText(getItem(position).Materia);
            holder.Profesor.setText(getItem(position).Profesor);
            holder.Profesor.setTextColor(R.color.colorSecondaryText);
            holder.Salon.setText(getItem(position).Salon);
            holder.Salon.setTextColor(R.color.colorSecondaryText);
            holder.Hora.setText(getItem(position).Hora);
            holder.Hora.setTextColor(R.color.colorSecondaryText);

            return convertView;
        }

    }

    private class HorarioFilter extends Filter {
        @SuppressLint("DefaultLocale")
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // 	We implement here the filter logic
            ArrayList<Horario> filters = new ArrayList<Horario>();
            if (constraint == null || constraint.length() == 0) {
                // 	No filter implemented we return all the list
                for (Horario player : listBackupDatas) {
                    filters.add(player);
                }
                results.values = filters;
                results.count = filters.size();
            } else {
                //We perform filtering operation
                for (Horario row : listBackupDatas) {
                    if (row.Materia.toUpperCase().startsWith(
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
                listaHorario.clear();
                adapter.notifyDataSetInvalidated();
            } else {
                listaHorario.clear();
                @SuppressWarnings("unchecked")
                ArrayList<Horario> resultList = (ArrayList<Horario>) results.values;
                for (Horario row : resultList) {
                    listaHorario.add(row);
                    adapter.notifyDataSetChanged();
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    private static class PlayerViewHolder {
        public ImageView thumb;
        public TextView Salon;
        public TextView Profesor;
        public TextView Materia;
        public TextView Hora;
    }
}
