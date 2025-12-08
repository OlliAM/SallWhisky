package application.controller;

import application.model.*;
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
     * @param kornsort Den valgte kornsort brugt til at skabe {@code BundDestillat}
     * @param rygemateriale Det rygemateriale brugt til at lave {@code BundDestillat}
     * @param init Underskriften af den medarbejder der skabte {@code BundDestillat}
     * @throws IllegalArgumentException Hvis slut datoen befinder sig før start datoen
     * @return {@code BundDestillat}
     */
    public BundDestillat createBundDestillat(String navn, LocalDate startDato, String kornsort,
                                             String rygemateriale, String init) {
        BundDestillat bundDestillat = new BundDestillat(navn, startDato, kornsort, rygemateriale, init);
        storage.addToDestilleringList(bundDestillat);
        return bundDestillat;
    }

    public BundDestillat createBundDestillat(String navn, LocalDate startDato, LocalDate slutDato, String kornsort, String rygemateriale, String init,
                                      double mængdeL, double alkoholProcent) {
        if (slutDato.isBefore(startDato)) {
            throw new IllegalArgumentException("Slutdato er før startdato");
        }
        BundDestillat bundDestillat = new BundDestillat(navn, startDato, slutDato, kornsort, rygemateriale, init, mængdeL, alkoholProcent);
        storage.addToDestillatList(bundDestillat);
        return bundDestillat;
    }

    public void færdiggørBundDestillat(BundDestillat bundDestillat, LocalDate slutDato, double mængdeL, double alkoholProcent) {
        if (slutDato != null && slutDato.isBefore(bundDestillat.getStartDato())) {
            throw new IllegalArgumentException("Slutdato er før startdato");
        }
        bundDestillat.setFærdigDato(slutDato);
        bundDestillat.setMængdeL(mængdeL);
        bundDestillat.setAlkoholprocent(alkoholProcent);
    }

    public void givKommentarTilDestillat(Destillat destillat, String kommentar) {
        destillat.setKommentar(kommentar);
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
        Destillat destillat = fad.fyldPåFraDestillat(bundDestillat, mængde, dato, init);
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
        Destillat destillat = fadTilFyldning.fyldPåFraFad(andetFad, mængde, dato, init);
        storage.addToDestillatList(destillat);
        return destillat;
    }

    /**
     * <p1><b><i>**Controller**</i></b></p1><br>
     * <p1>Opret et {@code Fad} objekt</p1>
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

    public void removeFad(Fad fad) {
        storage.removeFromFadList(fad);
    }

    /**
     * <p1><b><i>**Controller**</i></b></p1><br>
     * <p1>Metode til at oprette et endelige produkt, og automatisk tildele den til internte array</p1>
     * @param navn Internt tekst navn for {@code Fab} objektet
     * @param fade Et HashMap fuld a forskellige {@code Fad} objekter
     * @param tilsatVandL Mængden af vand som skal anvendes i {@code  Fad} objektet
     * @param vandOprindelse Historisk og Geologisk repræsentation af vandets process.
     * @param produktNr Det specifikke nummber tildelt det endelige produkt
     * @param beskrivelse Historiske beskrivelse af det endelige produkt
     * @param dato Den endelige dato for oprettelse af {@code Færdigprodukt}
     * @return {@code Færdigprodukt}
     */
    public Færdigprodukt createFærdigProdukt(String navn, Map<Fad, Double> fade,
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

            if (dato.isBefore(fad.getFadIndhold().getFærdigDato())) {
                throw new IllegalArgumentException("Dato for påfyldning af fad " + fad.getFadNr() + " er efter " +
                        "oprettelsesdatoen for færdigproduktet");
            }

            if (fad.getFadIndhold().getFærdigDato().until(dato).getYears() < 3) {
                throw new IllegalArgumentException("Fad " + fad.getFadNr() + " har ikke været lagret i 3 år endnu");
            }

            Destillat destillat = fad.getFadIndhold();
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

    /**
     * <p1><b><i>**Controller**</i></b></p1><br>
     * <p1>Metode til at omhælde det endelige produkt til individuelle flakser</p1>
     * @param færdigprodukt Det endelige produkt der skal hældes på flaske.
     * @param antal Antallet af flasker som metoden skal skabe
     * @param flaskeKapacitetL Mængden af det endelige produkt som skal påfyldes hver {@code Flaske} objekt
     * @return {@code void}
     */
    public void hældPåFlasker(Færdigprodukt færdigprodukt, int antal, double flaskeKapacitetL) {
        færdigprodukt.hældPåFlasker(antal, flaskeKapacitetL);
    }

    /**
     * <p1><b><i>**Controller**</i></b></p1><br>
     * <p1>Metode til skabe så mange mulige {@code Flaske} objekter ud fra det endelige produkt</p1>
     * @param færdigprodukt Det endelige produkt der skal hældes på flaske.
     * @param flaskeKapacitetL Mængden af det endelige produkt som skal påfyldes hver {@code Flaske} objekt
     * @return {@code void}
     */
    public void hældPåFlaskerMax(Færdigprodukt færdigprodukt, double flaskeKapacitetL) {
        færdigprodukt.hældPåFlaskerMax(flaskeKapacitetL);
    }

    /**
     * <p1><b><i>**Controller**</i></b></p1><br>
     * <p1>Metode til at etablere et nyt {@code Lager} objekt</p1>
     * @param navn Identificerende navn for det individuelle {@code Lager} objekt
     * @return {@code Lager}
     */
    public Lager createLager(String navn) {
        Lager lager = new Lager(navn);
        storage.addToLagerList(lager);
        return lager;
    }

    /**
     * <p1><b><i>**Controller**</i></b></p1><br>
     * <p1>Metode til at etablere en ny {@code Reol} på specifikt {@code Lager}</p1>
     * @param lager Det specifikke {@code Lager} objekt hvori reolen skal oprettes
     * @param id Identificerende tekst for {@code Lager} objektet
     * @param antalPladser Antal af tilgængelige lagerpladser {@code Reol} objektet skal indeholde
     * @return {@code Reol}
     */
    public Reol createReol(Lager lager, String id, int antalPladser) {
        return lager.createReol(id, antalPladser);
    }

    /**
     * <p1><b><i>**Controller**</i></b></p1><br>
     * <p1>Metode til at etablere et nyt {@code Indhold} objekt</p1>
     * @param navn Navnet på det {@code Indhold} som skal oprettes
     * @return {@code Indhold}
     */
    public Indhold createIndhold(String navn) {
        Indhold indhold = new Indhold(navn);
        storage.addToIndholdList(indhold);
        return indhold;
    }

    /**
     * <p1><b><i>**Controller**</i></b></p1><br>
     * <p1>Metode for at etablere et nyt {@code Rygemateriale}</p1>
     * @param rygemateriale Navnet på det rygemateriale der øsnkes at registrere
     * @return {@code void}
     */
    public void createRygemateriale(String rygemateriale) {
        storage.addToRygematerialeList(rygemateriale);
    }

    /**
     * <p1><b><i>**Controller**</i></b></p1><br>
     * <p1>Metode for at etablere en ny {@code Kornsort}</p1>
     * @param kornsort Navnet på den kornsort der øsnkes at registrere
     * @return {@code void}
     */
    public void createKornsort(String kornsort) {
        storage.addToKornsortList(kornsort);
    }

    /**
     * <p1><b><i>**Controller**</i></b></p1><br>
     * <p1>Metode for at etablere en ny {@code Fadtype}</p1>
     * @param fadtype Navnet på den fadtype der øsnkes at registrere
     * @return {@code void}
     */
    public void createFadtype(String fadtype) {
        storage.addToFadtypeList(fadtype);
    }

    public void createFadKapacitet(double kapacitet) {
        storage.addToFadKapacitetList(kapacitet);
    }

    /**
     * <p1><b><i>**Controller**</i></b></p1><br>
     * <p1>Metode til at lagre {@code Storable} objekt på tilgængelig lagerplads</p1>
     * @param lager Det specifikke {@code Lager} objekt som produktet skal lagres i
     * @param reol Det specifikke {@code reol} objekt som produktet skal lagres i
     * @param pladsNr Identificerende nummer for lagerplads
     * @param produkt {@code Storable} objekt som skal lagres
     * @return {@code void}
     */
    public void gemPåReol(Lager lager, Reol reol, int pladsNr, Storable produkt) {
        lager.gemPåReol(reol, pladsNr, produkt);
    }

    /**
     * <p1><b><i>**Controller**</i></b></p1><br>
     * <p1>Metode til at fjerne {@code Storable} objekt fra specifik lagerplads</p1>
     * @param lager Det specifikke {@code Lager} objekt som produktet skal fjernes fra
     * @param reol Det specifikke {@code reol} objekt som produktet skal fjernes fra
     * @param pladsNr Identificerende nummer for lagerplads
     * @return {@code Storable}
     */
    public Storable tagFraReol(Lager lager, Reol reol, int pladsNr) {
        return lager.tagFraReol(reol, pladsNr);
    }

    /**
     * <p1><b><i>**Controller**</i></b></p1><br>
     * <p1>Metode til at producere en liste af alle tomme lagerpladser</p1>
     * @param lager Det specifikke {@code Lager} objekt som ønskes søgt i
     * @return {@code Arraylist<String>}
     */
    public ArrayList<Plads> getTommePladser(Lager lager) {
        return lager.getTommePladser();
    }

    /**
     * <p1><b><i>**Controller**</i></b></p1><br>
     * <p1><b><i>**Overloaded**</i></b></p1><br>
     * <p1>Metode til at søge efter {@code Fad} objekt</p1>
     * @param lager Det specifikke {@code Lager} objekt som ønskes søgt i
     * @param fadNr Identificerende nummer for det ønskede {@code Fad} objekt
     * @return {@code String}
     */
    public Plads søgPåLager(Lager lager, int fadNr) {
        return lager.søgPåLager(fadNr);
    }

    /**
     * <p1><b><i>**Controller**</i></b></p1><br>
     * <p1><b><i>**Overloaded**</i></b></p1><br>
     * <p1>Metode til at søge efter alle {@code Fad} objekter som besidder fadtype</p1>
     * @param lager Det specifikke {@code Lager} objekt som ønskes søgt i
     * @param fadtype Den type af {@code Fad} som ønskes at finde
     * @return {@code Arraylist<String>}
     */
    public ArrayList<Plads> søgPåLager(Lager lager, String fadtype) {
        return lager.søgPåLager(fadtype);
    }

    /**
     * <p1><b><i>**Controller**</i></b></p1><br>
     * <p1><b><i>**Overloaded**</i></b></p1><br>
     * <p1>Metode til at søge efter alle {@code Fad} objekter som besidder destillat</p1>
     * @param lager Det specifikke {@code Lager} objekt som ønskes søgt i
     * @param destillat Det indhold som ønskes at finde
     * @return {@code Arraylist<String>}
     */
    public ArrayList<Plads> søgPåLager(Lager lager, Destillat destillat) {
        return lager.søgPåLager(destillat);
    }

    public Storage getStorage() {
        return storage;
    }
}
