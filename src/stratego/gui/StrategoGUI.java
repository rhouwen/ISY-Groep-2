package stratego.gui;

import javax.swing.*;
import java.awt.*;

public class StrategoGUI extends JPanel {
    //Abstracte class voor gedeelde GUI functies

    public StrategoGUI() {

        setLayout(new BorderLayout());
        add(new BoardPanel(), BorderLayout.CENTER);

        JPanel leftpanel = new JPanel(new BorderLayout());
        leftpanel.add(new CapturedPiecesPanel("Geslagen stukken van rood"), BorderLayout.NORTH);
        leftpanel.add(new ScorePanel("Score"), BorderLayout.CENTER);
        leftpanel.add(new CapturedPiecesPanel("Geslagen stukken van blauw"), BorderLayout.SOUTH);
        add(leftpanel, BorderLayout.WEST);

        //Water instellen
        BoardPanel.updateCell(4, 2, "", Color.BLUE);
        BoardPanel.updateCell(4, 3, "", Color.BLUE);
        BoardPanel.updateCell(5, 3, "", Color.BLUE);
        BoardPanel.updateCell(5, 2, "", Color.BLUE);

        BoardPanel.updateCell(4, 6, "", Color.BLUE);
        BoardPanel.updateCell(4, 7, "", Color.BLUE);
        BoardPanel.updateCell(5, 7, "", Color.BLUE);
        BoardPanel.updateCell(5, 6, "", Color.BLUE);

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Stratego");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,600);
        frame.add(new StrategoGUI());
        frame.setVisible(true);
    }

}
