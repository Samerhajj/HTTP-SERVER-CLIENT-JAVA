package il.ac.kinneret.mjmay.httpclient;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class HttpClientWindow implements Initializable {
    public TextField tfURL;
    public Button bSend;
    public TextField tfDNSResults;
    public TextArea taResponse;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void doSend(ActionEvent event) throws MalformedURLException, UnknownHostException {

        /// TODO: Fill in sending code

            URL url = new URL(tfURL.getText());
            String host = url.getHost();
            //FORMAT URL CORRECTLY
            System.out.println(host);
            //host is giving correct url
            //get the address dns from the server
            //append it to the field
            InetAddress[] addresses = InetAddress.getAllByName(host);
            StringBuilder sb = new StringBuilder();
            for (InetAddress address : addresses) {
                sb.append(address.getHostAddress()).append((" "));
            }
            tfDNSResults.setText(sb.toString());
//Once we have the URL formatted properly, we can then connect to the remote web server using an
//HttpURLConnection:
//HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
//GET RESPONSE
            //show request properties
            Map<String,List<String>> requestHeaders=connection.getRequestProperties();
            System.out.println(connection.getRequestProperties());
            Map<String, List<String>> headers = connection.getHeaderFields();
            for (String header : headers.keySet()) {
                System.out.println(header + ": " + headers.get(header));
                taResponse.appendText(header + ":" + headers.get(header) + "\n");
            }
          //  System.out.println(connection.getHeaderFields());
            for(String requestProperty:requestHeaders.keySet())
            {
                if(requestProperty!=null)
                {
                    taResponse.appendText(requestProperty +" :");
                }
                for (String s :requestHeaders.get(requestProperty))
                {
                    taResponse.appendText(s+", ");
                }
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine).append("\n");
            }
            in.close();
            taResponse.appendText("\n");
            taResponse.appendText(response.toString());

            connection.disconnect();
        } catch (IOException e) {
            System.err.println("I AM AN EXCEPTION MAN");

        }
    }
}
