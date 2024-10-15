public class Ship {
    private final int length;
    private int hitCount;  // Aantal hits dat het schip heeft opgelopen

    public Ship(int length) {
        this.length = length;
        this.hitCount = 0;  // Begin met 0 hits
    }

    public int getLength() {
        return length;
    }

    public void hit() {
        hitCount++;  // Verhoog het hit-aantal
    }

    public boolean isSunk() {
        return hitCount >= length;  // Controleer of het schip gezonken is
    }
}
