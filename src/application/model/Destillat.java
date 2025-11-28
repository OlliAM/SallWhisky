package application.model;

import java.time.LocalDate;
import java.util.*;

public abstract class Destillat implements Drinkable {
    private String kommentar;
    private double alkoholprocent;
    private Maltbatch maltbatch;
    private LocalDate færdigDato;
    private String init;

    public LocalDate getFærdigDato() {
        return færdigDato;
    }

    //    Vi laver setter-metode for de variable, som kan tilføjes senere, således at et destillat kan oprettes i systemet
    //    Før det er færdigt
    public Destillat(LocalDate færdigDato, String init) {
        this.færdigDato = færdigDato;
        this.init = init;
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


}