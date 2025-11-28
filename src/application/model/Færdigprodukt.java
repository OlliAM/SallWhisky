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

    /**
     * @param navn
     * @param anvendteDestillater
     * @param mængdeL
     * @param procentFørFortynding
     * @param procentEfterFortynding
     * @param tilsatVandL
     * @param vandOprindelse
     * @param produktNr
     * @param beskrivelse
     * @param dato if null dato = localDate.now()
     */
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
        if (dato == null) {
            dato = LocalDate.now();
        } else {
            this.dato = dato;
        }
        flasker = new ArrayList<>(); // ??
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

    public double getMængdeL() {
        return mængdeL;
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

//    public void setProduktNr(int produktNr) {
//        this.produktNr = produktNr;
//    }

//    public void setBeskrivelse(String beskrivelse) {
//        this.beskrivelse = beskrivelse;
//    }

    public LocalDate getDato() {
        return dato;
    }

    public void setDato(LocalDate dato) {
        this.dato = dato;
    }


    public Map<Destillat, Fad> getAnvendteDestillater() {
        return new HashMap<>(anvendteDestillater);
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
     *
     * @param antal > 0
     * @return List<Flaske> af x antal opfyldte flasker med en vis kapacitet i L.
     * @throws IllegalArgumentException, hvis der ikke er nok færdigprodukt til at instansiere det ønskede antal.
     * @Pre: flaskeKapacitetL > 0
     * @Pre: mængdeL >= flaskeKapacitetL
     */
    public List<Flaske> hældPåFlasker(int antal, double flaskeKapacitetL) {
        int muligtAntal = antalMuligeFlasker(flaskeKapacitetL);

        if (muligtAntal < antal) {
            throw new IllegalArgumentException("Du kan max lave " + muligtAntal + " flasker.");
        } else {
            for (int i = 1; i <= antal; i++) {
                Flaske flaske = new Flaske(this, flaskeKapacitetL, i);
                mængdeL -= flaskeKapacitetL;
                flasker.add(flaske);
            }
        }
        return new ArrayList<>(flasker);
    }

    /**
     * Returnerer en liste af så mange flasker, som mængden af færdigprodukt tillader.
     *
     * @return List<Flaske> af max mulige antal opfyldte flasker med en vis kapacitet i L
     * @throws IllegalArgumentException, hvis der ikke er nok færdigprodukt til at instansiere én flaske.
     * @Pre: flaskeKapacitetL > 0
     * @Pre: mængde >= flaskeKapacitetL
     */
    public List<Flaske> hældPåFlaskerMax(double flaskeKapacitetL) {
        int muligtAntal = antalMuligeFlasker(flaskeKapacitetL);

        if (muligtAntal < 1) {
            throw new IllegalArgumentException("Du kan max lave " + muligtAntal + " flasker.");
        }
        for (int i = 1; i <= muligtAntal; i++) {
            Flaske flaske = new Flaske(this, flaskeKapacitetL, i);
            mængdeL -= flaskeKapacitetL;
            flasker.add(flaske);
        }
        return new ArrayList<>(flasker);
    }

    /**
     * Metode, der returnerer antallet af, hvor mange flasker en vis mængde
     * færdigprodukt potentielt kan lave.
     * @param flaskeKapacitetL > 0
     * @return int antal potentielle flasker
     */
    public int antalMuligeFlasker(double flaskeKapacitetL) {
        return (int) (mængdeL / flaskeKapacitetL);
    }
}
