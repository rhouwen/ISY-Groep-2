package stratego.gui;
import Menus.StrategoMenu;
import stratego.game.Board;
import stratego.game.pieces.Piecefactory;
import stratego.gui.panels.PieceSelectionPanel;
import stratego.utils.ResourceLoader;
import stratego.server.*;
import javax.swing.*;
import java.awt.*;
import java.util.Map;
import stratego.gui.strategoSettings.*;

public class MultiplayerGUI extends JFrame {

    private StrategoGUI gameBoard;
    private PieceSelectionPanel pieceSelectionPanel;
    private serverConnection server;
    Map<String, String> settingsload = strategoSettings.loadSettings("src/stratego/utils/settings.txt");
    private String username = settingsload.getOrDefault("Username", "");

    public MultiplayerGUI() {
        setTitle("Stratego Multiplayer | " + username);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Opent serverconnection in een nieuwe Thread.
        server = new serverConnection();
        new Thread(server).start();

        // Achtergrond
        JPanel mainPanel = ResourceLoader.createBackgroundPanel("images/strategobackground.png");
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Linkerpaneel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setPreferredSize(new Dimension(150, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(Box.createVerticalStrut(20));

        // Back to home knop
        JButton backButton = new JButton("Back to home");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(backButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        backButton.addActionListener(e -> {
            // Sluit de serververbinding
            server.close();

            // Ga terug naar het menu
            this.getContentPane().removeAll();
            this.setContentPane(new Menus.StrategoMenu());
            this.revalidate();
            this.repaint();
        });


        mainPanel.add(buttonPanel, BorderLayout.WEST);

        // Middenstuk
        Board board = new Board(10, 10); // Create a board instance with size 10x10
        gameBoard = new StrategoGUI(board); // Pass the board to StrategoGUI
        JPanel boardPanel = new JPanel(new BorderLayout());
        boardPanel.setOpaque(false);
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Internal margins
        boardPanel.add(gameBoard, BorderLayout.CENTER);
        mainPanel.add(boardPanel, BorderLayout.CENTER);



        // Main panel toevoegen
        setContentPane(mainPanel);

    }

}
