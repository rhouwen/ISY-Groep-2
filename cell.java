public abstract class Cell {

    private boolean hasShip;  // Of de cel een schip bevat
    private boolean isHit;    // Of de cel geraakt is

    // Constructor
    public Cell() {
        this.hasShip = false;  // Standaard geen schip
        this.isHit = false;    // Standaard niet geraakt
    }

    // Methode om een schip op de cel te plaatsen
    public void placeShip() {
        this.hasShip = true;
    }

    // Methode om de cel aan te vallen
    public void attack() {
        this.isHit = true;
    }

    // Controleren of de cel een schip heeft
    public boolean hasShip() {
        return hasShip;
    }

    // Controleren of de cel geraakt is
    public boolean isHit() {
        return isHit;
    }
}
