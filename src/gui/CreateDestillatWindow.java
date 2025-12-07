package gui;

import application.controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDate;

public class CreateDestillatWindow extends Stage {
    private Controller controller;
    private TextField txfNavn, txfKornsort, txfRygemateriale, txfInit, txfMængdeL, txfAlkohoprocent;
    private DatePicker dpStartDato, dpSlutDato;
    private Button btnOpretDestillat, btnAnnuller;

    public CreateDestillatWindow(Stage owner) {
        this.initOwner(owner);
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setMinHeight(100);
        this.setMinWidth(200);
        this.setResizable(false);
        this.setTitle("Opret destillat");
        controller = new Controller();
        GridPane pane = new GridPane();
        this.initContent(pane);
        Scene scene = new Scene(pane);
        this.setScene(scene);
        pane.requestFocus();
    }

    private void initContent(GridPane pane) {
        pane.setPadding(new Insets(20));
        pane.setHgap(20);
        pane.setVgap(20);

        Label lblStartDato = new Label("Startdato");
        dpStartDato = new DatePicker();
        VBox startDatoVBox = new VBox(lblStartDato, dpStartDato);

        Label lblSlutDato = new Label("Slutdato");
        dpSlutDato = new DatePicker();
        VBox slutDatoVBox = new VBox(lblSlutDato, dpSlutDato);

        HBox datoHBox = new HBox(startDatoVBox, slutDatoVBox);
        pane.add(datoHBox, 0, 0);

        Label lblNavn = new Label("Navn");
        txfNavn = new TextField();

        VBox navnVBox = new VBox(lblNavn, txfNavn);
        navnVBox.setSpacing(10);
        pane.add(navnVBox, 0, 1);

        Label lblKornsort = new Label("Kornsort");
        txfKornsort = new TextField();
        txfKornsort.setEditable(false);
        txfKornsort.setOnMousePressed(e -> KornsortAction());

        VBox kornsortVBox = new VBox(lblKornsort, txfKornsort);
        kornsortVBox.setSpacing(10);
        pane.add(kornsortVBox, 0, 2);

        Label lblRygemateriale = new Label("Rygemateriale");
        txfRygemateriale = new TextField();
        txfRygemateriale.setEditable(false);
        txfRygemateriale.setOnMousePressed(e -> RygematerialeAction());

        VBox RygematerialeVBox = new VBox(lblRygemateriale, txfRygemateriale);
        RygematerialeVBox.setSpacing(10);
        pane.add(RygematerialeVBox, 0, 3);


        Label lblMængde = new Label("Mængde (Liter)");
        txfMængdeL = new TextField();

        VBox mængdeVBox = new VBox(lblMængde, txfMængdeL);
        mængdeVBox.setSpacing(10);
        pane.add(mængdeVBox, 0, 4);

        Label lblAlkoholProcent = new Label("Alkoholprocent");
        txfAlkohoprocent = new TextField();

        VBox alkoholProcentVBox = new VBox(lblAlkoholProcent, txfAlkohoprocent);
        alkoholProcentVBox.setSpacing(10);
        pane.add(alkoholProcentVBox, 0, 5);

        Label lblInit = new Label("Initialer");
        txfInit = new TextField();

        VBox initVBox = new VBox(lblInit, txfInit);
        initVBox.setSpacing(10);
        pane.add(initVBox, 0, 6);

        btnOpretDestillat = new Button("Opret destillat");
        btnOpretDestillat.setOnAction(e -> opretAction());

        btnAnnuller = new Button("Annuller");
        btnAnnuller.setOnAction(e -> {
            this.close();
        });

        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(10));
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);

        // Push one button left, the other right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        buttonBox.getChildren().addAll(btnOpretDestillat, spacer, btnAnnuller);
        pane.add(buttonBox, 0, 7);
    }

    private void opretAction() {
        String navn = txfNavn.getText().trim();
        LocalDate startDato = dpStartDato.getValue();
        LocalDate slutDato = dpSlutDato.getValue();
        String kornsort = txfKornsort.getText().trim();
        String rygemateriale = txfRygemateriale.getText().trim();
        String init = txfInit.getText().trim();
        String mængdeTxt = txfMængdeL.getText().trim();
        String alkoholprocentTxt = txfAlkohoprocent.getText().trim();


        if (!navn.isEmpty() && !init.isEmpty() && !kornsort.isEmpty() && !rygemateriale.isEmpty()) {
            //Færdig destillering
            if(slutDato != null && !alkoholprocentTxt.isEmpty() && !mængdeTxt.isEmpty()) {
                double mængde = Double.parseDouble(mængdeTxt);
                double alkoholprocent = Double.parseDouble(alkoholprocentTxt);
                controller.createBundDestillat(navn, startDato, slutDato, kornsort, rygemateriale, init, mængde, alkoholprocent);
            }
            //Igangværende destillering
            else {
                controller.createBundDestillat(navn, startDato, kornsort, rygemateriale, init);
            }
            this.close();
        }
    }

    private void KornsortAction() {
        Stage stage = new Stage();
        stage.setTitle("Vælg kornsort");
        stage.initOwner(this);
        stage.initModality(Modality.WINDOW_MODAL);

        // --- ListView ---
        ListView<String> listView = new ListView<>();
        listView.getItems().addAll(controller.getStorage().getKornsortList());

        // --- Buttons ---
        Button btnVælg = new Button("Vælg");
        Button btnAnnuller = new Button("Annuller");

        // Button bar (HBox with space between)
        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(10));
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);

        // Push one button left, the other right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        buttonBox.getChildren().addAll(btnVælg, spacer, btnAnnuller);

        // --- Layout ---
        VBox layout = new VBox(10, listView, buttonBox);
        layout.setPadding(new Insets(15));

        Scene scene = new Scene(layout, 350, 400);
        stage.setScene(scene);

        // --- Button Actions ---
        btnVælg.setOnAction(e -> {
            String kornsort = listView.getSelectionModel().getSelectedItem();
            txfKornsort.setText(kornsort);
            stage.close();
        });

        btnAnnuller.setOnAction(e -> {
            stage.close();
        });

        stage.showAndWait();
    }

    private void RygematerialeAction() {
        Stage stage = new Stage();
        stage.setTitle("Vælg Rygemateriale");
        stage.initOwner(this);
        stage.initModality(Modality.WINDOW_MODAL);

        // --- ListView ---
        ListView<String> listView = new ListView<>();
        listView.getItems().addAll(controller.getStorage().getRygematerialeList());

        // --- Buttons ---
        Button btnVælg = new Button("Vælg");
        Button btnAnnuller = new Button("Annuller");

        // Button bar (HBox with space between)
        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(10));
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);

        // Push one button left, the other right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        buttonBox.getChildren().addAll(btnVælg, spacer, btnAnnuller);

        // --- Layout ---
        VBox layout = new VBox(10, listView, buttonBox);
        layout.setPadding(new Insets(15));

        Scene scene = new Scene(layout, 350, 400);
        stage.setScene(scene);

        // --- Button Actions ---
        btnVælg.setOnAction(e -> {
            String rygemateriale = listView.getSelectionModel().getSelectedItem();
            txfRygemateriale.setText(rygemateriale);
            stage.close();
        });

        btnAnnuller.setOnAction(e -> {
            stage.close();
        });

        stage.showAndWait();
    }
}
