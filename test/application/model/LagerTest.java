package application.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class LagerTest {
    private Lager lager;
    private Reol reol1, reol2;
    private Fad fad1;

    @BeforeEach
    void setup() {
        lager = new Lager("Lager1");
        reol1 = lager.createReol("A", 10);
        reol2 = lager.createReol("B", 5);
        fad1 = new Fad(1, "Egetræ", 10, "Spanien");
    }

    @Test
    void getTommePladser() {
        //Arrange
        ArrayList<Plads> expectedTommePladser = new ArrayList<>();
        expectedTommePladser.addAll(Arrays.asList(reol1.getPladser()));
        expectedTommePladser.addAll(Arrays.asList(reol2.getPladser()));

        //Act
        ArrayList<Plads> actualTommePladser = lager.getTommePladser();

        //Assert
        assertEquals(expectedTommePladser, actualTommePladser);

        lager.gemPåLager(fad1);
        expectedTommePladser.removeFirst();
        actualTommePladser = lager.getTommePladser();
        assertEquals(expectedTommePladser, actualTommePladser);
    }

    @Test
    void gemPåLager_ledigePladser() {
        //Arrange
        Plads expectedPlads = lager.getReoler().getFirst().getPladser()[0];

        //Act & assert
        Plads actualPlads = lager.gemPåLager(fad1);
        assertEquals(expectedPlads, actualPlads);
        assertEquals(fad1, expectedPlads.getVare());
    }

    @Test
    void gemPåLager_ingenLedigePladser() {
        //Arrange
        Lager lager2 = new Lager("Lager2");
        lager2.createReol("A", 1);
        lager2.createReol("B", 0);
        Fad fad2 = new Fad(2, "Egetræ", 10, "Italien");
        lager2.gemPåLager(fad1);

        //Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> lager2.gemPåLager(fad2));

        //Assert
        assertEquals(lager2.getLagerNavn() + " har ingen tomme pladser",exception.getMessage());
    }

    @Test
    void søgPåLager_findFadtypePåReol() {
        //Arrange
        Fad fad2 = new Fad(2, "Egetræ", 10, "Italien");
        Fad fad3 = new Fad(3, "Bøgtræ", 20, "Rusland");
        String fadtype = "Egetræ";
        Plads plads1 = reol1.gemPåReol(fad1);
        reol1.gemPåReol(fad3);
        Plads plads2 = reol2.gemPåReol(fad2);
        ArrayList<Plads> expectedFade = new ArrayList<>(Arrays.asList(plads1, plads2));

        //Act
        ArrayList<Plads> actualFade = lager.søgPåLager(fadtype);

        //Assert
        assertEquals(expectedFade, actualFade);
    }

    @Test
    void søgPåLager_findFadtypeIkkePåReol() {
        //Arrange
        Fad fad2 = new Fad(2, "Egetræ", 10, "Italien");
        String fadtype = "Bøgtræ";
        reol1.gemPåReol(fad1);
        reol2.gemPåReol(fad2);
        ArrayList<Plads> expectedFade = new ArrayList<>();

        //Act
        ArrayList<Plads> actualFade = lager.søgPåLager(fadtype);

        //Assert
        assertEquals(expectedFade, actualFade);
    }

    @Test
    void søgPåLager_FindDestillatPåLager() {
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
        Fad fad2 = new Fad(2, "Egetræ", 10, "Italien");
        Fad fad3 = new Fad(3, "Egetræ", 10, "Uruguay");
        Fad fad4 = new Fad(4, "Egetræ", 10, "Dominica");
        fad1.fyldPåFraDestillat(bundDestillat1, 2, LocalDate.now(), "EH");
        fad1.fyldPåFraDestillat(bundDestillat2, 2, LocalDate.now(), "EH");
        fad1.fyldPåFraDestillat(bundDestillat3, 2, LocalDate.now(), "EH");
        fad2.fyldPåFraDestillat(bundDestillat2, 2, LocalDate.now(), "EH");
        fad3.fyldPåFraDestillat(bundDestillat4, 2, LocalDate.now(), "EH");
        fad3.fyldPåFraDestillat(bundDestillat5, 2, LocalDate.now(), "EH");

        reol1.gemPåReol(fad1);
        reol1.gemPåReol(fad2);
        reol2.gemPåReol(fad3);
        reol2.gemPåReol(fad4);

        //Act & assert
        ArrayList<Plads> actualPladser = lager.søgPåLager(bundDestillat1);
        ArrayList<Plads> expectedPladser = new ArrayList<>(Arrays.asList(fad1.getPlads()));
        assertEquals(expectedPladser, actualPladser);

        actualPladser = lager.søgPåLager(bundDestillat2);
        expectedPladser = new ArrayList<>(Arrays.asList(fad1.getPlads(), fad2.getPlads()));
        assertEquals(expectedPladser, actualPladser);
    }

    @Test
    void søgPåReol_findDestillatIkkePåReol() {
        //Arrange
        BundDestillat bundDestillat1 = new BundDestillat("Bunddestillat1", LocalDate.of(2025,1,1),
                LocalDate.of(2025,1,2),"Byg", "Tørv", "EH", 10,
                40);
        fad1.fyldPåFraDestillat(bundDestillat1, 2, LocalDate.now(), "EH");


        //Act & assert
        ArrayList<Plads> actualPladser = lager.søgPåLager(bundDestillat1);
        ArrayList<Plads> expectedPladser = new ArrayList<>();
        assertEquals(expectedPladser, actualPladser);
    }
}