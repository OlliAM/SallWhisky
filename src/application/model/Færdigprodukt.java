package application.model;

import java.util.ArrayList;
import java.util.List;

public class Færdigprodukt {
    private List<Fad> anvendteDestillater;
    private double mængdeL;
    private double procentFørFortynding;
    private double procentEfterFortynding;
    private double tilsatVandL;
    private String vandOprindelse;
    private int produktNr;
    private String beskrivelse;

    //Linkattributter
    List<Flaske> flasker;

    Færdigprodukt(Fad fad, List<Fad> anvendteDestillater, double mængdeL, double procentFørFortynding,
                  double procentEfterFortynding, double tilsatVandL, String vandOprindelse, int produktNr,
                  String beskrivelse) {
        this.anvendteDestillater = anvendteDestillater;
        this.mængdeL = mængdeL;
        this.procentFørFortynding = procentFørFortynding;
        this.procentEfterFortynding = procentEfterFortynding;
        this.tilsatVandL = tilsatVandL;
        this.vandOprindelse = vandOprindelse;
        this.produktNr = produktNr;
        this.beskrivelse = beskrivelse;
    }

    public List<Fad> getAnvendteDestillater() {
        return new ArrayList<>(anvendteDestillater);
    }

    public double getMængdeL() {
        return mængdeL;
    }

    public double getProcentFørFortynding() {
        return procentFørFortynding;
    }

    public double getProcentEfterFortynding() {
        return procentEfterFortynding;
    }

    public double getTilsatVandL() {
        return tilsatVandL;
    }

    public String getVandOprindelse() {
        return vandOprindelse;
    }

    public int getProduktNr() {
        return produktNr;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public List<Flaske> getFlasker() {
        return new ArrayList<>(flasker);
    }

    // to forksellige metoder: en der laver så mange flasker som muligt
    // en der laver én
    public Flaske hældPåFlaske() {
//        if (mængdeL > )
        return null;
    }

    public Flaske hældPåFlasker(int antal) {
        // TODO


        return null;
    }

    public Flaske hældPåFlaskerMax(Fad fad) {
        // TODO


        return null;
    }
}
