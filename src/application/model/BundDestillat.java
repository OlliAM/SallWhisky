package application.model;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

/**
 * <h2> ----- Attributer ----- </h2>
 * <br>
 * <p1><b>mængdeL (double):</b><br>
 * Den nuværende væskemængde repræsenteret i Liter.</p1><br>
 * <br>
 * <p1><b>startDato (LocalDate):</b><br>
 * Datoen hvorpå {@code BundDestillat} objket laves.</p1><br>
 * <br>
 * <p1><b>kornsort (String):</b><br>
 * Sorten af korn anvendt til at lave {@code BundDestillat}.</p1><br>
 * <br>
 * <p1><b>maltBatchKG (double):</b><br>
 * Kilogram a malte anvendt i at lave {@code BundDestillat}.</p1><br>
 * <br>
 * <p1><b>rygemateriale (String):</b><br>
 * Det ekstra materiale der anvendes til at ryge destillatet.</p1><br>
 * <br>
 * <h3>--------------------------------------------</h3>
 */
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
