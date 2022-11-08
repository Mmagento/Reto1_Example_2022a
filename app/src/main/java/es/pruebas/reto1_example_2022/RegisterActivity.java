package es.pruebas.reto1_example_2022;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    public EditText nombre;
    public EditText apellidos;
    public EditText email;
    public EditText password1;
    public EditText password2;
    Button botonRegistro;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intentacanciones = new Intent(RegisterActivity.this, MainActivity.class);

        // 1-Para transformar los datos en minusculas.
        botonRegistro = findViewById(R.id.botonRegistro);
        nombre = (EditText)findViewById(R.id.textNombre);
        apellidos = (EditText)findViewById(R.id.textApellidos);
        email = (EditText)findViewById(R.id.textEmail);
        password1 = (EditText)findViewById(R.id.textPassword1);
        password2 = (EditText)findViewById(R.id.textPassword2);
        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2-Para transformar los datos en minusculas.
                nombre.toString().toLowerCase();
                apellidos.toString().toLowerCase();
                email.toString().toLowerCase();
                password1.toString().toLowerCase();
                password2.toString().toLowerCase();

                //intentacanciones.putExtra();
                RegisterActivity.this.startActivity(intentacanciones);
            }
        });

    }

}