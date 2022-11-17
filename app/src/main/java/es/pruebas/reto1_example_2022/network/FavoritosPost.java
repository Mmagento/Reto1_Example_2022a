package es.pruebas.reto1_example_2022.network;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import es.pruebas.reto1_example_2022.beans.Cancion;
import es.pruebas.reto1_example_2022.beans.Favorito;

public class FavoritosPost extends NetConfiguration implements Runnable {


    private final String theUrl = theBaseUrl + "/favoritos";

    private ArrayList<Favorito> response;

    private Favorito favorito;

    public FavoritosPost(Favorito favoritoCons) {

        favorito = favoritoCons;
    }

    private int responseInt = 0;

    @Override
    public void run() {
        try {

            URL url = new URL(theUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-type", "application/json;charset=UTF-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            String jsonInputString = favorito.toString();
            try (OutputStream postSend = httpURLConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                postSend.write(input, 0, input.length);
            }

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 500) {
                // I am not a teapot...
                this.responseInt = 0;
            } else if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuffer response = new StringBuffer();
                String inputLine;

                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append(inputLine);
                }
                bufferedReader.close();
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public int getResponse() {
        return responseInt;
    }


}
