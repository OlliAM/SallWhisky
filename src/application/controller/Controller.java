package application.controller;

import application.model.*;
import org.jetbrains.annotations.NotNull;
import storage.Storage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Controller {
    private Storage storage;

    public Controller() {
        this.storage = Storage.getInstance();
    }

    /**
     * <p1><b><i>**Controller**</i></b></p1><br>
     * <p1>Metode til at oprette individuelt {@code BundDestillat} objekt og tilføje den i {@code Storage} lager</p1>
     * @param startDato Den endelige start dato for destillatet
     * @param slutDato Den endelige slut dato for destillatet
     * @param kornsort Den valgte kornsort brugt til at skabe {@code BundDestillat}
     * @param maltBatchKg Mængden af malte anvent i oprettelsen, repræsenteret i KG
     * @param rygemateriale Det rygemateriale brugt til at lave {@code BundDestillat}
     * @param init Underskriften af den medarbejder der skabte {@code BundDestillat}
     * @throws IllegalArgumentException Hvis slut datoen befinder sig før start datoen
     * @return {@code BundDestillat}
     */
    public BundDestillat createBundDestillat(@NotNull LocalDate startDato, LocalDate slutDato, String kornsort,
                                             double maltBatchKg, String rygemateriale, String init) {
        if (slutDato.isBefore(startDato)) {
            throw new IllegalArgumentException("Slutdato er før startdato");
        }
        BundDestillat bundDestillat = new BundDestillat(startDato, slutDato, kornsort, maltBatchKg, rygemateriale, init);
        storage.addToDestillatList(bundDestillat);
        return bundDestillat;
    }

    /**
     * <p1><b><i>**Controller**</i></b></p1><br>
     * <p1><b><i>**Overloaded**</i></b></p1><br>
     * <p1>Metode til at påfylde {@code Destillat} objekt til et udvalgt {@code Fad} objekt </p1>
     * @param fad Det {@code Fad} objekt som det endelige destillat ønkes lagret i
     * @param bundDestillat Det underordnede instans af {@code BundDestillat} klassen
     * @param mængde Mængden af væskes der skal påfyldes.
     * @param dato Oprettelsesdato for det individuelle {@code Destillat} objekt
     * @param init Underskriften af den medarbejder der skabte {@code Destillat}
     * @return {@code Destillat}
     */
    public Destillat fyldPåFad(Fad fad, BundDestillat bundDestillat, int mængde, LocalDate dato, String init) {
        Destillat destillat = fad.fyldPå(bundDestillat, mængde, dato, init);
        storage.addToDestillatList(destillat);
        return destillat;
    }

    /**
     * <p1><b><i>**Controller**</i></b></p1><br>
     * <p1><b><i>**Overloaded**</i></b></p1><br>
     * <p1>Metode til at påfylde {@code Fad} objekt til et udvalgt {@code Fad} objekt </p1>
     * @param fadTilFyldning Den generelle {@code Fad} hvis indhold skal påfyldes
     * @param andetFad Den andet {@code Fad} hvis indhold påhældes det nye fad
     * @param mængde Mængde af væske som skal påfyldes det endelige {@code Fad} objekt
     * @param dato Datoen for påfyldning af fad
     * @param init Underskriften fra medarbejderne som undertog påfyldningen.
     * @return {@code Destillat}
     */
    public Destillat fyldPåFad(Fad fadTilFyldning, Fad andetFad, int mængde, LocalDate dato, String init) {
        Destillat destillat = fadTilFyldning.fyldPå(andetFad, mængde, dato, init);
        storage.addToDestillatList(destillat);
        return destillat;
    }

    /**
     * <p1><b><i>**Controller**</i></b></p1><br>
     * <p1><b><i>**Overloaded**</i></b></p1><br>
     * @param fadtype Fadets overordnede type
     * @param kapacitetL Væskemængden af tilfængelige kapacitet repræsenteret i KG
     * @param oprindelse Tekst repræsentation af hvor {@code Fad} objektet stammer fra
     * @return {@code Fad}
     */
    public Fad createFad(String fadtype, double kapacitetL, String oprindelse) {
        // Automatisering af fadNr:
        int fadNr = 1;
        ArrayList<Fad> fade = storage.getFadList();

        if (!fade.isEmpty()) {
            fadNr = fade.getLast().getFadNr() + 1;
        }


        Fad fad = new Fad(fadNr, fadtype, kapacitetL, oprindelse);

        storage.addToFadList(fad);
        return fad;
    }

    public Færdigprodukt opretFærdigProdukt(String navn, Map<Fad, Double> fade,
                                            double tilsatVandL, String vandOprindelse, int produktNr, String beskrivelse,
                                            LocalDate dato) {

        HashMap<Destillat, Fad> anvendteDestillater = new HashMap<>();
        double alkoholVolumen = 0;
        double samletVolumen = 0;

        for (Fad fad : fade.keySet()) {
            double mængde = fade.get(fad);

            if (mængde > fad.getMængdeL()) {
                throw new IllegalArgumentException("Mængde der skal hældes fra fad " + fad.getFadNr() + " er større end" +
                        "indholdet");
            }

            if (dato.isBefore(fad.getIndhold().getFærdigDato())) {
                throw new IllegalArgumentException("Dato for påfyldning af fad " + fad.getFadNr() + " er efter " +
                        "oprettelsesdatoen for færdigproduktet");
            }

            if (fad.getIndhold().getFærdigDato().until(dato).getYears() < 3) {
                throw new IllegalArgumentException("Fad " + fad.getFadNr() + " har ikke været lagret i 3 år endnu");
            }

            Destillat destillat = fad.getIndhold();
            anvendteDestillater.put(destillat, fad);

            alkoholVolumen += mængde / 100 * destillat.getAlkoholprocent();
            samletVolumen += mængde;
        }

        double alkoholprocentFør = alkoholVolumen / samletVolumen * 100;
        samletVolumen += tilsatVandL;
        double alkoholprocentEfter = alkoholVolumen / samletVolumen * 100;

        if (alkoholprocentEfter < 40) {
            throw new IllegalArgumentException("Endelig alkoholprocent er under 40%");
        }

        Færdigprodukt færdigprodukt = new Færdigprodukt(navn, anvendteDestillater, samletVolumen, alkoholprocentFør,
                alkoholprocentEfter, tilsatVandL, vandOprindelse, produktNr, beskrivelse, dato);
        storage.addToFærdigproduktList(færdigprodukt);
        return færdigprodukt;
    }

    public void hældPåFlasker(Færdigprodukt færdigprodukt, int antal, double flaskeKapacitetL) {
        færdigprodukt.hældPåFlasker(antal, flaskeKapacitetL);
    }

    public void hældPåFlaskerMax(Færdigprodukt færdigprodukt, double flaskeKapacitetL) {
        færdigprodukt.hældPåFlaskerMax(flaskeKapacitetL);
    }

    public Lager createLager(String navn) {
        Lager lager = new Lager(navn);
        storage.addToLagerList(lager);
        return lager;
    }

    public Reol createReol(Lager lager, String id, int antalPladser) {
        return lager.opretReol(id, antalPladser);
    }

    public Indhold createIndhold(String navn) {
        Indhold indhold = new Indhold(navn);
        storage.addToIndholdList(indhold);
        return indhold;
    }

    public void createRygemateriale(String rygemateriale) {
        storage.addToRygematerialeList(rygemateriale);
    }

    public void createKornsort(String kornsort) {
        storage.addToKornsortList(kornsort);
    }

    public void createFadtype(String fadtype) {
        storage.addToFadtypeList(fadtype);
    }

    public void gemPåReol(Lager lager, Reol reol, int pladsNr, Storable produkt) {
        lager.gemPåReol(reol, pladsNr, produkt);
    }

    public Storable tagFraReol(Lager lager, Reol reol, int pladsNr) {
        return lager.tagFraReol(reol, pladsNr);
    }

    public ArrayList<String> getTommePladser(Lager lager) {
        return lager.getTommePladser();
    }

    public String søgPåLager(Lager lager, int fadNr) {
        return lager.søgPåLager(fadNr);
    }

    public ArrayList<String> søgPåLager(Lager lager, String fadtype) {
        return lager.søgPåLager(fadtype);
    }

    public ArrayList<String> søgPåLager(Lager lager, Destillat destillat) {
        return lager.søgPåLager(destillat);
    }

    public Storage getStorage() {
        return storage;
    }
}
