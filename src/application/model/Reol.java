package application.model;

import java.util.ArrayList;

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
    private int optagedePladser;

    //Linkattributes
    private Plads[] pladser;
    private Lager lager;

    // Constructor (protected).
    Reol(Lager lager, String ID, int antalPladser) {
        this.lager = lager;
        this.ID = ID;
        pladser = new Plads[antalPladser];
        for (int i = 0; i < antalPladser; i++) {
            pladser[i] = new Plads(this, i + 1);
        }
        optagedePladser = 0;
    }

    public Plads[] getPladser() {
        return pladser.clone();
    }

    // Return the internal ID String of the Reol-instance.
    public String getID() {
        return this.ID;
    }

    // Return the total num of occupied spaces in the Reol-instance.
    public int getOptagedePladser() {
        return optagedePladser;
    }

    /**
     * <p1>Metode til at udregne procentdelen af lagerpladser der på nuværende tidspunkt er optaget</p1>
     *
     * @return double
     */
    public double getProcentOptaget() {
        double procentOptaget = 0;
        if (optagedePladser > 0) {
            procentOptaget = ((double) optagedePladser / pladser.length * 100);
        }
        return procentOptaget;
    }

    /**
     * <p1>Metode til at lagre valgte instans af {@code Storable} i specificerede {@code Reol}.
     * Metoden opfanger hvilken subklasse objektet tilhører og optæller herefter
     * interne {@code antalFade} eller {@code antalFlasker} variabler.</p1>
     *
     * @param reol    reol til at gemme produktet i
     * @param produkt valgte produkt til at lagre
     * @return void
     * @throws IndexOutOfBoundsException Hvis {@code pladsNr} param er uden for Reolens kapacitet
     * @throws RuntimeException          Hvis den valgte lagerplads allerede anvendes.
     */
    public void gemPåReol(Reol reol, Storable produkt) {
        ArrayList<Plads> tommePladser = reol.getTommePladser();

        if(tommePladser.isEmpty()) {
            throw new IllegalArgumentException("Reol " + ID + " har ingen tomme pladser");
        }

        produkt.gemPåPlads(tommePladser.getFirst());
        optagedePladser++;
    }

    /**
     * <p1>Utager et {@code Storable} objekt fra den specificerede lagerplads</p1>
     *
     * @param pladsNr Identificerende nummer for lagerplads
     * @return {@code Storable}
     * @throws IndexOutOfBoundsException Hvis plads nummeret er uden for Reolens kapacitet
     * @throws RuntimeException          Hvis den nuværende lagerplads er {@code null}
     */
    public Storable tagFraPlads(int pladsNr) {
        // Check if the pladsNr is out of bounds.
        if (pladsNr > pladser.length) {
            throw new IndexOutOfBoundsException("Pladsen er udenfor reolens kapacitet.");
        }

        // Check if the space at pladsNr-index is currently occupied.
        if (pladser[pladsNr] == null) {
            throw new RuntimeException("Space: " + pladsNr + " is currently empty!");
        }

        // Remove the Storable-value from specified index & decrement counter.
        Storable værdi = pladser[pladsNr].getVare();
        pladser[pladsNr].setVare(null);
        optagedePladser--;
        return værdi;
    }

    /**
     * <p1>Metode til at producere en liste af alle individuelle lagerpladser som er ledige</p1>
     *
     * @return {@code Arraylist<String>}
     */
    public ArrayList<Plads> getTommePladser() {
        // Initiate the internal Arraylist.
        ArrayList<Plads> tommePladser = new ArrayList<>();

        // For loop to iterate over all spaces & add if necessary.
        for (Plads plads : pladser) {
            if (plads.getVare() != null) {
                tommePladser.add(plads);
            }
        }
        // Return final list.
        return tommePladser;
    }

    /**
     * <p1><b><i>**Overloaded**</i></b></p1><br>
     * <p1>Metode til at søge efter et individuelt {@code Fad} objekt på det tilhørende reol
     * ud fra objektets ID nummer. Udfører en Linear Søgning eftersom placeringen er sporadisk</p1>
     *
     * @param fadNr ID nummer for fadet som søges
     * @return {@code Plads} / {@code null}
     */
    public Plads søgPåReol(int fadNr) {
        boolean found = false;
        Plads result = null;

        // 'iterate over all spaces.
        for (Plads plads : pladser) {
            Storable vare = plads.getVare();
            if (vare instanceof Fad) {
                if (((Fad) vare).getFadNr() == fadNr) {
                    result = vare.getPlads();
                }
            }
        }
        return result;
    }

    /**
     * <p1><b><i>**Overloaded**</i></b></p1><br>
     * <p1>Metode til at søge efter alle {@code Fad} objekter på reolen
     * som besidder den specifikke {@code fadType} variabel.
     * Udfører en Linear Søgning eftersom placeringen er sporadisk</p1>
     *
     * @param fadtype Typen af {@code Fad} der søges
     * @return {@code Arraylist<String>} / {@code null}
     */
    public ArrayList<Plads> søgPåReol(String fadtype) {

        // Instantiate new arraylist.
        ArrayList<Plads> list = new ArrayList<>();

        // 'iterate over all spaces.
        for (Plads plads : pladser) {
            if (plads.getVare() instanceof Fad fad) {
                if (fad.getFadtype().equals(fadtype)) {
                    list.add(plads);
                }
            }
        }
        return list;
    }

    /**
     * <p1><b><i>**Overloaded**</i></b></p1><br>
     * <p1>Metode til at søge efter alle {@code Fad} objekter på reolen
     * som indeholder det specifikke {@code destillat}.
     * Udfører en Linear Søgning eftersom placeringen er sporadisk</p1>
     *
     * @param destillat Typen af {@code Destillat} som søges
     * @return {@code Arraylist<String>} / {@code null}
     */
    public ArrayList<Plads> søgPåReol(Destillat destillat) {

        // Instantiate new arraylist.
        ArrayList<Plads> list = new ArrayList<>();

        for (Plads plads : pladser) {
            if (plads.getVare() instanceof Fad fad) {
                if (fad.getFadIndhold() instanceof KombiDestillat kd) {
                    if(kd.indeholderDestillat(destillat)) {
                        list.add(fad.getPlads());
                    }
                }
            }
        }
        return list;
    }

    public Lager getLager() {
        return lager;
    }

    @Override
    public String toString() {
        return ID;
    }
}
