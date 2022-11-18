package es.pruebas.reto1_example_2022.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import es.pruebas.reto1_example_2022.beans.Favorito;

public class DeleteFavorito extends NetConfiguration implements Runnable{

    private final String theUrl = theBaseUrl + "/favoritos";
    private Favorito favorito;
    private int reponse;
    private long idUser;
    private long idSong;

    public DeleteFavorito(Favorito favoritoCons) {
        favorito = favoritoCons;
    }

    @Override
    public void run() {
        try {
            String urlFinal = theUrl +"/"+idUser +"/"+ idSong;
            URL url = new URL(urlFinal);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("DELETE");
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
            if(responseCode==500){

                reponse=responseCode;

            }else if (responseCode == HttpURLConnection.HTTP_OK) {

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
        System.out.println("RESPONSE"+reponse);
        return reponse;

    }


}
