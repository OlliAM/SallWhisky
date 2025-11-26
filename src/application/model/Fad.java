package application.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Fad implements Storable{
    //Fadene kan typisk bruges mange gange.
    //Størrelserne på fadene kan variere og Sall ser på anvendelse af mindre fade på 30 og 50 liter, men også på
    //store, som kan indeholde 100, 190 eller 250 liter væske

    private int fadNr;
    private Fadtype fadtype;
    private double kapacitetL;
    private String oprindelse;
    private double mængdeL;

    //Linkattributter
    private Map<LocalDate, ArrayList<Drinkable>> indholdshistorik;
    private Destillat indhold;

    public Fad(int fadNr, Fadtype fadtype, double kapacitetL, String oprindelse, double mængdeL) {
        this.fadNr = fadNr;
        this.fadtype = fadtype;
        this.kapacitetL = kapacitetL;
        this.oprindelse = oprindelse;
        this.mængdeL = mængdeL;
        indholdshistorik = new TreeMap<>();
    }

    public void addIndhold(Drinkable drinkable, LocalDate dato) {
        if(dato == null) {
            dato = LocalDate.now();
        }
        ArrayList<Drinkable> indhold = new ArrayList<>();
        if(indholdshistorik.containsKey(dato)) {
            indhold = indholdshistorik.get(dato);
        }

        if(!indhold.contains(drinkable)) {
            indhold.add(drinkable);
            indholdshistorik.put(dato, indhold);

            if(drinkable instanceof Destillat) {
                ((Destillat) drinkable).addFad(this, dato);
            }
        }
    }

    public void removeIndhold(Drinkable drinkable, LocalDate dato) {
        ArrayList<Drinkable> indhold = indholdshistorik.get(dato);
        if(indhold != null) {
            if(indhold.contains(drinkable)) {
                indhold.remove(drinkable);
                if(drinkable instanceof Destillat) {
                    ((Destillat) drinkable).removeFad(this, dato);
                }
            }
        }
    }

    public Map<LocalDate, ArrayList<Drinkable>> getIndholdshistorik() {
        return new TreeMap<>(indholdshistorik);
    }

    public Destillat getIndhold() {
        return indhold;
    }

    public int getFadNr() {
        return fadNr;
    }

    public void setFadNr(int fadNr) {
        this.fadNr = fadNr;
    }

    public Fadtype getFadtype() {
        return fadtype;
    }

    public void setFadtype(Fadtype fadtype) {
        this.fadtype = fadtype;
    }

    public double getKapacitetL() {
        return kapacitetL;
    }

    public void setKapacitetL(double kapacitetL) {
        this.kapacitetL = kapacitetL;
    }

    public String getOprindelse() {
        return oprindelse;
    }

    public void setOprindelse(String oprindelse) {
        this.oprindelse = oprindelse;
    }

    public double getMængdeL() {
        return mængdeL;
    }

    public void setMængdeL(double mængdeL) {
        this.mængdeL = mængdeL;
    }
}
