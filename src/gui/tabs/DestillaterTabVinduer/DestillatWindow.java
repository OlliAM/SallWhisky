package gui.tabs.DestillaterTabVinduer;

import application.controller.Controller;
import application.model.BundDestillat;
import application.model.Destillat;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDate;

public class DestillatWindow extends Stage {
    private BundDestillat destillat;
    private Controller controller;
    private TextField txfNavn, txfKornsort, txfRygemateriale, txfInit, txfMængdeL, txfAlkohoprocent, txfKommentar,
            txfValidation;
    private DatePicker dpStartDato, dpSlutDato;
    private Button btnOpretDestillat, btnAnnuller;
    private boolean færdig;

    public DestillatWindow(Stage owner) {
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

    public DestillatWindow(Stage owner, BundDestillat destillat) {
        this(owner);
        this.destillat = destillat;
        dpStartDato.setValue(destillat.getStartDato());
        dpSlutDato.setValue(destillat.getFærdigDato());
        txfNavn.setText(destillat.getNavn());
        txfKornsort.setText(destillat.getKornsort());
        txfRygemateriale.setText(destillat.getRygemateriale());
        txfMængdeL.setText(destillat.getMængdeL() + "");
        txfAlkohoprocent.setText(destillat.getAlkoholprocent() + "");
        txfInit.setText(destillat.getInit());
        txfKommentar.setText(destillat.getKommentar());
        btnOpretDestillat.setText("Ændr destillat");
        færdig = dpSlutDato != null;
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

        Label lblKommentar = new Label("Kommentar");
        txfKommentar = new TextField();

        VBox kommentarVBox = new VBox(lblKommentar, txfKommentar);
        kommentarVBox.setSpacing(10);
        pane.add(kommentarVBox, 0, 7);

        btnOpretDestillat = new Button("Opret destillat");
        btnOpretDestillat.setOnAction(e -> opretAction());

        btnAnnuller = new Button("Annuller");
        btnAnnuller.setOnAction(e -> {
            destillat = null;
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
        pane.add(buttonBox, 0, 8);

        txfValidation = new TextField();
        txfValidation.setEditable(false);
        txfValidation.setVisible(false);
        txfValidation.setStyle(
                "-fx-background-color: transparent;" +  // no background
                        "-fx-border-color: transparent;" +      // no border
                        "-fx-text-fill: red;" +                 // red text
                        "-fx-font-weight: bold;"                // bold text
        );
        pane.add(txfValidation, 0, 9);
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
        String kommentar = txfKommentar.getText().trim();

        // --- VALIDATION ---
        if (startDato == null) {
            showValidation("Ingen startdato valgt");
            return;
        }

        if (navn.isEmpty()) {
            showValidation("Intet navn valgt");
            return;
        }

        if (kornsort.isEmpty()) {
            showValidation("Ingen kornsort valgt");
            return;
        }

        if (init.isEmpty()) {
            showValidation("Ingen initialer valgt");
            return;
        }

        if (slutDato != null) {
            if (slutDato.isBefore(startDato)) {
                showValidation("Slutdato er før startdato");
                return;
            }
            if (mængdeTxt.isEmpty()) {
                showValidation("Ingen mængde valgt");
                return;
            }
            if (alkoholprocentTxt.isEmpty()) {
                showValidation("Ingen alkoholprocent valgt");
                return;
            }
        }

        // --- CREATE OR UPDATE DESTILLAT ---
        if (destillat == null) {
            BundDestillat oprettetDestillat;
            if (slutDato != null) {
                double mængde = Double.parseDouble(mængdeTxt);
                double alkoholprocent = Double.parseDouble(alkoholprocentTxt);
                oprettetDestillat = controller.createBundDestillat(
                        navn, startDato, slutDato, kornsort, rygemateriale, init, mængde, alkoholprocent);
            } else {
                oprettetDestillat = controller.createBundDestillat(
                        navn, startDato, kornsort, rygemateriale, init);
            }
            oprettetDestillat.setKommentar(kommentar);
            destillat = oprettetDestillat;
        } else {
            destillat.setNavn(navn);
            destillat.setStartDato(startDato);
            destillat.setInit(init);
            destillat.setKornsort(kornsort);
            destillat.setKommentar(kommentar);

            if (slutDato != null && !mængdeTxt.isEmpty() && !alkoholprocentTxt.isEmpty()) {
                double mængde = Double.parseDouble(mængdeTxt);
                double alkoholprocent = Double.parseDouble(alkoholprocentTxt);
                destillat.setFærdigDato(slutDato);
                destillat.setMængdeL(mængde);
                destillat.setAlkoholprocent(alkoholprocent);

                if (!controller.getStorage().getDestillatList().contains(destillat)) {
                    controller.getStorage().addToDestillatList(destillat);
                    controller.getStorage().removeFromDestilleringList(destillat);
                }
            } else {
                if (!controller.getStorage().getDestilleringList().contains(destillat)) {
                    controller.getStorage().addToDestilleringList(destillat);
                    controller.getStorage().removeFromDestillatList(destillat);
                }
            }
        }

        // Close the window if everything is valid
        this.close();
    }

    private void KornsortAction() {
        Stage stage = new Stage();
        stage.setTitle("Vælg kornsort");
        stage.initOwner(this);
        stage.initModality(Modality.WINDOW_MODAL);

        // --- ListView ---
        ListView<String> listView = new ListView<>();
        listView.getItems().addAll(controller.getStorage().getKornsortList());

        // --- Top Buttons ---
        Button btnOpret = new Button("Opret kornsort");
        Button btnFjern = new Button("Fjern kornsort");
        btnFjern.setDisable(true); // disabled until selection

        // Listener to enable Fjern button
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            btnFjern.setDisable(newVal == null);
        });

        HBox topButtons = new HBox(10, btnOpret, btnFjern);
        topButtons.setPadding(new Insets(5, 5, 10, 5));
        topButtons.setAlignment(Pos.CENTER);

        // --- Bottom Buttons (Choose / Cancel) ---
        Button btnVælg = new Button("Vælg");
        Button btnAnnuller = new Button("Annuller");

        HBox bottomButtons = new HBox();
        bottomButtons.setPadding(new Insets(10));
        bottomButtons.setSpacing(10);
        bottomButtons.setAlignment(Pos.CENTER);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        bottomButtons.getChildren().addAll(btnVælg, spacer, btnAnnuller);

        // --- Layout ---
        VBox layout = new VBox(10, topButtons, listView, bottomButtons);
        layout.setPadding(new Insets(15));

        Scene scene = new Scene(layout, 350, 430);
        stage.setScene(scene);

        // --- Button Actions ---
        btnVælg.setOnAction(e -> {
            String kornsort = listView.getSelectionModel().getSelectedItem();
            if (kornsort != null) {
                txfKornsort.setText(kornsort);
            }
            stage.close();
        });

        btnAnnuller.setOnAction(e -> stage.close());

        btnOpret.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Ny kornsort");
            dialog.setHeaderText("Opret en ny kornsort");
            dialog.setContentText("Navn:");

            dialog.showAndWait().ifPresent(navn -> {
                if (!navn.trim().isEmpty()) {
                    controller.createKornsort(navn.trim());
                    listView.getItems().setAll(controller.getStorage().getKornsortList());
                }
            });
        });

