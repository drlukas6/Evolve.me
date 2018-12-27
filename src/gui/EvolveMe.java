package gui;

import genetics.organism.Organism;
import gui.networkInfo.NetworkInfo;
import gui.panes.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class EvolveMe extends Application {

    private TopPane topPane = new TopPane(600.);
    private LeftPane leftPane = new LeftPane(150.);
    private RightPane rightPane = new RightPane(150.);
    private BottomPane bottomPane = new BottomPane(600.);
    private CenterPane centerPane = new CenterPane(300.);

    private NetworkInfo networkInputInfo;
    private NetworkInfo networkOutputInfo;

    private Organism organism;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        root.setTop(topPane);
        root.setBottom(bottomPane);
        root.setLeft(leftPane);
        root.setRight(rightPane);
        root.setCenter(centerPane);

        Scene mainScene = new Scene(root, 600, 400);

        setActions();

        primaryStage.setTitle("Evolve.me");
        primaryStage.setScene(mainScene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    private void setActions() {
        topPane.getTrainButton().setOnAction(e -> {
            this.centerPane.clearStatusText();
            networkInputInfo = leftPane.getNetworkInfo();
            networkOutputInfo = rightPane.getNetworkInfo();
            try {
                int rows = centerPane.getNumberOfRows();
                int columns = centerPane.getNumberOfColumns();
                int levelsBack = centerPane.getLevelsBack();
                int maxGenerations = centerPane.getMaxGenerations();
                int numberOfNetworks = centerPane.getNumberOfNetworks();
            } catch (NumberFormatException exception ){
                this.centerPane.setStatusText("Number Formatting error");
            }
        });
    }

}
