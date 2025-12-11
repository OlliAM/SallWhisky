package gui.tabs;

import application.controller.Controller;
import application.model.*;
import gui.tabs.DestillaterTabVinduer.DestillatWindow;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Optional;

public class DestillaterTab extends Tab {
    private Destillat destillat;
    private Controller controller = new Controller();

    private ListView<Destillat> lvwDestillater;
    private ListView<String> lvwInformation;
    private Button btnCreateDestillat, btnÆndrDestillat, btnSletDestillat, btnPåfyldFad, btnSkiftView;
    private TextArea txaKommentar;
    private boolean færdigeDestillater, suppressListener;

    public DestillaterTab() {
        super("Destillater");

        GridPane gridPane = new GridPane();
        this.setContent(gridPane);
        initContent(gridPane);
        færdigeDestillater = true;
        suppressListener = false;
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

        btnSletDestillat = new Button("Slet destillat");
        btnSletDestillat.setOnAction(e -> sletDestillatAction());
        btnSletDestillat.setDisable(true);

        btnSkiftView = new Button("Se igangværende destilleringer");
        btnSkiftView.setOnAction(e -> skiftViewAction());
        btnSkiftView.setWrapText(true);
        btnSkiftView.setMaxWidth(120);

        btnPåfyldFad = new Button("Påfyld fad");
        btnPåfyldFad.setOnAction(e -> påfyldFadAction());
        btnPåfyldFad.setDisable(true);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        lagerButtonVBox.getChildren().addAll(btnCreateDestillat, btnÆndrDestillat, btnSletDestillat, btnSkiftView,
                spacer, btnPåfyldFad);

        VBox destillaterVBox = new VBox();
        destillaterVBox.setSpacing(10);
        pane.add(destillaterVBox, 1, 1, 1, 2);

        Label lblDestillater = new Label("Destillater");
        lblDestillater.setStyle("-fx-font-size: 14px; -fx-font-weight: 300;");
        lvwDestillater = new ListView<>();
        lvwDestillater.setPrefHeight(400);
        ChangeListener<Destillat> destillaterListener = (ov, oldDestillat, newDestillat) ->
        {
            if (!suppressListener) {
                destillatChanged();
            }
        };
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

    private void sletDestillatAction() {
        Destillat selectedDestillat = destillat;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bekræft sletning");
        alert.setHeaderText("Er du sikker på, at du vil slette dette destillat?");
        alert.setContentText(
                "navn: " + selectedDestillat.getNavn() + "\n" +
                        "Anvendt i " + controller.søgIStorage(selectedDestillat).size() + " varer \n\n" +
                        "Denne handling kan ikke fortrydes."
        );

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            //Fjerne fra storage
            if (selectedDestillat.getFærdigDato() != null) {
                controller.getStorage().getDestillatList().remove(selectedDestillat);
            } else {
                controller.getStorage().getDestilleringList().remove(selectedDestillat);
            }

            //Fjerne fra gui
            destillat = null;
            lvwDestillater.getSelectionModel().clearSelection();
            destillatChanged();
            updateTab();

            //Success dialog
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setHeaderText(null);
            info.setTitle("Lager slettet");
            info.setContentText("Lager " + selectedDestillat + " blev slettet.");
            info.showAndWait();
        }
    }

    private void skiftViewAction() {
        if (færdigeDestillater) {
            færdigeDestillater = false;
            lvwDestillater.getItems().setAll(controller.getStorage().getDestilleringList());
            lvwDestillater.getSelectionModel().clearSelection();
            btnSkiftView.setText("Se færdige destillater");
        } else {
            færdigeDestillater = true;
            lvwDestillater.getItems().setAll(controller.getStorage().getDestillatList());
            lvwDestillater.getSelectionModel().clearSelection();
            btnSkiftView.setText("Se igangværende destilleringer");
        }
    }


    private void destillatChanged() {
        Destillat valgteDestillat = lvwDestillater.getSelectionModel().getSelectedItem();
        if (valgteDestillat != null) {
            btnÆndrDestillat.setDisable(false);
            btnSletDestillat.setDisable(false);
            String færdigDato = "";
            if (færdigeDestillater) {
                færdigDato = valgteDestillat.getFærdigDato().toString();
            }
            String maltBatch = valgteDestillat.getMaltbatch().toString();
            String init = valgteDestillat.getInit();
            String alkoholprocent = valgteDestillat.getAlkoholprocent() + "%";

            lvwInformation.getItems().setAll(
                    "Færdigdato: " + færdigDato,
                    "Maltbatch: " + maltBatch,
                    "Initialer: " + init,
                    "Alkoholprocent: " + alkoholprocent);
            txaKommentar.setText(valgteDestillat.getKommentar());

            if (valgteDestillat instanceof BundDestillat) {
                String startDato = ((BundDestillat) valgteDestillat).getStartDato().toString();
                String kornSort = ((BundDestillat) valgteDestillat).getKornsort();
                String mængde = ((BundDestillat) valgteDestillat).getMængdeL() + "L";
                lvwInformation.getItems().addFirst("Startdato: " + startDato);
                lvwInformation.getItems().addAll(
                        "Kornsort: " + kornSort,
                        "Mængde: " + mængde);
                btnPåfyldFad.setDisable(!færdigeDestillater);
                btnÆndrDestillat.setDisable(false);

            } else if (valgteDestillat instanceof KombiDestillat kd) {
                Destillat destillat1 = kd.getChild(0);
                Destillat destillat2 = kd.getChild(1);
                lvwInformation.getItems().addAll(
                        "Destillat 1: " + destillat1,
                        "Destillat 2: " + destillat2);
                btnÆndrDestillat.setDisable(true);
                btnPåfyldFad.setDisable(true);
            }
        } else {
            btnÆndrDestillat.setDisable(true);
            btnSletDestillat.setDisable(true);
            txaKommentar.clear();
            lvwInformation.getItems().clear();
        }
        destillat = valgteDestillat;
    }

