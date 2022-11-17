package es.pruebas.reto1_example_2022.network;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import es.pruebas.reto1_example_2022.beans.Cancion;
import es.pruebas.reto1_example_2022.beans.Favorito;

public class FavoritosPost extends NetConfiguration implements Runnable {


    private final String theUrl = theBaseUrl + "/favoritos";

    private ArrayList<Favorito> response;

    private Favorito favorito;

    /*public FavoritosPost(Favorito favoritoCons){

        favorito.this=favoritoCons;
    }*/

    @Override
    public void run() {

        try {
            // The URL
            URL url = new URL( theUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod( "GET" );

            // Sending...
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == 418){
                // I am not a teapot...
                this.response = null;

            } else if (responseCode == HttpURLConnection.HTTP_OK) {
                // Response...
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader( httpURLConnection.getInputStream() ) );

                StringBuffer response = new StringBuffer();
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append( inputLine );
                }
                bufferedReader.close();

                // Processing the JSON...
                String theUnprocessedJSON = response.toString();

                JSONArray mainArray = new JSONArray (theUnprocessedJSON);

                this.response = new ArrayList<>();

                Favorito favorito;
                for(int i=0; i < mainArray.length(); i++) {
                    JSONObject object = mainArray.getJSONObject( i );

                    favorito = new Favorito();
                    favorito.setIdUsuario((long) object.getInt("idUser"));
                    favorito.setIdCancion((long) object.getInt("idSong"));
                    this.response.add( favorito );
                }
            }

        } catch (Exception e) {
            System.out.println( "ERROR: " + e.getMessage() );
        }
    }

    public ArrayList<Favorito> getResponse() {
        return response;
    }



}
