package gui;

import gui.tabs.DestillaterTab;
import gui.tabs.FadeTab;
import gui.tabs.LagerTab;
import gui.tabs.ProdukterTab;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;


public class MainWindow extends Application {
    Stage stage;


    public void start(Stage stage) {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setPrefSize(800, 600);
        tabPane.setMaxSize(800, 600);

        // Create Tabs
        LagerTab lagerTab = new LagerTab();
        lagerTab.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                // Tab was switched to
                lagerTab.updateTab();
            }
        });
        DestillaterTab destillaterTab = new DestillaterTab();
        destillaterTab.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                // Tab was switched to
                destillaterTab.updateTab();
            }
        });
        FadeTab fadeTab = new FadeTab();
        fadeTab.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                // Tab was switched to
                fadeTab.updateTab();
            }
        });
        ProdukterTab produkterTab = new ProdukterTab();
        produkterTab.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                // Tab was switched to
                produkterTab.onTabSelected();
            }
        });
        tabPane.getTabs().addAll(lagerTab, destillaterTab, fadeTab, produkterTab);

        this.stage = stage;
        stage.setTitle("Sall Whisky");
        Scene scene = new Scene(tabPane);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}







