public class Cell {
    private boolean occupied; // Is er een schip aanwezig?
    private boolean hit;      // Is het vakje geraakt?
    private Ship ship;       // Het schip dat in deze cel is geplaatst

    public Cell() {
        this.occupied = false; // Cel is standaard niet bezet
        this.hit = false;      // Cel is standaard niet geraakt
        this.ship = null;      // Geen schip is standaard aanwezig
    }

    // Methode om te controleren of de cel bezet is
    public boolean isOccupied() {
        return occupied;
    }

    // Methode om de bezetting van de cel in te stellen
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    // Methode om te controleren of de cel is geraakt
    public boolean isHit() {
        return hit;
    }

    // Methode om in te stellen dat de cel is geraakt
    public void setHit(boolean hit) {
        this.hit = hit;
    }

    // Methode om het schip in de cel in te stellen
    public void setShip(Ship ship) {
        this.ship = ship;
    }

    // Optionele methode om het schip op te halen (indien nodig)
    public Ship getShip() {
        return ship;
    }
}
