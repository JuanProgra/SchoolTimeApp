package gt.com.kinal.juanlopez.schooltime.Activitys;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Sincro.cls_sincro;
import gt.com.kinal.juanlopez.schooltime.R;


public class Activity_login extends ActionBarActivity {


    private EditText edtUser;
    private EditText edtPassword;
    private Button btnLogin;
    String user,password;
    private String resultado_proceso;
    public ProgressDialog pd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_login);

        user = "";
        password = "";

        edtUser = (EditText) findViewById(R.id.edtUser);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autheLogin();
            }
        });

    }
    public void autheLogin()
    {
        if (edtUser.getText().length() > 0)
        {
            if (edtPassword.getText().length() > 0)
            {
                user = edtUser.getText().toString();
                password = edtPassword.getText().toString();

                this.pd = ProgressDialog.show(this, "Verificando credenciales", "Espere por favor", true, false);

                new DownloadTask().execute("Tarea");
            }
            else
            {
                Toast.makeText(this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "Ingrese su usuario", Toast.LENGTH_SHORT).show();
        }
    }
    public void Messagebox (String mensaje) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Credenciales");

        builder.setMessage(mensaje);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private class DownloadTask extends AsyncTask<Object, Object, Object> {
        protected void onPostExecute(Object result) {
            Activity_login.this.pd.dismiss();

            if (resultado_proceso.equals("1")){
                Intent mainIntent = new Intent().setClass(
                        Activity_login.this, Activity_Menu.class);
                startActivity(mainIntent);
                finish();
            }else{
                String invalido = resultado_proceso;
                Messagebox ("-"+invalido);
            }

        }

        @Override
        protected Object doInBackground(Object... params) {
            cls_sincro sincronizacion = new cls_sincro();
            resultado_proceso = sincronizacion.GetUsuarioValido(getApplicationContext(),user,password);
            if (resultado_proceso.substring(0,1).equals("1")){
                resultado_proceso = "1";
            }else{
                String invalido = "Verifique credenciales";
                resultado_proceso = invalido+" " + resultado_proceso;
            }
            return null;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_login, menu);
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
