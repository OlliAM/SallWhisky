package application.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h2> ----- Attributter ----- </h2>
 * <br>
 * <p1><b>navn (String):</b><br>
 * Endelige navn på det færdige produkt.</p1><br>
 * <br>
 * <p1><b>produktNr (int):</b><br>
 * Identifikations nummeret tilhørende dette {@code Færdigprodukt} objekt.</p1><br>
 * <br>
 * <p1><b>mængdeL (double):</b><br>
 * Den nuværende væskemængde af det færdige produkt repræsenteret i Liter.</p1><br>
 * <br>
 * <p1><b>procentFørFortynding (double):</b><br>
 * Procentdelen af det endelige produkt som er alkohol før fortyndingsprocessen.</p1><br>
 * <br>
 * <p1><b>procentEfterFortynding (double):</b><br>
 * Procentdelen af det endelige produkt som er alkohol efter fortyndingsprocessen.</p1><br>
 * <br>
 * <p1><b>tilsatVandL (double):</b><br>
 * Mængden af vand der er blevet tilføjet det endelige produkt repræsenteret i liter.</p1><br>
 * <br>
 * <p1><b>vandOprindelse (String):</b><br>
 * Tekst repræsentation af hvorfra det vand anvendt i fortyndingsprocessen stammer fra.</p1><br>
 * <br>
 * <p1><b>beskrivelse (String):</b><br>
 * En kort beskrivelse af det endelige produkt og dets indholdshistorik.</p1><br>
 * <br>
 * <p1><b>dato (LocalDate):</b><br>
 * Datoen for hvornår det endelige produkt blev færdig lavet.</p1><br>
 * <br>
 * <h2> ----- Metoder ----- </h2>
 * <br>
 * <p1><b>hældPåFlasker (int antal, double flaskeKapacitetL)</b><br>
 * Metode for at påfylde det relaterede {@code Færdigprodukt} til et antal flasker</p1><br>
 * <br>
 * <p1><b>hældPåFlaskerMax (double flaskeKapacitetL)</b><br>
 * Metode til at lave så mange {@code Flaske} objekter som mængden af det endelige produkt tillader</p1><br>
 * <br>
 * <h3>---------------------------------------------------------</h3>
 */
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
     * @param fade
     * @param tilsatVandL
     * @param vandOprindelse
     * @param produktNr
     * @param beskrivelse
     * @param dato if null dato = localDate.now()
     * @Pre: anvendteDestillater er ikke tom
     */
    public Færdigprodukt(String navn, Map<Fad, Double> fade, double tilsatVandL, String vandOprindelse,
                         int produktNr, String beskrivelse, LocalDate dato) {
        this.navn = navn;
        this.tilsatVandL = tilsatVandL;
        this.vandOprindelse = vandOprindelse;
        this.produktNr = produktNr;
        this.beskrivelse = beskrivelse;
        if (dato == null) {
            this.dato = LocalDate.now();
        } else {
            this.dato = dato;
        }
        flasker = new ArrayList<>();
        anvendteDestillater = new HashMap<>();

        double alkoholVolumen = 0;
        double samletVolumen = 0;

        for (Fad fad : fade.keySet()) {
            double mængde = fade.get(fad);

            if(fad.getFadIndhold() == null) {
                throw new IllegalArgumentException("Fadet er tomt");
            }
            if (mængde > fad.getMængdeL()) {
                throw new IllegalArgumentException("Mængde der skal hældes fra fad " + fad.getFadNr() + " er større end " +
                        "indholdet");
            }

            if (dato.isBefore(fad.getFadIndhold().getFærdigDato())) {
                throw new IllegalArgumentException("Dato for påfyldning af fad " + fad.getFadNr() + " er efter " +
                        "oprettelsesdatoen for færdigproduktet");
            }

            if (fad.getFadIndhold().getFærdigDato().until(dato).getYears() < 3) {
                throw new IllegalArgumentException("Fad " + fad.getFadNr() + " har ikke været lagret i 3 år endnu");
            }

            Destillat destillat = fad.getFadIndhold();
            anvendteDestillater.put(destillat, fad);

            alkoholVolumen += mængde / 100 * destillat.getAlkoholprocent();
            samletVolumen += mængde;
        }

        procentFørFortynding = alkoholVolumen / samletVolumen * 100;
        samletVolumen += tilsatVandL;
        procentEfterFortynding = alkoholVolumen / samletVolumen * 100;
        mængdeL = samletVolumen;

        if (procentEfterFortynding < 40) {
            throw new IllegalArgumentException("Endelig alkoholprocent er under 40%");
        }
    }

    public Færdigprodukt(String navn, Fad fad, double mængde, double tilsatVandL, String vandOprindelse, int produktNr,
                         String beskrivelse, LocalDate dato) {
        this(navn, Map.of(fad, mængde), tilsatVandL, vandOprindelse, produktNr, beskrivelse, dato);
    }

    /**
     * <p1>Metode for at påfylde det relaterede {@code Færdigprodukt} til et antal flasker</p1>
     * @param antal Antallet af {@code Flaske} objekter der ønskes (not null & antal > 0)
     * @param flaskeKapacitetL Den endelige kapacitet tilgængelige i hvert {@code Flaske} objekt
     * @throws IllegalArgumentException Hvis det ønskede antal overstiger hvor mange {@code Flaske} objekter kan laves
     * @return {@code List<Flaske>}
     */
    public List<Flaske> hældPåFlasker(int antal, double flaskeKapacitetL) {
        int muligtAntal = antalMuligeFlasker(flaskeKapacitetL);

        if (muligtAntal < antal) {
            throw new IllegalArgumentException("Du kan max lave " + muligtAntal + " flasker af " + this + ".");
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
     * <p1>Metode til at lave så mange {@code Flaske} objekter som mængden af det endelige produkt tillader</p1>
     * @param flaskeKapacitetL Den endelige kapacitet tilgængelig i hvert {@code Flaske} objekt. Skal være > 0
     * @throws IllegalArgumentException Hvis kun 0 {@code Flaske} objekter kan laves
     * @return {@code List<Flaske>}
     */
    public List<Flaske> hældPåFlaskerMax(double flaskeKapacitetL) {
        int muligtAntal = antalMuligeFlasker(flaskeKapacitetL);

        if (muligtAntal < 1) {
            throw new IllegalArgumentException("Du kan max lave " + muligtAntal + " flasker af " + this + ".");
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
     * @throws IllegalArgumentException hvis (@flaskekapacitet) <= 0
     */
    public int antalMuligeFlasker(double flaskeKapacitetL) {
        if (flaskeKapacitetL <= 0) {
            throw new IllegalArgumentException("Kapacitet skal være større end 0.");
        }

        return (int) (mængdeL / flaskeKapacitetL);
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

    @Override
    public String toString() {
        return navn;
    }
}
