package application.model;

import java.util.ArrayList;
import java.util.Random;

/**
 * <h2>----- Attributer -----</h2>
 * <br>
 * <p1><b>ID (String)</b><br>
 * Tekst repræsentation af den individuelle Reol</p1><br>
 * <br>
 * <p1><b>pladser (Storable[])</b><br>
 * Array af tilgængelige lagerpladser</p1><br>
 * <br>
 * <p1><b>optagedePladser (int)</b><br>
 * Nummeret af Reolens pladser der er optagede</p1><br>
 * <br>
 * <h2>----- Metoder -----</h2>
 * <br>
 * <p1><b>getProcentOptaget()</b><br>
 * Udregner procentdelen af lagerpladser der på nuværende tidspunkt er optager i denne
 * {@code Reol}</p1><br>
 * <br>
 * <p1><b>gemPåPlads(int pladsNr, Storable produkt)</b><br>
 * Metode til at lagre et {@code Storable} objekt på den specificerede lagerplads.<br>
 * <i>Throws</i><br>
 * - IndexOutOfBoundsException: Hvis plads nummeret er udenfor reolen's kapacitet<br>
 * - Exception: Hvis lagerpladsen er allerede optaget af andet produkt.</p1><br>
 * <br>
 * <p1><b>tagFraPlads(int pladsNr)</b><br>
 * Metode til at udtage {@code Storable} objekt på den specificerede lagerplads.<br>
 * <i>Throws</i><br>
 * - IndexOutOfBoundsException: Hvis plads nummeret er udenfor reolen's kapacitet<br>
 * - Exception: Hvis lagerpladsen er ikke besidder et {@code Storable} objekt.</p1><br>
 * <br>
 * <p1><b>getTommePladser()</b><br>
 * Returnerer en liste af alle individuelle lagerpladser der på nuværende tidspunkt
 * ikke indeholder et {@code Storable} objekt</p1><br>
 * <br>
 * <p1><b>søgPåReol(int fadNr)</b><br>
 * Metode til at søge efter specifikke produkter gemt på lageret.<br>
 * Metoden er Overloadet - Yderligere information på metodens JavaDoc.</p1><br>
 * <br>
 * <h3>-----------------------------------------------------------</h3>
 */
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

    // Return the internal ID String of the Reol-instance.
    public String getID() {
        return this.ID;
    }

    // Return the total num of occupied spaces in the Reol-instance.
    public int getOptagedePladser() {
        return this.optagedePladser;
    }

    /**
     * <p1>Metode til at udregne procentdelen af lagerpladser der på nuværende tidspunkt er optaget</p1>
     * @return double
     */
    public double getProcentOptaget() {
        return (pladser.length / optagedePladser);
    }

    /**
     * <p1>Metode til at lagre et {@code Storable} objekt på det specificerede lagerplads nummer</p1>
     * @param pladsNr Identificerende nummer for lagerplads
     * @param produkt {@code Storable} objekt til at lagre
     * @throws IndexOutOfBoundsException Hvis plads nummeret er uden for Reolens kapacitet
     * @throws RuntimeException Hvis den nuværende lagerplads er optaget
     * @return void
     */
    public void gemPåPlads(int pladsNr, Storable produkt) {
        // Check if the pladsNr is out of bounds.
        if (pladsNr > pladser.length) {
            throw new IndexOutOfBoundsException("Index out of bounds!");
        }

        // Check if the space at pladsNr-index is currently occupied.
        if (pladser[pladsNr] != null) {
            throw new RuntimeException("Space: " + pladsNr + ", is already in use!");
        }

        // Add the produkt-instance to specified index & increment by 1.
        pladser[pladsNr] = produkt;
        optagedePladser += 1;
    }

    /**
     * <p1>Utager et {@code Storable} objekt fra den specificerede lagerplads</p1>
     * @param pladsNr Identificerende nummer for lagerplads
     * @throws IndexOutOfBoundsException Hvis plads nummeret er uden for Reolens kapacitet
     * @throws RuntimeException Hvis den nuværende lagerplads er {@code null}
     * @return {@code Storable}
     */
    public Storable tagFraPlads(int pladsNr) {
        // Check if the pladsNr is out of bounds.
        if (pladsNr > pladser.length) {
            throw new IndexOutOfBoundsException("Index out of bounds!");
        }

        // Check if the space at pladsNr-index is currently occupied.
        if (pladser[pladsNr] == null) {
            throw new RuntimeException("Space: " + pladsNr + ", is currently empty!");
        }

        // Remove the Storable-value from specified index & decrement counter.
        Storable værdi = pladser[pladsNr];
        optagedePladser -= 1;
        return værdi;
    }

    /**
     * <p1>Metode til at producere en liste af alle individuelle lagerpladser som er ledige</p1>
     * @return {@code Arraylist<String>}
     */
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

    /**
     * <p1><b><i>**Overloaded**</i></b></p1><br>
     * <p1>Metode til at søge efter et individuelt {@code Fad} objekt på det tilhørende reol
     * ud fra objektets ID nummer. Udfører en Linear Søgning eftersom placeringen er sporadisk</p1>
     * @param fadNr ID nummer for fadet som søges
     * @return {@code String} / {@code null}
     */
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

    /**
     * <p1><b><i>**Overloaded**</i></b></p1><br>
     * <p1>Metode til at søge efter alle {@code Fad} objekter på reolen
     * som besidder den specifikke {@code fadType} variabel.
     * Udfører en Linear Søgning eftersom placeringen er sporadisk</p1>
     * @param fadtype Typen af {@code Fad} der søges
     * @return {@code Arraylist<String>} / {@code null}
     */
    public ArrayList<String> søgPåReol(String fadtype) {

        // Instantiate new arraylist.
        ArrayList<String> list = new ArrayList<>();

        // 'iterate over all spaces.
        for (int i = 0; i < pladser.length; i++) {
            Storable plads = pladser[i];
            // check if the Storable value is off type: 'Fadtype'.
            if (plads instanceof Fad) {
                Fad fad = (Fad) plads;
                if (fad.getFadtype().equals(fadtype)) {
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

    /**
     * <p1><b><i>**Overloaded**</i></b></p1><br>
     * <p1>Metode til at søge efter alle {@code Fad} objekter på reolen
     * som indeholder det specifikke {@code destillat}.
     * Udfører en Linear Søgning eftersom placeringen er sporadisk</p1>
     * @param destillat Typen af {@code destillat} som søges
     * @return {@code Arraylist<String>} / {@code null}
     */
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
