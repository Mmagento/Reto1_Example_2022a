package es.pruebas.reto1_example_2022;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    public EditText nombre;
    public EditText apellidos;
    public EditText email;
    public EditText password1;
    public EditText password2;
    Button botonRegistro;
    public String insertado="Cliente insertado";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        botonRegistro = findViewById(R.id.botonRegistro);
        nombre = findViewById(R.id.textNombre);
        apellidos = findViewById(R.id.textApellidos);
        email = findViewById(R.id.textEmail);
        password1 = findViewById(R.id.textPassword1);
        password2 = findViewById(R.id.textPassword2);

        Intent intentacanciones = new Intent(RegisterActivity.this, MainActivity.class);

        // 1-Para transformar los datos en minusculas.
        DataManager dataManager = new DataManager(this);
        Users usuario = new Users();

        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2-Para transformar los datos en minusculas.

                String mnombre = nombre.getText().toString().toLowerCase();
                String mapellidos = apellidos.getText().toString().toLowerCase();
                String memail = email.getText().toString().toLowerCase();
                String mpassword1 = password1.getText().toString();
                String mpassword2 = password2.getText().toString();

                if(mpassword1.equals(mpassword2)){
                    usuario.setApellidos(mapellidos);
                    usuario.setEmail(memail);
                    usuario.setNombre(mnombre);
                    usuario.setPassword(mpassword1);

                    dataManager.insert(usuario);
                    Toast.makeText(getApplicationContext(), getString( R.string.insertadocorrectamente ), Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(getApplicationContext(), getString( R.string.insertadonocorrectamente ), Toast.LENGTH_LONG).show();

                }

                RegisterActivity.this.startActivity(intentacanciones);


            }
        });

    }

}