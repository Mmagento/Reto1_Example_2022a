package es.pruebas.reto1_example_2022;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import es.pruebas.reto1_example_2022.adapters.MyTableAdapter;
import es.pruebas.reto1_example_2022.beans.Video;
import es.pruebas.reto1_example_2022.network.VideoFacade;
import es.pruebas.reto1_example_2022.network.VideosFacade;

/**
 * Main Activity
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        ArrayList<Video> listado = new ArrayList<>();

        MyTableAdapter myTableAdapter = new MyTableAdapter (this, R.layout.myrow_layout, listado);
        ((ListView) findViewById( R.id.listView)).setAdapter (myTableAdapter);

        findViewById(R.id.getOneButton ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    VideoFacade videoFacade = new VideoFacade( 1 );
                    Thread thread = new Thread( videoFacade );
                    try {
                        thread.start();
                        thread.join(); // Awaiting response from the server...
                    } catch (InterruptedException e) {
                        // Nothing to do here...
                    }

                    // Processing the answer
                    Video user = videoFacade.getResponse();
                    listado.add( user );
                    ((ListView) MainActivity.this.findViewById( R.id.listView )).setAdapter( myTableAdapter );
                }
            }
        } );

        findViewById(R.id.getAllButton ).setOnClickListener( v -> {
            if (isConnected()) {
                VideosFacade videosFacade = new VideosFacade();
                Thread thread = new Thread( videosFacade );
                try {
                    thread.start();
                    thread.join(); // Awaiting response from the server...
                } catch (InterruptedException e) {
                    // Nothing to do here...
                }
                // Processing the answer
                ArrayList<Video> listVideos = videosFacade.getResponse();
                listado.addAll( listVideos );
                ((ListView) findViewById( R.id.listView)).setAdapter (myTableAdapter);
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