    private void påfyldFadAction() {
        BundDestillat selectedDestillat = (BundDestillat) destillat;
        Stage stage = new Stage();
        stage.setTitle("Fyld på fad");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        Label lblFad = new Label("Fade");
        ListView<Fad> lvwFad = new ListView<>();
        lvwFad.getItems().addAll(controller.getStorage().getFadList());
        lvwFad.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        Label lblAmount = new Label("Mængde at fylde (L)");

        Slider slider = new Slider();
        slider.setMin(0);
        slider.setValue(0);
        slider.setBlockIncrement(1);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setSnapToTicks(true);

        // TextField to display/edit slider value
        TextField txfAmount = new TextField("0");
        txfAmount.setMaxWidth(60);
        txfAmount.setAlignment(Pos.CENTER);

        // Update slider when TextField changes
        txfAmount.setOnAction(e -> {
            try {
                double val = Double.parseDouble(txfAmount.getText());
                if (val < slider.getMin()) val = slider.getMin();
                if (val > slider.getMax()) val = slider.getMax();
                slider.setValue(val);
            } catch (NumberFormatException ex) {
                txfAmount.setText(String.format("%.1f", slider.getValue()));
            }
        });

        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            txfAmount.setText(String.format("%.1f", newVal.doubleValue()));
        });

        lvwFad.getSelectionModel().selectedItemProperty().addListener((obs, oldFad, newFad) -> {
            if (newFad != null && selectedDestillat != null) {
                double maxFad = newFad.getKapacitetL() - newFad.getMængdeL();
                double maxDestillat = selectedDestillat.getMængdeL();
                double sliderMax = Math.min(maxFad, maxDestillat); // max amount we can fill
                slider.setMax(sliderMax);
                slider.setValue(0);
            } else {
                slider.setMax(0);
                slider.setValue(0);
            }
        });

        Label lblInit = new Label("Initialer");
        TextField txfInit = new TextField();

        Button btnSelect = new Button("Vælg");
        btnSelect.setOnAction(event -> {
            Fad selectedFad = lvwFad.getSelectionModel().getSelectedItem();
            double påfyldningsMængde = slider.getValue();
            String init = txfInit.getText().trim();
            if (selectedFad != null && påfyldningsMængde > 0 && !init.isEmpty()) {
                Destillat nyeDestillat = controller.fyldPåFad(selectedFad, selectedDestillat, påfyldningsMængde,
                        LocalDate.now(), init);
                if(lvwDestillater.getItems().contains(nyeDestillat)) {
                    lvwDestillater.getSelectionModel().select(destillat);
                }
                destillatChanged();
                updateTab();
                stage.close();
            }
        });

        vbox.getChildren().addAll(lblFad, lvwFad, lblAmount, slider, txfAmount, lblInit, txfInit, btnSelect);

        Scene scene = new Scene(vbox, 350, 500);
        stage.setScene(scene);
        stage.initOwner(this.getTabPane().getScene().getWindow());
        stage.show();

    }

    private void ændrDestillatAction() {
        Destillat selectedDestillat = destillat;
        Stage owner = (Stage) this.getTabPane().getScene().getWindow();
        DestillatWindow destillatWindow = new DestillatWindow(owner, (BundDestillat) selectedDestillat);
        destillatWindow.showAndWait();
        BundDestillat ændretDestillat = destillatWindow.getDestillat();
        if (ændretDestillat != null) {
            destillatChanged();
            updateTab();
        }
    }

    private void createDestillat() {
        Stage owner = (Stage) this.getTabPane().getScene().getWindow();
        DestillatWindow opretDestillat = new DestillatWindow(owner);
        opretDestillat.showAndWait();
        updateTab();
    }

    public void updateTab() {
        suppressListener = true;
        Destillat selectedDestillat = destillat;

        if (færdigeDestillater) {
            lvwDestillater.getItems().setAll(controller.getStorage().getDestillatList());
        } else {
            lvwDestillater.getItems().setAll(controller.getStorage().getDestilleringList());
        }
        lvwDestillater.getSelectionModel().select(selectedDestillat);

        if (destillat == null) {
            lvwInformation.getItems().clear();
        }
        suppressListener = false;
    }
}
