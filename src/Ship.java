import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Ship {
    private int size; // Grootte van het schip
    private int hits; // Aantal hits op het schip

    public Ship(int size) {
        this.size = size;
        this.hits = 0;
    }

    // Registreert een hit op het schip
    public void hit() {
        hits++;
    }

    // Controleert of het schip gezonken is
    public boolean isSunk() {
        return hits >= size;
    }
}

