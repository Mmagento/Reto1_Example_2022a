package es.pruebas.reto1_example_2022.network;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import es.pruebas.reto1_example_2022.beans.Cancion;

public class GetFavoritos extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "/favoritos";

    private ArrayList<Cancion> response;

    private long idUser;

    public GetFavoritos(long id) {
        super();
        idUser = id;
    }

    @Override
    public void run() {

        try {
            // The URL
            String url2 = theUrl + "/" + idUser;
            URL url = new URL(url2);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            // Sending...
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 418) {
                // I am not a teapot...
                response = null;

            } else if (responseCode == HttpURLConnection.HTTP_OK) {
                // Response...
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream()));

                StringBuffer response = new StringBuffer();
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append(inputLine);
                }
                bufferedReader.close();

                // Processing the JSON...
                String theUnprocessedJSON = response.toString();

                JSONArray mainArray = new JSONArray(theUnprocessedJSON);

                this.response = new ArrayList<>();

                Cancion favorito;
                for (int i = 0; i < mainArray.length(); i++) {
                    JSONObject object = mainArray.getJSONObject(i);

                    favorito = new Cancion();
                    favorito.setId((long) object.getInt("idSong"));
                    favorito.setTitulo(object.getString("titulo"));
                    favorito.setAutor(object.getString("autor"));
                    favorito.setUrl(object.getString("url"));
                    this.response.add(favorito);
                }
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public ArrayList<Cancion> getResponse() {

        return response;
    }


}
