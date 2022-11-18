package es.pruebas.reto1_example_2022;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import es.pruebas.reto1_example_2022.beans.Usuario;
import es.pruebas.reto1_example_2022.network.UsuariosFacade;

/**
 * Main Activity
 */

public class MainActivity extends AppCompatActivity {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void initializeCamera() {
        super.onResume();
        recordarUsuario.setActivated(true);
    }

    private EditText editUser;
    private EditText editPassword;
    private CheckBox recordarUsuario;
    Button iniciarSesion, registro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataManager dataManager = new DataManager(this);

        editUser = findViewById(R.id.textUserLogin);
        editPassword = findViewById(R.id.textPasswordLogin);
        recordarUsuario = findViewById(R.id.recordarSesion);
        iniciarSesion = findViewById(R.id.botonIniciarLogin);
        registro = findViewById(R.id.botonRegistroLogin);

        recuerdameSetIntoText();

        //---DATOS RECOGIDOS DE EL INTENT DE RegisterActivity---
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String login = extras.getString("Login");
            String password = extras.getString("Password");

            editUser.setText(login);
            editPassword.setText(password);
        }

        iniciarSesion.setOnClickListener(view -> {
            boolean login = inicioSesion();
            Usuario usuario = new Usuario();
            usuario.setEmail(editUser.getText().toString());
            usuario.setPassword(editPassword.getText().toString());
            deleteAllFromDB();
            if (recordarUsuario.isChecked()) {
                deleteAllFromDB();
                dataManager.insert(usuario);

            } else if (!recordarUsuario.isChecked()) {
                deleteAllFromDB();
            }

            if (login) {
                if (!recordarUsuario.isChecked()) {
                    deleteAllFromDB();
                }
                Intent intentComunity = new Intent(MainActivity.this, ComunityActivity.class);

                intentComunity.putExtra("emailUsuario", usuario.getEmail());
                startActivity(intentComunity);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), R.string.errorInicioSesion, Toast.LENGTH_SHORT).show();
            }
        });

        recordarUsuario.setOnCheckedChangeListener((compoundButton, checked) -> {
            Usuario usuario = new Usuario();
            usuario.setEmail(editUser.getText().toString());
            usuario.setPassword(editPassword.getText().toString());
            deleteAllFromDB();
            if (recordarUsuario.isChecked()) {
                deleteAllFromDB();
                dataManager.insert(usuario);
                recordarUsuario.setActivated(true);

            } else if (!recordarUsuario.isChecked()) {
                deleteAllFromDB();
            }
        });

        registro.setOnClickListener(view -> {
            Intent intentRegister = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intentRegister);
        });

    }

    private Boolean inicioSesion() {

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
        boolean existe = false;

        List<Usuario> personas = usuariosFacade.getResponse();

        for (int i = 0; i < personas.size(); i++) {
            if (personas.get(i).getEmail().equalsIgnoreCase(usuarioString)) {
                if (personas.get(i).getPassword().equals(password)) {
                    existe = true;
                    break;
                }
            }
        }
        return existe;
    }


    public void recuerdameSetIntoText() {
        DataManager dataManager = new DataManager(this);

        List<Usuario> user = dataManager.selectAllUsers();
        if (user.size() != 0) {
            recordarUsuario.setActivated(true);
            editUser.setText(user.get(0).getEmail());
            editPassword.setText(user.get(0).getPassword());

        }
    }

    public void deleteAllFromDB() {
        DataManager dataManager = new DataManager(this);
        List<Usuario> usuariosListBorrar = dataManager.selectAllUsers();

        if (usuariosListBorrar.size() != 0) {
            for (int i = 0; i < usuariosListBorrar.size(); i++) {
                dataManager.deleteByEmail(usuariosListBorrar.get(i).getEmail());
            }
        }
    }

}