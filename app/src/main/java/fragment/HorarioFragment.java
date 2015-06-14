package fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import BaseDatos.BDD_sqlite;
import Beans.Horario;
import Sincro.cls_sincro;
import gt.com.kinal.juanlopez.schooltime.Activitys.Activity_Menu;
import gt.com.kinal.juanlopez.schooltime.R;


public class HorarioFragment extends Fragment {

    private Button button;
    String codigo, time, fecha;
    private TextView text;

    public SQLiteDatabase db;
    public BDD_sqlite sqlite;

    public HorarioAdapter adapter;
    public ListView listView;

    private String resultado_proceso;

    ArrayList<Horario> listaHorario = new ArrayList<Horario>();
    ArrayList<Horario> listBackupDatas = new ArrayList<Horario>();

    public HorarioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horario, container, false);

        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    public void llenarLista() {
        listaHorario.clear();
        listBackupDatas.clear();

        sqlite = new BDD_sqlite(getActivity().getBaseContext());
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
                        getActivity().getApplicationContext()).inflate(
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
