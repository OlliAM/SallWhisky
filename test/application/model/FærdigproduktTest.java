package application.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FærdigproduktTest {
    private Færdigprodukt færdigprodukt1;
    private BundDestillat bundDestillat1;
    private Fad fad1, fad2;

    @BeforeEach
    void setup() {
        bundDestillat1 = new BundDestillat("Bunddestillat", LocalDate.of(2020,1,1),
                LocalDate.of(2020,1,2), "Byg", "Tørv", "EH",
                10, 50);
        fad1 = new Fad(1, "Egetræ", 20, "Spain");
        fad2 = new Fad(2, "Egetræ", 20, "Italien");
        fad1.fyldPåFraDestillat(bundDestillat1, 5, LocalDate.of(2020,1,2), "EH");
        færdigprodukt1 = new Færdigprodukt("Færdigprodukt",
                fad1, 1, 0, "Spanien", 3, "",
                LocalDate.of(2025,1,1));
    }

    @Test
    void opretFærdigProdukt_enkeltFad() {
        //Arrange
        String navn = "færdigprodukt";
        double påfyldningsMængde = 5;
        double mængdeVand = 1;
        double totalMængde = påfyldningsMængde + mængdeVand;
        double alkoholProcentFør = bundDestillat1.getAlkoholprocent();
        double mængdeAlkohol = 5 * alkoholProcentFør / 100;
        double alkoholProcentEfter = mængdeAlkohol/totalMængde * 100;
        LocalDate dato = LocalDate.now();
        String beskrivelse = "Mmmh, det lækker :)";
        String vandoprindelse = "Klam brønd";

        //Act
        Færdigprodukt færdigprodukt = new Færdigprodukt(navn, fad1, påfyldningsMængde,
                mængdeVand, vandoprindelse, 1, beskrivelse, dato);


        //Assert
        assertEquals(totalMængde, færdigprodukt.getMængdeL());
        assertEquals(mængdeVand, færdigprodukt.getTilsatVandL());
        assertEquals(alkoholProcentFør, færdigprodukt.getProcentFørFortynding());
        assertEquals(alkoholProcentEfter, færdigprodukt.getProcentEfterFortynding());
        assertEquals(navn, færdigprodukt.getNavn());
        assertEquals(beskrivelse, færdigprodukt.getBeskrivelse());
        assertEquals(vandoprindelse, færdigprodukt.getVandOprindelse());
    }

    @Test
    void opretFærdigProdukt_flereFad() {
        //Arrange
        BundDestillat destillat2 = new BundDestillat("Bunddestillat2", LocalDate.of(2020,1,1),
                LocalDate.of(2020,1,2), "Byg", "Tørv", "EH",
                10, 50);
        fad2.fyldPåFraDestillat(destillat2, 5, LocalDate.now(), "EH");
        Map<Fad, Double> fade = new HashMap<>();
        fade.put(fad1, 5.0);
        fade.put(fad2, 5.0);

        String navn = "færdigprodukt";
        double påfyldningsMængde = 0;
        for(double mængde : fade.values()) {
            påfyldningsMængde += mængde;
        }

        double mængdeVand = 2;
        double totalMængde = påfyldningsMængde + mængdeVand;
        double alkoholVol = 0;
        for(Fad fad : fade.keySet()) {
            alkoholVol += fade.get(fad) / 100 * fad.getFadIndhold().getAlkoholprocent();
        }

        double alkoholProcentFør = alkoholVol / påfyldningsMængde * 100;
        double alkoholProcentEfter = alkoholVol / totalMængde * 100;
        LocalDate dato = LocalDate.now();
        String beskrivelse = "Mmmh, det lækker :)";
        String vandoprindelse = "Klam brønd";

        //Act
        Færdigprodukt færdigprodukt = new Færdigprodukt(navn, fade, mængdeVand, vandoprindelse, 2, beskrivelse,
                dato);

        //Assert
        assertEquals(totalMængde, færdigprodukt.getMængdeL());
        assertEquals(mængdeVand, færdigprodukt.getTilsatVandL());
        assertEquals(alkoholProcentFør, færdigprodukt.getProcentFørFortynding());
        assertEquals(alkoholProcentEfter, færdigprodukt.getProcentEfterFortynding());
        assertEquals(navn, færdigprodukt.getNavn());
        assertEquals(beskrivelse, færdigprodukt.getBeskrivelse());
        assertEquals(vandoprindelse, færdigprodukt.getVandOprindelse());
    }

    @Test
    void opretFærdigProdukt_intetIndhold() {
        //Act
        Exception expected =  assertThrows(IllegalArgumentException.class, () -> new Færdigprodukt("Færdigprodukt",
                fad2, 1, 1, "Spanien", 1, "",
                LocalDate.now()));

        //Assert
        assertEquals("Fadet er tomt", expected.getMessage());
    }
    
    @Test
    void opretFærdigProdukt_påfyldningsmængdeStørreEndFadmængde() {
        Exception expected =  assertThrows(IllegalArgumentException.class, () -> new Færdigprodukt("Færdigprodukt",
                fad1, 11, 1, "Spanien", 1, "",
                LocalDate.now()));

        //Assert
        assertEquals("Mængde der skal hældes fra fad " + fad1.getFadNr() + " er større end " +
                "indholdet", expected.getMessage());
    }

    @Test
    void opretFærdigProdukt_datoFørDestillatFærdigdato() {
        Exception expected =  assertThrows(IllegalArgumentException.class, () -> new Færdigprodukt("Færdigprodukt",
                fad1, 5, 1, "Spanien", 1, "",
                LocalDate.of(2019,1,1)));

        //Assert
        assertEquals("Dato for påfyldning af fad " + fad1.getFadNr() + " er efter " +
                "oprettelsesdatoen for færdigproduktet", expected.getMessage());
    }

    @Test
    void opretFærdigProdukt_ikkeLagretI3År() {
        Exception expected =  assertThrows(IllegalArgumentException.class, () -> new Færdigprodukt("Færdigprodukt",
                fad1, 5, 1, "Spanien", 1, "",
                LocalDate.of(2021,1,1)));

        //Assert
        assertEquals("Fad " + fad1.getFadNr() + " har ikke været lagret i 3 år endnu", expected.getMessage());
    }

    @Test
    void opretFærdigProdukt_procentEfterUnder40() {
        Exception expected =  assertThrows(IllegalArgumentException.class, () -> new Færdigprodukt("Færdigprodukt",
                fad1, 5, 5, "Spanien", 1, "",
                LocalDate.of(2025,1,1)));

        //Assert
        assertEquals("Endelig alkoholprocent er under 40%", expected.getMessage());
    }

    @Test
    void hældPåFlasker() {
        //Arrange
        Færdigprodukt færdigprodukt = new Færdigprodukt("Færdigprodukt",
                fad1, 5, 0, "Spanien", 1, "",
                LocalDate.of(2025,1,1));

        //Act
        ArrayList<Flaske> flasker = new ArrayList<>(færdigprodukt.hældPåFlasker(2,2));

        //Assert
        assertEquals(2, flasker.size());
        Flaske flaske = flasker.getFirst();
        assertEquals(færdigprodukt, flaske.getFærdigprodukt());
        assertEquals(2, flaske.getKapacitetL());
        assertEquals(færdigprodukt.getProduktNr() + "-1", flaske.getFlaskeID());
    }

    @Test
    void hældPåFlaskerMax_mængdeMindreEndFlaskeKapacitet() {
        //Arrange
        double kapacitet = 2;

        //Act
        Exception expected =  assertThrows(IllegalArgumentException.class, () ->
                færdigprodukt1.hældPåFlaskerMax(kapacitet));

        //Assert
        assertEquals("Der er ikke nok væske til at lave en flaske med kapacitet " +
                kapacitet + "L", expected.getMessage());
    }

    @Test
    void hældPåFlaske_antalFlaskerStørreEndMængde() {
        //Arrange
        double kapacitet = 1;

        //Act
        Exception expected =  assertThrows(IllegalArgumentException.class, () ->
                færdigprodukt1.hældPåFlasker(2, kapacitet));

        //Assert
        assertEquals("Du kan max lave " + færdigprodukt1.antalMuligeFlasker(kapacitet) + " flasker af " +
                færdigprodukt1 + ".", expected.getMessage());
    }
}
