package stratego.gui;

public class ClickHandler {
    private static ClickHandler instance;

    private ClickHandler() {}

    public static ClickHandler getInstance() {
        if (instance == null) {
            instance = new ClickHandler();
        }
        return instance;
    }

    public void handleCellClick(int row, int col) {
        System.out.println("Rij: " + row + ", Col: " + col);

    }
}
