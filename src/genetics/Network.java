package genetics;

import genetics.abstractions.Node;
import genetics.abstractions.NodeType;

import java.util.*;

public class Network {
    private List<InputNode> inputNodes;
    private List<List<FunctionNode>> functionNodes;
    private List<OutputNode> outputNodes;
    private int numberOfRows;
    private int numberOfColumns;
    private int numberOfInputs;
    private int numberOfOutputs;
    private int levelsBack;

    public Network(int numberOfRows, int numberOfColumns, int numberOfInputs, int numberOfOutputs, int levelsBack) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.inputNodes = new ArrayList<>();

//        Row
        this.functionNodes = new ArrayList<>();

        this.outputNodes = new ArrayList<>();
        this.numberOfInputs = numberOfInputs;
        this.numberOfOutputs = numberOfOutputs;
        this.levelsBack = levelsBack;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void generateRandomInputs() {
        for (int i = 0; i < numberOfInputs; i++) {
            Random r = new Random();
            double randomOutput = r.nextDouble();
            Map<String, Integer> coordinates = new HashMap<>();
            coordinates.put("x", i);
            coordinates.put("y", 0);
            inputNodes.add(new InputNode(NodeType.INPUT, randomOutput, coordinates));
        }
        System.out.println("========\tGenerated " + inputNodes.size() + " Input nodes\t\t========");
    }

    public void generateRandomFunctionNodes() {
        int numberOfNodes = 0;
        for (int i = 0; i < numberOfRows; i++) {
//            Creating columns
            functionNodes.add(new ArrayList<>());
            for (int j = 0; j < numberOfColumns; j++) {
                numberOfNodes++;
                Map<String, Integer> coordinates = new HashMap<>();
                coordinates.put("x", i);
                coordinates.put("y", j);
                functionNodes.get(i).add(new FunctionNode(NodeType.FUNCTION, 0.0, coordinates));
            }
        }
        System.out.println("========\tGenerated " + numberOfNodes + " Function nodes\t\t========");
    }

    public void generateRandomOutputNodes() {
        for (int i = 0; i < numberOfOutputs; i++) {
            Map<String, Integer> coordinates = new HashMap<>();
            coordinates.put("x", i);
            coordinates.put("y", numberOfColumns + 1);
            outputNodes.add(new OutputNode(NodeType.OUTPUT, 0.0, coordinates));
        }
        System.out.println("========\tGenerated " + outputNodes.size() + " Output nodes\t\t========");
    }

    public void randomConnectNodes() {
        Random r = new Random();
        for(List<FunctionNode> row: functionNodes) {
            for(FunctionNode node: row) {
                do {
                    int maxColumn = node.getCoordinates().get("y") - 1;
                    int minColumn = node.getCoordinates().get("y") - levelsBack;
                    int randomRow;
                    int randomColumn = r.nextInt(maxColumn + 1 - minColumn) + minColumn;
                    Node foundNode;
                    if(randomColumn < 0) {
                        randomRow = r.nextInt(inputNodes.size() - 1 + 1);
                        foundNode = inputNodes.get(randomRow);
                    }
                    else {
                        randomRow = r.nextInt(numberOfRows);
                        foundNode = functionNodes.get(randomRow).get(randomColumn);
                    }
                    if(!node.getInputs().contains(foundNode)) {
                        node.getInputs().add(foundNode);
                    }
                } while(node.getInputs().size() < node.getOperation().getOperationArity());
                System.out.println("Connected node at " + node + " with operation: " + node.getOperation().getOperationId() + " to nodes: " + node.getInputs());
            }
        }
    }

    public void initialSetup() {
        generateRandomInputs();
        generateRandomFunctionNodes();
        generateRandomOutputNodes();
        randomConnectNodes();
    }
}
