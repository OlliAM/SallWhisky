package application.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StorableTest {
    private Fad fad;
    private Lager lager;
    private Reol reol;
    private Plads plads;

    @BeforeEach
    void setup() {
        fad = new Fad(1, "Egetræ", 10, "Spanien");
        lager = new Lager("Lager1");
        reol = lager.createReol("A", 10);
        plads = reol.getPladser()[0];
    }

    @Test
    void gemPåPlads_storableIkkePåLager() {
        //Act & assert
        assertNull(plads.getVare());
        fad.gemPåPlads(plads);

        assertEquals(fad, plads.getVare());
        assertEquals(plads, fad.getPlads());
    }

    @Test
    void gemPåPlads_optagetPlads() {
        //Arrange
        Fad fad2 = new Fad(2, "Egetræ", 50, "Italien");
        fad.gemPåPlads(plads);

        //Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> fad2.gemPåPlads(plads));

        //Assert
        assertNull(fad2.getPlads());
        assertEquals(fad, plads.getVare());
        assertEquals("Den valgte plads er allerede optaget", exception.getMessage());
    }

    @Test
    void gemPåPlads_storableAlleredePåLager() {
        //Arrange
        Plads plads2 = reol.getPladser()[1];
        fad.gemPåPlads(plads);

        //Act
        fad.gemPåPlads(plads2);

        //Assert
        assertEquals(fad, plads2.getVare());
        assertNull(plads.getVare());
    }

    @Test
    void gemPåPlads_inkrementerOgDekrementerReolFriePladser() {
        //Arrange
        Reol reol2 = lager.createReol("B", 10);
        Plads pladsSammeReol = reol.getPladser()[1];
        Plads pladsNyReol = reol2.getPladser()[0];
        int expectedFriePladser = reol.getFriePladser();

        //Act & assert
        assertEquals(expectedFriePladser, reol.getFriePladser());

        fad.gemPåPlads(plads);
        expectedFriePladser--;
        assertEquals(expectedFriePladser, reol.getFriePladser());

        fad.gemPåPlads(pladsSammeReol);
        assertEquals(expectedFriePladser, reol.getFriePladser());

        fad.gemPåPlads(pladsNyReol);
        expectedFriePladser++;
        assertEquals(expectedFriePladser, reol.getFriePladser());
    }

    @Test
    void gemPåPlads_inkrementerOgDekrementerLagerAntalFade() {
        //Arrange
        Lager lager2 = new Lager("Lager2");
        Reol reol2 = lager.createReol("B", 10);
        Reol reol3 = lager2.createReol("A", 10);
        Plads pladsSammeLager = reol2.getPladser()[0];
        Plads pladsAndetLager = reol3.getPladser()[0];
        int expectedAntalFade = 0;

        //Act & assert
        assertEquals(expectedAntalFade, lager.getAntalFade());

        fad.gemPåPlads(plads);
        expectedAntalFade++;
        assertEquals(expectedAntalFade, lager.getAntalFade());

        fad.gemPåPlads(pladsSammeLager);
        assertEquals(expectedAntalFade, lager.getAntalFade());

        fad.gemPåPlads(pladsAndetLager);
        expectedAntalFade--;
        assertEquals(expectedAntalFade, lager.getAntalFade());
    }

    @Test
    void gemPåPlads_inkrementerOgDekrementerLagerAntalFlasker() {
        //Arrange
        BundDestillat destillat = new BundDestillat("Destillat", LocalDate.of(2020,1,1),
                LocalDate.of(2020,1,2), "Byg", "Tørv", "EH",
                10, 40);
        fad.fyldPåFraDestillat(destillat, 5, LocalDate.of(2020,1,2), "EH");

        Færdigprodukt færdigprodukt = new Færdigprodukt("Færdigprodukt", fad, 5, 0,
                "Vandhane", 1, "", LocalDate.now());
        ArrayList<Flaske> flasker = new ArrayList<>(færdigprodukt.hældPåFlaskerMax(2));
        Flaske flaske = flasker.get(0);

        Lager lager2 = new Lager("Lager2");
        Reol reol2 = lager.createReol("B", 10);
        Reol reol3 = lager2.createReol("A", 10);
        Plads pladsSammeLager = reol2.getPladser()[0];
        Plads pladsAndetLager = reol3.getPladser()[0];
        int expectedAntalFlasker = 0;

        //Act & assert
        assertEquals(expectedAntalFlasker, lager.getAntalFlasker());

        flaske.gemPåPlads(plads);
        expectedAntalFlasker++;
        assertEquals(expectedAntalFlasker, lager.getAntalFlasker());

        flaske.gemPåPlads(pladsSammeLager);
        assertEquals(expectedAntalFlasker, lager.getAntalFlasker());

        flaske.gemPåPlads(pladsAndetLager);
        expectedAntalFlasker--;
        assertEquals(expectedAntalFlasker, lager.getAntalFlasker());
    }

    @Test
    void fjernFraPlads() {
        //Arrange
        fad.gemPåPlads(plads);

        //Act
        fad.fjernFraPlads();

        //Assert
        assertNull(plads.getVare());
        assertNull(fad.getPlads());
        fad.fjernFraPlads();
    }
}