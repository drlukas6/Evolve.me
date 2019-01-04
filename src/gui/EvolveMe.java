package gui;

import genetics.organism.Organism;
import gui.networkInfo.NetworkInfo;
import gui.panes.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

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
                this.organism = new Organism(numberOfNetworks, rows, columns,
                        levelsBack, networkInputInfo.getDimension(), networkOutputInfo.getDimension(),
                        networkInputInfo.getValues(), networkOutputInfo.getValues().get(0), maxGenerations,
                        writer, centerPane.getCheckedOperations());
                Service<Void> service = new Service<Void>() {
                    @Override
                    protected Task<Void> createTask() {
                        return new Task<Void>() {
                            @Override
                            protected Void call() {
                                organism.performInitialExecution();
                                while (organism.getGeneration() < organism.getMaxGeneration()) {
                                    organism.completeGeneration();
                                    Platform.runLater(() -> bottomPane.updateProgressBy(1./organism.getMaxGeneration()));
                                }
                                Platform.runLater(() -> showGraph(organism.getFitnessProgressData()));
                                return null;
                            }
                        };
                    }
                };
                service.start();

            } catch (NumberFormatException exception ) {
                this.centerPane.setStatusText("Number Formatting error");
            } catch (FileNotFoundException exc) {
                this.centerPane.setStatusText("File not found error");
            } catch (IllegalStateException exception) {
                this.centerPane.setStatusText("Network info error");
            }
        });
    }

    private void showGraph(Map<Integer, Double> values) {
        Stage graphWindow = new Stage();

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> fitnessProgressGraph = new LineChart<>(xAxis, yAxis);
        XYChart.Series series = new XYChart.Series();

        xAxis.setLabel("Generation");
        yAxis.setLabel("Fitness");
        fitnessProgressGraph.setTitle("Fitness progress graph");
        series.setName("Fitnes progress data");

        for(Integer key: values.keySet()) {
            Double value = values.get(key);
            series.getData().add(new XYChart.Data<>(key, value));
        }

        Scene graphScene = new Scene(fitnessProgressGraph, 400, 400);
        fitnessProgressGraph.getData().add(series);

        graphWindow.setTitle("Fitness progress graph");
        graphWindow.setScene(graphScene);
        graphWindow.show();
    }

}
