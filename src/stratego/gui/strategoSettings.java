package stratego.gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;


public class strategoSettings extends JPanel {
    public strategoSettings(JFrame frame) {

        // Layout van de GUI
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Marges tussen de componenten
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Username
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        JTextField usernameVeld = new JTextField(15);
        add(usernameVeld, gbc);

        // IP-Adress
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("IP Address:"), gbc);
        gbc.gridx = 1;
        JTextField ipVeld = new JTextField(15);
        add(ipVeld, gbc);

        // Port
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Port:"), gbc);
        gbc.gridx = 1;
        JTextField portVeld = new JTextField(15);
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

        // Standaard is normaal stratego
        tienVeld.setSelected(true);

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

}



