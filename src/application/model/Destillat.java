package application.model;

import java.time.LocalDate;
import java.util.*;

public abstract class Destillat implements Drinkable {
    private double mængdeL;
    private String kommentar;
    private double alkoholprocent;

    //Linkattributter
    private Map<LocalDate, ArrayList<Fad>> fadHistorik;

    //    Vi laver setter-metode for de variable, som kan tilføjes senere, således at et destillat kan oprettes i systemet
    //    Før det er færdigt


    public Destillat() {
        this.fadHistorik = new TreeMap<>();
    }

    public void addFad(Fad fad, LocalDate dato) {
        if(dato == null) {
            dato = LocalDate.now();
        }
        ArrayList<Fad> fade = new ArrayList<>();
        if(fadHistorik.containsKey(dato)) {
            fade = fadHistorik.get(dato);
        }

        if(!fade.contains(fad)) {
            fade.add(fad);
            fadHistorik.put(dato, fade);
            fad.addIndhold(this, dato);
        }

    }

    public void removeFad(Fad fad, LocalDate dato) {
        ArrayList<Fad> fade = fadHistorik.get(dato);
        if(fade != null) {
            if(fade.contains(fad)) {
                fade.remove(fad);
                fad.removeIndhold(this, dato);
            }
        }
    }

    public Map<LocalDate, ArrayList<Fad>> getFadHistorik() {
        return new TreeMap<>(fadHistorik);
    }

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