package il.ac.kinneret.mjmay.httpserver;

import il.ac.kinneret.mjmay.httpserver.model.HttpServerState;
import il.ac.kinneret.mjmay.httpserver.model.ThreadPerTaskExecutor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

public class HttpServerWindow implements Initializable {
    public ComboBox cbIPs;
    public TextField tfPort;
    private ThreadPerTaskExecutor executor;
    public TextField tfServerContext;
    public Button bStartStop;
    public TextArea taLog;
    public TextField tfFileRoot;

    private boolean listening;
    private HttpServerState serverState;
    private DirectoryChooser chooser;

    public void startStopServer(ActionEvent actionEvent) {

        /// TODO: Fill in the Server start and stop logic
        if(!listening)
        {
            //start server
            InetAddress chosenIP=(InetAddress) cbIPs.getValue();
            int port = Integer.parseInt(tfPort.getText());
            String serverContext=tfServerContext.getText();
            File root = new File(tfFileRoot.getText());
            Path rootPath = root.toPath();
            System.out.println(root);
            if(!Files.isDirectory(rootPath)){
                taLog.appendText("Invalid file root directory.\n");
                return;
            }
                serverState= new HttpServerState(chosenIP,port,serverContext,rootPath.toString());
            taLog.appendText("Server started on " + chosenIP + ":" + port + "\n");
            // Start the thread that listens for incoming connections
            listening=true;
            bStartStop.setText("Stop");
            executor = new ThreadPerTaskExecutor(Thread::new);
            executor.execute(() -> {
                serverState.isListening();
            });
            }
        else{
            // Stop the server
            listening = false;
            serverState.stop();
            bStartStop.setText("Start");
            taLog.appendText("Server stopped.\n");
        }
        }


    public void chooseFileRoot(ActionEvent actionEvent) {
        File chosen = chooser.showDialog(ServerMain.theStage);
        // get the directory chosen
        if ( chosen != null)
        {
            tfFileRoot.setText(chosen.toString());
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // fill in the IP addresses of the computer in the combobox
        ObservableList<InetAddress> localIPs = FXCollections.observableArrayList(getLocalIPs());
        cbIPs.setItems(localIPs);
        cbIPs.setValue("0.0.0.0");

        // we're not listening yet
        listening = false;

        chooser = new DirectoryChooser();
        chooser.setTitle("Choose files root directory");
    }

    public static Vector<InetAddress> getLocalIPs()
    {
        // make a list of addresses to choose from
        // add in the usual ones
        Vector<InetAddress> adds = new Vector<InetAddress>();
        try {
            adds.add(InetAddress.getByAddress(new byte[] {0, 0, 0, 0}));
            adds.add(InetAddress.getByAddress(new byte[] {127, 0, 0, 1}));
        } catch (UnknownHostException ex) {
            // something is really weird - this should never fail
            System.out.println("Can't find IP address 0.0.0.0: " + ex.getMessage());
            ex.printStackTrace();
            return adds;
        }

        try {
            // get the local IP addresses from the network interface listing
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while ( interfaces.hasMoreElements() )
            {
                NetworkInterface ni = interfaces.nextElement();
                // see if it has an IPv4 address
                Enumeration<InetAddress> addresses =  ni.getInetAddresses();
                while ( addresses.hasMoreElements())
                {
                    // go over the addresses and add them
                    InetAddress add = addresses.nextElement();
                    // make sure it's an IPv4 address
                    if (!add.isLoopbackAddress() && add.getClass() == Inet4Address.class)
                    {
                        adds.addElement(add);
                    }
                }
            }
        }
        catch (SocketException ex)
        {
            // can't get local addresses, something's wrong
            System.out.println("Can't get network interface information: " + ex.getLocalizedMessage());
        }
        return adds;
    }

}
