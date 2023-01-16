package il.ac.kinneret.mjmay.httpserver.model;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class HttpServerState {

    /**
     * The server that handles the incoming requests.
     */
    private HttpServer httpServer;

    /**
     * Builds a new Http Server listening on the IP and port that we should listen on as well as the context to listen on
     * @param address The IP address to listen on
     * @param port The port to listen on
     * @param context The context to serve based on
     */
    public HttpServerState(InetAddress address, int port, String context, String fileRoot)
    {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(address, port), 50);
            httpServer.createContext(context, new FilesHandler(context, fileRoot));
            httpServer.setExecutor(new RequestExecutor());
            httpServer.start();
        } catch (IOException e) {
            httpServer = null;
        }
    }

    /**
     * Whether the server is currently listening or not
     * @return True if the server is listening.  False otherwise.
     */
    public boolean isListening()
    {
        return (httpServer != null);
    }

    /**
     * Stops listening on the server
     */
    public void stop ()
    {
        if ( httpServer != null) {
            // stop this in 5 seconds.
            httpServer.stop(5);
            // we're done
            return;
        }
    }

}
