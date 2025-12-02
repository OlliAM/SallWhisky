package application.controller;

import application.model.Fad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storage.Storage;

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
    void createBundDestillat() {

    }

    @Test
    void createFad_OpretterFadIStorage() {
        //Act && assert
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
}