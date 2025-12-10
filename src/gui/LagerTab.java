package gui;

import application.controller.Controller;
import application.model.Lager;
import application.model.Plads;
import application.model.Reol;
import application.model.Storable;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.function.UnaryOperator;
import java.util.logging.Filter;
import java.util.regex.Pattern;

public class LagerTab extends Tab {
    private Lager lager;
    private Controller controller = new Controller();
    private ListView<Reol> lvwReoler;
    private ListView<Plads> lvwPladser;
    private Button btncreateLager, btnVælgLager, btncreateReol, btnTømPlads, btnTilføjTilLager;
    private Label lblLagerNavn;

    public LagerTab() {
        super("Lager");
        GridPane pane = new GridPane();
        this.setContent(pane);
        initContent(pane);
    }

    public void initContent(GridPane pane) {
        pane.setGridLinesVisible(false);
        pane.setPadding(new Insets(20));
        pane.setHgap(20);
        pane.setVgap(20);

        Label title = new Label("Lagerstyring");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: 300;");
        VBox titleBox = new VBox(title);
        titleBox.setAlignment(Pos.CENTER);

        lblLagerNavn = new Label("Lager Navn: ");
        VBox nameBox = new VBox(lblLagerNavn);
        nameBox.setAlignment(Pos.CENTER);

        Separator vSeparator = new Separator(Orientation.VERTICAL);
        vSeparator.setPrefHeight(30);

        HBox titleHbox = new HBox(10);
        GridPane.setColumnSpan(titleHbox, 3);
        titleHbox.getChildren().addAll(titleBox, vSeparator, nameBox);
        titleHbox.setAlignment(Pos.CENTER_LEFT);
        pane.add(titleHbox, 0, 0);

        VBox lagerButtonVBox = new VBox();
        lagerButtonVBox.setSpacing(15);
        pane.add(lagerButtonVBox, 0, 1);

        btnVælgLager = new Button("Vælg lager");
        btnVælgLager.setOnAction(e -> vælgLagerAction());

        btncreateLager = new Button("Opret lager");
        btncreateLager.setOnAction(e -> createLagerAction());

        btncreateReol = new Button("Opret reol");
        btncreateReol.setOnAction(e -> createReolAction());
        btncreateReol.setDisable(true);

        btnTømPlads = new Button("Tøm plads");
        btnTømPlads.setOnAction(e -> tømPladsAction());
        btnTømPlads.setDisable(true);

        btnTilføjTilLager = new Button("Tilføj til lager");
        btnTilføjTilLager.setOnAction(e -> tilføjTilLagerAction());
        btnTilføjTilLager.setDisable(true);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        lagerButtonVBox.getChildren().addAll(btnVælgLager, btncreateLager, btncreateReol, spacer, btnTømPlads, btnTilføjTilLager);

        VBox reolerVBox = new VBox();
        reolerVBox.setAlignment(Pos.CENTER);
        reolerVBox.setSpacing(10);
        pane.add(reolerVBox, 1, 1);

        Label lblReoler = new Label("Reoler");
        lblReoler.setStyle("-fx-font-size: 14px; -fx-font-weight: 300;");
        lvwReoler = new ListView<>();
        ChangeListener<Reol> reolListener = (ov, oldReol,
                                             newReol) -> this.reolChanged();
        lvwReoler.getSelectionModel().selectedItemProperty().addListener(reolListener);

        reolerVBox.getChildren().addAll(lblReoler, lvwReoler);

        VBox pladserVBox = new VBox();
        pladserVBox.setAlignment(Pos.CENTER);
        pladserVBox.setSpacing(10);
        pane.add(pladserVBox, 2, 1);

        Label lblPladser = new Label("Pladser");
        lblPladser.setStyle("-fx-font-size: 14px; -fx-font-weight: 300;");
        lvwPladser = new ListView<>();
        lvwPladser.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Plads plads, boolean empty) {
                super.updateItem(plads, empty);
                if (empty || plads == null) {
                    setText(null);
                } else {
                    String str = plads.getPladsNr() + " - ";
                    if (plads.getVare() == null) {
                        setText(str + "Tom plads");
                    } else {
                        setText(str + plads.getVare().toString());
                    }
                }
            }
        });
        pladserVBox.getChildren().addAll(lblPladser, lvwPladser);

    }

    private void tilføjTilLagerAction() {
    }

    private void tømPladsAction() {
    }

    private void createReolAction() {
        //Integer filter
        Pattern validEditingState = Pattern.compile("([0-9]+)?");

        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (validEditingState.matcher(text).matches()) {
                return c;
            } else {
                return null;
            }
        };

        Stage stage = new Stage();
        stage.setTitle("Opret reol");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        TextField txfReolNavn = new TextField();
        txfReolNavn.setPromptText("Indtast navn på reol her");

        TextField txfAntal = new TextField();
        txfAntal.setPromptText("Indtast antal pladser");
        txfAntal.setTextFormatter(new TextFormatter<>(filter));

        Button btnSelect = new Button("Vælg");
        btnSelect.setOnAction(event -> {
            String navn = txfReolNavn.getText().trim();
            String antalPladserText = txfAntal.getText().trim();
            if (!navn.isEmpty() && !antalPladserText.isEmpty()) {
                int antalPladser = Integer.parseInt(antalPladserText);
                Reol reol = controller.createReol(lager, navn, antalPladser);
                lvwReoler.getItems().setAll(lager.getReoler());
                lvwReoler.getSelectionModel().select(reol);
                lvwPladser.getItems().setAll(reol.getPladser());
                lvwPladser.getSelectionModel().clearSelection();
                stage.close();
            }
        });

        Button btnAnnuller = new Button("Annuller");
        btnAnnuller.setOnAction(e -> {
            stage.close();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox buttonHBox = new HBox(btnSelect, spacer, btnAnnuller);

        vbox.getChildren().addAll(txfReolNavn, txfAntal, buttonHBox);

        // Show the window
        Scene scene = new Scene(vbox,300,150);
        stage.setScene(scene);
        stage.initOwner(this.getTabPane().getScene().getWindow()); // make it modal relative to main window
        stage.show();
        txfReolNavn.getParent().requestFocus();
    }

    private void createLagerAction() {
        Stage stage = new Stage();
        stage.setTitle("Opret Lager");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        TextField txfLagerNavn = new TextField();
        txfLagerNavn.setPromptText("Indtast lagernavn");


        Button btnSelect = new Button("Vælg");
        btnSelect.setOnAction(event -> {
            String navn = txfLagerNavn.getText().trim();
            if (!navn.isEmpty()) {
                lager = controller.createLager(navn);
                lblLagerNavn.setText("Lager navn: " + lager.getLagerNavn());
                lvwReoler.getItems().setAll(lager.getReoler());
                lvwReoler.getSelectionModel().clearSelection();
                lvwPladser.getItems().clear();
                btncreateReol.setDisable(false);
                stage.close();
            }
        });

        Button btnAnnuller = new Button("Annuller");
        btnAnnuller.setOnAction(e -> {
            stage.close();
        });
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox buttonHBox = new HBox(btnSelect, spacer, btnAnnuller);

        vbox.getChildren().addAll(txfLagerNavn, buttonHBox);

        // Show the window
        Scene scene = new Scene(vbox, 300, 100);
        stage.setScene(scene);
        stage.initOwner(this.getTabPane().getScene().getWindow()); // make it modal relative to main window
        stage.show();
        txfLagerNavn.getParent().requestFocus();
    }

    private void reolChanged() {
        Reol selectedReol = lvwReoler.getSelectionModel().getSelectedItem();
        if (selectedReol != null) {
            lvwPladser.getItems().setAll(selectedReol.getPladser());
        }
    }

    private void vælgLagerAction() {
        Stage stage = new Stage();
        stage.setTitle("Vælg Lager");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        ListView<Lager> lvwLager = new ListView<>();
        lvwLager.getItems().addAll(controller.getStorage().getLagerList());
        lvwLager.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        Button btnSelect = new Button("Vælg");
        btnSelect.setOnAction(event -> {
            Lager selectedLager = lvwLager.getSelectionModel().getSelectedItem();
            if (selectedLager != null) {
                this.lager = selectedLager;
                lvwReoler.getItems().setAll(lager.getReoler());
                lvwPladser.getItems().clear();
                lvwReoler.getSelectionModel().clearSelection();
                btncreateReol.setDisable(false);
                lblLagerNavn.setText("Lager navn: " + lager.getLagerNavn());
                stage.close();
            }
        });

        vbox.getChildren().addAll(lvwLager, btnSelect);

        // Show the window
        Scene scene = new Scene(vbox, 300, 400);
        stage.setScene(scene);
        stage.initOwner(this.getTabPane().getScene().getWindow()); // make it modal relative to main window
        stage.show();
    }
}