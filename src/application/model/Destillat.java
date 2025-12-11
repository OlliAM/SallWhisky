package application.model;

import java.time.LocalDate;
import java.util.*;

/**
 * <h2> ----- Attributer ----- </h2>
 * <br>
 * <p1><b>kommentar (String):</b><br>
 * Tekst repræsentation af mulige kommentarer fra medarbejdere.</p1><br>
 * <br>
 * <p1><b>alkoholprocent (double):</b><br>
 * Nuværende procent del af {@code Destillat} objektet som er alkohol.</p1><br>
 * <br>
 * <p1><b>færdigDato (LocalDate):</b><br>
 * Den endelige dato hvorved {@code Destillat} objektet er færdig lavet.</p1><br>
 * <br>
 * <p1><b>init (String):</b><br>
 * Initialer fra den medarbejder der laver destillatet.</p1><br>
 * <br>
 * <h3>-------------------------------------------</h3>
 */
public abstract class Destillat implements Drinkable, Comparable<Destillat>{
    private String navn;
    private String kommentar;
    private double alkoholprocent;
    private Maltbatch maltbatch;
    private LocalDate færdigDato;
    private String init;

    //Vi laver setter-metode for de variable, som kan tilføjes senere, således at et destillat kan oprettes i systemet
    //Før det er færdigt
    public Destillat(String navn, LocalDate færdigDato, String init) {
        this.navn = navn;
        this.færdigDato = færdigDato;
        this.init = init;
    }

    public LocalDate getFærdigDato() {
        return færdigDato;
    }

    public void setFærdigDato(LocalDate færdigDato) {
        this.færdigDato = færdigDato;
    }

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        this.init = init;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    public void setAlkoholprocent(double alkoholprocent) {
        this.alkoholprocent = alkoholprocent;
    }

    public String getKommentar() {
        return kommentar;
    }

    public double getAlkoholprocent() {
        return alkoholprocent;
    }

    public Maltbatch getMaltbatch() {
        return maltbatch;
    }

    public void setMaltbatch(Maltbatch maltbatch) {
        this.maltbatch = maltbatch;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getNavn() {
        return navn;
    }

    @Override
    public String toString() {
        return navn + "\n" + alkoholprocent + "%";
    }

    @Override
    public int compareTo(Destillat other) {
        int result;
        if(færdigDato != null) {
            if(other.getFærdigDato() != null) {
                result = other.getFærdigDato().compareTo(færdigDato);
            }
            else if(other instanceof BundDestillat bd) {
                result = bd.getStartDato().compareTo(færdigDato);
            }
            else result = navn.compareTo(other.getNavn());
        }
        else if(this instanceof BundDestillat bd) {
            if(other.getFærdigDato() != null) {
                result = other.getFærdigDato().compareTo(bd.getStartDato());
            }
            else if(other instanceof BundDestillat bd2) {
                result = bd2.getStartDato().compareTo(bd.getStartDato());
            }
            else result = navn.compareTo(other.getNavn());
        }
        else result = navn.compareTo(other.getNavn());

        return result;
    }
}