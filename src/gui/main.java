package gui;

import application.controller.Controller;
import application.model.Lager;

public class main {
    public static void main(String[] args) {
        initStorage();
        MainWindow.launch(MainWindow.class);
    }

    public static void initStorage() {
        Controller controller = new Controller();
        Lager lager = controller.createLager("Whisky lager");
    }
}
