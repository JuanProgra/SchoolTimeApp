package com.kinal.appschooltime.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kinal.appschooltime.BaseDatos.BDD_sqlite;
import com.kinal.appschooltime.R;
import com.kinal.appschooltime.beans.Horario;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateChangedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class HorarioFragment extends Fragment implements OnDateChangedListener {
    private SimpleDateFormat format = new SimpleDateFormat("EEEE");
    private TextView textView;
    private ListView listView;

    public SQLiteDatabase db;
    public BDD_sqlite sqlite;

    public HorarioAdapter adapter;

    String s;

    ArrayList<Horario> listaHorario = new ArrayList<Horario>();
    ArrayList<Horario> listBackupDatas = new ArrayList<Horario>();

    public HorarioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horario, container, false);

        textView = (TextView) view.findViewById(R.id.textView);
        listView = (ListView) view.findViewById(R.id.listView);

        MaterialCalendarView widget = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        Calendar calendar = Calendar.getInstance();
        widget.setSelectedDate(calendar.getTime());
        widget.setOnDateChangedListener(this);

        return view;
    }

    @Override
    public void onDateChanged(MaterialCalendarView widget, CalendarDay date) {
        s = format.format(date.getDate());
        llenarLista(s);
        textView.setText(s);
    }

    public void llenarLista(String p) {
        listaHorario.clear();
        listBackupDatas.clear();

        sqlite = new BDD_sqlite(getActivity().getBaseContext());
        db = sqlite.getReadableDatabase();

        String Sql = "SELECT * FROM HORARIO WHERE DIA='" + p + "'";

        Cursor cc = db.rawQuery(Sql, null);
        Horario obj;

        if (cc.moveToFirst()) {
            do {
                obj = new Horario();
                obj.setHora(cc.getString(0));
                obj.setSalon(cc.getString(1));
                obj.setMateria(cc.getString(2));

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
                holder.Hora = (TextView) convertView.findViewById(R.id.textHora);
                holder.thumb = (ImageView) convertView.findViewById(R.id.imageView);

                convertView.setTag(holder);
            } else {
                holder = (PlayerViewHolder) convertView.getTag();
            }
            holder.Materia.setText(getItem(position).Materia);
            holder.Salon.setText(getItem(position).Salon);
            holder.Hora.setText(getItem(position).Hora);

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


