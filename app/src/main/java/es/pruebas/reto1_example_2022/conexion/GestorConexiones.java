package es.pruebas.reto1_example_2022.conexion;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import es.pruebas.reto1_example_2022.R;
import es.pruebas.reto1_example_2022.adapters.MyTableAdapter;
import es.pruebas.reto1_example_2022.beans.Cancion;
import es.pruebas.reto1_example_2022.network.CancionFacade;
import es.pruebas.reto1_example_2022.network.CancionesFacade;

public class GestorConexiones extends AppCompatActivity {

    TextView textUser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    ArrayList<Cancion> listado = new ArrayList<>();
    ArrayAdapter<String> adapter;

    MyTableAdapter myTableAdapter = new MyTableAdapter (this, R.layout.myrow_layout, listado);

    findViewById(R.id.botonIniciarLogin ).setOnClickListener( new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isConnected()) {
                CancionFacade cancionFacade = new CancionFacade( 1 );
                Thread thread = new Thread(cancionFacade);
                try {
                    thread.start();
                    thread.join(); // Awaiting response from the server...
                } catch (InterruptedException e) {
                    // Nothing to do here...
                }

                // Processing the answer
                Cancion user = cancionFacade.getResponse();
                listado.add( user );
            }
        }
    } );

    findViewById(R.id.botonIniciarLogin ).setOnClickListener( v -> {
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
            ArrayList<Cancion> listVideos = cancionesFacade.getResponse();
            listado.addAll( listVideos );
        }
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
