package application.model;

import java.util.ArrayList;

public class Lager {
    // Class variables.
    private String navn;
    private int antalFade;
    private int antalFlasker;

    // Link variables.
    private ArrayList<Reol> reoler = new ArrayList<>();

    // Constructor (private).
    Lager(String navn) {
        this.navn = navn;
        this.antalFade = 0;
        this.antalFlasker = 0;
    }

    // Create & add new Reol-instance to internal Lager-class.
    public Reol opretReol(String ID, int pladser) {
        // Instantiate the new Reol-class to store.
        Storable[] nyePladser = new Storable[pladser];
        Reol newReol = new Reol(ID, nyePladser, 0);

        // Check if the Reol-class already exists.
        if (!reoler.contains(newReol)) {
            reoler.add(newReol);
        }

        // Return Reol-class -> Used for Controller.
        return newReol;
    }

    // Store the specified product at index.
    public void gemPåReol(Reol reol, int pladsNr, Storable produkt) {
        // Call the storage method in Reol-instace -> Handles Errors.
        reol.gemPåPlads(pladsNr, produkt);

        // Check which type of Storable was added & increment internal counter.
        if (produkt instanceof Fad) {
            antalFade += 1;
        } else {
            antalFlasker += 1;
        }
    }

    // Method for returning all currently empty spaces.
    public ArrayList<String> getTommePladser() {

        // Instantialize new Arraylist.
        ArrayList<String> list = new ArrayList<>();

        // Iterate through all currently stored Reol-instances.
        for (Reol reol : reoler) {
            ArrayList<String> reolList = reol.getTommePladser();
            list.addAll(reolList);
        }

        // Return the finalized list.
        return  list;
    }

    // Linear Search method (Override).
    // FadNr-search.
    public String søgPåLager(int fadNr) {

        // Iterate over entire list of Reol-instances.
        for (Reol reol : reoler) {
            String værdi = reol.søgPåReol(fadNr);
            // If null is returned the value was not found!
            // If a String-type was returned the value was found!
            if (værdi != null) {
                return værdi;
            }
        }
        // Default = Unsuccessfull search.
        return "Cask with ID: " + fadNr + ", not found!";
    }

    // Linear Search method (Override).
    // Fadtype-search.
    public ArrayList<String> søgPåLager(Fadtype fadtype) {

        // Initiate new arraylist instance.
        ArrayList<String> list = new ArrayList<>();

        // Iterate over entire list of Reol-instances.
        for (Reol reol : reoler) {
            ArrayList<String> værdier = reol.søgPåReol(fadtype);
            // If null is returned the value was not found!
            // If an Arraylist-type was returned the value was found!
            if (værdier != null) {
                list.addAll(værdier);
            }
        }
        // Default = Unsuccessfull search.
        if (list.isEmpty()) {
            return null;
        } else {
            return list;
        }
    }

    // Linear Search method (Override).
    // Destillat-search.
    public ArrayList<String> søgPåLager(Destillat destillat) {

        // Initiate new arraylist instance.
        ArrayList<String> list = new ArrayList<>();

        // Iterate over entire list of Reol-instances.
        for (Reol reol : reoler) {
            ArrayList<String> værdier = reol.søgPåReol(destillat);
            // If null is returned the value was not found!
            // If an Arraylist-type was returned the value was found!
            if (værdier != null) {
                list.addAll(værdier);
            }
        }
        // Default = Unsuccessfull search.
        if (list.isEmpty()) {
            return null;
        } else {
            return list;
        }
    }
}
