package es.pruebas.reto1_example_2022.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeleteFavorito extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "/favoritos";
    private int reponse;
    private long idUser;
    private long idSong;

    public DeleteFavorito(long user, long song) {

        idUser = user;
        idSong = song;
    }

    @Override
    public void run() {
        try {
            String urlFinal = theUrl + "/" + idUser + "/" + idSong;
            URL url = new URL(urlFinal);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("DELETE");
            httpURLConnection.setRequestProperty("Content-type", "application/json;charset=UTF-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);


            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 500) {

                reponse = responseCode;

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
        return reponse;

    }


}
