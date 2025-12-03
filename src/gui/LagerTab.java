package gui;

import application.controller.Controller;
import application.model.Lager;
import application.model.Reol;
import application.model.Storable;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LagerTab extends Tab {
    private Lager lager;
    private Controller controller = new Controller();
    private ListView<Reol> lvwReoler;
    private ListView<Storable> lvwPladser;
    private Button btnOpretLager, btnVælgLager, btnOpretReol, btnTømPlads, btnTilføjTilLager;

    public LagerTab() {
        super("Lager");
        GridPane gridPane = new GridPane();
        this.setContent(gridPane);
        initContent(gridPane);
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

        Label name = new Label("Lager Navn: ");
        VBox nameBox = new VBox(name);
        nameBox.setAlignment(Pos.CENTER);

        Separator vSeparator = new Separator(Orientation.VERTICAL);
        vSeparator.setPrefHeight(30);

        HBox titleHbox = new HBox(10);
        titleHbox.getChildren().addAll(titleBox, vSeparator, nameBox);
        titleHbox.setAlignment(Pos.CENTER_LEFT);
        pane.add(titleHbox, 0, 0);

        VBox lagerButtonVBox = new VBox();
        lagerButtonVBox.setSpacing(15);
        pane.add(lagerButtonVBox, 0, 1);

        btnVælgLager = new Button("Vælg lager");
        btnVælgLager.setOnAction(e -> openLagerSelectionWindow());
        btnOpretLager = new Button("Opret lager");
        btnOpretReol = new Button("Opret reol");
        btnTømPlads = new Button("Tøm plads");
        btnTilføjTilLager = new Button("Tilføj til lager");

        Region spacer = new Region();
        spacer.setPrefHeight(200);

        lagerButtonVBox.getChildren().addAll(btnVælgLager, btnOpretLager, btnOpretReol, spacer, btnTømPlads, btnTilføjTilLager);

        VBox reolerVBox = new VBox();
        reolerVBox.setAlignment(Pos.CENTER);
        reolerVBox.setSpacing(10);
        pane.add(reolerVBox, 1,1);

        Label lblReoler = new Label("Reoler");
        lblReoler.setStyle("-fx-font-size: 14px; -fx-font-weight: 300;");
        lvwReoler = new ListView<>();

        reolerVBox.getChildren().addAll(lblReoler, lvwReoler);

        VBox pladserVBox = new VBox();
        pladserVBox.setAlignment(Pos.CENTER);
        pladserVBox.setSpacing(10);
        pane.add(pladserVBox,2,1);

        Label lblPladser = new Label("Pladser");
        lblPladser.setStyle("-fx-font-size: 14px; -fx-font-weight: 300;");
        lvwPladser = new ListView<>();
        ChangeListener<Reol> reolListener = (ov, oldReol,
                                             newReol) -> this.reolChanged();

        pladserVBox.getChildren().addAll(lblPladser, lvwPladser);
    }

    private void reolChanged() {
        Reol selectedReol = lvwReoler.getSelectionModel().getSelectedItem();
        if(selectedReol != null) {
            lvwPladser.getItems().setAll(selectedReol.getPladser());
        }
    }

    private void openLagerSelectionWindow() {
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