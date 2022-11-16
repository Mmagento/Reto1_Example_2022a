package es.pruebas.reto1_example_2022.network;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


import es.pruebas.reto1_example_2022.beans.Usuario;

public class UsuarioPost extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "/usuarios";
    private Usuario usuario;
    public UsuarioPost(Usuario usuarioConstructor ) {
        this.usuario = usuarioConstructor;
    }

    private int responseInt;

    @Override
    public void run() {
        try {

            URL url = new URL(theUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod( "POST" );
            httpURLConnection.setRequestProperty("Content-type" , "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            System.out.println("1");
            String jsonInputString = usuario.toString();
            System.out.println("2");
            try(OutputStream postSend = httpURLConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                postSend.write(input, 0, input.length);
                System.out.println("PRUEBA PRUEBA PRUEBA"+input.length);
            }
            System.out.println("3");
            //no llega al 4

            int responseCode = httpURLConnection.getResponseCode();
            System.out.println(responseCode);
            if (responseCode == 500){
                // I am not a teapot...
                this.responseInt = 0;
                System.out.println("He entrado en el if de error 500");
            } else if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                System.out.println("4");
                StringBuffer response = new StringBuffer();
                String inputLine;

                while((inputLine = bufferedReader.readLine()) != null){

                    response.append(inputLine);

                }
                bufferedReader.close();
                System.out.println("5");

            }
        } catch (Exception e) {
            System.out.println( "ERRORKKKKKKKKKKKKKKKKK: " + e.getMessage() );
        }
    }

    public int getResponse() {
        return responseInt;
    }

}
