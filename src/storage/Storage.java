package storage;

import application.model.Destillat;

import java.util.ArrayList;

public class Storage {
    private static Storage storage;
    private ArrayList<Destillat> destillatList;

    private Storage() {
        destillatList = new ArrayList<>();
    }

    public static Storage getInstance() {
        if(storage == null) {
            storage = new Storage();
        }
        return storage;
    }
}
