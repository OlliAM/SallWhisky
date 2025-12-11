package gui.tabs;

import application.model.*;
import gui.tabs.FadeTabVinduer.FadWindow;
import gui.tabs.ProdukterTabVinduer.FærdigProduktWindow;
import javafx.beans.value.ChangeListener;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import application.controller.Controller;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.*;

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

        lvwFadeIndhold.setCellFactory(list -> createFadCell());
        lvwFadeTomme.setCellFactory(list -> createFadCell());

        updateTab();
    }

    private ListCell<Fad> createFadCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Fad fad, boolean empty) {
                super.updateItem(fad, empty);

                if (empty || fad == null) {
                    setText(null);
                    return;
                }

                String pladsText = (fad.getPlads() != null)
                        ? fad.getPlads().toString()
                        : "Ingen plads";

                setText(fad + "\n" + pladsText);
            }
        };
    }


    private void fadeTomChanged(Fad selectedFad) {
        if (selectedFad != null) {
            lvwFadeIndhold.getSelectionModel().clearSelection();
            fad = selectedFad;
            btnSletFad.setDisable(false);
            btnHistorik.setDisable(false);
            btnPåfyldFad.setDisable(false);
            btnTilføjTilLager.setDisable(false);
        } else {
            fad = null;
            btnSletFad.setDisable(true);
            btnHistorik.setDisable(true);
            btnPåfyldFad.setDisable(true);
            btnTilføjTilLager.setDisable(true);

        }
        btnTapFad.setDisable(true);
    }

    private void fadeIndholdChanged(Fad selectedFad) {
        if (selectedFad != null) {
            lvwFadeTomme.getSelectionModel().clearSelection();
            fad = selectedFad;
            btnSletFad.setDisable(false);
            btnHistorik.setDisable(false);
            btnPåfyldFad.setDisable(false);
            LocalDate sidstePåfyldningsDato = Collections.max(selectedFad.getIndholdshistorik().keySet());
            btnTapFad.setDisable(!sidstePåfyldningsDato.isBefore(LocalDate.now().minusYears(3)));
            btnTilføjTilLager.setDisable(false);
        } else {
            fad = null;
            btnSletFad.setDisable(true);
            btnHistorik.setDisable(true);
            btnPåfyldFad.setDisable(true);
            btnTapFad.setDisable(true);
            btnTilføjTilLager.setDisable(true);
        }
    }

    public void updateTab() {
        Fad selectedFad = fad;
        lvwFadeTomme.getItems().clear();
        lvwFadeIndhold.getItems().clear();

        for (Fad fad : controller.getStorage().getFadList()) {
            if (fad.getFadIndhold() == null) {
                lvwFadeTomme.getItems().add(fad);
            } else {
                lvwFadeIndhold.getItems().add(fad);
            }
        }
        if (lvwFadeTomme.getItems().contains(selectedFad)) {
            lvwFadeTomme.getSelectionModel().select(selectedFad);
        } else if (lvwFadeIndhold.getItems().contains(selectedFad)) {
            lvwFadeIndhold.getSelectionModel().select(selectedFad);
        }
    }

    private void createFadAction() {
        Stage owner = (Stage) this.getTabPane().getScene().getWindow();
        FadWindow opretFad = new FadWindow(owner);
        opretFad.showAndWait();
        updateTab();
    }

    private void sletFadAction() {
        Fad selectedFad = fad;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bekræft sletning");
        alert.setHeaderText("Er du sikker på, at du vil slette dette fad?");
        alert.setContentText(
                "Fadnummer: " + selectedFad.getFadNr() + "\n" +
                        "Fadtype: " + selectedFad.getFadtype() + "\n" +
                        "Mængde destillat i fad: " + selectedFad.getMængdeL() + "L\n" +
                        "Oprindelse: " + selectedFad.getOprindelse() + "\n\n" +
                        "Denne handling kan ikke fortrydes."
        );

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            //Fjerne fra storage
            controller.getStorage().removeFromFadList(fad);

            //Fjerne fra gui
            updateTab();

            //Success dialog
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setHeaderText(null);
            info.setTitle("Fad slettet");
            info.setContentText("Fad " + selectedFad.getFadNr() + " blev slettet.");
            info.showAndWait();
        }
    }

    private void seHistorikAction() {
        Fad selectedFad = fad;
        Stage stage = new Stage();
        stage.setTitle("Se fadhistorik");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        Label lblHistorik = new Label("Historik for Fad " + selectedFad.getFadNr());
        ListView<String> lvwHistorik = new ListView<>();
        Map<LocalDate, ArrayList<Drinkable>> fadHistorik = selectedFad.getIndholdshistorik();

        for (LocalDate dato : selectedFad.getIndholdshistorik().keySet()) {
            for (Drinkable drinkable : fadHistorik.get(dato)) {
                lvwHistorik.getItems().add(dato + "\n" + drinkable);
            }
        }

        vbox.getChildren().addAll(lblHistorik, lvwHistorik);

        // Show the window
        Scene scene = new Scene(vbox, 300, 400);
        stage.setScene(scene);
        stage.initOwner(this.getTabPane().getScene().getWindow()); // make it modal relative to main window
        stage.show();
    }

    private void tapFadAction() {
        Fad selectedFad = fad;
        Stage owner = (Stage) this.getTabPane().getScene().getWindow();
        FærdigProduktWindow færdigProduktWindow = new FærdigProduktWindow(owner, selectedFad);
        færdigProduktWindow.showAndWait();
    }

    private void påfyldFadAction() {
        Fad selectedFad = fad;
        Stage stage = new Stage();
        stage.setTitle("Fyld på fad");

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        // ---------- LISTVIEWS ----------
        HBox listsBox = new HBox(15);
        listsBox.setAlignment(Pos.CENTER);

        // Left list: BundDestillater
        VBox leftBox = new VBox(5);
        Label lblDestillater = new Label("Destillater");
        ListView<BundDestillat> lvwDest = new ListView<>();
        ArrayList<BundDestillat> bunddestillater = new ArrayList<>();
        for (Destillat destillat : controller.getStorage().getDestillatList()) {
            if (destillat instanceof BundDestillat bd) {
                bunddestillater.add(bd);
            }
        }
        lvwDest.getItems().addAll(bunddestillater);
        lvwDest.setPrefWidth(150);
        leftBox.getChildren().addAll(lblDestillater, lvwDest);

        // Right list: Fade
        VBox rightBox = new VBox(5);
        Label lblFade = new Label("Fade");
        ListView<Fad> lvwFad = new ListView<>();
        lvwFad.getItems().addAll(controller.getStorage().getFadList());
        lvwFad.getItems().remove(selectedFad);
        lvwFad.setPrefWidth(150);
        rightBox.getChildren().addAll(lblFade, lvwFad);

        listsBox.getChildren().addAll(leftBox, rightBox);

        // ---------- SLIDER + TEXTFIELD ----------
        Label lblAmount = new Label("Mængde at fylde (L)");

        Slider slider = new Slider(0, 0, 0);
        slider.setBlockIncrement(1);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setSnapToTicks(true);

        TextField txfAmount = new TextField("0");
        txfAmount.setMaxWidth(60);
        txfAmount.setAlignment(Pos.CENTER);

        txfAmount.setOnAction(e -> {
            try {
                double val = Double.parseDouble(txfAmount.getText());
                val = Math.max(slider.getMin(), Math.min(val, slider.getMax()));
                slider.setValue(val);
            } catch (NumberFormatException ex) {
                txfAmount.setText(String.format("%.1f", slider.getValue()));
            }
        });

        slider.valueProperty().addListener((obs, oldVal, newVal) ->
                txfAmount.setText(String.format("%.1f", newVal.doubleValue()))
        );

        Runnable updateSlider = () -> {
            double maxFad = selectedFad.getKapacitetL() - selectedFad.getMængdeL();
            BundDestillat selDest = lvwDest.getSelectionModel().getSelectedItem();
            Fad selFad = lvwFad.getSelectionModel().getSelectedItem();

            slider.setMax(0);
            slider.setValue(0);
            double max = 0;
            if (selDest != null) {
                max = Math.min(maxFad, selDest.getMængdeL());
            } else if (selFad != null) {
                max = Math.min(maxFad, selFad.getMængdeL());
            }
            slider.setMax(max);
            slider.setValue(max > 0 ? max : 0);
        };

        // ---------- LISTENERS ----------
        lvwDest.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                lvwFad.getSelectionModel().clearSelection();
                updateSlider.run();
            }
        });

        lvwFad.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                lvwDest.getSelectionModel().clearSelection();
                updateSlider.run();
            }
        });

        Label lblDato = new Label("Påfyldningsdato");
        DatePicker dpPåfyldningsdato = new DatePicker(LocalDate.now());

        // ---------- INITIALER ----------
        Label lblInit = new Label("Initialer");
        TextField txfInit = new TextField();

        // ---------- SELECT BUTTON ----------
        Button btnSelect = new Button("Fyld på");
        btnSelect.setOnAction(event -> {
            double amount = slider.getValue();
            String init = txfInit.getText().trim();
            LocalDate påfyldningsDato = dpPåfyldningsdato.getValue();

            if (selectedFad != null && amount > 0 && !init.isEmpty() && påfyldningsDato != null) {
                BundDestillat selDest = lvwDest.getSelectionModel().getSelectedItem();
                Fad selFad = lvwFad.getSelectionModel().getSelectedItem();
                if(selDest != null) {
                    controller.fyldPåFad(fad, selDest, amount, påfyldningsDato , init);
                }
                else {
                    controller.fyldPåFad(selectedFad, selFad, amount, LocalDate.now(), init);
                }
                updateTab();
                stage.close();
            }
        });

        // ---------- LAYOUT ----------
        root.getChildren().addAll(
                listsBox,
                lblAmount,
                slider,
                txfAmount,
                lblDato,
                dpPåfyldningsdato,
                lblInit,
                txfInit,
                btnSelect
        );

        Scene scene = new Scene(root, 400, 550);
        stage.setScene(scene);
        stage.initOwner(this.getTabPane().getScene().getWindow());
        stage.show();
    }


    private void tilføjTilLagerAction() {
        Fad selectedFad = fad;
        Stage stage = new Stage();
        stage.setTitle("Tilføj til lager");

        VBox lagerVBox = new VBox();
        lagerVBox.setPadding(new Insets(20));
        lagerVBox.setAlignment(Pos.CENTER);

        Label lblLager = new Label("Lagre");

        ListView<Lager> lvwLager = new ListView<>();
        lvwLager.getItems().addAll(controller.getStorage().getLagerList());
        lvwLager.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        lagerVBox.getChildren().addAll(lblLager, lvwLager);

        VBox reolVBox = new VBox();
        reolVBox.setPadding(new Insets(20));
        reolVBox.setAlignment(Pos.CENTER);

        Label lblReol = new Label("Reoler");

        ListView<Reol> lvwReoler = new ListView<>();;
        lvwReoler.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        reolVBox.getChildren().addAll(lblReol, lvwReoler);

        VBox pladsVBox = new VBox();
        pladsVBox.setPadding(new Insets(20));
        pladsVBox.setAlignment(Pos.CENTER);

        Label lblPlads = new Label("Lagre");

        ListView<Plads> lvwPlads = new ListView<>();
        lvwPlads.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        pladsVBox.getChildren().addAll(lblPlads, lvwPlads);

        Button btnSelect = new Button("Vælg");
        btnSelect.setOnAction(event -> {
            Plads selectedPlads = lvwPlads.getSelectionModel().getSelectedItem();
            if (selectedPlads != null) {
                selectedFad.gemPåPlads(selectedPlads);
                stage.close();
                updateTab();
            }
        });
        btnSelect.setDisable(true);


        HBox listviewHBox = new HBox();
        listviewHBox.setPadding(new Insets(20));
        listviewHBox.setAlignment(Pos.CENTER);

        listviewHBox.getChildren().setAll(lagerVBox, reolVBox, pladsVBox);

        VBox windowVBox = new VBox(10);
        windowVBox.setPadding(new Insets(20));
        windowVBox.setAlignment(Pos.CENTER);

        windowVBox.getChildren().addAll(listviewHBox, btnSelect);

        //Listeners
        lvwLager.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                lvwReoler.getSelectionModel().clearSelection();
                lvwReoler.getItems().setAll(sel.getReoler());
            }
            else {
                lvwReoler.getSelectionModel().clearSelection();
                lvwReoler.getItems().clear();
            }
        });

        lvwReoler.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                lvwPlads.getSelectionModel().clearSelection();
                ArrayList<Plads> tommePladser = new ArrayList<>();
                for(Plads plads : sel.getPladser()) {
                    if (plads.getVare() == null) {
                        tommePladser.add(plads);
                    }
                }
                lvwPlads.getItems().setAll(tommePladser);
            }
            else {
                lvwPlads.getSelectionModel().clearSelection();
                lvwPlads.getItems().clear();
            }
        });

        lvwPlads.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            btnSelect.setDisable(sel == null);
        });

        // Show the window
        Scene scene = new Scene(windowVBox);
        stage.setScene(scene);
        stage.initOwner(this.getTabPane().getScene().getWindow()); // make it modal relative to main window
        stage.show();
    }
}
