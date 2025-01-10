package stratego.gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;


public class strategoSettings extends JPanel {
    public strategoSettings(JFrame frame) {

        // Layout van de GUI
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Marges tussen de componenten
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        Map<String, String> settingsload = loadSettings("src/stratego/utils/settings.txt");

        // Username
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        JTextField usernameVeld = new JTextField(settingsload.getOrDefault("Username", ""), 15);;
        add(usernameVeld, gbc);

        // IP-Adress
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("IP Address:"), gbc);
        gbc.gridx = 1;
        JTextField ipVeld = new JTextField(settingsload.getOrDefault("IP Address", ""), 15);
        add(ipVeld, gbc);

        // Port
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Port:"), gbc);
        gbc.gridx = 1;
        JTextField portVeld = new JTextField(settingsload.getOrDefault("Port", ""), 15);;
        add(portVeld, gbc);

        // 8x8
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Quick Stratego (8x8)"), gbc);
        gbc.gridx = 1;
        JRadioButton achtVeld = new JRadioButton("8x8");
        add(achtVeld, gbc);

        // 10x10
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Normaal Stratego (10x10)"), gbc);
        gbc.gridx = 1;
        JRadioButton tienVeld = new JRadioButton("10x10");
        add(tienVeld, gbc);

        // 8x8 of 10x10 groeperen zodat er maar 1 mogelijkheid is
        ButtonGroup boardGrootte = new ButtonGroup();
        boardGrootte.add(achtVeld);
        boardGrootte.add(tienVeld);

        String speltype = settingsload.getOrDefault("Speltype", "10x10");
        if (speltype.equals("8x8")) {
            achtVeld.setSelected(true);
        } else {
            tienVeld.setSelected(true); // Standaard 10x10 want dat is normale stratego
        }

        // De save knop
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton saveButton = new JButton("SAVE");
        add(saveButton, gbc);

        

        // De gegevens opslaan in onze settings file
        saveButton.addActionListener(e -> {
            // Verzamel de gegevens
            String username = usernameVeld.getText();
            String ipAddress = ipVeld.getText();
            String port = portVeld.getText();
            String boardgrootte = achtVeld.isSelected() ? "8x8" : "10x10";

            if (!port.matches("\\d{4}")) {
                JOptionPane.showMessageDialog(this, "Onjuiste port.", "Fout", JOptionPane.ERROR_MESSAGE);
                return;
            }



            // Maak een string voor het tekstbestand
            String settings = "Username: " + username + "\n" +
                    "IP Address: " + ipAddress + "\n" +
                    "Port: " + port + "\n" +
                    "Speltype: " + boardgrootte;

            // Schrijf naar een tekstbestand
            String filepath = "src/stratego/utils/settings.txt";
            try (FileWriter fileWriter = new FileWriter(filepath)) {
                fileWriter.write(settings);
                JOptionPane.showMessageDialog(this, "Opgeslagen!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }



        });

        // Terugknop
        JButton backButton = new JButton("Back");
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        add(backButton, gbc);

        // Verandert de frame naar die van StrategoMenu
        backButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.setContentPane(new Menus.StrategoMenu());
            frame.revalidate();
            frame.repaint();
        });



    }

    // Deze klasse helpt met het inlezen van de settings.txt file
    public static Map<String, String> loadSettings(String filePath) {
        Map<String, String> settingsmap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(": ", 2);
                if (parts.length == 2) {
                    settingsmap.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            // Als het bestand niet gevonden wordt of een fout optreedt, leeg laten
            System.err.println("Error: " + e.getMessage());
        }
        return settingsmap;
    }

}



