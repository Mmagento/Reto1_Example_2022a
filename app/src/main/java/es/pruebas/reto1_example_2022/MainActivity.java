package es.pruebas.reto1_example_2022;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.pruebas.reto1_example_2022.adapters.MyTableAdapter;
import es.pruebas.reto1_example_2022.beans.Video;
import es.pruebas.reto1_example_2022.network.VideoFacade;
import es.pruebas.reto1_example_2022.network.VideosFacade;

/**
 * Main Activity
 */
public class MainActivity extends AppCompatActivity {

    private EditText editUser;
    private EditText editPassword;
    private CheckBox recordarUsuario;
    Button inicarSesion, registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        String login = extras.getString("Login");
        String password = extras.getString("Password");

        editUser = findViewById(R.id.textUserLogin);
        editPassword = findViewById(R.id.textPasswordLogin);
        recordarUsuario = findViewById(R.id.recordarSesion);
        inicarSesion = findViewById(R.id.botonIniciarLogin);
        registro = findViewById(R.id.botonRegistroLogin);

        Intent intentComunity = new Intent(MainActivity.this, ComunityActivity.class);

        inicarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pasarMinusculas();
                inicioSesion();
                //intentComunity.putExtra("email", editUser);

            }
        });

        recordarUsuario.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

            }

        });

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegister = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intentRegister); }
        });

    }

    protected void pasarMinusculas(){

        editUser.toString().toLowerCase();
        editPassword.toString().toLowerCase();
    }

    private void inicioSesion(){

        String usuario = editUser.toString();
        String password = editPassword.toString();

        DataManager data = new DataManager(this);
        data.getWritableDatabase();
        List<Users> personas = data.selectAllUsers();


        for(int i = 0; i<personas.size();i++){

            if(personas.get(i).getEmail().equalsIgnoreCase(usuario)){

                //esta bien
            }else{


                //no esta bien
            }



        }


    }

}