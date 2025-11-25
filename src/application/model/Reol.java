package application.model;

import java.util.ArrayList;
import java.util.Random;

public class Reol {

    // Class variables.
    private String ID;
    private Storable[] pladser;
    private int optagedePladser;

    // Constructor (private).
    Reol(String ID, Storable[] pladser, int optagedePladser) {
        this.ID = ID;
        this.pladser = pladser;
        this.optagedePladser = optagedePladser;
    }

    // Store the Storable-instance at the specified index.
    public void gemPåPlads(int pladsNr, Storable produkt) {
        // Check if the pladsNr is out of bounds.
        if (pladsNr > pladser.length) {
            throw new IndexOutOfBoundsException("Index out of bounds!");
        }

        // Check if the space at pladsNr-index is currently occupied.
        if (pladser[pladsNr] != null) {
            throw new RuntimeException("Space: " + pladsNr + ", is already in use!");
        }

        // Add the produkt-instance to specified index.
        pladser[pladsNr] = produkt;
    }

    // Get individual slots that contain values.
    public ArrayList<String> getTommePladser() {

        // Initiate the internal Arraylist.
        ArrayList<String> list = new ArrayList<>();

        // For loop to iterate over all spaces & add if necessary.
        for (int i = 0; i < pladser.length; i++) {
            if (pladser[i] != null) {
                list.add(ID + ": " + i);
            }
        }

        // Return final list.
        return list;
    }

    // Internal helper-method for searching (Override).
    // FadNr-search.
    public String søgPåReol(int fadNr) {

        // 'iterate over all spaces.
        for (int i = 0; i < pladser.length; i++) {
            Storable plads = pladser[i];
            // check if the Storable value is off type: 'Fad'.
            if (plads instanceof Fad) {
                Fad fad = (Fad) plads;
                if (fad.getFadNr() == fadNr) {
                    return ID + ": " + i;
                }
            } else {
                continue;
            }
        }

        // Default -> No match found!
        return null;
    }

    // Internal helper-method for searching (Override).
    // Fadtype-search.
    public ArrayList<String> søgPåReol(Fadtype fadtype) {

        // Instantiate new arraylist.
        ArrayList<String> list = new ArrayList<>();

        // 'iterate over all spaces.
        for (int i = 0; i < pladser.length; i++) {
            Storable plads = pladser[i];
            // check if the Storable value is off type: 'Fadtype'.
            if (plads instanceof Fad) {
                Fad fad = (Fad) plads;
                if (fad.getFadtype() == fadtype) {
                    String værdi = ID + ": " + i;
                    list.add(værdi);
                }
            } else {
                continue;
            }
        }

        // Default -> No match found!
        if (list.isEmpty()) {
            return null;
        } else {
            return list;
        }
    }

    // Internal helper-method for searching (Override).
    // Destillat-search.
    public ArrayList<String> søgPåReol(Destillat destillat) {

        // Instantiate new arraylist.
        ArrayList<String> list = new ArrayList<>();

        // 'iterate over all spaces.
        for (int i = 0; i < pladser.length; i++) {
            Storable plads = pladser[i];
            // check if the Storable value is off type: 'Destillat'.
            if (plads instanceof Fad) {
                Fad fad = (Fad) plads;
                if (fad.getIndhold() == destillat) {
                    String værdi = ID + ": " + i;
                    list.add(værdi);
                }
            } else {
                continue;
            }
        }

        // Default -> No match found!
        if (list.isEmpty()) {
            return null;
        } else {
            return list;
        }
    }
}
