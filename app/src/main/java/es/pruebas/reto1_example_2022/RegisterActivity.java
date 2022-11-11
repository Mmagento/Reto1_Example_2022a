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

public class RegisterActivity extends AppCompatActivity {

    public EditText Login;
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

        Login = findViewById(R.id.textLogin);
        nombre = findViewById(R.id.textNombre);
        apellidos = findViewById(R.id.textApellidos);
        email = findViewById(R.id.textEmail);
        password1 = findViewById(R.id.textPassword1);
        password2 = findViewById(R.id.textPassword2);

        botonRegistro = findViewById(R.id.botonRegistro);

        Intent intentacanciones = new Intent(RegisterActivity.this, MainActivity.class);

        DataManager dataManager = new DataManager(this);

        Users usuario = new Users();

        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ANTES="Estoy antes de el if de comprobar valores nulos";

                // 1-Para transformar los datos en minusculas.

                String mlogin = Login.getText().toString().toLowerCase();
                String mnombre = nombre.getText().toString().toLowerCase();
                String mapellidos = apellidos.getText().toString().toLowerCase();
                String memail = email.getText().toString().toLowerCase();
                String mpassword1 = password1.getText().toString();
                String mpassword2 = password2.getText().toString();



                // 2-Comprobar que las contraseñas son iguales y que los campos no estan vacios.
                //TODO -La parte de la comparación de las variable de "isEmpty()" eliminarla porque se puede controlar desde el Servidor (Eclipse)
                if(mpassword1.equals(mpassword2) && !mlogin.isEmpty() && !mnombre.isEmpty() && !mapellidos.isEmpty() && !memail.isEmpty() && !mpassword1.isEmpty() && !mpassword2.isEmpty()){

                    usuario.setLogin(mlogin);
                    usuario.setNombre(mnombre);
                    usuario.setApellidos(mapellidos);
                    usuario.setEmail(memail);
                    usuario.setPassword(mpassword1);

                    boolean error = existeUsuario(usuario);

                    //si existe
                    if(error == false){
                    dataManager.insert(usuario);
                        intentacanciones.putExtra("Login",mlogin);
                        intentacanciones.putExtra("Password",mpassword1);
                        startActivity(intentacanciones);
                    }else{

                        //msj de que existe y no se puede registrar.


                    }
                  //  Toast.makeText(getApplicationContext(), ANTES , Toast.LENGTH_LONG).show();




                   // Toast.makeText(getApplicationContext(), getString( R.string.insertadocorrectamente ), Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(getApplicationContext(), getString( R.string.insertadonocorrectamente ), Toast.LENGTH_LONG).show();
                }
            }
            public boolean existeUsuario(Users usuario){

                //String existe="Registro incorrecto, ya existe un usuario con ese email o ese login";
                //String noexiste="Registro correcto, usuario nuevo";

                Boolean existe = false;//no existe de base

                //DataManager data = new DataManager(this);
                //dataManager.getWritableDatabase();
                List<Users> personas = dataManager.selectAllUsers();

                System.out.println("aaaaa"+personas.size());

                for(int i = 0; i<personas.size();i++){
                    if(personas.get(i).getLogin().equalsIgnoreCase(usuario.getLogin()) ){
                        //&& personas.get(i).getLogin().equalsIgnoreCase(login1)
                        //Toast.makeText(getApplicationContext(), existe, Toast.LENGTH_SHORT).show();
                        existe = true;
                        break;
                    }else{

                        existe=false;
                        //Toast.makeText(getApplicationContext(), noexiste, Toast.LENGTH_SHORT).show();

                    }
                }

                return existe;
            }
        });

    }

}