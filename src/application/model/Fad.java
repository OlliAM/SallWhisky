package application.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Fad implements Storable {
    //Fadene kan typisk bruges mange gange.
    //Størrelserne på fadene kan variere og Sall ser på anvendelse af mindre fade på 30 og 50 liter, men også på
    //store, som kan indeholde 100, 190 eller 250 liter væske

    private int fadNr;
    private String fadtype;
    private double kapacitetL;
    private String oprindelse;
    private double mængdeL;

    //Linkattributter
    private Map<LocalDate, ArrayList<Drinkable>> indholdshistorik;
    private Destillat indhold;

    /**
     *
     * @param fadNr
     * @param fadtype
     * @param kapacitetL
     * @param oprindelse
     */
    public Fad(int fadNr, String fadtype, double kapacitetL, String oprindelse) {
        this.fadNr = fadNr;
        this.fadtype = fadtype;
        this.kapacitetL = kapacitetL;
        this.oprindelse = oprindelse;
        indholdshistorik = new TreeMap<>();
    }

    public void addToHistorik(Drinkable drinkable, LocalDate dato) {
        if (dato == null) {
            dato = LocalDate.now();
        }
        ArrayList<Drinkable> indhold = new ArrayList<>();
        if (indholdshistorik.containsKey(dato)) {
            indhold = indholdshistorik.get(dato);
        }

        if (!indhold.contains(drinkable)) {
            indhold.add(drinkable);
            indholdshistorik.put(dato, indhold);
        }
    }

    public void removeFromHistorik(Drinkable drinkable, LocalDate dato) {
        ArrayList<Drinkable> indhold = indholdshistorik.get(dato);
        if (indhold != null) {
            indhold.remove(drinkable);
        }
    }

    public Destillat fyldPåFraFad(Fad andetFad, int mængde, LocalDate dato, String init) {
        if(mængde > andetFad.getMængdeL() || mængde > (kapacitetL - mængdeL)) {
            throw new IllegalArgumentException("Den givne mængde er ikke indefor fadenes parametre");
        }

        if(dato == null) {
            dato = LocalDate.now();
        }

        Destillat destillat;
        Destillat andetIndhold = andetFad.getIndhold();

        if(indhold == null) {
            destillat = andetIndhold;
        }
        else {
            destillat = new KombiDestillat(dato, init);
            if(andetIndhold.getMaltbatch() == Maltbatch.SINGLE_MALT || andetIndhold.getMaltbatch() == Maltbatch.SINGLE_CASK) {
                destillat.setMaltbatch(Maltbatch.BLENDED);
            }
            ((KombiDestillat) destillat).add(andetFad.getIndhold());
            ((KombiDestillat) destillat).add(indhold);
        }

        indhold = destillat;
        addToHistorik(indhold, dato);
        mængdeL += mængde;
        andetFad.setMængdeL(andetFad.getMængdeL() - mængde);

        return destillat;
    }

    public Destillat fyldPåFraDestillat(BundDestillat bundDestillat, int mængde, LocalDate dato, String init) {
        if(mængde > bundDestillat.getMængdeL() || mængde > (kapacitetL - mængdeL)) {
            throw new IllegalArgumentException("Den givne mængde er ikke indefor fadet og destillatets parametre");
        }

        if(dato == null) {
            dato = LocalDate.now();
        }

        Destillat destillat;

        if(indhold == null) {
            destillat = bundDestillat;
        }
        else {
            destillat = new KombiDestillat(dato, init);

            if(bundDestillat.getMaltbatch() == Maltbatch.SINGLE_MALT) {
                destillat.setMaltbatch(Maltbatch.SINGLE_CASK);
            } else {
                destillat.setMaltbatch(Maltbatch.BLENDED);
            }

            ((KombiDestillat) destillat).add(bundDestillat);
            ((KombiDestillat) destillat).add(indhold);
        }

        indhold = destillat;
        addToHistorik(indhold, dato);
        mængdeL += mængde;
        bundDestillat.setMængdeL(bundDestillat.getMængdeL() - mængde);

        return destillat;
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

    public String getFadtype() {
        return fadtype;
    }

    public void setFadtype(String fadtype) {
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
