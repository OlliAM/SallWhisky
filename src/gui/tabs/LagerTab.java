package gui.tabs;

import application.controller.Controller;
import application.model.*;
import gui.tabs.LagerTabVinduer.LagerWindow;
import gui.tabs.LagerTabVinduer.ReolWindow;
import gui.tabs.LagerTabVinduer.TilføjTilLagerWindow;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class LagerTab extends Tab {
    private Lager lager;
    private Reol reol;
    private Plads plads;
    private Controller controller = new Controller();
    private ListView<Reol> lvwReoler;
    private ListView<Plads> lvwPladser;
    private Button btncreateLager, btnVælgLager, btncreateReol, btnTømPlads, btnTilføjTilLager, btnRetLager;
    private Button btnSletlager, btnRetReol, btnSletReol;
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

        btnRetLager = new Button("Ret lager");
        btnRetLager.setOnAction(e -> changeLagerAction());

        btnSletlager = new Button("Slet lager");
        btnSletlager.setOnAction(e -> deleteLagerAction());

        btncreateReol = new Button("Opret reol");
        btncreateReol.setOnAction(e -> createReolAction());

        btnRetReol = new Button("Ret reol");
        btnRetReol.setOnAction(e -> changeReolAction());
        btnRetReol.setDisable(true);

        btnSletReol = new Button("Slet reol");
        btnSletReol.setOnAction(e -> deleteReolAction());
        btnSletReol.setDisable(true);

        btnTømPlads = new Button("Tøm plads");
        btnTømPlads.setOnAction(e -> tømPladsAction());
        btnTømPlads.setDisable(true);

        btnTilføjTilLager = new Button("Tilføj til lager");
        btnTilføjTilLager.setOnAction(e -> tilføjTilLagerAction());
        btnTilføjTilLager.setDisable(true);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        lagerButtonVBox.getChildren().addAll(btnVælgLager, btncreateLager, btnRetLager, btnSletlager, btncreateReol,
                btnRetReol, btnSletReol, spacer, btnTømPlads, btnTilføjTilLager);

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
        ChangeListener<Plads> pladsListener = (ov, oldPlads,
                                               newPlads) -> this.pladsChanged();
        lvwPladser.getSelectionModel().selectedItemProperty().addListener(pladsListener);

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
        this.lager = controller.getStorage().getLagerList().getFirst();
        lvwReoler.getItems().setAll(lager.getReoler());
        lvwPladser.getItems().clear();
        lvwReoler.getSelectionModel().clearSelection();
        lblLagerNavn.setText("Lager navn: " + lager.getLagerNavn());
    }

    private void pladsChanged() {
        Plads selectedPlads = lvwPladser.getSelectionModel().getSelectedItem();
        if (selectedPlads != null) {
            plads = selectedPlads;
            if (selectedPlads.getVare() != null) {
                btnTømPlads.setDisable(false);
                btnTilføjTilLager.setDisable(true);
            } else {
                btnTømPlads.setDisable(true);
                btnTilføjTilLager.setDisable(false);
            }
        } else {
            plads = null;
            btnTømPlads.setDisable(true);
            btnTilføjTilLager.setDisable(true);
        }
    }

    private void deleteReolAction() {
        Reol selectedReol = reol;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bekræft sletning");
        alert.setHeaderText("Er du sikker på, at du vil slette denne reol?");
        alert.setContentText(
                "ID: " + reol.getID() + "\n" +
                        "Lager: " + lager.getLagerNavn() + "\n" +
                        "Antal produkter på lager: " + (reol.getPladser().length - reol.getFriePladser()) + "\n\n" +
                        "Denne handling kan ikke fortrydes."
        );

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            //Fjerne fra storage
            controller.removeReol(selectedReol);

            //Fjerne fra gui
            lvwReoler.getItems().setAll(lager.getReoler());
            lvwReoler.getSelectionModel().clearSelection();

            //Success dialog
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setHeaderText(null);
            info.setTitle("Reol slettet");
            info.setContentText("Reol " + selectedReol.getID() + " blev slettet.");
            info.showAndWait();
        }
    }

    private void changeReolAction() {
        Stage owner = (Stage) this.getTabPane().getScene().getWindow();
        ReolWindow reolWindow = new ReolWindow(owner, lager, reol);
        reolWindow.showAndWait();
        Reol selectedReol = reolWindow.getReol();
        if(selectedReol != null) {
            lvwReoler.getItems().setAll(lager.getReoler());
            lvwReoler.getSelectionModel().select(selectedReol);
            lvwPladser.getItems().setAll(selectedReol.getPladser());
            lvwPladser.getSelectionModel().clearSelection();
            pladsChanged();
            reol = selectedReol;
        }
    }

    private void deleteLagerAction() {
        Lager selectedLager = lager;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bekræft sletning");
        alert.setHeaderText("Er du sikker på, at du vil slette dette lager?");
        alert.setContentText(
                "navn: " + lager.getLagerNavn() + "\n" +
                        "Antal produkter på lager: " + (lager.getAntalFade() + lager.getAntalFlasker()) + "\n\n" +
                        "Denne handling kan ikke fortrydes."
        );

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            //Fjerne fra storage
            controller.removeLager(selectedLager);

            //Fjerne fra gui
            lvwReoler.getItems().clear();;
            lvwReoler.getSelectionModel().clearSelection();

            //Success dialog
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setHeaderText(null);
            info.setTitle("Lager slettet");
            info.setContentText("Lager " + selectedLager + " blev slettet.");
            info.showAndWait();
        }
    }

    private void changeLagerAction() {
        Stage owner = (Stage) this.getTabPane().getScene().getWindow();
        LagerWindow lagerWindow = new LagerWindow(owner, lager);
        lagerWindow.showAndWait();
        Lager selectedLager = lagerWindow.getLager();

        if (selectedLager != null) {
            opdaterLager(selectedLager);
        }
    }

    private void opdaterLager(Lager selectedLager) {
        lager = selectedLager;
        Reol selectedReol = reol;
        Plads selectedPlads = plads;
        lblLagerNavn.setText("Lager navn: " + selectedLager.getLagerNavn());
        lvwReoler.getItems().setAll(lager.getReoler());
        lvwReoler.getSelectionModel().clearSelection();
        lvwPladser.getItems().clear();
        btncreateReol.setDisable(false);
        lvwReoler.getSelectionModel().select(selectedReol);
        lvwPladser.getSelectionModel().select(selectedPlads);
    }

    private void tilføjTilLagerAction() {
        Plads currentPlads = plads;
        Stage owner = (Stage) this.getTabPane().getScene().getWindow();
        TilføjTilLagerWindow tilføjTilLagerWindow = new TilføjTilLagerWindow(owner);
        tilføjTilLagerWindow.showAndWait();
        Storable vare = tilføjTilLagerWindow.getVare();
        if (vare != null) {
            controller.gemPåPlads(currentPlads, vare);
            lvwPladser.getItems().setAll(reol.getPladser());
            lvwPladser.getSelectionModel().select(currentPlads);
        }
    }

    private void tømPladsAction() {
        Plads selectedPlads = plads;
        Storable vare = plads.getVare();
        if (vare != null) {
            controller.tømPlads(plads);
            lvwPladser.getItems().setAll(reol.getPladser());
            lvwPladser.getSelectionModel().select(selectedPlads);
        }
    }

    private void createReolAction() {
        Stage owner = (Stage) this.getTabPane().getScene().getWindow();
        ReolWindow reolWindow = new ReolWindow(owner, lager);
        reolWindow.showAndWait();
        Reol selectedReol = reolWindow.getReol();
        if(selectedReol != null) {
            reol = selectedReol;
            lvwReoler.getItems().setAll(lager.getReoler());
            lvwReoler.getSelectionModel().select(reol);
            lvwPladser.getItems().setAll(reol.getPladser());
            lvwPladser.getSelectionModel().clearSelection();
            pladsChanged();
        }
    }

    private void createLagerAction() {
        Stage owner = (Stage) this.getTabPane().getScene().getWindow();
        LagerWindow lagerWindow = new LagerWindow(owner);
        lagerWindow.showAndWait();
        Lager selectedLager = lagerWindow.getLager();

        if (selectedLager != null) {
            opdaterLager(selectedLager);
            System.out.println("her");
        }

    }

    private void reolChanged() {
        Reol selectedReol = lvwReoler.getSelectionModel().getSelectedItem();
        if (selectedReol != null) {
            reol = selectedReol;
            lvwPladser.getItems().setAll(selectedReol.getPladser());
            btnRetReol.setDisable(false);
            btnSletReol.setDisable(false);
        } else {
            reol = null;
            lvwPladser.getItems().clear();
            btnRetReol.setDisable(true);
            btnSletReol.setDisable(true);
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

    public void updateTab() {
        Lager selectedLager = lager;
        Reol selectedReol = reol;
        Plads selectedPlads = plads;

        if(selectedLager != null) {
            lvwReoler.getItems().setAll(lager.getReoler());
            lvwReoler.getSelectionModel().select(selectedReol);

            if(selectedReol != null) {
                lvwPladser.getItems().setAll(reol.getPladser());
                lvwPladser.getSelectionModel().select(selectedPlads);
            }
        }
    }
}