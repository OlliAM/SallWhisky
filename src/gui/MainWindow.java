package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class MainWindow extends Application {
    Stage stage;



    public void start(Stage stage) {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Create Tabs
        LagerTab lagerTab = new LagerTab();
        Tab destillaterTab = new Tab("Destillater");
        Tab fadeTab = new FadeTab();
        Tab produkterTab = new Tab("Produkter");
        tabPane.getTabs().addAll(lagerTab, destillaterTab, fadeTab, produkterTab);

        this.stage = stage;
        stage.setTitle("Sall Whisky");
        Scene scene = new Scene(tabPane);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void initContent(GridPane pane) {
        pane.setGridLinesVisible(false);
        pane.setPadding(new Insets(20));
        pane.setHgap(20);
        pane.setVgap(20);

        // Label for Konference
        Label lblKonference = new Label("Konferencer");
        pane.add(lblKonference, 0, 0);
    }

}







