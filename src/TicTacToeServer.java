import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class TicTacToeServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new TicTacToeHandler());
        server.createContext("/game", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                new Thread(() -> {
                    TicTacToeGame.main(new String[0]);
                }).start();
                String response = "<html><body><h1>Tic Tac Toe Game started!</h1>"
                        + "<a href=\"/\">Back</a></body></html>";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started at http://localhost:8080");
    }

    static class TicTacToeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "<html><body><h1>Welcome to Tic Tac Toe!</h1>"
                    + "<a href=\"/game\">Start Game</a></body></html>";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}