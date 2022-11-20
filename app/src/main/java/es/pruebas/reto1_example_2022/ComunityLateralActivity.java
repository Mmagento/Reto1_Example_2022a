package es.pruebas.reto1_example_2022;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import es.pruebas.reto1_example_2022.adapters.MyTableAdapter;
import es.pruebas.reto1_example_2022.beans.Cancion;
import es.pruebas.reto1_example_2022.beans.Favorito;
import es.pruebas.reto1_example_2022.beans.Usuario;
import es.pruebas.reto1_example_2022.databinding.ActivityComunityLateralBinding;
import es.pruebas.reto1_example_2022.network.CancionesFacade;
import es.pruebas.reto1_example_2022.network.DeleteFavorito;
import es.pruebas.reto1_example_2022.network.FavoritosPost;
import es.pruebas.reto1_example_2022.network.GetFavoritos;
import es.pruebas.reto1_example_2022.network.UsuariosFacade;

public class ComunityLateralActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityComunityLateralBinding binding;
    private ListView listCanciones;
    private ArrayList<Cancion> listado = new ArrayList<>();
    private String emailUsuario;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityComunityLateralBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarComunityLateral.toolbar);
        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_login, R.id.nav_favoritos)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_comunity_lateral);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);


        listCanciones = findViewById(R.id.listViewFinal);

        Bundle extras = getIntent().getExtras();
        emailUsuario = extras.getString("emailUsuario");

        MyTableAdapter myTableAdapter = new MyTableAdapter(this, R.layout.myrow_layout, listado);
        listCanciones.setAdapter(myTableAdapter);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.comunity_lateral, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_comunity_lateral);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_login:
                Intent intent_login = new Intent(ComunityLateralActivity.this, MainActivity.class);
                startActivity(intent_login);
                finish();
                break;
            case R.id.nav_favoritos:
                Intent intentFavorito = new Intent(ComunityLateralActivity.this, favoritos_lateral_activity.class);
                intentFavorito.putExtra("idUser", emailUsuario);
                startActivity(intentFavorito);
                finish();
                break;
            default:
                throw new IllegalArgumentException("menu option not implemented!!");
        }

        drawer.closeDrawer(GravityCompat.START);
        return false;
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

        PopupMenu popupMenu = new PopupMenu(ComunityLateralActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.opcion_canciones, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {

            if (item.getTitle().equals(popupMenu.getMenu().getItem(0).getTitle())) {
                long idUser = getIdByUserEmail(emailUsuario);

                Favorito favorito = new Favorito();
                favorito.setIdCancion(listado.get(position).getId());
                favorito.setIdUsuario(idUser);

                int codigo = 0;

                if (isConnected()) {
                    FavoritosPost favoritosPost = new FavoritosPost(favorito);

                    Thread thread = new Thread(favoritosPost);
                    try {
                        thread.start();
                        thread.join(); // Awaiting response from the server...
                    } catch (InterruptedException e) {
                        // Nothing to do here...
                    }
                    codigo = favoritosPost.getResponse();
                }
                if (codigo == 500) {
                    Toast.makeText(ComunityLateralActivity.this, "Cancion ya asignada a favoritos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ComunityLateralActivity.this, "Cancion añadida a favoritos", Toast.LENGTH_SHORT).show();
                }
            } else if (item.getTitle().equals(popupMenu.getMenu().getItem(1).getTitle())) {
                Uri uri = Uri.parse(listado.get(position).getUrl());
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            } else if (item.getTitle().equals(popupMenu.getMenu().getItem(2).getTitle())) {
                System.out.println("ESTOAOAOSOSAOSOOA");
                long idUser = getIdByUserEmail(emailUsuario);
                long idSong = listado.get(position).getId();
                int codigo = 0;
                boolean existe = comprobarFavoritos(idUser, idSong);
                if (existe) {
                    if (isConnected()) {
                        DeleteFavorito deleteFavorito = new DeleteFavorito(idUser, idSong);

                        Thread thread = new Thread(deleteFavorito);
                        try {
                            thread.start();
                            thread.join(); // Awaiting response from the server...
                        } catch (InterruptedException e) {
                            // Nothing to do here...
                        }
                        codigo = deleteFavorito.getResponse();
                    }
                    Toast.makeText(ComunityLateralActivity.this, "Cancion eliminada con exito", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(ComunityLateralActivity.this, "La cancion no se encuentra en favoritos", Toast.LENGTH_SHORT).show();
                }

            }
            return true;
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


    private boolean comprobarFavoritos(long idUser, long idSong) {

        boolean existe = false;
        GetFavoritos getFavoritos = new GetFavoritos(idUser);
        Thread thread = new Thread(getFavoritos);
        try {
            thread.start();
            thread.join(); // Awaiting response from the server...
        } catch (InterruptedException e) {
            // Nothing to do here...

        }
        ArrayList<Cancion> favoritos = getFavoritos.getResponse();

        for (int i = 0; i < favoritos.size(); i++) {
            if (favoritos.get(i).getId() == idSong) {
                existe = true;
                break;

            }
        }
        return existe;
    }


}