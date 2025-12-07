package gui;

import application.model.Fad;
import javafx.beans.value.ChangeListener;
import javafx.geometry.*;
import javafx.scene.control.*;
import application.controller.Controller;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Optional;

public class FadeTab extends Tab {
    private Fad fad;
    private Controller controller = new Controller();

    private ListView<Fad> lvwFadeIndhold, lvwFadeTomme;
    private Button btncreateFad, btnPåfyldFad, btnTapFad, btnHistorik, btnTilføjTilLager, btnSletFad;
    private Label lblFadeNavn;

    public FadeTab() {
        super("Fade");

        GridPane gridPane = new GridPane();
        this.setContent(gridPane);
        initContent(gridPane);
    }

    public void initContent(GridPane pane) {
        pane.setGridLinesVisible(false);
        pane.setPadding(new Insets(20));
        pane.setHgap(20);
        pane.setVgap(20);

        Label title = new Label("Fade");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: 300;");
        VBox titleBox = new VBox(title);
        titleBox.setAlignment(Pos.CENTER);

        lblFadeNavn = new Label("Fade navn: ");
        VBox nameBox = new VBox(lblFadeNavn);
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

        btncreateFad = new Button("Opret fad");
        btncreateFad.setOnAction(e -> createFadAction());

        btnPåfyldFad = new Button("Påfyld fad");
        btnPåfyldFad.setOnAction(e -> påfyldFadAction());
        btnPåfyldFad.setDisable(true);

        btnTapFad = new Button("Tap fad");
        btnTapFad.setOnAction(e -> tapFadAction());
        btnTapFad.setDisable(true);

        btnHistorik = new Button("Historik");
        btnHistorik.setOnAction(e -> seHistorikAction());
        btnHistorik.setDisable(true);

        btnTilføjTilLager = new Button("Tilføj til lager");
        btnTilføjTilLager.setOnAction(e -> tilføjTilLagerAction());
        btnTilføjTilLager.setDisable(true);

        btnSletFad = new Button("Slet fad");
        btnSletFad.setOnAction(e -> sletFadAction());
        btnSletFad.setDisable(true);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        lagerButtonVBox.getChildren().addAll(btncreateFad, btnPåfyldFad, btnTapFad, btnHistorik, spacer,
                btnTilføjTilLager, btnSletFad);

        VBox fyldteFadeVBox = new VBox();
        fyldteFadeVBox.setAlignment(Pos.CENTER);
        fyldteFadeVBox.setSpacing(10);
        pane.add(fyldteFadeVBox, 1, 1);

        Label lblFyldteFade = new Label("Fade med indhold");
        lblFyldteFade.setStyle("-fx-font-size: 14px; -fx-font-weight: 300;");
        lvwFadeIndhold = new ListView<>();
        ChangeListener<Fad> fyldtFadListener = (ov, oldFad,
                                                newFad) -> this.fadeIndholdChanged(newFad);
        lvwFadeIndhold.getSelectionModel().selectedItemProperty().addListener(fyldtFadListener);

        fyldteFadeVBox.getChildren().addAll(lblFyldteFade, lvwFadeIndhold);

        VBox tommeFadeVBox = new VBox();
        tommeFadeVBox.setAlignment(Pos.CENTER);
        tommeFadeVBox.setSpacing(10);
        pane.add(tommeFadeVBox, 2, 1);

        Label lblTommeFade = new Label("Tomme fade");
        lblTommeFade.setStyle("-fx-font-size: 14px; -fx-font-weight: 300;");
        lvwFadeTomme = new ListView<>();
        ChangeListener<Fad> tomFadListener = (ov, oldFad,
                                              newFad) -> this.fadeTomChanged(newFad);
        lvwFadeTomme.getSelectionModel().selectedItemProperty().addListener(tomFadListener);

        tommeFadeVBox.getChildren().addAll(lblTommeFade, lvwFadeTomme);

        updateListViews();
    }

    private void fadeTomChanged(Fad selectedFad) {
        if (selectedFad != null) {
            lvwFadeIndhold.getSelectionModel().clearSelection();
            fad = selectedFad;
            btnSletFad.setDisable(false);
            btnHistorik.setDisable(false);
            btnPåfyldFad.setDisable(false);
        } else {
            fad = null;
            btnSletFad.setDisable(true);
            btnHistorik.setDisable(true);
            btnPåfyldFad.setDisable(true);
            btnTapFad.setDisable(true);
        }
    }

    private void fadeIndholdChanged(Fad selectedFad) {
        if (selectedFad != null) {
            lvwFadeTomme.getSelectionModel().clearSelection();
            fad = selectedFad;
            btnSletFad.setDisable(false);
            btnHistorik.setDisable(false);
            btnPåfyldFad.setDisable(false);
            btnTapFad.setDisable(false);
        } else {
            fad = null;
            btnSletFad.setDisable(true);
            btnHistorik.setDisable(true);
            btnPåfyldFad.setDisable(true);
            btnTapFad.setDisable(true);
        }
    }

    private void updateListViews() {
        lvwFadeTomme.getItems().clear();
        lvwFadeIndhold.getItems().clear();

        for (Fad fad : controller.getStorage().getFadList()) {
            if (fad.getFadIndhold() == null) {
                lvwFadeTomme.getItems().add(fad);
            } else {
                lvwFadeIndhold.getItems().add(fad);
            }
        }
    }

    private void createFadAction() {
        Stage owner = (Stage) this.getTabPane().getScene().getWindow();
        CreateFadWindow opretFad = new CreateFadWindow(owner);
        opretFad.showAndWait();
        updateListViews();
    }

    private void sletFadAction() {
        Fad selectedFad = fad;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bekræft sletning");
        alert.setHeaderText("Er du sikker på, at du vil slette dette fad?");
        alert.setContentText(
                "Fadnummer: " + selectedFad.getFadNr() + "\n" +
                        "Fadtype: " + selectedFad.getFadtype() + "\n" +
                        "Oprindelse: " + selectedFad.getOprindelse() + "\n\n" +
                        "Denne handling kan ikke fortrydes."
        );

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            //Fjerne fra storage
            controller.getStorage().removeFromFadList(fad);

            //Fjerne fra gui
            updateListViews();

            //Success dialog
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setHeaderText(null);
            info.setTitle("Fad slettet");
            info.setContentText("Fad " + selectedFad.getFadNr() + " blev slettet.");
            info.showAndWait();
        }
    }

    private void seHistorikAction() {
    }

    private void tapFadAction() {

    }

    private void påfyldFadAction() {
    }

    private void tilføjTilLagerAction() {

    }
}
