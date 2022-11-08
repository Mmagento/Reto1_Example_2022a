package es.pruebas.reto1_example_2022;

import android.annotation.SuppressLint;
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

                String mnombre = nombre.toString().toLowerCase();
                String mapellidos = apellidos.toString().toLowerCase();
                String memail = email.toString().toLowerCase();
                String mpassword1 = password1.toString().toLowerCase();
                String mpassword2 = password2.toString().toLowerCase();

                if(mpassword1.equals(mpassword2)){
                    usuario.setApellidos(mapellidos);
                    usuario.setEmail(memail);
                    usuario.setNombre(mnombre);
                    usuario.setPassword(mpassword1);

                    dataManager.insert(usuario);


                }else{
                    //Mensaje de error en pswd
                }

                RegisterActivity.this.startActivity(intentacanciones);


            }
        });

    }

}