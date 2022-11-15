package es.pruebas.reto1_example_2022;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import es.pruebas.reto1_example_2022.beans.Usuario;
import es.pruebas.reto1_example_2022.network.UsuariosFacade;

/**
 * Main Activity
 */
public class MainActivity extends AppCompatActivity {

    private EditText editUser;
    private EditText editPassword;
    private CheckBox recordarUsuario;
    Button iniciarSesion, registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editUser = findViewById(R.id.textUserLogin);
        editPassword = findViewById(R.id.textPasswordLogin);
        recordarUsuario = findViewById(R.id.recordarSesion);
        iniciarSesion = findViewById(R.id.botonIniciarLogin);
        registro = findViewById(R.id.botonRegistroLogin);

        //---DATOS RECOGIDOS DE EL INTENT DE RegisterActivity---
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            String login = extras.getString("Login");
            String password = extras.getString("Password");

            editUser.setText(login);
            editPassword.setText(password);
        }

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean login = inicioSesion();
                if(login){
                    Toast.makeText(getApplicationContext(),"HOLAA",Toast.LENGTH_SHORT);
                    Intent intentComunity = new Intent(MainActivity.this, ComunityActivity.class);
                    startActivity(intentComunity);
                }else{
                    Toast.makeText(getApplicationContext(), R.string.errorInicioSesion , Toast.LENGTH_SHORT).show();
                }
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

    private Boolean inicioSesion(){

        UsuariosFacade usuariosFacade = new UsuariosFacade();
        Thread thread = new Thread(usuariosFacade);
        try {
            thread.start();
            thread.join(); // Awaiting response from the server...
        } catch (InterruptedException e) {
            // Nothing to do here...
        }

        String usuarioString = editUser.getText().toString();
        String password = editPassword.getText().toString();
        Boolean existe = false;

        List<Usuario> personas = usuariosFacade.getResponse();

        for(int i = 0; i < personas.size();i++){
            if(personas.get(i).getEmail().equalsIgnoreCase(usuarioString)){
                if (personas.get(i).getPassword().equals(password)){
                    existe = true;
                    break;
                }else {
                    existe = false;
                }
            }else{
                existe = false;
            }
        }
        return existe;
    }

}