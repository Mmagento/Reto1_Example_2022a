package es.pruebas.reto1_example_2022;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Locale;

import es.pruebas.reto1_example_2022.beans.Usuario;

public class RegisterActivity extends AppCompatActivity {

    public EditText Login;
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

        Login = findViewById(R.id.textLogin);
        nombre = findViewById(R.id.textNombre);
        apellidos = findViewById(R.id.textApellidos);
        email = findViewById(R.id.textEmail);
        password1 = findViewById(R.id.textPassword1);
        password2 = findViewById(R.id.textPassword2);
        botonRegistro = findViewById(R.id.botonRegistro);
        botonSalir = findViewById(R.id.botonSalir);

        Intent intentalogin = new Intent(RegisterActivity.this, MainActivity.class);

        DataManager dataManager = new DataManager(this);

        Usuario usuario = new Usuario();

        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mlogin = Login.getText().toString().toLowerCase();
                String mnombre = nombre.getText().toString().toLowerCase();
                String mapellidos = apellidos.getText().toString().toLowerCase();
                String memail = email.getText().toString().toLowerCase();
                String mpassword1 = password1.getText().toString();
                String mpassword2 = password2.getText().toString();

                boolean error = existeUsuario(mlogin);

                if(error == false){
                    if(mpassword1.equals(mpassword2) && !mlogin.isEmpty() && !mnombre.isEmpty() && !mapellidos.isEmpty() && !memail.isEmpty() && !mpassword1.isEmpty() && !mpassword2.isEmpty()){

                        usuario.setLogin(mlogin);
                        usuario.setNombre(mnombre);
                        usuario.setApellidos(mapellidos);
                        usuario.setEmail(memail);
                        usuario.setPassword(mpassword1);

                        dataManager.insert(usuario);
                        intentalogin.putExtra("Login",mlogin);
                        intentalogin.putExtra("Password",mpassword1);
                        startActivity(intentalogin);
                        Toast.makeText(RegisterActivity.this, R.string.registradocorrectamente, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, R.string.usuarioYaRegistrado, Toast.LENGTH_SHORT).show();
                }
            }
        });

        botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intentalogin);
            }
        });

    }
    private boolean existeUsuario(String login) {

            DataManager dataManager = new DataManager(this);

            Boolean existe = false;//no existe de base

            List<Usuario> personas = dataManager.selectAllUsers();

            for(int i = 0; i<personas.size();i++){
                if(personas.get(i).getLogin().equalsIgnoreCase(login) ){
                    existe = true;
                    break;
                }else{
                    existe=false;
                }
            }

            return existe;

    }

}