package gui;

import constants.Coordinates;
import genetics.networks.Network;
import genetics.nodes.FunctionNode;
import genetics.nodes.InputNode;
import genetics.nodes.Node;
import genetics.nodes.OutputNode;
import genetics.organism.Organism;
import gui.networkInfo.NetworkInfo;
import gui.panes.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class EvolveMe extends Application {
    private final static Double circleRadius = 25.;


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
                                Platform.runLater(() -> {
                                    showGraph(organism.getFitnessProgressData());
                                    drawBestNetwork(organism.getBestOfAllTime());
                                });
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

    private void drawBestNetwork(Network bestOfAllTime) {
        Stage drawingWindow = new Stage();
        Group group = new Group();
        Scene mainScene = new Scene(group, 1000, 600);

        List<Circle> inputCircles = getInputCircles(bestOfAllTime);
        List<Circle> functionCircles = getFunctionCircles(bestOfAllTime);
        List<Circle> outputCircles = getOutputCircles(bestOfAllTime);
        List<Line> outputFunctionLines = connectOutputsToFunctions(outputCircles, functionCircles, bestOfAllTime);
        List<Line> functionLines = connectFunctionNodes(inputCircles, functionCircles, bestOfAllTime);

        group.getChildren().addAll(outputFunctionLines);
        group.getChildren().addAll(functionLines);
        group.getChildren().addAll(inputCircles);
        group.getChildren().addAll(functionCircles);
        group.getChildren().addAll(outputCircles);



        drawingWindow.setScene(mainScene);
        drawingWindow.show();
    }

    private List<Circle> getInputCircles(Network bestOfAllTime) {
        List<Circle> circles = new ArrayList<>();
        double initialXCoordinate = 40;
        double initialYCoordinate = bestOfAllTime.getNumberOfInputs() == 1 ? 300 : (300 - (bestOfAllTime.getNumberOfInputs() - 1) * 75);

        for(int i = 0; i < bestOfAllTime.getNumberOfInputs(); i++) {
            Circle circle = new Circle(initialXCoordinate, initialYCoordinate + i*150, circleRadius, Color.BLUE);
            circles.add(circle);
        }
        return circles;
    }

    private List<Circle> getFunctionCircles(Network bestOfAllTime) {
        List<Circle> circles = new ArrayList<>();
        double initialXCoordinate = 180;
        double initialYCoordinate = bestOfAllTime.getNumberOfRows() == 1 ? 300 : (300 - (bestOfAllTime.getNumberOfRows() - 1) * 37.5);


        for(int i = 0; i < bestOfAllTime.getNumberOfRows(); i++) {
            for(int j = 0; j < bestOfAllTime.getNumberOfColumns(); j++) {
                Circle circle = new Circle(initialXCoordinate + i*150, initialYCoordinate + j*75, circleRadius, Color.DARKGRAY);
                if(bestOfAllTime.getFunctionNodes().get(i).get(j).isActive()) {
                    circle.setFill(Color.RED);
                }
                circles.add(circle);
            }
        }
        return circles;
    }

    private List<Circle> getOutputCircles(Network bestOfAllTime) {
        List<Circle> circles = new ArrayList<>();
        double initialXCoordinate = bestOfAllTime.getNumberOfColumns()*200;
        double initialYCoordinate = bestOfAllTime.getNumberOfOutputs() == 1 ? 300 : (300 - (bestOfAllTime.getNumberOfOutputs() - 1) * 75);

        for(int i = 0; i < bestOfAllTime.getNumberOfOutputs(); i++) {
            Circle circle = new Circle(initialXCoordinate, initialYCoordinate + i*150, circleRadius, Color.GREEN);
            circles.add(circle);
        }
        return circles;
    }

    private List<Line> connectOutputsToFunctions(List<Circle> outputCircles, List<Circle> functionCircles, Network bestOfAllTime) {
        List<Line> lines = new ArrayList<>();
        int numberOfColumns = bestOfAllTime.getNumberOfColumns();
        for(int i = 0; i<outputCircles.size(); i++) {
            OutputNode observedOutputNode = bestOfAllTime.getOutputNodes().get(i);
            Map<String, Integer> coordinates = observedOutputNode.getInput().getCoordinates();
            // broj_retka(krece od 0) * broj_stupaca + broj_stupca(krece od 0)
            int index = coordinates.get(Coordinates.x) * numberOfColumns + coordinates.get(Coordinates.y);
            Circle outputCircle = outputCircles.get(i);
            Circle functionCircle = functionCircles.get(index);
            lines.add(new Line(outputCircle.getCenterX(), outputCircle.getCenterY(), functionCircle.getCenterX(), functionCircle.getCenterY()));
        }

        return lines;
    }

    private List<Line> connectFunctionNodes(List<Circle> inputNodes, List<Circle> functionCircles, Network bestOfAllTime) {
        List<Line> lines = new ArrayList<>();

        for(int row = 0; row < bestOfAllTime.getNumberOfRows(); row++) {
            for(int col = 0; col < bestOfAllTime.getNumberOfColumns(); col++) {
                FunctionNode observedNode = bestOfAllTime.getFunctionNodes().get(col).get(row);
                Map<String, Integer> observedNodeCoordinates = observedNode.getCoordinates();
                int observedNodeIndex =  observedNodeCoordinates.get(Coordinates.x) * bestOfAllTime.getNumberOfColumns() + observedNodeCoordinates.get(Coordinates.y);
                Circle observedNodeCircle = functionCircles.get(observedNodeIndex);
                for(Node nodesInput: observedNode.getInputs()) {
                    int index;
                    Circle circle;
                    if(nodesInput.getClass() == InputNode.class ) {
                        index = nodesInput.getCoordinates().get(Coordinates.y);
                        circle = inputNodes.get(index);
                    } else {
                        Map<String, Integer> nodesInputCoordinates = nodesInput.getCoordinates();
                        index = nodesInputCoordinates.get(Coordinates.x) * bestOfAllTime.getNumberOfColumns() + nodesInputCoordinates.get(Coordinates.y);
                        circle = functionCircles.get(index);
                    }
                    Line line = new Line(observedNodeCircle.getCenterX(), observedNodeCircle.getCenterY(), circle.getCenterX(), circle.getCenterY());
                    lines.add(line);
                }
            }
        }

        return lines;
    }


}
