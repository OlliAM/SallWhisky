package gui;

import application.controller.Controller;
import application.model.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class main {
    public static void main(String[] args) {
        initStorage();
        MainWindow.launch(MainWindow.class);
    }

    public static void initStorage() {
        Controller controller = new Controller();

        // --- Register base data ---
        controller.createKornsort("Byg");
        controller.createKornsort("Hvede");
        controller.createRygemateriale("Tørv");
        controller.createRygemateriale("Eg");
        controller.createFadtype("Bourbon Barrel");
        controller.createFadtype("Sherry Cask");

        // --- Opret Lager & Reoler ---
        Lager lager1 = controller.createLager("Whisky lager");
        Reol reol1 = controller.createReol(lager1, "A", 10);
        Reol reol2 = controller.createReol(lager1, "B", 10);

        // --- Opret BundDestillater (lagret i 4–5 år for at være gyldige) ---
        BundDestillat bundDestillat1 = controller.createBundDestillat("Bunddestillat1",
                LocalDate.of(2018, 3, 12),
                LocalDate.of(2020, 7, 25),
                "Byg", "Tørv", "MK", 200, 50
        );
        bundDestillat1.setMængdeL(200);
        bundDestillat1.setAlkoholprocent(50);

        BundDestillat bundDestillat2 = controller.createBundDestillat(
                "Bunddestillat2",
                LocalDate.of(2017, 1, 5),
                LocalDate.of(2017, 1, 7),
                "Hvede", "Eg", "MK", 300, 60
        );

        BundDestillat bundDestillat3IkkeFærdig = controller.createBundDestillat("Bunddestillat3IkkeFærdigt",
                LocalDate.of(2023, 1, 5),
                "Hvede", "Eg", "MK");

        // --- Opret Fad ---
        Fad fad1 = controller.createFad("Bourbon Barrel", 200, "Kentucky");
        Fad fad2 = controller.createFad("Sherry Cask", 250, "Spanien");
        Fad fad3 = controller.createFad("Sherry Cask", 250, "Italien");

        // --- Fyld bunddestillat på fade ---
        // Datoen skal være > slutDato på bunddestillat
        Destillat d1 = controller.fyldPåFad(fad1, bundDestillat1, 150,
                LocalDate.of(2020, 8, 1),
                "MK"
        );

        Destillat d2 = controller.fyldPåFad(fad2, bundDestillat2, 180,
                LocalDate.of(2019, 5, 1),
                "MK"
        );


        // --- Læg fade på lager ---
        controller.gemPåReol(lager1, reol1, 0, fad1);
        controller.gemPåReol(lager1, reol1, 1, fad2);
        controller.gemPåReol(lager1, reol1, 2, fad3);
        controller.gemPåReol(lager1, reol1, 3, fad3);
        controller.gemPåReol(lager1, reol2, 3, fad1);
        controller.gemPåReol(lager1, reol2, 4, fad2);

        // --- Skab Færdigprodukt (efter min. 3 år lagring) ---
        Map<Fad, Double> fadeTilProdukt = new HashMap<>();
        fadeTilProdukt.put(fad1, 50.0);  // Træk 50L fra fad 1
        fadeTilProdukt.put(fad2, 60.0);  // Træk 60L fra fad 2

        Færdigprodukt fp1 = controller.createFærdigProdukt(
                "Classic Blend 2024",
                fadeTilProdukt,
                10.0,                       // tilsat vand
                "Aarhus Grundvand",
                1,
                "Blød og røget udgave af vores klassiker.",
                LocalDate.of(2024, 8, 1)
        );

        // --- Hæld på flasker ---
        controller.hældPåFlasker(fp1, 50, 1.0);   // 50 flasker à 1 liter
    }
}

