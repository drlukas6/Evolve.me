package gui;

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

        primaryStage.setTitle("Evolve.me");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

}
