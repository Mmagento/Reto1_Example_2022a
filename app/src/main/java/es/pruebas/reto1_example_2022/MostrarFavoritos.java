package es.pruebas.reto1_example_2022;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import es.pruebas.reto1_example_2022.adapters.MyTableAdapter;
import es.pruebas.reto1_example_2022.beans.Cancion;
import es.pruebas.reto1_example_2022.network.GetFavoritos;

public class MostrarFavoritos extends AppCompatActivity {


    private ListView listCanciones;
    private ArrayList<Cancion> favoritos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_favoritos);

        listCanciones = findViewById(R.id.listViewFinal);

        Bundle extras = getIntent().getExtras();
        long id = extras.getLong("idUser");

        MyTableAdapter myTableAdapter = new MyTableAdapter(this, R.layout.myrow_layout, favoritos);
        listCanciones.setAdapter(myTableAdapter);


        GetFavoritos getFavoritos = new GetFavoritos(id);
        Thread thread = new Thread(getFavoritos);
        try {
            thread.start();
            thread.join(); // Awaiting response from the server...
        } catch (InterruptedException e) {
            // Nothing to do here...


            //System.out.println("AAAAAA"+favoritos.get(1).get.toString());
        }
        favoritos.addAll(getFavoritos.getResponse());


    }


}