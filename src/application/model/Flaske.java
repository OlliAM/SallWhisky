package application.model;

import java.util.HashSet;
import java.util.Set;

public class Flaske implements Storable {
    private int flaskeNr;
    private double kapacitetL = 0.7;
    private double aktuelleMængdeL;
    private String beskrivelse; // ???
    //Linkattributter
    Set<Færdigprodukt> færdigprodukter;

    public Flaske(int flaskeNr, double aktuelleMængdeL, String historie) {
        this.flaskeNr = flaskeNr;
        this.aktuelleMængdeL = aktuelleMængdeL;
        færdigprodukter = new HashSet<>();

        // TODO Beskrivelse/Historie?
        // Hvordan laver vi flaskeNr?
        // Dobbeltrettethed
    }

    public int getFlaskeNr() {
        return flaskeNr;
    }

    public double getAktuelleMængdeL() {
        return aktuelleMængdeL;
    }

    public void setAktuelleMængdeL(double aktuelleMængdeL) {
        this.aktuelleMængdeL = aktuelleMængdeL;
    }

    public double getKapacitetL() {
        return kapacitetL;
    }

    public HashSet<Færdigprodukt> getFærdigprodukter() {
        return new HashSet<>(færdigprodukter);
    }

    public void addFærdigprodukt(Færdigprodukt færdigprodukt) {
        færdigprodukter.add(færdigprodukt);
    }

    public Færdigprodukt removeFærdigprodukt(Færdigprodukt færdigprodukt) {
        færdigprodukter.remove(færdigprodukt);
        return færdigprodukt;
    }


    public void påfyldFærdigprodukt(Færdigprodukt færdigprodukt) {


        // TODO

    }
}
