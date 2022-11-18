package es.pruebas.reto1_example_2022;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
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
import es.pruebas.reto1_example_2022.databinding.ActivityFavoritosLateralBinding;
import es.pruebas.reto1_example_2022.network.FavoritosPost;
import es.pruebas.reto1_example_2022.network.GetFavoritos;
import es.pruebas.reto1_example_2022.network.UsuariosFacade;

public class favoritos_lateral_activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityFavoritosLateralBinding binding;
    private ListView listCanciones;
    private ArrayList<Cancion> favoritos = new ArrayList<>();
    private DrawerLayout drawer;
    private String emailUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFavoritosLateralBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_login_2, R.id.nav_canciones)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_favoritos_lateral);
        //NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);

        listCanciones = findViewById(R.id.listViewFinal);

        Bundle extras = getIntent().getExtras();
        emailUsuario = extras.getString("idUser");

        long id = getIdByUserEmail(emailUsuario);

        MyTableAdapter myTableAdapter = new MyTableAdapter(this, R.layout.myrow_layout, favoritos);
        listCanciones.setAdapter(myTableAdapter);

        GetFavoritos getFavoritos = new GetFavoritos(id);
        Thread thread = new Thread(getFavoritos);
        try {
            thread.start();
            thread.join(); // Awaiting response from the server...
        } catch (InterruptedException e) {
            // Nothing to do here...

        }
        favoritos.addAll(getFavoritos.getResponse());

        listCanciones.setOnItemClickListener(this::onItemClick);
    }


    //TO DO ACABAR EL METODO ESTE CON UN MENU NUEVO
    private void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        PopupMenu popupMenu = new PopupMenu(favoritos_lateral_activity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.opcion_canciones, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {

            if (item.getTitle().equals(popupMenu.getMenu().getItem(0).getTitle())) {
                long idUser = getIdByUserEmail(emailUsuario);

                Favorito favorito = new Favorito();
                favorito.setIdCancion(favoritos.get(position).getId());
                favorito.setIdUsuario(idUser);

                int codigo = 0;

                    FavoritosPost favoritosPost = new FavoritosPost(favorito);

                    Thread thread = new Thread(favoritosPost);
                    try {
                        thread.start();
                        thread.join(); // Awaiting response from the server...
                    } catch (InterruptedException e) {
                        // Nothing to do here...
                    }
                    codigo = favoritosPost.getResponse();


                if(codigo==500){
                    Toast.makeText(favoritos_lateral_activity.this, "Cancion ya existente", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(favoritos_lateral_activity.this, "Cancion añadida a favoritos", Toast.LENGTH_SHORT).show();
                }


            } else if (item.getTitle().equals(popupMenu.getMenu().getItem(1).getTitle())) {

                Uri uri = Uri.parse(favoritos.get(position).getUrl());
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
            return true;
        });
        popupMenu.show();




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_favoritos_lateral);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_login_2:
                Intent intent_login = new Intent(favoritos_lateral_activity.this, MainActivity.class);
                startActivity(intent_login);
                finish();
                break;
            case R.id.nav_canciones:
                Intent intentCanciones = new Intent(favoritos_lateral_activity.this, ComunityLateralActivity.class);
                intentCanciones.putExtra("emailUsuario",emailUsuario);
                startActivity(intentCanciones);
                finish();
                break;

            default:
                throw new IllegalArgumentException("menu option not implemented!!");
        }

        drawer.closeDrawer(GravityCompat.START);
        return false;
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