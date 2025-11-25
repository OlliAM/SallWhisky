package application.model;

import java.util.ArrayList;

public class KombiDestillat extends Destillat{
    //Linkattributter
    private ArrayList<Destillat> destillater;

    public KombiDestillat(ArrayList<Destillat> destillater) {
        this.destillater = new ArrayList<>();
    }


}
