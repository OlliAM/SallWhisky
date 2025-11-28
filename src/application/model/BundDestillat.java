package application.model;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;


public class BundDestillat extends Destillat{
    private double mængdeL;
    private LocalDate startDato;
    private String kornsort;
    private String rygemateriale;

    public BundDestillat(@NotNull LocalDate startDato, LocalDate slutDato, String kornsort,
                         String rygemateriale, String init) {
        super(slutDato, init);
        this.startDato = startDato;
        this.kornsort = kornsort;
        this.rygemateriale = rygemateriale;

        if(kornsort.equals("Byg")) {
            super.setMaltbatch(Maltbatch.SINGLE_CASK);
        }
        else {
            super.setMaltbatch(Maltbatch.GRAIN);
        }
    }

    //    Vi laver setter-metode for de variable, som kan tilføjes senere, således at et destillat kan oprettes i systemet
    //    Før det er færdigt

    public double getMængdeL() {
        return mængdeL;
    }

    public void setMængdeL(double mængdeL) {
        this.mængdeL = mængdeL;
    }

    @Override
    public void setFærdigDato(LocalDate færdigDato) {
        if(færdigDato.isBefore(startDato)) {
            throw new IllegalArgumentException("færdig dato er før startdato");
        }
    }

    public LocalDate getStartDato() {
        return startDato;
    }

    public String getKornsort() {
        return kornsort;
    }

    public String getRygemateriale() {
        return rygemateriale;
    }
}
