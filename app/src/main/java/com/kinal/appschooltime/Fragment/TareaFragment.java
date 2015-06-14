package com.kinal.appschooltime.Fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kinal.appschooltime.BaseDatos.BDD_sqlite;
import com.kinal.appschooltime.DetalleActivity;
import com.kinal.appschooltime.R;
import com.kinal.appschooltime.Sincronizacion.cls_sincro;
import com.kinal.appschooltime.beans.Materia;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TareaFragment extends Fragment {

    private TextView textView;
    private ListView listView;

    public SQLiteDatabase db;
    public BDD_sqlite sqlite;
    private String resultado_proceso;

    public MateriaAdapter adapter;

    String s;
    String materia;

    ArrayList<Materia> listaMateria = new ArrayList<Materia>();
    ArrayList<Materia> listBackupDatas = new ArrayList<Materia>();


    public TareaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tarea, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        materia = "";
        llenarLista();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                materia = listaMateria.get(position).getNombre();
                new DownloadTask().execute("Tarea");
            }
        });
        return view;
    }

    public void llenarLista() {
        listaMateria.clear();
        listBackupDatas.clear();

        sqlite = new BDD_sqlite(getActivity().getBaseContext());
        db = sqlite.getReadableDatabase();

        String Sql = "SELECT * FROM MATERIA";

        Cursor cc = db.rawQuery(Sql, null);
        Materia obj;

        if (cc.moveToFirst()) {
            do {
                obj = new Materia();
                obj.setNombre(cc.getString(0));

                listaMateria.add(obj);
                listBackupDatas.add(obj);

            } while (cc.moveToNext());
        }
        adapter = new MateriaAdapter();
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class DownloadTask extends AsyncTask<Object, Object, Object> {
        protected void onPostExecute(Object result) {


            if (resultado_proceso.equals("1")) {
                Intent iMod = new Intent(getActivity(), DetalleActivity.class);
                iMod.putExtra("codigo", materia);
                startActivity(iMod);
            } else {
                String invalido = resultado_proceso;
                Messagebox("-" + invalido);
            }

        }

        @Override
        protected Object doInBackground(Object... params) {
            cls_sincro sincronizacion = new cls_sincro();

            resultado_proceso = sincronizacion.GetTarea(getActivity().getApplicationContext(), materia);

            if (resultado_proceso.substring(0, 1).equals("1")) {
                resultado_proceso = "1";
            } else {
            }
            return null;
        }
    }

    public void Messagebox(String mensaje) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Horario");

        builder.setMessage(mensaje);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }


    private class MateriaAdapter extends BaseAdapter implements
            Filterable {
        private MateriaFilter materiaFilter;

        @Override
        public int getCount() {
            return listaMateria.size();
        }

        @Override
        public Filter getFilter() {
            if (materiaFilter == null)
                materiaFilter = new MateriaFilter();
            return materiaFilter;
        }

        @Override
        public Materia getItem(int position) {
            return listaMateria.get(position);
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
                        R.layout.item_materia, null);
                holder = new PlayerViewHolder();

                holder.Materia = (TextView) convertView.findViewById(R.id.textMateri);

                convertView.setTag(holder);
            } else {
                holder = (PlayerViewHolder) convertView.getTag();
            }
            holder.Materia.setText(getItem(position).getNombre());

            return convertView;
        }

    }

    private class MateriaFilter extends Filter {
        @SuppressLint("DefaultLocale")
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // 	We implement here the filter logic
            ArrayList<Materia> filters = new ArrayList<Materia>();
            if (constraint == null || constraint.length() == 0) {
                // 	No filter implemented we return all the list
                for (Materia player : listBackupDatas) {
                    filters.add(player);
                }
                results.values = filters;
                results.count = filters.size();
            } else {
                //We perform filtering operation
                for (Materia row : listBackupDatas) {
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
                listaMateria.clear();
                adapter.notifyDataSetInvalidated();
            } else {
                listaMateria.clear();
                @SuppressWarnings("unchecked")
                ArrayList<Materia> resultList = (ArrayList<Materia>) results.values;
                for (Materia row : resultList) {
                    listaMateria.add(row);
                    adapter.notifyDataSetChanged();
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    private static class PlayerViewHolder {
        public ImageView thumb;
        public TextView Materia;
    }


}
