package application.controller;

import storage.Storage;

public class Controller {
    private Storage storage;

    public Controller() {
        this.storage = Storage.getInstance();
    }

    public Storage getStorage() {
        return storage;
    }
}
