package es.pruebas.reto1_example_2022;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import es.pruebas.reto1_example_2022.adapters.MyTableAdapter;
import es.pruebas.reto1_example_2022.beans.Cancion;
import es.pruebas.reto1_example_2022.beans.Favorito;
import es.pruebas.reto1_example_2022.beans.Usuario;
import es.pruebas.reto1_example_2022.network.CancionesFacade;
import es.pruebas.reto1_example_2022.network.FavoritosPost;
import es.pruebas.reto1_example_2022.network.GetFavoritos;
import es.pruebas.reto1_example_2022.network.UsuarioPost;
import es.pruebas.reto1_example_2022.network.UsuariosFacade;

public class ComunityActivity extends AppCompatActivity {


    private ListView listCanciones;
    private ArrayList<Cancion> listado = new ArrayList<>();
    private String emailUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunity);

        Button volver = findViewById(R.id.buttonVolverLogin);
        Button mostrarFavoritos = findViewById(R.id.buttonVerFavoritos);
        listCanciones = findViewById(R.id.listView);

        MyTableAdapter myTableAdapter = new MyTableAdapter(this, R.layout.myrow_layout, listado);
        listCanciones.setAdapter(myTableAdapter);



        Bundle extras = getIntent().getExtras();
        emailUsuario = extras.getString("emailUsuario");

        if (isConnected()) {
            CancionesFacade cancionesFacade = new CancionesFacade();
            Thread thread = new Thread(cancionesFacade);
            try {
                thread.start();
                thread.join(); // Awaiting response from the server...
            } catch (InterruptedException e) {
                // Nothing to do here...
            }
            listado.addAll(cancionesFacade.getResponse());
        }

        listCanciones.setOnItemClickListener(this::onItemClick);

        volver.setOnClickListener(view -> {

            Intent intentLogin = new Intent(ComunityActivity.this, MainActivity.class);
            startActivity(intentLogin);
            finish();

        });

        mostrarFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = getIdByUserEmail(emailUsuario);

                Intent intentFavorito = new Intent(ComunityActivity.this, MostrarFavoritos.class);
                intentFavorito.putExtra("idUser", id);
                startActivity(intentFavorito);
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
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if ((networkInfo != null) && (networkInfo.isAvailable()) && (networkInfo.isConnected()))
                ret = true;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_communication), Toast.LENGTH_SHORT).show();
        }
        return ret;
    }

    private void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        PopupMenu popupMenu = new PopupMenu(ComunityActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.opcion_canciones, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getTitle().equals(popupMenu.getMenu().getItem(0).getTitle())) {
                    long idUser = getIdByUserEmail(emailUsuario);

                    Favorito favorito = new Favorito();

                    favorito.setIdCancion(listado.get(position).getId());
                    favorito.setIdUsuario(idUser);

                    if (isConnected()) {
                        FavoritosPost favoritosPost = new FavoritosPost(favorito);
                        Thread thread = new Thread(favoritosPost);
                        try {
                            thread.start();
                            thread.join(); // Awaiting response from the server...
                        } catch (InterruptedException e) {
                            // Nothing to do here...
                        }
                    }

                    Toast.makeText(ComunityActivity.this, "Cancion añadida a favoritos", Toast.LENGTH_SHORT).show();
                } else if (item.getTitle().equals(popupMenu.getMenu().getItem(1).getTitle())) {

                    Uri uri = Uri.parse(listado.get(position).getUrl());
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(i);
            }
                return true;
            }
        });
        popupMenu.show();

    }


    private long getIdByUserEmail(String email) {

        UsuariosFacade usuariosFacade = new UsuariosFacade();
        Thread thread = new Thread(usuariosFacade);
        try {
            thread.start();
            thread.join(); // Awaiting response from the server...
        } catch (InterruptedException e) {
            // Nothing to do here...
        }
        long idUser = 0;

        List<Usuario> personas = usuariosFacade.getResponse();
        for (int i = 0; i < personas.size(); i++) {
            if (personas.get(i).getEmail().equalsIgnoreCase(email)) {
                idUser = personas.get(i).getId();
                break;
            }
        }
        return idUser;

    }


}