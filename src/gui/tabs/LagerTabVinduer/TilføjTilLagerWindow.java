package gui.tabs.LagerTabVinduer;

import application.controller.Controller;
import application.model.Fad;
import application.model.Flaske;
import application.model.Storable;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class TilføjTilLagerWindow extends Stage {
    private Storable vare;
    private ListView<Fad> lvwFade;
    private ListView<Flaske> lvwFlasker;

    private Button btnVælg, btnAnnuller;

    private final Controller controller;

    public TilføjTilLagerWindow(Stage owner) {
        this.initOwner(owner);
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setResizable(false);
        this.setTitle("Tilføj vare til lager");

        controller = new Controller();

        VBox pane = new VBox(10);
        this.initContent(pane);

        Scene scene = new Scene(pane);
        this.setScene(scene);

        pane.requestFocus();
    }

    private void initContent(VBox pane) {
        pane.setPadding(new Insets(20));
        pane.setAlignment(Pos.CENTER);

        lvwFade = new ListView<>();
        lvwFade.getItems().setAll(controller.getStorage().getFadList());
        ChangeListener<Fad> fadListener = (ov, oldFad,
                                           newFad) -> fadChanged();
        lvwFade.getSelectionModel().selectedItemProperty().addListener(fadListener);

        lvwFlasker = new ListView<>();
        lvwFlasker.getItems().setAll(controller.getStorage().getFlaskeList());
        ChangeListener<Flaske> flaskeListener = (ov, oldFlaske,
                                           newFlaske) -> flaskeChanged();
        lvwFlasker.getSelectionModel().selectedItemProperty().addListener(flaskeListener);

        btnVælg = new Button("Vælg");
        btnAnnuller = new Button("Annuller");

        btnVælg.setOnAction(e -> this.close());
        btnAnnuller.setOnAction(e -> annullerAction());

        // Button bar
        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setAlignment(Pos.CENTER);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        buttonBox.getChildren().addAll(btnVælg, spacer, btnAnnuller);

        // Add to layout
        pane.getChildren().addAll(lvwFade, lvwFlasker, buttonBox);

        lvwFade.setCellFactory(vareCellFactory());
        lvwFlasker.setCellFactory(vareCellFactory());

    }

    private static <T extends Storable> Callback<ListView<T>, ListCell<T>> vareCellFactory() {
        return list -> new ListCell<>() {
            @Override
            protected void updateItem(T vare, boolean empty) {
                super.updateItem(vare, empty);
                if (empty || vare == null) {
                    setText(null);
                } else {
                    String str = vare + "\n";
                    if (vare.getPlads() == null) {
                        setText(str + "Ikke på lager");
                    } else {
                        setText(str + vare.getPlads().toString());
                    }
                }
            }
        };
    }


    private void flaskeChanged() {
        Flaske selectedFlaske = lvwFlasker.getSelectionModel().getSelectedItem();
        if(selectedFlaske != null) {
            lvwFade.getSelectionModel().clearSelection();
            vare = selectedFlaske;
        }
    }

    private void fadChanged() {
        Fad selectedFad = lvwFade.getSelectionModel().getSelectedItem();
        if(selectedFad != null) {
            lvwFlasker.getSelectionModel().clearSelection();
            vare = selectedFad;
        }
    }

    private void annullerAction() {
        vare = null;
        this.close();
    }

    public Storable getVare() {
        return vare;
    }
}
