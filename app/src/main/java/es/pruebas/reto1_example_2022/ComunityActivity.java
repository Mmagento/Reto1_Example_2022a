package es.pruebas.reto1_example_2022;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import es.pruebas.reto1_example_2022.adapters.MyTableAdapter;
import es.pruebas.reto1_example_2022.beans.Cancion;
import es.pruebas.reto1_example_2022.network.CancionesFacade;

public class ComunityActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_comunity );

        ArrayList<Cancion> listado = new ArrayList<>();

        Button volver = findViewById(R.id.buttonVolverLogin);

        MyTableAdapter myTableAdapter = new MyTableAdapter (this, R.layout.myrow_layout, listado);
        ((ListView) findViewById( R.id.listView)).setAdapter (myTableAdapter);

        if (isConnected()) {
            CancionesFacade cancionesFacade = new CancionesFacade();
            Thread thread = new Thread(cancionesFacade);
            try {
                thread.start();
                thread.join(); // Awaiting response from the server...
            } catch (InterruptedException e) {
                // Nothing to do here...
            }
            // Processing the answer
            ArrayList<Cancion> listCanciones = cancionesFacade.getResponse();
            listado.addAll( listCanciones );
            ((ListView) findViewById( R.id.listView)).setAdapter (myTableAdapter);
        }

        volver.setOnClickListener(view -> {

            Intent intentLogin = new Intent(ComunityActivity.this,MainActivity.class);
            startActivity(intentLogin);
            finish();

        });
    }

    /**
     * Returns true if we are conected to the network. False otherwise
     *
     * @return True or False
     */
    public boolean isConnected() {
        boolean ret = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                    .getSystemService( Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if ((networkInfo != null) && (networkInfo.isAvailable()) && (networkInfo.isConnected()))
                ret = true;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_communication), Toast.LENGTH_SHORT).show();
        }
        return ret;
    }
}