package application.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Færdigprodukt {
    private String navn;
    private int produktNr;
    private double mængdeL;
    private double procentFørFortynding;
    private double procentEfterFortynding;
    private double tilsatVandL;
    private String vandOprindelse;
    private String beskrivelse;
    private LocalDate dato;

    //Linkattributter
    private List<Flaske> flasker;
    private Map<Destillat, Fad> anvendteDestillater;

    public Færdigprodukt(String navn, Map<Destillat, Fad> anvendteDestillater, double mængdeL, double procentFørFortynding,
                         double procentEfterFortynding, double tilsatVandL, String vandOprindelse, int produktNr,
                         String beskrivelse, LocalDate dato) {
        this.navn = navn;
        this.anvendteDestillater = anvendteDestillater;
        this.mængdeL = mængdeL;
        this.procentFørFortynding = procentFørFortynding;
        this.procentEfterFortynding = procentEfterFortynding;
        this.tilsatVandL = tilsatVandL;
        this.vandOprindelse = vandOprindelse;
        this.produktNr = produktNr;
        this.beskrivelse = beskrivelse;
        this.dato = dato;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public void setAnvendteDestillater(Map<Destillat, Fad> anvendteDestillater) {
        this.anvendteDestillater = anvendteDestillater;
    }

    public void setMængdeL(double mængdeL) {
        this.mængdeL = mængdeL;
    }

    public void setProcentFørFortynding(double procentFørFortynding) {
        this.procentFørFortynding = procentFørFortynding;
    }

    public void setProcentEfterFortynding(double procentEfterFortynding) {
        this.procentEfterFortynding = procentEfterFortynding;
    }

    public void setTilsatVandL(double tilsatVandL) {
        this.tilsatVandL = tilsatVandL;
    }

    public void setVandOprindelse(String vandOprindelse) {
        this.vandOprindelse = vandOprindelse;
    }

    public void setProduktNr(int produktNr) {
        this.produktNr = produktNr;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public LocalDate getDato() {
        return dato;
    }

    public void setDato(LocalDate dato) {
        this.dato = dato;
    }

    public void setFlasker(List<Flaske> flasker) {
        this.flasker = flasker;
    }

    public Map<Destillat, Fad> getAnvendteDestillater() {
        return new HashMap<>(anvendteDestillater);
    }

    public double getMængdeL() {
        return mængdeL;
    }

    public double getProcentFørFortynding() {
        return procentFørFortynding;
    }

    public double getProcentEfterFortynding() {
        return procentEfterFortynding;
    }

    public double getTilsatVandL() {
        return tilsatVandL;
    }

    public String getVandOprindelse() {
        return vandOprindelse;
    }

    public int getProduktNr() {
        return produktNr;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public List<Flaske> getFlasker() {
        return new ArrayList<>(flasker);
    }

    /**
     * Producerer og returnerer en liste af et int antal flasker indeholdende færdigproduktet.
     * @Pre: mængdeL >= 0.7L
     * @param antal > 0 & not null
     * @return List</Flaske> af x antal opfyldte flasker
     */
    public List<Flaske> hældPåFlasker(int antal) {
        int muligtAntal = antalMuligeFlasker();

        if (muligtAntal < antal) {
            throw new IllegalArgumentException("Du kan max lave " + muligtAntal + " flasker");
        } else {
            for (int i = 1; i <= antal; i++) {
                Flaske flaske = new Flaske(this, i);
                flasker.add(flaske);
            }
        }
        return new ArrayList<>(flasker);
    }

    /**
     * Returnerer en liste af så mange flasker, som mængden af færdigprodukt tillader.
     * @Pre: this.mængde >= 0.7
     * @return List</Flaske> af max mulig opfyldte flasker
     */
    public List<Flaske> hældPåFlaskerMax() {
        int muligtAntal = antalMuligeFlasker();

        if (muligtAntal < 1) {
            throw new IllegalArgumentException("Du kan max lave " + muligtAntal + "flasker");
        }
            for (int i = 1; i <= muligtAntal; i++) {
                Flaske flaske = new Flaske(this, i);
                flasker.add(flaske);
            }
        return new ArrayList<>(flasker);
    }

    /**
     * Metode, der returnerer antallet af, hvor mange flasker en vis mængde
     * færdigprodukt potentielt kan lave.
     * @return int antal potentielle flasker
     */
    public int antalMuligeFlasker() {
        return (int) (mængdeL / 0.7);
    }
}