        btnFjern.setOnAction(e -> {
            String valgt = listView.getSelectionModel().getSelectedItem();
            if (valgt != null) {
                controller.getStorage().removeFromKornsortList(valgt);
                listView.getItems().setAll(controller.getStorage().getKornsortList());
            }
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

        // --- Top Buttons ---
        Button btnOpret = new Button("Opret Rygemateriale");
        Button btnFjern = new Button("Fjern Rygemateriale");
        btnFjern.setDisable(true); // Disabled until selection

        // Listener to enable Fjern button
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            btnFjern.setDisable(newVal == null);
        });

        HBox topButtons = new HBox(10, btnOpret, btnFjern);
        topButtons.setPadding(new Insets(5, 5, 10, 5));
        topButtons.setAlignment(Pos.CENTER);

        // --- Bottom Buttons (Vælg / Annuller) ---
        Button btnVælg = new Button("Vælg");
        Button btnAnnuller = new Button("Annuller");

        HBox bottomButtons = new HBox();
        bottomButtons.setPadding(new Insets(10));
        bottomButtons.setSpacing(10);
        bottomButtons.setAlignment(Pos.CENTER);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        bottomButtons.getChildren().addAll(btnVælg, spacer, btnAnnuller);

        // --- Layout ---
        VBox layout = new VBox(10, topButtons, listView, bottomButtons);
        layout.setPadding(new Insets(15));

        Scene scene = new Scene(layout, 350, 430);
        stage.setScene(scene);

        // --- Button Actions ---
        btnVælg.setOnAction(e -> {
            String rygemateriale = listView.getSelectionModel().getSelectedItem();
            if (rygemateriale != null) {
                txfRygemateriale.setText(rygemateriale);
            }
            stage.close();
        });

        btnAnnuller.setOnAction(e -> stage.close());

        btnOpret.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Nyt Rygemateriale");
            dialog.setHeaderText("Opret nyt Rygemateriale");
            dialog.setContentText("Navn:");

            dialog.showAndWait().ifPresent(navn -> {
                if (!navn.trim().isEmpty()) {
                    controller.createRygemateriale(navn.trim());
                    listView.getItems().setAll(controller.getStorage().getRygematerialeList());
                }
            });
        });

        btnFjern.setOnAction(e -> {
            String valgt = listView.getSelectionModel().getSelectedItem();
            if (valgt != null) {
                controller.getStorage().removeFromRygematerialeList(valgt);
                listView.getItems().setAll(controller.getStorage().getRygematerialeList());
            }
        });

        stage.showAndWait();
    }

    private void showValidation(String message) {
        txfValidation.setText(message);
        txfValidation.setVisible(true);
    }


    public BundDestillat getDestillat() {
        return destillat;
    }
}
