package br.com.devdojo.springbootessentials.javaclient;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Exemplo de requisição com Java Puro
 */
public class JavaClientTest {

    public static void main(String[] args) {

        //para a requisição
        HttpURLConnection connection = null;
        //para ler a resposta da requisição
        BufferedReader reader = null;

        try {
            URL url = new URL("http://localhost:8080/v1/protected/students/3");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Authorization", "Basic " + encodeUserPassword("igor.fraga", "igor.api"));
            System.out.println(encodeUserPassword("igor.fraga", "igor.api"));
            //obtendo o retorno da requisição
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder jsonSB = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null) {
                jsonSB.append(line);
            }

            //mostrando o resultado do retorno coletado no loop acima no console
            System.out.println(jsonSB.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //desconectando e fechando todos os recursos abertos
            IOUtils.closeQuietly(reader);
            if (connection != null) {
                connection.disconnect();
            }
        }


    }

    private static String encodeUserPassword(String user, String password) {
        String userpassword = user + ":" + password;
        return new String(Base64.encodeBase64(userpassword.getBytes()));
    }
}
