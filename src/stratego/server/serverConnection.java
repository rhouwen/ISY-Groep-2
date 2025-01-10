package stratego.server;

import java.io.*;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.*;

public class serverConnection implements Runnable {
    // Deze variabelen moeten uit de settings.txt worden gehaald, die gekoppeld moet worden aan de optie menu.
    // De optie menu maakt Jelmer
    private String ip_address;
    private String port;
    private String username;
    private boolean placed;
    private boolean running = true;


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

            System.out.println("Connected to " + ip_address + ":" + port + " as " + username + ".....");

            String inputMessage;
            while ((inputMessage = in.readLine()) != null && running) {
                // Er is een game match gevonden tegen tegenstander x
                if (inputMessage.startsWith("SVR GAME MATCH")) {
                    System.out.println("Match gevonden");
                    placed = false;
                    // Reset game state klasse
                }

                // Wij zijn aan de beurt om stukken te plaatsen of om te moven
                if (inputMessage.startsWith("SVR GAME YOURTURN")) {
                    if (!placed) {
                        // onze beurt om stukken te plaatsen
                        // AI plaatst stukken
                        // place <index (int)> <rank (string)>
                        placed = true;
                    }
                    if (placed) {
                        // onze beurt voor een zet
                        // AI doet zet
                        // move from <index (int)> to <index (int)>
                    }

                }

                if (inputMessage.startsWith("SVR GAME DEFENSE RESULT")) {
                    // Resultaat van een defensie
                    // Hier moeten we de resultaten verwerken WIP
                }

                if (inputMessage.startsWith("SVR GAME ATTACK RESULT")) {
                    // Resultaat van een aanval
                    // Hier moeten we de resultaten verwerken WIP
                }

                if (inputMessage.startsWith("SVR GAME MOVE")) {
                    // Het verplaatsen van stukken ook van tegenstander
                    // Wellicht nodig om GUI te updated?


                }


            }


        }
        catch (IOException e) {
            if (running) {
                System.err.println("Serverfout: " + e.getMessage());
            }
        }

        
    }
    // Stopt de serverconnectie
    public void close() {
        running = false;
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (client != null && !client.isClosed()) client.close();
            System.out.println("Verbinding verbroken");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
