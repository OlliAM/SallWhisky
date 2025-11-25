package application.model;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public abstract class Destillat implements Drinkable {
    private double mængdeL;
    private String kommentar;
    private double alkoholprocent;


//    Vi laver setter-metode for de variable, som kan tilføjes senere, således at et destillat kan oprettes i systemet
//    Før det er færdigt



    public void setMængdeL(double mængdeL) {
        this.mængdeL = mængdeL;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    public void setAlkoholprocent(double alkoholprocent) {
        this.alkoholprocent = alkoholprocent;
    }

    public double getMængdeL() {
        return mængdeL;
    }

    public String getKommentar() {
        return kommentar;
    }

    public double getAlkoholprocent() {
        return alkoholprocent;
    }
}