package es.pruebas.reto1_example_2022;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    public String nombre;
    public String apellidos;
    public String email;
    public String password1;
    public String password2;
    Button botonRegistro;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main );

        botonRegistro.findViewById(R.id.botonRegistro);

        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
            }
        });

    }

}
