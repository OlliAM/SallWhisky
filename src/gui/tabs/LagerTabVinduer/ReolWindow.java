package gui.tabs.LagerTabVinduer;

import application.controller.Controller;
import application.model.Lager;
import application.model.Reol;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class ReolWindow extends Stage {

    private final Controller controller;
    private final Lager lager;

    private TextField txfReolNavn;
    private TextField txfAntalPladser;
    private Button btnVælg, btnAnnuller;

    private Reol reol; // Returned (new or edited)

    public ReolWindow(Stage owner, Lager lager) {
        this.initOwner(owner);
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setResizable(false);
        this.setTitle("Opret reol");

        this.controller = new Controller();
        this.lager = lager;

        VBox pane = new VBox();
        initContent(pane);

        Scene scene = new Scene(pane);
        this.setScene(scene);

        pane.requestFocus();
    }

    public ReolWindow(Stage owner, Lager lager, Reol reol) {
        this(owner, lager);
        this.reol = reol;

        txfReolNavn.setText(reol.getID());
        txfAntalPladser.setText(String.valueOf(reol.getPladser().length));
        this.setTitle("Rediger reol");
        btnVælg.setText("Ændr reol");
    }

    private void initContent(VBox pane) {
        pane.setSpacing(10);
        pane.setPadding(new Insets(20));
        pane.setAlignment(Pos.CENTER);

        txfReolNavn = new TextField();
        txfReolNavn.setPromptText("Indtast navn på reol");

        // Integer filter like your original method
        Pattern validEditingState = Pattern.compile("([0-9]+)?");
        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (validEditingState.matcher(text).matches()) {
                return c;
            } else {
                return null;
            }
        };

        txfAntalPladser = new TextField();
        txfAntalPladser.setPromptText("Indtast antal pladser");
        txfAntalPladser.setTextFormatter(new TextFormatter<>(filter));

        btnVælg = new Button("Opret reol");
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

        pane.getChildren().addAll(txfReolNavn, txfAntalPladser, buttonHBox);
    }

    private void vælgAction() {
        String navn = txfReolNavn.getText().trim();
        String antalStr = txfAntalPladser.getText().trim();

        if (!navn.isEmpty() && !antalStr.isEmpty()) {
            int antal = Integer.parseInt(antalStr);
            try {
                if (reol == null) {
                    // Create new
                    reol = controller.createReol(lager, navn, antal);
                } else {
                    // Editing existing reol
                    reol.changeSize(antal);
                    reol.setID(navn);
                }

                this.close();
            } catch(IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void annullerAction() {
        reol = null;
        this.close();
    }

    public Reol getReol() {
        return reol;
    }
}
