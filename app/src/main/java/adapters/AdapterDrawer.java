package adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import BaseDatos.BDD_sqlite;
import Beans.Horario;
import Beans.Information;
import Beans.Usuario;
import gt.com.kinal.juanlopez.schooltime.R;

/**
 * Created by Godinez Miranda on 23/05/2015.
 */
public class AdapterDrawer extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Information> data= Collections.emptyList();

    private TextView usuario;
    private TextView nombre;

    private static final int TYPE_HEADER=0;
    private static final int TYPE_ITEM=1;
    private LayoutInflater inflater;
    private Context context;
    public AdapterDrawer(Context context, List<Information> data){
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.data=data;
    }

    public void delete(int position){
        data.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_HEADER){
            View view=inflater.inflate(R.layout.drawer_header, parent,false);
            usuario = (TextView)view.findViewById(R.id.textUsuario);
            nombre = (TextView)view.findViewById(R.id.textNombre);
            llenarLista();
            HeaderHolder holder=new HeaderHolder(view);

            return holder;
        }
        else{
            View view=inflater.inflate(R.layout.item_drawer, parent,false);
            ItemHolder holder=new ItemHolder(view);
            return holder;
        }

    }
    public void llenarLista() {
        BDD_sqlite usdbh = new BDD_sqlite(this.context);
        SQLiteDatabase db = usdbh.getWritableDatabase();

        usdbh = new BDD_sqlite(this.context);
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
                usuario.setText(listaUsuario.get(x).getUsuario().toString());
                nombre.setText(listaUsuario.get(x).getNombre().toString());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return TYPE_HEADER;
        }
        else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderHolder ){

        }
        else{
            ItemHolder itemHolder= (ItemHolder) holder;
            Information current=data.get(position-1);
            itemHolder.title.setText(current.title);
            itemHolder.icon.setImageResource(current.iconId);
        }

    }
    @Override
    public int getItemCount() {
        return data.size()+1;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;
        public ItemHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.listText);
            icon= (ImageView) itemView.findViewById(R.id.listIcon);
        }
    }
    class HeaderHolder extends RecyclerView.ViewHolder {

        public HeaderHolder(View itemView) {
            super(itemView);

        }
    }
}
