package application.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class KombiDestillat extends Destillat{
    //Linkattributter
    private ArrayList<Destillat> destillater;

    public KombiDestillat(LocalDate færdigDato, String init) {
        super(færdigDato, init);
        this.destillater = new ArrayList<>();
    }

    public void add(Destillat destillat) {
        if(!destillater.contains(destillat)) {
            destillater.add(destillat);
        }
    }

    public void remove(Destillat destillat) {
        destillater.remove(destillat);
    }

    public Destillat getChild(int i) {
        if(destillater.size() >= i) {
            return destillater.get(i);
        }
        else {
            throw new ArrayIndexOutOfBoundsException("Destillatet består kun af 2");
        }
    }
}
