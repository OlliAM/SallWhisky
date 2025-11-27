package application.model;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;


public class BundDestillat extends Destillat{
    private double mængdeL;
    private LocalDate startDato;
    private String kornsort;
    private double maltBatchKg;
    private String rygemateriale;

    public BundDestillat(@NotNull LocalDate startDato, LocalDate slutDato, String kornsort, double maltBatchKg,
                         String rygemateriale, String init) {
        super(slutDato, init);
        this.startDato = startDato;
        this.kornsort = kornsort;
        this.maltBatchKg = maltBatchKg;
        this.rygemateriale = rygemateriale;
    }

    //    Vi laver setter-metode for de variable, som kan tilføjes senere, således at et destillat kan oprettes i systemet
    //    Før det er færdigt

    public double getMængdeL() {
        return mængdeL;
    }

    public void setMængdeL(double mængdeL) {
        this.mængdeL = mængdeL;
    }

    public LocalDate getStartDato() {
        return startDato;
    }

    public String getKornsort() {
        return kornsort;
    }

    public double getMaltBatchKg() {
        return maltBatchKg;
    }

    public String getRygemateriale() {
        return rygemateriale;
    }
}
