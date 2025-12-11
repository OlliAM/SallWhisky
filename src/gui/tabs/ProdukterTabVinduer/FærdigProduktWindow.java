package gui.tabs.ProdukterTabVinduer;

import application.controller.Controller;
import application.model.Fad;
import application.model.Færdigprodukt;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FærdigProduktWindow extends Stage {

    private final Controller controller = new Controller();

    // Replaced with FadExtraction list:
    private final ListView<FadExtraction> lvwFade = new ListView<>();

    private final Button btnAddFad = new Button("Tilføj fad");

    private final TextField txfTotalAmount = new TextField();
    private final TextField txfAlcoholPct = new TextField();

    private final Slider sldWater = new Slider();
    private final TextField txfWater = new TextField();

    private final Button btnCreate = new Button("Opret færdigprodukt");
    private final Button btnCancel = new Button("Annuller");

    // The list we compute from:
    private final javafx.collections.ObservableList<FadExtraction> fadExtractions =
            javafx.collections.FXCollections.observableArrayList();


    public FærdigProduktWindow(Stage owner) {
        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Opret færdigprodukt");
        setResizable(false);

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        initFadListSection(root);
        initComputedSection(root);
        initWaterSection(root);
        initButtons(root);

        updateTotals();

        Scene scene = new Scene(root, 550, 650);
        setScene(scene);
    }

    public FærdigProduktWindow(Stage owner, Fad fad) {
        this(owner);
        fadExtractions.add(new FadExtraction(fad));
    }

    private void initFadListSection(VBox root) {
        Label lbl = new Label("Fade og mængdeudtræk");

        btnAddFad.setOnAction(e -> openFadSelectorWindow());

        HBox box = new HBox(10, lbl, btnAddFad);
        box.setAlignment(Pos.CENTER_LEFT);

        lvwFade.setPrefHeight(220);
        lvwFade.setItems(fadExtractions);

        lvwFade.setCellFactory(list -> new ListCell<>() {

            private final Label lblName = new Label();
            private final Slider slider = new Slider();
            private final TextField txtValue = new TextField();
            private final Button btnRemove = new Button("Remove fad");
            private final HBox row = new HBox(10);
            private final Region spacer = new Region();

            {
                row.setAlignment(Pos.CENTER_LEFT);

                // Expand spacer between label and slider
                HBox.setHgrow(spacer, Priority.ALWAYS);

                txtValue.setPrefWidth(60);
                txtValue.setAlignment(Pos.CENTER);

                slider.setShowTickLabels(true);
                slider.setShowTickMarks(true);
                slider.setMinorTickCount(0);
                slider.setMajorTickUnit(1);
                slider.setSnapToTicks(true);
                slider.setPrefWidth(180);

                // Slider → TextField + model update
                slider.valueProperty().addListener((obs, oldVal, newVal) -> {
                    txtValue.setText(String.format("%.1f", newVal.doubleValue()));
                    FadExtraction fe = getItem();
                    if (fe != null) {
                        fe.amount.set(newVal.doubleValue());
                        updateTotals();
                    }
                });

                // TextField → Slider
                txtValue.setOnAction(e -> {
                    try {
                        double v = Double.parseDouble(txtValue.getText());
                        v = Math.max(slider.getMin(), Math.min(slider.getMax(), v));
                        slider.setValue(v);
                    } catch (NumberFormatException ignored) {}
                });

                // Remove button
                btnRemove.setOnAction(e -> {
                    FadExtraction fe = getItem();
                    if (fe != null) {
                        fadExtractions.remove(fe);
                        updateTotals();
                    }
                });

                row.getChildren().addAll(lblName, spacer, slider, txtValue, btnRemove);
            }

            @Override
            protected void updateItem(FadExtraction fe, boolean empty) {
                super.updateItem(fe, empty);

                if (empty || fe == null) {
                    setGraphic(null);
                    return;
                }

                lblName.setText(fe.fad.toString());
                slider.setMin(0);
                slider.setMax(fe.fad.getMængdeL());
                slider.setValue(fe.amount.get());
                txtValue.setText(String.format("%.1f", fe.amount.get()));

                setGraphic(row);
            }
        });

        root.getChildren().addAll(box, lvwFade);
    }


    private void openFadSelectorWindow() {
        Stage stage = new Stage();
        stage.initOwner(this);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Vælg fade til færdigprodukt");

        List<Fad> fade = new ArrayList<>();
        for (Fad fad : controller.getStorage().getFadList()) {
            if (fad.getFadIndhold() != null) {
                LocalDate sidstePåfyldningsDato = Collections.max(fad.getIndholdshistorik().keySet());
                if (sidstePåfyldningsDato.isBefore(LocalDate.now().minusYears(3))) {
                    fade.add(fad);
                }
            }
        }

        ListView<Fad> lvw = new ListView<>();
        lvw.getItems().addAll(fade);
        for(FadExtraction fadExtraction : fadExtractions) {
            lvw.getItems().remove(fadExtraction.fad);
        }

        Button btnVælg = new Button("Vælg");
        btnVælg.setOnAction(e -> {
            Fad selected = lvw.getSelectionModel().getSelectedItem();
            if (selected != null) {
                fadExtractions.add(new FadExtraction(selected));
                updateTotals();
                stage.close();
            }
        });

        Button btnCancel = new Button("Annuller");
        btnCancel.setOnAction(e -> stage.close());

        HBox buttons = new HBox(12, btnVælg, btnCancel);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(15, lvw, buttons);
        layout.setPadding(new Insets(20));
        stage.setScene(new Scene(layout, 300, 400));
        stage.showAndWait();
    }

    private void initComputedSection(VBox root) {
        Label lblTotal = new Label("Total mængde (L)");
        Label lblPct = new Label("Alkoholprocent");

        txfTotalAmount.setEditable(false);
        txfTotalAmount.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        txfAlcoholPct.setEditable(false);
        txfAlcoholPct.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        txfAlcoholPct.setPrefWidth(60);
        txfAlcoholPct.setMaxWidth(60);


        // --- PERCENT SIGN MOVED OUT OF THE TEXTFIELD ---
        Label lblPercent = new Label("%");


        HBox pctBox = new HBox(5, txfAlcoholPct, lblPercent);
        pctBox.setAlignment(Pos.CENTER_LEFT);

        // Enable/disable Create button when percentage changes
        txfAlcoholPct.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                btnCreate.setDisable(true);
                return;
            }

            try {
                double pct = Double.parseDouble(newValue);
                btnCreate.setDisable(pct < 40);
            } catch (NumberFormatException ex) {
                btnCreate.setDisable(true);
            }
        });

        root.getChildren().addAll(lblTotal, txfTotalAmount, lblPct, pctBox);
    }


    private void updateTotals() {
        double extractedL = fadExtractions.stream()
                .mapToDouble(fe -> fe.amount.get())
                .sum();

        double pureAlcohol = fadExtractions.stream()
                .mapToDouble(fe -> fe.amount.get() *
                        (fe.fad.getFadIndhold().getAlkoholprocent() / 100.0))
                .sum();

        double water = sldWater.getValue();
        double totalL = extractedL + water;

        double pct = totalL > 0 ? (pureAlcohol / totalL) * 100 : 0;

        txfTotalAmount.setText(String.format("%.2f", totalL));   // removed "L"
        txfAlcoholPct.setText(String.format("%.2f", pct));       // removed "%"

        updateWaterSlider(pureAlcohol, extractedL);
    }



    private void initWaterSection(VBox root) {
        Label lbl = new Label("Tilføj vand (L)");

        sldWater.setShowTickMarks(true);
        sldWater.setShowTickLabels(true);

        sldWater.valueProperty().addListener((obs, old, val) -> {
            txfWater.setText(String.format("%.2f", val.doubleValue()));
            updateTotals();
        });

        txfWater.setMaxWidth(60);
        txfWater.setAlignment(Pos.CENTER);
        txfWater.setOnAction(e -> {
            try {
                double v = Double.parseDouble(txfWater.getText());
                v = Math.max(0, Math.min(v, sldWater.getMax()));
                sldWater.setValue(v);
            } catch (NumberFormatException ignored) {}
        });

        HBox h = new HBox(10, sldWater, txfWater);
        h.setAlignment(Pos.CENTER);

        root.getChildren().addAll(lbl, h);
    }

    private void updateWaterSlider(double pureAlcohol, double extractedL) {

        // Case: No alcohol or no extract → no water allowed
        if (pureAlcohol <= 0 || extractedL <= 0) {
            sldWater.setDisable(true);
            sldWater.setMax(0);
            sldWater.setMin(0);
            sldWater.setValue(0);
            return;
        }

        // Normal case
        sldWater.setDisable(false);

        // pureAlcohol / (extractedL + maxWater) = 40%
        double maxWater = (pureAlcohol / 0.40) - extractedL;

        if (maxWater < 0) maxWater = 0;

        // IMPORTANT: update min/max BEFORE setting value
        sldWater.setMin(0);
        sldWater.setMax(maxWater);

        // Ensure current value is valid
        if (Double.isNaN(sldWater.getValue()) || sldWater.getValue() > maxWater) {
            sldWater.setValue(maxWater);
        }
    }


    private void initButtons(VBox root) {
        btnCreate.setOnAction(e -> createFærdigProduktAction());

        btnCancel.setOnAction(e -> close());

        HBox h = new HBox(20, btnCreate, btnCancel);
        h.setAlignment(Pos.CENTER);
        root.getChildren().add(h);
    }

    private void createFærdigProduktAction() {

    }

    public static class FadExtraction {
        public final Fad fad;
        public final DoubleProperty amount = new SimpleDoubleProperty(0);

        public FadExtraction(Fad fad) {
            this.fad = fad;
        }
    }
}
