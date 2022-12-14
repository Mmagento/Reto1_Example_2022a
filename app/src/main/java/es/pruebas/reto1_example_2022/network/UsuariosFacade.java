package es.pruebas.reto1_example_2022.network;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import es.pruebas.reto1_example_2022.beans.Usuario;

/**
 * One class per endpoint. This one is for a list of Videos
 */
public class UsuariosFacade extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "/usuarios";

    private ArrayList<Usuario> response;

    public UsuariosFacade() {
        super();
    }

    @Override
    public void run() {

        try {
            // The URL
            URL url = new URL(theUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            // Sending...
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == 418) {
                // I am not a teapot...
                this.response = null;

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

                Usuario usuario;
                for (int i = 0; i < mainArray.length(); i++) {
                    JSONObject object = mainArray.getJSONObject(i);

                    usuario = new Usuario();
                    usuario.setId(object.getInt("id"));
                    usuario.setNombre(object.getString("nombre"));
                    usuario.setApellidos(object.getString("apellidos"));
                    usuario.setEmail(object.getString("email"));
                    usuario.setPassword(object.getString("password"));
                    this.response.add(usuario);
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public ArrayList<Usuario> getResponse() {
        return response;
    }
}