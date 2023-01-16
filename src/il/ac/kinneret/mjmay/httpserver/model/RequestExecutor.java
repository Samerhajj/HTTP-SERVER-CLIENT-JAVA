package il.ac.kinneret.mjmay.httpserver.model;

import java.util.concurrent.Executor;

/**
 * Handles requests for the web server by opening a new thread for each request
 */
public class RequestExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        // start a new thread for the command
        Thread thread = new Thread(command);
        // run it.
        thread.start();
    }
}
