package storage;

import application.model.*;

import java.util.ArrayList;

public class Storage {
    private static Storage storage;
    private ArrayList<Destillat> destillatList;
    private ArrayList<Fad> fadList;
    private ArrayList<Færdigprodukt> færdigproduktList;
    private ArrayList<Lager> lagerList;
    private ArrayList<Indhold> indholdList;

    //Ikke-klasse lister
    private ArrayList<String> rygematerialeList;
    private ArrayList<String> kornsortList;
    private ArrayList<String> fadtypeList;

    private Storage() {
        destillatList = new ArrayList<>();
        fadList = new ArrayList<>();
        færdigproduktList = new ArrayList<>();
        lagerList = new ArrayList<>();
        indholdList = new ArrayList<>();
        rygematerialeList = new ArrayList<>();
        kornsortList = new ArrayList<>();
        fadtypeList = new ArrayList<>();
    }

    public static Storage getInstance() {
        if(storage == null) {
            storage = new Storage();
        }
        return storage;
    }

    // ---------- Destillat ----------
    public void addToDestillatList(Destillat d) {
        destillatList.add(d);
    }

    public void removeFromDestillatList(Destillat d) {
        destillatList.remove(d);
    }

    public ArrayList<Destillat> getDestillatList() {
        return destillatList;
    }

    // ---------- Fad ----------

    public void addToFadList(Fad f) {
        fadList.add(f);
    }

    public void removeFromFadList(Fad f) {
        fadList.remove(f);
    }

    public ArrayList<Fad> getFadList() {
        return fadList;
    }

    // ---------- Færdigprodukt ----------

    public void addToFærdigproduktList(Færdigprodukt fp) {
        færdigproduktList.add(fp);
    }

    public void removeFromFærdigproduktList(Færdigprodukt fp) {
        færdigproduktList.remove(fp);
    }

    public ArrayList<Færdigprodukt> getFærdigproduktList() {
        return færdigproduktList;
    }

    // ---------- Lager ----------
    public void addToLagerList(Lager l) {
        lagerList.add(l);
    }

    public void removeFromLagerList(Lager l) {
        lagerList.remove(l);
    }
    public ArrayList<Lager> getLagerList() {
        return lagerList;
    }


    // ---------- Indhold ----------
    public void addToIndholdList(Indhold i) {
        indholdList.add(i);
    }

    public void removeFromIndholdList(Indhold i) {
        indholdList.remove(i);
    }

    public ArrayList<Indhold> getIndholdList() {
        return indholdList;
    }


    // ---------- Strings ----------
    public void addToRygematerialeList(String s) {
        rygematerialeList.add(s);
    }

    public void removeFromRygematerialeList(String s) {
        rygematerialeList.remove(s);
    }

    public ArrayList<String> getRygematerialeList() {
        return rygematerialeList;
    }

    public void addToKornsortList(String s) {
        kornsortList.add(s);
    }

    public void removeFromKornsortList(String s) {
        kornsortList.remove(s);
    }

    public ArrayList<String> getKornsortList() {
        return kornsortList;
    }

    public void addToFadtypeList(String s) {
        fadtypeList.add(s);
    }

    public void removeFromFadtypeList(String s) {
        fadtypeList.remove(s);
    }

    public ArrayList<String> getFadtypeList() {
        return fadtypeList;
    }
}
