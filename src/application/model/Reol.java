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
    private String ID;
    private int friePladser;

    //Linkattributter
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
        friePladser = antalPladser;
    }

    /**
     * <p1>Metode til at udregne procentdelen af lagerpladser der på nuværende tidspunkt er optaget</p1>
     *
     * @return double
     */
    public double getProcentOptaget() {
        int optagedePladser = pladser.length - friePladser;
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
     * @param produkt valgte produkt til at lagre
     * @return void
     * @throws IndexOutOfBoundsException Hvis {@code pladsNr} param er uden for Reolens kapacitet
     * @throws RuntimeException          Hvis den valgte lagerplads allerede anvendes.
     */
    public Plads gemPåReol(Storable produkt) {
        ArrayList<Plads> tommePladser = getTommePladser();

        if(tommePladser.isEmpty()) {
            throw new IllegalArgumentException("Reol " + ID + " har ingen ledige pladser.");
        }

        Plads plads = tommePladser.getFirst();
        produkt.gemPåPlads(plads);
        return plads;
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
        int actualPlads = pladsNr -1;
        if (pladsNr > pladser.length) {
            throw new IndexOutOfBoundsException("Reol " + ID + " har kun " + pladser.length + " pladser.");
        }

        // Check if the space at pladsNr-index is currently occupied.
        if (pladser[actualPlads].getVare() == null) {
            throw new IllegalArgumentException("Plads " + pladsNr + " på " + this + " er tom.");
        }

        // Remove the Storable-value from specified index & decrement counter.
        Storable værdi = pladser[actualPlads].getVare();
        pladser[actualPlads].setVare(null);
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
            if (plads.getVare() == null) {
                tommePladser.add(plads);
            }
        }
        // Return final list.
        return tommePladser;
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
                else if(fad.getFadIndhold() instanceof BundDestillat bd) {
                    if(bd == destillat) {
                        list.add(fad.getPlads());
                    }
                }
            }
        }
        return list;
    }

    public Plads[] getPladser() {
        return pladser.clone();
    }

    // Return the internal ID String of the Reol-instance.
    public String getID() {
        return this.ID;
    }

    public Lager getLager() {
        return lager;
    }

    public int getFriePladser() {
        return friePladser;
    }

    public void incrementFriePladser() {
        friePladser++;
    }

    public void decrementFriePladser() {
        friePladser--;
    }

    @Override
    public String toString() {
        return ID;
    }
}
