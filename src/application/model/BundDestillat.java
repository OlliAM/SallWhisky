package application.model;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;


public class BundDestillat extends Destillat{
    private LocalDate startDato;
    private LocalDate slutDato;
    private Kornsort kornsort;
    private double maltBatchKg;
    private Rygemateriale rygemateriale;

    public BundDestillat(@NotNull LocalDate startDato, LocalDate slutDato, Kornsort kornsort, double maltBatchKg,
                         Rygemateriale rygemateriale) {
        this.startDato = startDato;
        this.slutDato = slutDato;
        this.kornsort = kornsort;
        this.maltBatchKg = maltBatchKg;
        this.rygemateriale = rygemateriale;
    }

    //    Vi laver setter-metode for de variable, som kan tilføjes senere, således at et destillat kan oprettes i systemet
    //    Før det er færdigt

    public void setSlutDato(LocalDate slutDato) {
        this.slutDato = slutDato;
    }

    public LocalDate getStartDato() {
        return startDato;
    }

    public LocalDate getSlutDato() {
        return slutDato;
    }

    public Kornsort getKornsort() {
        return kornsort;
    }

    public double getMaltBatchKg() {
        return maltBatchKg;
    }

    public Rygemateriale getRygemateriale() {
        return rygemateriale;
    }
}
