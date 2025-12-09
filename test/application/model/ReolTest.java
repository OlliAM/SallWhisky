package application.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.BinaryRefAddr;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReolTest {
    private Reol reol;
    private Fad fad, fad2;
    private Lager lager;

    @BeforeEach
    void setup() {
        lager = new Lager("Lager1");
        reol = new Reol(lager,"A", 10);
        fad = new Fad(1, "Egetræ", 10, "Spanien");
        fad2 = new Fad(2, "Egetræ", 10, "Italien");
    }

    @Test
    void getTommePladser_returnererAlleTommePladser() {
        //Arrange
        ArrayList<Plads> expectedTommePladser = new ArrayList<>(Arrays.asList(reol.getPladser()));

        //Act & assert
        ArrayList<Plads> actualTommePladser = reol.getTommePladser();
        assertEquals(expectedTommePladser, actualTommePladser);

        Plads plads = reol.getPladser()[0];
        fad.gemPåPlads(plads);
        expectedTommePladser.remove(plads);
        actualTommePladser = reol.getTommePladser();
        assertEquals(expectedTommePladser, actualTommePladser);
    }

    @Test
    void gemPåReol_gemmerPåFørsteTommePlads() {
        //Arrange
        fad.gemPåPlads(reol.getPladser()[0]);
        
        //Act & assert
        assertNull(reol.getPladser()[1].getVare());
        reol.gemPåReol(fad2);
    }

    @Test
    void gemPåReol_ingenTommePladser() {
        // Arrange
        Reol reol2 = lager.createReol("A", 1);
        reol2.gemPåReol(fad);

        //Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> reol2.gemPåReol(fad2));

        //Assert
        assertEquals("Reol " + reol2.getID() + " har ingen ledige pladser.", exception.getMessage());
    }

    @Test
    void tagFraPlads_pladsOutOfBounds() {
        //Act && assert
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> reol.tagFraPlads(11));

        assertEquals("Reol " + reol.getID() + " har kun " + reol.getPladser().length + " pladser.",
                exception.getMessage());

    }

    @Test
    void tagFraPlads_pladsTom() {
        //Act & assert
        Exception exception = assertThrows(RuntimeException.class, () -> reol.tagFraPlads(1));

        assertEquals("Plads " + reol.getPladser()[0].getPladsNr() + " på " + reol + " er tom.",
                exception.getMessage());
    }


    @Test
    void tagFraPlads_pladsOptaget() {
        //Arrange
        fad.gemPåPlads(reol.getPladser()[0]);

        //Act
        Storable expected = fad;
        Storable actual = reol.tagFraPlads(1);

        //Assert
        assertNull(reol.getPladser()[0].getVare());
        assertEquals(expected, actual);
    }

    @Test
    void getProcentOptaget() {
        //Arrange
        Fad fad2 = new Fad(2, "Egetræ", 10, "Italien");

        //Act & assert
        assertEquals(0, reol.getProcentOptaget());
        reol.gemPåReol(fad);
        assertEquals(10, reol.getProcentOptaget());
        reol.gemPåReol(fad2);
        assertEquals(20, reol.getProcentOptaget());
    }

    @Test
    void søgPåReol_findFadtypePåReol() {
        //Arrange
        Fad fad3 = new Fad(3, "Bøgtræ", 20, "Rusland");
        String fadtype = "Egetræ";
        reol.gemPåReol(fad);
        reol.gemPåReol(fad3);
        reol.gemPåReol(fad2);
        ArrayList<Plads> expectedFade = new ArrayList<>(Arrays.asList(fad.getPlads(), fad2.getPlads()));

        //Act
        ArrayList<Plads> actualFade = reol.søgPåReol(fadtype);

        //Assert
        assertEquals(expectedFade, actualFade);
    }

    @Test
    void søgPåReol_findFadtypeIkkePåReol() {
        //Arrange
        String fadtype = "Bøgtræ";
        reol.gemPåReol(fad);
        reol.gemPåReol(fad2);

        //Act
        ArrayList<Plads> actualFade = reol.søgPåReol(fadtype);

        //Assert
        assertTrue(actualFade.isEmpty());
    }

    @Test
    void søgPåReol_findDestillatPåReol() {
        //Arrange
        BundDestillat bundDestillat1 = new BundDestillat("Bunddestillat1", LocalDate.of(2025,1,1),
                LocalDate.of(2025,1,2),"Byg", "Tørv", "EH", 10,
                40);
        BundDestillat bundDestillat2 = new BundDestillat("Bunddestillat2", LocalDate.of(2025,1,1),
                LocalDate.of(2025,1,2),"Byg", "Tørv", "EH", 10,
                40);
        BundDestillat bundDestillat3 = new BundDestillat("Bunddestillat3", LocalDate.of(2025,1,1),
                LocalDate.of(2025,1,2),"Byg", "Tørv", "EH", 10,
                40);
        BundDestillat bundDestillat4 = new BundDestillat("Bunddestillat4", LocalDate.of(2025,1,1),
                LocalDate.of(2025,1,2),"Byg", "Tørv", "EH", 10,
                40);
        BundDestillat bundDestillat5 = new BundDestillat("Bunddestillat5", LocalDate.of(2025,1,1),
                LocalDate.of(2025,1,2),"Byg", "Tørv", "EH", 10,
                40);
        Fad fad3 = new Fad(3, "Egetræ", 10, "Uruguay");
        Fad fad4 = new Fad(4, "Egetræ", 10, "Dominica");
        fad.fyldPåFraDestillat(bundDestillat1, 2, LocalDate.now(), "EH");
        fad.fyldPåFraDestillat(bundDestillat2, 2, LocalDate.now(), "EH");
        fad.fyldPåFraDestillat(bundDestillat3, 2, LocalDate.now(), "EH");
        fad2.fyldPåFraDestillat(bundDestillat2, 2, LocalDate.now(), "EH");
        fad3.fyldPåFraDestillat(bundDestillat4, 2, LocalDate.now(), "EH");
        fad3.fyldPåFraDestillat(bundDestillat5, 2, LocalDate.now(), "EH");

        reol.gemPåReol(fad);
        reol.gemPåReol(fad2);
        reol.gemPåReol(fad3);
        reol.gemPåReol(fad4);

        //Act & assert
        ArrayList<Plads> actualPladser = reol.søgPåReol(bundDestillat1);
        ArrayList<Plads> expectedPladser = new ArrayList<>(Arrays.asList(fad.getPlads()));
        assertEquals(expectedPladser, actualPladser);

        actualPladser = reol.søgPåReol(bundDestillat2);
        expectedPladser = new ArrayList<>(Arrays.asList(fad.getPlads(), fad2.getPlads()));
        assertEquals(expectedPladser, actualPladser);
    }

    @Test
    void søgPåReol_findDestillatIkkePåReol() {
        //Arrange
        BundDestillat bundDestillat1 = new BundDestillat("Bunddestillat1", LocalDate.of(2025,1,1),
                LocalDate.of(2025,1,2),"Byg", "Tørv", "EH", 10,
                40);
        fad.fyldPåFraDestillat(bundDestillat1, 2, LocalDate.now(), "EH");


        //Act & assert
        ArrayList<Plads> actualPladser = reol.søgPåReol(bundDestillat1);
        ArrayList<Plads> expectedPladser = new ArrayList<>();
        assertEquals(expectedPladser, actualPladser);
    }
}