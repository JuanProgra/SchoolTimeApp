package gt.com.kinal.juanlopez.schooltime.Activitys;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import gt.com.kinal.juanlopez.schooltime.R;


public class Activity_login extends ActionBarActivity {


    private EditText edtUser;
    private EditText edtPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_login);

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
                Intent mainIntent = new Intent().setClass(
                        Activity_login.this, Activity_Menu.class);
                startActivity(mainIntent);
                finish();
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
