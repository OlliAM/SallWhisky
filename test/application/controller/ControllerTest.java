package application.controller;

import application.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storage.Storage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
class ControllerTest {
    Controller controller;
    Storage storage;

    @BeforeEach
    void setUp() {
        controller = new Controller();
        storage = controller.getStorage();
    }

    @Test
    void createBundDestillat_createBunddestillatIStorage() {
        //Act && Assert

        assertTrue(storage.getDestillatList().isEmpty());

        BundDestillat expectedBunddestillat = controller.createBundDestillat("Bunddestillat1",
                LocalDate.of(2025,1,1), "Byg", "Tørv", "EH");
        assertEquals(1, storage.getDestillatList().size());

        Destillat actualBunddestillat = storage.getDestillatList().getLast();
        assertEquals(expectedBunddestillat, actualBunddestillat);

        assertEquals(0, expectedBunddestillat.getMængdeL());
        assertEquals("Byg", expectedBunddestillat.getKornsort());
        assertEquals("Tørv", expectedBunddestillat.getRygemateriale());
        assertEquals("EH", expectedBunddestillat.getInit());
        assertEquals(LocalDate.of(2025,1,1), expectedBunddestillat.getStartDato());
        assertEquals(LocalDate.of(2025,1,2), expectedBunddestillat.getFærdigDato());
        assertNull(expectedBunddestillat.getKommentar());
        assertEquals(0, expectedBunddestillat.getAlkoholprocent());
        assertEquals(0, expectedBunddestillat.getMængdeL());
        assertEquals(Maltbatch.SINGLE_CASK, expectedBunddestillat.getMaltbatch());
    }

    @Test
    void createBunddestillat_SlutdatoFørStartdato() {
        //Act
        Exception exception = assertThrows(RuntimeException.class, () -> controller.createBundDestillat(
                "Bunddestillat 1", LocalDate.of(2025,1,2),
                "Byg", "Tørv", "EH"
        ));

        //Assert
        assertEquals("Slutdato er før startdato", exception.getMessage());
    }

    @Test
    void createFad_createterFadIStorage() {
        //Act & assert
        assertTrue(storage.getFadList().isEmpty());

        Fad expectedFad = controller.createFad("Egetræ", 50, "Spanien");
        assertEquals(1, storage.getFadList().size());

        Fad actualFad = storage.getFadList().getLast();
        assertEquals(expectedFad, actualFad);

        assertEquals(1, actualFad.getFadNr());
        assertTrue(expectedFad.getIndholdshistorik().isEmpty());
        assertNull(expectedFad.getIndhold());
        assertEquals(0, expectedFad.getMængdeL());
        assertEquals(50, expectedFad.getKapacitetL());
        assertEquals("Egetræ", expectedFad.getFadtype());
        assertEquals("Spanien", expectedFad.getOprindelse());
    }

    @Test
    void createFad_tildelerFadNrKorrekt() {
        //Act & assert
        Fad fad1 = controller.createFad("Egetræ", 50, "Spanien");
        assertEquals(1, fad1.getFadNr());

        Fad fad2 = controller.createFad("Egetræ", 10, "Italien");
        assertEquals(2, fad2.getFadNr());

        Fad fad3 = controller.createFad("Egetræ", 100, "Danmark");
        assertEquals(3, fad3.getFadNr());

        controller.removeFad(fad2);
        Fad fad4 = controller.createFad("Egetræ", 25, "Sverige");
        assertEquals(4, fad4.getFadNr());

        controller.removeFad(fad4);
        Fad fad5 = controller.createFad("Egetræ", 5, "Mali");
        assertEquals(4, fad5.getFadNr());
    }

    @Test
    void removeFad_fjernerFadFraStorage() {
        //Arrange
        Fad fad1 = controller.createFad("Egetræ", 50, "Spanien");

        //Act & assert
        assertEquals(1, storage.getFadList().size());

        storage.removeFromFadList(fad1);
        assertTrue(storage.getFadList().isEmpty());
    }

    @Test
    void createLager_createterLagerIStorage() {
        //Act & assert
        assertTrue(storage.getLagerList().isEmpty());

        Lager expectedLager = controller.createLager("Lager1");
        assertEquals(1, storage.getLagerList().size());

        Lager actualLager = storage.getLagerList().getLast();
        assertEquals(expectedLager,actualLager);

        assertEquals("Lager1", actualLager.getLagerNavn());
        assertEquals(0, actualLager.getAntalFade());
        assertEquals(0, actualLager.getAntalFlasker());
        assertEquals(0, actualLager.getReoler().size());
    }

    @Test
    void createReol_createterReolPåLager() {
        //Arrange
        Lager lager = controller.createLager("Lager1");

        //Act
        Reol reol = lager.createReol("A", 5);

        //Assert
        assertTrue(lager.getReoler().contains(reol));
        assertEquals(5, reol.getPladser().length);
        assertEquals("A", reol.getID());
    }
}