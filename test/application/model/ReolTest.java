package application.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.BinaryRefAddr;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReolTest {
    private Reol reol;
    private Fad fad;
    private Lager lager;

    @BeforeEach
    void setup() {
        lager = new Lager("Lager1");
        reol = new Reol(lager,"A", 0);
        fad = new Fad(1, "Egetræ", 10, "Spanien");
    }

    @Test
    void gemPåPlads_pladsOutOfBounds() {
        //Act && assert
        Exception exception = assertThrows(RuntimeException.class, () -> reol.gemPåPlads(11, fad));

        assertEquals("Index out of bounds!", exception.getMessage());
    }

    @Test
    void gemPåPlads_pladsOptaget() {
        //Arrange
        Fad fad2 = new Fad(2, "Egetræ", 10, "Italien");
        reol.gemPåPlads(1, fad);

        //Act & assert
        Exception exception = assertThrows(RuntimeException.class, () -> reol.gemPåPlads(1, fad2));

        assertEquals("Space: 1, is already in use!", exception.getMessage());
    }

    @Test
    void gemPåPlads_friPlads() {
        //Act & assert
        reol.gemPåPlads(1, fad);
        assertEquals(fad, reol.getPladser()[1]);
    }

    @Test
    void tagFraPlads_pladsOutOfBounds() {
        //Act && assert
        Exception exception = assertThrows(RuntimeException.class, () -> reol.tagFraPlads(11));

        assertEquals("Index out of bounds!", exception.getMessage());
    }

    @Test
    void tagFraPlads_pladsTom() {
        //Act & assert
        Exception exception = assertThrows(RuntimeException.class, () -> reol.tagFraPlads(1));

        assertEquals("Space: 1 is currently empty!", exception.getMessage());
    }

    @Test
    void tagFraPlads_pladsOccupied() {
        //Arrange
        reol.gemPåPlads(1, fad);

        //Act
        Storable actual = reol.tagFraPlads(1);

        //Assert
        assertNull(reol.getPladser()[1]);
        assertEquals(fad, actual);
    }

    @Test
    void getTommePladser() {
        //TODO venter indtil vi har besluttet os for hvordan vi håndterer pladser
    }

    @Test
    void getProcentOptaget() {
        //Arrange
        Fad fad2 = new Fad(2, "Egetræ", 10, "Italien");

        //Act & assert
        assertEquals(0, reol.getProcentOptaget());
        reol.gemPåPlads(1, fad);
        assertEquals(10, reol.getProcentOptaget());
        reol.gemPåPlads(2, fad2);
        assertEquals(20, reol.getProcentOptaget());
    }


}