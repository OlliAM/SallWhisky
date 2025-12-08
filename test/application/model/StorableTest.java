package application.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StorableTest {
    private Fad fad;
    private Plads plads;

    @BeforeEach
    void setup() {
        fad = new Fad(1, "Egetræ", 10, "Spanien");
        Lager lager = new Lager("Lager1");
        Reol reol = lager.createReol("A", 10);
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
        Plads plads2 = plads.getReol().getPladser()[1];
        fad.gemPåPlads(plads);

        //Act
        fad.gemPåPlads(plads2);

        //Assert
        assertEquals(fad, plads2.getVare());
        assertNull(plads.getVare());
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
    }
}