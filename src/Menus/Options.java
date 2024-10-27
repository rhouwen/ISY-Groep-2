package Menus;

import javax.swing.*;
import java.awt.*;


public class Options extends JPanel {

    private String returnTo;

    public Options(String returnTo) {
        this.returnTo = returnTo;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel optionsTitle = new JLabel("Options");
        optionsTitle.setFont(new Font("Arial", Font.BOLD, 40));
        optionsTitle.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(optionsTitle, gbc);

        JLabel volumeLabel = new JLabel("Volume");
        volumeLabel.setForeground(Color.WHITE);
        gbc.gridy++;
        add(volumeLabel, gbc);

        JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        volumeSlider.setMajorTickSpacing(20);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        gbc.gridy++;
        add(volumeSlider, gbc);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame != null) {
                frame.getContentPane().removeAll(); // Remove existing content
                if ("Home".equals(returnTo)) {
                    frame.setContentPane(new Home()); // Go back to Home
                } else {
                    frame.setContentPane(new Menu()); // Go back to Menu
                }
                frame.revalidate();
                frame.repaint();
            }
        });
        gbc.gridy++;
        add(backButton, gbc);

        setBackground(Color.DARK_GRAY);
    }
}
