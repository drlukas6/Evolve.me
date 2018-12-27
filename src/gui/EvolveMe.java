package gui;

import genetics.organism.Organism;
import gui.networkInfo.NetworkInfo;
import gui.panes.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;

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
            this.bottomPane.setProgress(0.);
            networkInputInfo = leftPane.getNetworkInfo();
            networkOutputInfo = rightPane.getNetworkInfo();
            if (networkInputInfo == null || networkOutputInfo == null)
                throw new IllegalStateException();
            try {

                int rows = centerPane.getNumberOfRows();
                int columns = centerPane.getNumberOfColumns();
                int levelsBack = centerPane.getLevelsBack();
                int maxGenerations = centerPane.getMaxGenerations();
                int numberOfNetworks = centerPane.getNumberOfNetworks();

                String organismId = new Date().toString().replace(" ", "-");

                Writer writer = new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(String.format("Organisms/organism-%s.cgp", organismId)), StandardCharsets.UTF_8)
                );
                Organism organism = new Organism(numberOfNetworks, rows, columns,
                        levelsBack, networkInputInfo.getDimension(), networkOutputInfo.getDimension(),
                        networkInputInfo.getValues(), networkOutputInfo.getValues().get(0), maxGenerations,
                        writer, centerPane.getCheckedOperations());
                organism.performInitialExecution();
                while (organism.getGeneration() < organism.getMaxGeneration()) {
                    organism.completeGeneration();
                    Thread.sleep(1000);
                    bottomPane.updateProgressBy(1./organism.getMaxGeneration());
                }
//                new Thread(() -> {
//
//                    try {
//                        Writer writer = new BufferedWriter(
//                                new OutputStreamWriter(
//                                        new FileOutputStream(String.format("Organisms/organism-%s.cgp", organismId)), StandardCharsets.UTF_8)
//                        );
//                        Organism organism = new Organism(numberOfNetworks, rows, columns,
//                                levelsBack, networkInputInfo.getDimension(), networkOutputInfo.getDimension(),
//                                networkInputInfo.getValues(), networkOutputInfo.getValues().get(0), maxGenerations,
//                                writer, centerPane.getCheckedOperations());
//                        organism.performInitialExecution();
//                        while (organism.getGeneration() < organism.getMaxGeneration()) {
//                            organism.completeGeneration();
//                            Platform.runLater(() -> bottomPane.updateProgressBy(1./organism.getMaxGeneration()));
//                        }
//                    } catch (FileNotFoundException e1) {
//                        e1.printStackTrace();
//                    }
//                }).run();


            } catch (NumberFormatException exception ) {
                this.centerPane.setStatusText("Number Formatting error");
            } catch (FileNotFoundException exc) {
                this.centerPane.setStatusText("File not found error");
            } catch (IllegalStateException exception) {
                this.centerPane.setStatusText("Network info error");
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        });
    }

}
