package es.pruebas.reto1_example_2022.network;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


import es.pruebas.reto1_example_2022.beans.Usuario;

public class UsuarioPost extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "/usuarios";
    Usuario usuario;
    public UsuarioPost( Usuario usuarioConstructor) {
        usuario = usuarioConstructor;
        System.out.println("AAAAAAAAAAAAAAAAAAA"+usuario.getEmail());
        run();
    }

    @Override
    public void run() {
        try {



            // The URL
            URL url = new URL( theUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod( "POST" );
            httpURLConnection.setRequestProperty("Content-type" , "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);


            String jsonInputString = "{nombre:"+ usuario.getNombre()+", apellidos:"+usuario.getApellidos()+", email:"+usuario.getEmail()+", password:"+usuario.getPassword()+"}";


            try(OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine ;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                br.close();
            }

        } catch (Exception e) {
            System.out.println( "ERROR: " + e.getMessage() );
        }
    }
}
