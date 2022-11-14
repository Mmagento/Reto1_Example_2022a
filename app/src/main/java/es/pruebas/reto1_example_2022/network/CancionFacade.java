package es.pruebas.reto1_example_2022.network;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import es.pruebas.reto1_example_2022.beans.Cancion;

/**
 * One class per endpoint. This one is for a single Video
 */
public class CancionFacade extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "getVideo";

    private final int id;
    private Cancion response;

    public CancionFacade(int id) {
        super();
        this.id = id;
    }

    @Override
    public void run() {

        try {
            // The URL
            URL url = new URL( theUrl + "\\1");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod( "GET" );
            httpURLConnection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");

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

                JSONObject mainObject = new JSONObject(theUnprocessedJSON);

                this.response = new Cancion();
                this.response.setId((long) mainObject.getInt("id"));
                this.response.setTitulo( mainObject.getString("title"));
                this.response.setUrl( mainObject.getString("url"));
            }

        } catch (Exception e) {
            System.out.println( "ERROR: " + e.getMessage() );
        }
    }

    public Cancion getResponse() {
        return response;
    }
}