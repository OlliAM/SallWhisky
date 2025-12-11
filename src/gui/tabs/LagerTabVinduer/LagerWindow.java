package gui.tabs.LagerTabVinduer;

import application.controller.Controller;
import application.model.Lager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LagerWindow extends Stage {
    private Controller controller;
    private TextField txfLagerNavn;
    private Button btnVælg, btnAnnuller;
    private Lager lager;

    public LagerWindow(Stage owner) {
        this.initOwner(owner);
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setResizable(false);
        this.setTitle("Opret lager");
        controller = new Controller();

        VBox pane = new VBox();
        initContent(pane);

        Scene scene = new Scene(pane);
        this.setScene(scene);

        pane.requestFocus();
    }

    public LagerWindow(Stage owner, Lager lager) {
        this(owner);
        this.lager = lager;
        txfLagerNavn.setText(lager.getLagerNavn());
        btnVælg.setText("Ændr lager");
    }

    private void initContent(VBox pane) {
        pane.setSpacing(10);
        pane.setPadding(new Insets(20));
        pane.setAlignment(Pos.CENTER);

        txfLagerNavn = new TextField();
        txfLagerNavn.setPromptText("Indtast lagernavn");

        btnVælg = new Button("Opret lager");
        btnAnnuller = new Button("Annuller");

        btnVælg.setOnAction(e -> vælgAction());
        btnAnnuller.setOnAction(e -> annullerAction());

        // Buttons with spacer
        HBox buttonHBox = new HBox();
        buttonHBox.setSpacing(10);
        buttonHBox.setPadding(new Insets(10));
        buttonHBox.setAlignment(Pos.CENTER);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        buttonHBox.getChildren().addAll(btnVælg, spacer, btnAnnuller);

        pane.getChildren().addAll(txfLagerNavn, buttonHBox);
    }

    private void annullerAction() {
        lager = null;
        this.close();
    }

    private void vælgAction() {
        String lagerNavn = txfLagerNavn.getText().trim();

        if(!lagerNavn.isEmpty()) {
            if(lager == null) {
                lager = controller.createLager(lagerNavn);
            }
            else {
                lager.setNavn(lagerNavn);
            }
            this.close();
        }
    }

    public Lager getLager() {
        return lager;
    }
}
