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
    void createFad_OpretterFadIStorage() {
        //Act && assert
        assertTrue(storage.getFadList().isEmpty());
        Fad fad = controller.createFad("Egetræ", 50, "Spanien");
    }
}