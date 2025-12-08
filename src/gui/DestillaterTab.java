package gui;

import application.controller.Controller;
import application.model.BundDestillat;
import application.model.Destillat;
import application.model.Fad;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DestillaterTab extends Tab {
    private Destillat destillat;
    private Controller controller = new Controller();

    private ListView<Destillat> lvwDestillater;
    private ListView<String> lvwInformation;
    private Button btnCreateDestillat, btnÆndrDestillat, btnPåfyldFad, btnSkiftView;
    private TextArea txaKommentar;
    private boolean færdigeDestillater;

    public DestillaterTab() {
        super("Destillater");

        GridPane gridPane = new GridPane();
        this.setContent(gridPane);
        initContent(gridPane);
        færdigeDestillater = true;
    }

    public void initContent(GridPane pane) {
        pane.setGridLinesVisible(false);
        pane.setPadding(new Insets(20));
        pane.setHgap(20);
        pane.setVgap(20);

        Label title = new Label("Destillater");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: 300;");
        VBox titleBox = new VBox(title);
        titleBox.setAlignment(Pos.CENTER);

        Label lblDestillat = new Label("Destillater");
        VBox nameBox = new VBox(lblDestillat);
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
        pane.add(lagerButtonVBox, 0, 1, 1, 2);

        btnCreateDestillat = new Button("Opret destillat");
        btnCreateDestillat.setOnAction(e -> createDestillat());

        btnÆndrDestillat = new Button("Ændr destillat");
        btnÆndrDestillat.setOnAction(e -> ændrDestillatAction());
        btnÆndrDestillat.setDisable(true);

        btnSkiftView = new Button("Se igangværende destilleringer");
        btnSkiftView.setOnAction(e -> skiftViewAction());
        btnSkiftView.setWrapText(true);
        btnSkiftView.setMaxWidth(120);

        btnPåfyldFad = new Button("Påfyld fad");
        btnPåfyldFad.setOnAction(e -> påfyldFadAction());
        btnPåfyldFad.setDisable(true);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        lagerButtonVBox.getChildren().addAll(btnCreateDestillat, btnÆndrDestillat, btnSkiftView, spacer, btnPåfyldFad);

        VBox destillaterVBox = new VBox();
        destillaterVBox.setSpacing(10);
        pane.add(destillaterVBox, 1, 1, 1, 2);

        Label lblDestillater = new Label("Destillater");
        lblDestillater.setStyle("-fx-font-size: 14px; -fx-font-weight: 300;");
        lvwDestillater = new ListView<>();
        lvwDestillater.setPrefHeight(400);
        ChangeListener<Destillat> destillaterListener = (ov, oldDestillat,
                                                         newDestillat) -> this.destillatChanged();
        lvwDestillater.getSelectionModel().selectedItemProperty().addListener(destillaterListener);

        destillaterVBox.getChildren().addAll(lblDestillater, lvwDestillater);

        VBox informationVBox = new VBox();
        informationVBox.setSpacing(10);
        pane.add(informationVBox, 2, 1);

        Label lblInformation = new Label("Information");
        lblInformation.setStyle("-fx-font-size: 14px; -fx-font-weight: 300;");
        lvwInformation = new ListView<>();
        lvwInformation.setPrefHeight(250);
        lvwInformation.setPrefWidth(350);

        informationVBox.getChildren().addAll(lblInformation, lvwInformation);

        Label lblKommentar = new Label("Kommentar");
        lblKommentar.setStyle("-fx-font-size: 14px; -fx-font-weight: 300;");
        txaKommentar = new TextArea();
        txaKommentar.setEditable(false);
        txaKommentar.setPrefHeight(150);
        txaKommentar.setPrefWidth(350);

        VBox kommentarVBox = new VBox();
        kommentarVBox.setSpacing(10);
        pane.add(kommentarVBox, 2, 2);

        kommentarVBox.getChildren().addAll(lblKommentar, txaKommentar);

        // ---- GridPane sizing ----

        RowConstraints row0 = new RowConstraints();
        row0.setVgrow(Priority.NEVER);

        RowConstraints row1 = new RowConstraints();
        row1.setVgrow(Priority.ALWAYS);


        RowConstraints row2 = new RowConstraints();
        row2.setVgrow(Priority.ALWAYS);

        pane.getRowConstraints().addAll(row0, row1, row2);

        GridPane.setVgrow(destillaterVBox, Priority.ALWAYS);
        GridPane.setVgrow(informationVBox, Priority.ALWAYS);
        GridPane.setVgrow(kommentarVBox, Priority.ALWAYS);

        VBox.setVgrow(lvwDestillater, Priority.ALWAYS);
        VBox.setVgrow(lvwInformation, Priority.ALWAYS);
        VBox.setVgrow(txaKommentar, Priority.ALWAYS);

        lvwDestillater.getItems().setAll(controller.getStorage().getDestillatList());
    }

    private void skiftViewAction() {
        if(færdigeDestillater) {
            færdigeDestillater = false;
            lvwDestillater.getItems().setAll(controller.getStorage().getDestilleringList());
            lvwDestillater.getSelectionModel().clearSelection();
            btnSkiftView.setText("Se færdige destillater");
        }
        else {
            færdigeDestillater = true;
            lvwDestillater.getItems().setAll(controller.getStorage().getDestillatList());
            lvwDestillater.getSelectionModel().clearSelection();
            btnSkiftView.setText("Se igangværende destilleringer");
        }
    }


    private void destillatChanged() {
        btnPåfyldFad.setDisable(true);
        Destillat selectedDestillat = lvwDestillater.getSelectionModel().getSelectedItem();
        if (selectedDestillat != null) {
            btnÆndrDestillat.setDisable(false);
            String startDato = "";
            String færdigDato = "";
            if(færdigeDestillater) {
                færdigDato = selectedDestillat.getFærdigDato().toString();
            }
            String maltBatch = selectedDestillat.getMaltbatch().toString();
            String init = selectedDestillat.getInit();
            String alkoholprocent = selectedDestillat.getAlkoholprocent() + "%";
            String kornSort = "";
            String mængde = "";

            if (selectedDestillat instanceof BundDestillat) {
                startDato = ((BundDestillat) selectedDestillat).getStartDato().toString();
                kornSort = ((BundDestillat) selectedDestillat).getKornsort();
                mængde = ((BundDestillat) selectedDestillat).getMængdeL() + "L";
                if(færdigeDestillater) {
                    btnPåfyldFad.setDisable(false);
                }
            }

            lvwInformation.getItems().setAll(
                    "Startdato: " + startDato,
                    "Færdigdato: " + færdigDato,
                    "Maltbatch: " + maltBatch,
                    "Initialer: " + init,
                    "Alkoholprocent: " + alkoholprocent,
                    "Kornsort: " + kornSort,
                    "Mængde: " + mængde);

                txaKommentar.setText(selectedDestillat.getKommentar());
        }
        else {
            btnÆndrDestillat.setDisable(true);
            updateListViews();
        }
    }

    private void påfyldFadAction() {
    }

    private void ændrDestillatAction() {
    }

    private void createDestillat() {
        Stage owner = (Stage) this.getTabPane().getScene().getWindow();
        CreateDestillatWindow opretDestillat = new CreateDestillatWindow(owner);
        opretDestillat.showAndWait();
        updateListViews();
    }

    private void updateListViews() {
        if(færdigeDestillater) {
            lvwDestillater.getItems().setAll(controller.getStorage().getDestillatList());
        }
        else {
            lvwDestillater.getItems().setAll(controller.getStorage().getDestilleringList());
        }
        lvwDestillater.getSelectionModel().clearSelection();
        lvwInformation.getItems().clear();
        txaKommentar.clear();
    }

}
