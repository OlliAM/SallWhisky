package application.model;

import java.util.ArrayList;

/**
 * <h2> ----- Attributer ----- </h2>
 * <br>
 * <p1><b>navn (String):</b><br>
 * Navn repræsenterer lagerets navn/ID.</p1><br>
 * <br>
 * <p1><b>antalFade (int)</b><br>
 * Antallet af Fad-klasse instanser nuværende gemt på lageret.<br>
 * Instantialiseret til 0.</p1><br>
 * <br>
 * <p1><b>antalFlasker (int)</b><br>
 * Antallet af Flaske-klasse instanser nuværende gemt på lageret.<br>
 * Instantialiseret til 0.</p1><br>
 * <br>
 * <h2> ----- Metoder ----- </h2>
 * <br>
 * <p1><b>opretReol(String ID, int pladser):</b><br>
 * Metode til at oprette ny instans af Reol-klasse med prekonfigureret antal af lagerpladser.<br>
 * Reol-instansen gemmes herefter på den tilhørende Lager-klasse instans.</p1><br>
 * <br>
 *
 * <p1><b>gemPåReol(Reol reol, int PladsNr, Storable produkt):</b><br>
 * Metode til at lagrere den tilhørende produkt på specificeret Reol og lagerplads nr.<br>
 * <i>Throws</i><br>
 * - IndexOutOfBoundsException: Hvis plads nummeret er udenfor reolen's kapacitet.<br>
 * - Exception: Hvis lagerpladsen er allerede optaget af andet produkt.</p1><br>
 * <br>
 *
 * <p1><b>tagFraReol(Reol reol, int pladsNr):</b><br>
 * Metode til at returnere Storable-objektet fra en specifik Reol og lagerplads nummer.<br>
 * <i>Throws</i><br>
 * - IndexOutOfBoundsException: Hvis plads nummeret er udenfor reolen's kapacitet.<br>
 * - Exception: Hvis lagerpladsen ikke besidder et Storable-objekt.</p1><br>
 * <br>
 *
 * <p1><b>getTommePladser():</b><br>
 * Metode til at returnere en liste af alle ledige lagerpladser nuværende på lageret.<br>
 * Listen består af String representationer af de forskellige lagerpladser.</p1><br>
 * <br>
 * <p1><b>søgPåLager(...)</b><br>
 * Metode til at søge efter specifikke produkter gemt på lageret.<br>
 * Metoden er Overloadet - Yderligere information på metodens JavaDoc.</p1><br>
 * <br>
 * <h3>-----------------------------------------------------------</h3>
 */
public class Lager {
    private String navn;
    private int antalFade;
    private int antalFlasker;

    // Link variables.
    private ArrayList<Reol> reoler = new ArrayList<>();

    // Constructor
    public Lager(String navn) {
        this.navn = navn;
        this.antalFade = 0;
        this.antalFlasker = 0;
    }

    // Return the internal name String of the Lager-instance.
    public String getLagerNavn() {
        return this.navn;
    }

    // Return the total num of Cask-instances in storage..
    public int getAntalFade() {
        return this.antalFade;
    }

    // Return the total num of Bottle-instances in storage..
    public int getAntalFlasker() {
        return this.antalFlasker;
    }

    /**
     * <p1> Opretter et instanse af {@code Reol-klassen} med prekonfigureret lagerplads
     * og lagrer den i tilhørende {@code Lager-klasse}.</p1><br>
     * @param ID String id
     * @param pladser nummer af tilgængelige lagerpladser
     * @return Reol
     */
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

    /**
     * <p1>Metode til at lagre valgte instans af {@code Storable} i specificerede {@code Reol}.
     * Metoden opfanger hvilken subklasse objektet tilhører og optæller herefter
     * interne {@code antalFade} eller {@code antalFlasker} variabler.</p1>
     * @param reol reol til at gemme produktet i
     * @param pladsNr tilhørende nummer af lagerplads
     * @param produkt valgte produkt til at lagre
     * @throws IndexOutOfBoundsException Hvis {@code pladsNr} param er uden for Reolens kapacitet
     * @throws RuntimeException Hvis den valgte lagerplads allerede anvendes.
     * @return void
     */
    public void gemPåReol(Reol reol, int pladsNr, Storable produkt) {
        // Call the storage method in Reol-instance -> Handles Errors.
        try {
            reol.gemPåPlads(pladsNr, produkt);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        // Check which type of Storable was added & increment internal counter.
        if (produkt instanceof Fad) {
            antalFade += 1;
        } else {
            antalFlasker += 1;
        }
    }

    /**
     * <p1>Metode til at udtage et {@code Storable} objekt fra den specificerede lagerplads.
     * Metoden opfanger hvilken subklasse objektet tilhører og nedsætter herefter
     * interne {@code antalFade} eller {@code antalFlasker} variabler. </p1>
     * @param reol reol til at tage produktet fra
     * @param pladsNr tilhørende nummer af lagerplads
     * @throws IndexOutOfBoundsException Hvis {@code pladsNr} param er uden for Reolens kapacitet
     * @throws RuntimeException Hvis den valgte lagerplads er uden {@code Storable} objekt.
     * @return Storable
     */
    public Storable tagFraReol(Reol reol, int pladsNr) {
        // Initiate new Storable variable.
        Storable produkt = null;

        // Call the removal method in Reol-instace -> Handles Errors
        try {
            produkt = reol.tagFraPlads(pladsNr);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        // Check which type of Storable was removed & decrement internal counter.
        if (produkt instanceof Fad) {
            antalFade -= 1;
        } else {
            antalFlasker -= 1;
        }

        // Return Storable object.
        return produkt;
    }

    /**
     * <p1>Metode til at udarbejde en liste af individuelle lagerpladser der på nuværende tidspunkt
     * ikke indeholder et {@code Storable} objekt</p1>
     * @return {@code Arraylist<String>}
     */
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

    /**
     * <p1><b><i>**Overloaded**</i></b></p1><br>
     * <p1>Metode til at søge efter et individuelt {@code Fad} objekt på det tilhørende lager
     * ud fra objektets ID nummer. Udfører en Linear Søgning eftersom placeringen er sporadisk</p1>
     * @param fadNr ID nummer for fadet som søges
     * @return String
     */
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

    /**
     * <p1><b><i>**Overloaded**</i></b></p1><br>
     * <p1>Metode til at søge efter alle {@code Fad} objekter på det tilhørende lager
     * som besidder den specifikke {@code fadType} variabel.
     * Udfører en Linear Søgning eftersom placeringen er sporadisk</p1>
     * @param fadtype Typen af {@code Fad} der søges
     * @return {@code Arraylist<String>} / {@code null}
     */
    public ArrayList<String> søgPåLager(String fadtype) {

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

    /**
     * <p1><b><i>**Overloaded**</i></b></p1><br>
     * <p1>Metode til at søge efter alle {@code Fad} objekter på det tilhørende lager
     * som indeholder det specifikke {@code destillat}.
     * Udfører en Linear Søgning eftersom placeringen er sporadisk</p1>
     * @param destillat Typen af {@code destillat} som søges
     * @return {@code Arraylist<String>} / {@code null}
     */
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

    public ArrayList<Reol> getReoler() {
        return new ArrayList<>(reoler);
    }
}
