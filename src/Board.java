public class Board {
    private Cell[][] cells; // 8x8 grid of cells

    public Board() {
        cells = new Cell[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    public boolean placeShip(Ship ship, int x, int y, boolean horizontal) {
        // Controleer of het schip kan worden geplaatst
        if (horizontal) {
            if (y + ship.getLength() > 8) return false; // Buiten het bord
            for (int i = 0; i < ship.getLength(); i++) {
                if (cells[x][y + i].isOccupied()) return false; // Al bezet
            }
            // Plaats het schip
            for (int i = 0; i < ship.getLength(); i++) {
                cells[x][y + i].setOccupied(true);
                cells[x][y + i].setShip(ship);
            }
        } else {
            if (x + ship.getLength() > 8) return false; // Buiten het bord
            for (int i = 0; i < ship.getLength(); i++) {
                if (cells[x + i][y].isOccupied()) return false; // Al bezet
            }
            // Plaats het schip
            for (int i = 0; i < ship.getLength(); i++) {
                cells[x + i][y].setOccupied(true);
                cells[x + i][y].setShip(ship);
            }
        }
        return true; // Succesvol geplaatst
    }

    public boolean hitCell(int x, int y) {
        if (cells[x][y].isOccupied()) {
            // Als het vakje bezet is, markeer het als geraakt
            cells[x][y].setHit(true);
            return true; // Hit
        }
        return false; // Miss
    }

    public boolean hasShips() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (cells[i][j].isOccupied()) {
                    return true; // Er zijn nog schepen
                }
            }
        }
        return false; // Geen schepen meer
    }
}
