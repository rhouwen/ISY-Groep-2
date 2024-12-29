package stratego.gui;

import javax.swing.*;
import stratego.server.*;

public class MultiplayerGUI extends JPanel {
    public MultiplayerGUI() {
        JLabel label = new JLabel("Multiplayer GUI (Placeholder)");
        add(label);
        new serverConnection().run();
    }
}
