package application.controller;

import application.model.*;
import org.jetbrains.annotations.NotNull;
import storage.Storage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Controller {
    private Storage storage;

    public Controller() {
        this.storage = Storage.getInstance();
    }

    public BundDestillat createBundDestillat(@NotNull LocalDate startDato, LocalDate slutDato, String kornsort,
                                             double maltBatchKg, String rygemateriale, String init) {
        if(slutDato.isBefore(startDato)) {
            throw new IllegalArgumentException("Slutdato er før startdato");
        }
        BundDestillat bundDestillat = new BundDestillat(startDato, slutDato, kornsort, maltBatchKg, rygemateriale, init);
        storage.addToDestillatList(bundDestillat);
        return bundDestillat;
    }

    public Destillat fyldPåFad(Fad fad, BundDestillat bundDestillat, int mængde, LocalDate dato, String init) {
        Destillat destillat = fad.fyldPå(bundDestillat, mængde, dato, init);
        storage.addToDestillatList(destillat);
        return destillat;
    }

    public Destillat fyldPåFad(Fad fadTilFyldning, Fad andetFad, int mængde, LocalDate dato, String init) {
        Destillat destillat = fadTilFyldning.fyldPå(andetFad, mængde, dato, init);
        storage.addToDestillatList(destillat);
        return destillat;
    }

    public Fad createFad(int fadNr, String fadtype, double kapacitetL, String oprindelse) {
        Fad fad = new Fad(fadNr, fadtype, kapacitetL, oprindelse);
        storage.addToFadList(fad);
        return fad;
    }

    public Færdigprodukt opretFærdigProdukt(String navn, Map<Fad, Integer> fade,
                                            double tilsatVandL, String vandOprindelse, int produktNr, String beskrivelse,
                                            LocalDate dato) {
        HashMap<Destillat, Fad> anvendteDestillater = new HashMap<>();
        double alkoholVolumen = 0;
        double samletVolumen = 0;
        for(Fad fad : fade.keySet()) {
            double mængde = fade.get(fad);

            if(mængde > fad.getMængdeL()) {
                throw new IllegalArgumentException("Mængde der skal hældes fra fad " + fad.getFadNr() + " er større end" +
                        "indholdet");
            }

            if(dato.isBefore(fad.getIndhold().getFærdigDato())) {
                throw new IllegalArgumentException("Dato for påfyldning af fad " + fad.getFadNr() + " er efter " +
                        "oprettelsesdatoen for færdigproduktet");
            }

            if(fad.getIndhold().getFærdigDato().until(dato).getYears() < 3) {
                throw new IllegalArgumentException("Fadets indhold har ikke været lagret i 3 år endnu");
            }

            Destillat destillat = fad.getIndhold();
            anvendteDestillater.put(destillat, fad);

            alkoholVolumen += mængde / 100 * destillat.getAlkoholprocent();
            samletVolumen += mængde;
        }

        double alkoholprocentFør = alkoholVolumen / samletVolumen * 100;
        samletVolumen += tilsatVandL;
        double alkoholprocentEfter = alkoholVolumen / samletVolumen * 100;

        if(alkoholprocentEfter < 40) {
            throw new IllegalArgumentException("Endelig alkoholprocent er under 40%");
        }

        Færdigprodukt færdigprodukt = new Færdigprodukt(navn, anvendteDestillater, samletVolumen, alkoholprocentFør,
                alkoholprocentEfter, tilsatVandL, vandOprindelse, produktNr, beskrivelse, dato);
        storage.addToFærdigproduktList(færdigprodukt);
        return færdigprodukt;
    }

    public void hældPåFlasker(Færdigprodukt færdigprodukt, int antal) {
        færdigprodukt.hældPåFlasker(antal);
    }

    public void hældPåFlaskerMax(Færdigprodukt færdigprodukt) {
        //TODO udkommenter når metoden er lavet
        //færdigprodukt.hældPåFlaskerMax();
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

    public Storage getStorage() {
        return storage;
    }
}
