package gui;

import application.controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CreateFadWindow extends Stage {
    private TextField txfFadStørrelse, txfFadtype, txfOprindelse;
    private String fadtype;
    private Button btnOpretFad, btnAnnuller;
    Controller controller;

    public CreateFadWindow(Stage owner) {
        this.initOwner(owner);
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setMinHeight(100);
        this.setMinWidth(200);
        this.setResizable(false);
        this.setTitle("Opret fad");
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

        Label lblFadtype = new Label("Fadtype");
        txfFadtype = new TextField();
        txfFadtype.setText("Egetræ");
        txfFadtype.setEditable(false);
        txfFadtype.setOnMousePressed(e -> fadtypeAction());

        VBox fadTypeVBox = new VBox(lblFadtype, txfFadtype);
        fadTypeVBox.setSpacing(10);
        pane.add(fadTypeVBox, 0, 0);

        Label lblFadStørrelse = new Label("Fadstørrelse");
        txfFadStørrelse = new TextField();
        txfFadStørrelse.setEditable(false);
        txfFadStørrelse.setOnMousePressed(e -> fadstørrelseAction());

        VBox fadStørrelseVBox = new VBox(lblFadStørrelse, txfFadStørrelse);
        fadStørrelseVBox.setSpacing(10);
        pane.add(fadStørrelseVBox, 0, 1);

        Label lblOprindelse = new Label("Oprindelse");
        txfOprindelse = new TextField();

        VBox oprindelseVBox = new VBox(lblOprindelse, txfOprindelse);
        oprindelseVBox.setSpacing(10);
        pane.add(oprindelseVBox, 0, 2);

        btnOpretFad = new Button("Opret fad");
        btnOpretFad.setOnAction(e -> opretAction());

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

        buttonBox.getChildren().addAll(btnOpretFad, spacer, btnAnnuller);
        pane.add(buttonBox, 0, 3);
    }

    private void opretAction() {
        String fadtype = txfFadtype.getText().trim();

        String fadKapacitetTxt = txfFadStørrelse.getText().trim();
        fadKapacitetTxt = fadKapacitetTxt.substring(0, fadKapacitetTxt.length() - 1);

        String oprindelse = txfOprindelse.getText().trim();

        if(!fadKapacitetTxt.isEmpty() && !fadtype.isEmpty() && !oprindelse.isEmpty()) {
            double fadKapacitet = Double.parseDouble(fadKapacitetTxt);
            controller.createFad(fadtype, fadKapacitet, oprindelse);
            this.close();
        }
    }

    private void fadtypeAction() {
        Stage stage = new Stage();
        stage.setTitle("Vælg fadtype");
        stage.initOwner(this);
        stage.initModality(Modality.WINDOW_MODAL);

        // --- ListView ---
        ListView<String> listView = new ListView<>();
        listView.getItems().addAll(controller.getStorage().getFadtypeList());

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

        final String[] selected = {null};

        // --- Button Actions ---
        btnVælg.setOnAction(e -> {
            fadtype = listView.getSelectionModel().getSelectedItem();
            txfFadtype.setText(fadtype);
            stage.close();
        });

        btnAnnuller.setOnAction(e -> {
            stage.close();
        });

        stage.showAndWait();
    }

    private void fadstørrelseAction() {
            Stage stage = new Stage();
            stage.setTitle("Vælg fadstørrelse");
            stage.initOwner(this);
            stage.initModality(Modality.WINDOW_MODAL);

            // --- ListView ---
            ListView<Double> listView = new ListView<>();
            listView.getItems().addAll(controller.getStorage().getFadKapacitetList());

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

            final String[] selected = {null};

            // --- Button Actions ---
            btnVælg.setOnAction(e -> {
                double fadStørrelse = listView.getSelectionModel().getSelectedItem();
                txfFadStørrelse.setText(fadStørrelse + "L");
                stage.close();
            });

            btnAnnuller.setOnAction(e -> {
                stage.close();
            });

            stage.showAndWait();
    }
}
