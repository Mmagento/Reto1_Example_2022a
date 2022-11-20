package es.pruebas.reto1_example_2022;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.regex.Pattern;

import es.pruebas.reto1_example_2022.beans.Usuario;
import es.pruebas.reto1_example_2022.network.UsuarioPost;
import es.pruebas.reto1_example_2022.network.UsuariosFacade;

public class RegisterActivity extends AppCompatActivity {

    public EditText nombre;
    public EditText apellidos;
    public EditText email;
    public EditText password1;
    public EditText password2;
    public Button botonRegistro;
    public Button botonSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nombre = findViewById(R.id.textNombre);
        apellidos = findViewById(R.id.textApellidos);
        email = findViewById(R.id.textEmail);
        password1 = findViewById(R.id.textPassword1);
        password2 = findViewById(R.id.textPassword2);
        botonRegistro = findViewById(R.id.botonRegistro);
        botonSalir = findViewById(R.id.botonSalir);

        Intent intentalogin = new Intent(RegisterActivity.this, MainActivity.class);

        Usuario usuario = new Usuario();

        botonRegistro.setOnClickListener(view -> {

            String mnombre = nombre.getText().toString().toLowerCase();
            String mapellidos = apellidos.getText().toString().toLowerCase();
            String memail = email.getText().toString().toLowerCase();
            String mpassword1 = password1.getText().toString();
            String mpassword2 = password2.getText().toString();

            //si el mail esta bien es true
            boolean errorEmail = validarEmail(memail);


            if (!mnombre.isEmpty() && !mapellidos.isEmpty() && !mpassword1.isEmpty() && !mpassword2.isEmpty()) {
                if (errorEmail) {
                    if (mpassword1.equals(mpassword2)) {
                        boolean error = existeUsuario(memail);
                        if (!error) {
                            usuario.setNombre(mnombre);
                            usuario.setApellidos(mapellidos);
                            usuario.setEmail(memail);
                            usuario.setPassword(mpassword1);

                            registrarUsuario(usuario);

                            intentalogin.putExtra("Login", memail);
                            intentalogin.putExtra("Password", mpassword1);
                            startActivity(intentalogin);
                            finish();
                            Toast.makeText(RegisterActivity.this, R.string.registradocorrectamente, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(RegisterActivity.this, R.string.usuarioYaRegistrado, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, R.string.errorContraseñas, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, R.string.errorFormatoEmail, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RegisterActivity.this, R.string.errorCampos, Toast.LENGTH_SHORT).show();
            }

        });

        botonSalir.setOnClickListener(view -> {
            startActivity(intentalogin);
            finish();

        });

    }

    private boolean existeUsuario(String email) {

        UsuariosFacade usuariosFacade = new UsuariosFacade();
        Thread thread = new Thread(usuariosFacade);
        try {
            thread.start();
            thread.join(); // Awaiting response from the server...
        } catch (InterruptedException e) {
            // Nothing to do here...
        }

        boolean existe = false;//no existe de base

        List<Usuario> personas = usuariosFacade.getResponse();

        for (int i = 0; i < personas.size(); i++) {
            if (personas.get(i).getEmail().equalsIgnoreCase(email)) {
                existe = true;
                break;
            }
        }
        return existe;
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void registrarUsuario(Usuario usuario) {

        if (isConnected()) {
            UsuarioPost usuarioPost = new UsuarioPost(usuario);
            Thread thread = new Thread(usuarioPost);
            try {
                thread.start();
                thread.join(); // Awaiting response from the server...
            } catch (InterruptedException e) {
                // Nothing to do here...
            }
        }
    }

    public boolean isConnected() {
        boolean ret = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if ((networkInfo != null) && (networkInfo.isAvailable()) && (networkInfo.isConnected()))
                ret = true;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_communication), Toast.LENGTH_SHORT).show();
        }
        return ret;
    }

}