package stratego.server;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class serverConnection implements Runnable {
    // Deze variabelen moeten uit de settings.txt worden gehaald, die gekoppeld moet worden aan de optie menu.
    // De optie menu maakt Jelmer
    private String ip_address;
    private String port;
    private String username;


    // Deze variabelen zijn nodig voor de server connectie
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    public serverConnection() {
        Map<String, String> settings = stratego.gui.strategoSettings.loadSettings("src/stratego/utils/settings.txt");

        this.ip_address = settings.getOrDefault("IP Address", "");
        this.port = settings.getOrDefault("Port", "");
        this.username = settings.getOrDefault("Username", "");

    }

    public void run() {

        try {
            // TEST LINE DEZE KAN VERWIJDERD WORDEN LATER, DE ONDERSTE VERSIE WEL BEHOUDEN!!!!!! (33)
            System.out.println("Connecting to " + ip_address + ":" + port + " as " + username + ".....");

            
            client = new Socket(ip_address, Integer.parseInt(port));
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));


            // Login met onze username
            out.println("login " + username);

            System.out.println("Connecting to " + ip_address + ":" + port + " as " + username + ".....");


            // HIER KOMEN DE INPUT MESSAGES --> DIE DAN DE NODIGE FUNCTIE VOOR DE AI AANROEPT.

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}
