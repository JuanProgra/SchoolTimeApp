package com.kinal.appschooltime;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kinal.appschooltime.Sincronizacion.cls_sincro;


public class LoginActivity extends ActionBarActivity {
    private EditText edtUsuario;
    private EditText edtPassword;
    private Button btnLogin;
    private String resultado_proceso;
    public ProgressDialog pd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsuario = (EditText) findViewById(R.id.edtUser);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login(v);
            }
        });

    }

    public void Login(View v) {
        if (edtUsuario.getText().length() > 0) {
            if (edtPassword.getText().length() > 0) {
                this.pd = ProgressDialog.show(this, "Verificando credenciales", "Espere por favor", true, false);

                new DownloadTask().execute("Tarea");
            } else {
                Snackbar.make(v, "Ingrese su contraseña", Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(v, "Ingrese su usuario", Snackbar.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void Messagebox(String mensaje) {

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
            LoginActivity.this.pd.dismiss();

            if (resultado_proceso.equals("1")) {
                Intent mainIntent = new Intent().setClass(
                        LoginActivity.this, MenuActivity.class);
                startActivity(mainIntent);
                finish();
            } else {
                String invalido = resultado_proceso;
                Messagebox("-" + invalido);
            }

        }

        @Override
        protected Object doInBackground(Object... params) {
            cls_sincro sincronizacion = new cls_sincro();
            resultado_proceso = sincronizacion.GetUsuarioValido(getApplicationContext(), edtUsuario.getText().toString(), edtPassword.getText().toString());
            if (resultado_proceso.substring(0, 1).equals("1")) {
                resultado_proceso = "1";
            } else {
                String invalido = "Verifique credenciales";
                resultado_proceso = invalido + " " + resultado_proceso;
            }
            return null;
        }
    }

}
