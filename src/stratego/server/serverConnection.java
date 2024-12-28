package stratego.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class serverConnection implements Runnable {
    // Deze variabelen moeten uit de settings.json worden gehaald, die gekoppeld moet worden aan de optie menu.
    // De optie menu maakt Jelmer
    private String ip_address;
    private int port;
    private String username;

    // Deze variabelen zijn nodig voor de server connectie
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    public void run() {

        try {
            client = new Socket(ip_address, port);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));


            // Login met onze username
            out.println("login " + username);


            // HIER KOMEN DE INPUT MESSAGES --> DIE DAN DE NODIGE FUNCTIE VOOR DE AI AANROEPT.

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
