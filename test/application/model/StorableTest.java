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
    }

    @Test
    void gemPåPlads_optagetPlads() {
        //
    }

    @Test
    void gemPåPlads_storableAlleredePåLager() {

    }

    @Test
    void fjernFraPlads() {
    }
}