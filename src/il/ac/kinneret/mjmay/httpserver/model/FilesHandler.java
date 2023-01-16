package il.ac.kinneret.mjmay.httpserver.model;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilesHandler implements HttpHandler {
    private final String context;
    private File filesRoot;

    /**
     * Retrieves files from the files root given
     * @param context The context that we're listening on
     * @param filesRoot The files root to look for files in
     */
    public FilesHandler(String context, String filesRoot) {
        this.context = context;
        System.out.println(filesRoot);
        this.filesRoot = new File(filesRoot);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        /// TODO: Handle the client request
        String requestURI=exchange.getRequestURI().getPath();
       // System.out.println(requestURI);
        File file=new File(requestURI);
        String fileName=file.getName();
        System.out.println(fileName);
        Path requestedFile=filesRoot.toPath().resolve(fileName);
        System.out.println(requestedFile);
        System.out.println(filesRoot);
        if (Files.exists(requestedFile)&& !Files.isDirectory(requestedFile)){
            Headers headers = exchange.getResponseHeaders();
            //headers.set("Content-Type", "application/octet-stream");
            headers.set("Content-Type", "text/html");
            exchange.sendResponseHeaders(200,Files.size(requestedFile));
            try (OutputStream os = exchange.getResponseBody();
                 FileInputStream fis = new FileInputStream(requestedFile.toFile())) {
                fis.transferTo(os);
            }
        } else {
           send404(exchange);
        }
        exchange.close();
        }



    private void send404 (HttpExchange exchange)
    {
        String response = "<!DOCTYPE html>\n" +
                "<html dir=\"ltr\" lang=\"en\">\n" +
                "  <head>\n" +
                "    <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">\n" +
                "   <title>404!</title>" +
                "</head>" +
                "   <body>Error 404: Couldn't find the file requested.</body>" +
                "</html>";
        try {
            exchange.sendResponseHeaders(404, response.getBytes().length);
            BufferedWriter brOut = new BufferedWriter(new OutputStreamWriter(exchange.getResponseBody()));
            brOut.write(response);
            brOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses the query part of a URL
     * @param query The encoded URL query to parse
     * @param parameters The parameters object that the query will be put in
     * @throws UnsupportedEncodingException If the URL encoding doesn't work
     * @implNote Taken from https://www.codeproject.com/tips/1040097/create-a-simple-web-server-in-java-http-server by Andy Feng
     */
    public static void parseQuery(String query, Map<String,
                Object> parameters) throws UnsupportedEncodingException {

            /// TODO: Write query parsing logic
    }
}
