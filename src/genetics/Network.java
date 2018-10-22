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
//        As this is the first version of my network, inputs will be random double values
        for (int i = 0; i < numberOfInputs; i++) {
            Random r = new Random();
            double randomOutput = r.nextDouble();
            Map<String, Integer> coordinates = new HashMap<>();
            coordinates.put("x", 0);
            coordinates.put("y", i);
            inputNodes.add(new InputNode(NodeType.INPUT, randomOutput, coordinates, "i"));
        }
        System.out.println("================\tGenerated " + inputNodes.size() + " Input nodes\t\t================");
        System.out.println(inputNodes);
    }

    public void generateRandomFunctionNodes() {
        int numberOfNodes = 0;
        for (int i = 0; i < numberOfColumns; i++) {
//            Creating columns
            functionNodes.add(new ArrayList<>());
            for (int j = 0; j < numberOfRows; j++) {
                numberOfNodes++;
                Map<String, Integer> coordinates = new HashMap<>();
                coordinates.put("x", i);
                coordinates.put("y", j);
                functionNodes.get(i).add(new FunctionNode(NodeType.FUNCTION, 0.0, coordinates, "f"));
            }
        }
        System.out.println("================\tGenerated " + numberOfNodes + " Function nodes\t================");
        System.out.println(functionNodes);
    }

    public void generateRandomOutputNodes() {
        for (int i = 0; i < numberOfOutputs; i++) {
            Map<String, Integer> coordinates = new HashMap<>();
            coordinates.put("x", 0);
            coordinates.put("y", i);
            outputNodes.add(new OutputNode(NodeType.OUTPUT, 0.0, coordinates, "o"));
        }
        System.out.println("================\tGenerated " + outputNodes.size() + " Output nodes\t================");
        System.out.println(outputNodes);
    }


    public void randomConnectFunctionNodes() {
        System.out.println("================\tConnecting function nodes\t================");
        Random r = new Random();
        for(List<FunctionNode> row: functionNodes) {
            for(FunctionNode node: row) {
                int randomRow;
                int randomColumn;
                do {
                    int maxColumn = node.getCoordinates().get("x") - 1;
                    int minColumn = node.getCoordinates().get("x") - 1 - levelsBack;
                  randomColumn = r.nextInt(maxColumn + 1 - minColumn) + minColumn;
                    Node foundNode;
                    if(randomColumn < 0) {
                        randomRow = r.nextInt(inputNodes.size() - 1 + 1);
                        foundNode = inputNodes.get(randomRow);
                    }
                    else {
                        randomRow = r.nextInt(numberOfRows);
                        foundNode = functionNodes.get(randomColumn).get(randomRow);
                    }
                    if(!node.getInputs().contains(foundNode) && node != foundNode) {
                        node.getInputs().add(foundNode);
                    }
                } while(node.getInputs().size() < node.getOperation().getOperationArity());
                node.execute();
                System.out.println("Connected node at " + node + " with operation: " + node.getOperation().getOperationId() + " to node(s): " + node.getInputs());
            }
        }
    }

    public void randomConnectOutputNodes() {
        Random r = new Random();
        System.out.println("================\tConnecting Output nodes\t\t================");
        for (OutputNode node: outputNodes) {
            int randomRow;
            int randomCol;
            switch(r.nextInt(1 + 1)) {
                case 0:
                    randomRow = r.nextInt(numberOfInputs);
                    node.setInput(inputNodes.get(randomRow));
                    break;
                case 1:
                    randomRow = r.nextInt(numberOfRows);
                    randomCol = r.nextInt(numberOfColumns);
                    node.setInput(functionNodes.get(randomCol).get(randomRow));
                    break;
            }
            node.execute();
            System.out.println("Connected output node at " + node + " to node: " + node.getInput());
        }
    }

    private void checkActiveNodes() {
        System.out.println("================\tMarking selected nodes\t\t================");
        for(List<FunctionNode> nodeCol: functionNodes) {
            for(FunctionNode node: nodeCol) {
                node.setActive(false);
            }
        }
        for(OutputNode output: outputNodes) {
            output.setActive(true);
        }
        int sum = 0;
        for(List<FunctionNode> nodeCol: functionNodes) {
            for(FunctionNode node: nodeCol) {
                if(node.isActive()) { sum++; }
            }
        }
        System.out.println(sum + " Functional node(s) are active");
    }

    private void singlePointMutation() {
        Random r = new Random();
        boolean isChangedActive = false;
        do {
            int randomColumn;
            int randomRow;
//            1 => Output Node ; 0 => Function Node
            switch (r.nextInt(1 + 1)) {
                case 0:
                    int randomOutputNode = r.nextInt(numberOfOutputs + 1);
                    randomColumn = r.nextInt(numberOfColumns + 1 - (numberOfColumns - levelsBack)) + (numberOfColumns - levelsBack);
                    randomRow = r.nextInt(numberOfRows + 1);
                    OutputNode toChange = outputNodes.get(randomOutputNode)
                    toChange.setInput(functionNodes.get(randomColumn).get(randomRow));
                    isChangedActive = toChange.isActive();
                    System.out.println("Changing output node " + toChange + "; NEW CONNECTION: " + toChange.getInput());
                    break;
                case 1:
//                  1 => Function, 0 => Connection
                    int changeType = r.nextInt(1 + 1);
                    switch(changeType) {
                        case 0:
                            break;
                        case 1:
                            break;
                    }
                    break;
            }

            int mutationType = r.nextInt(1 + 1);
            int nodeType = r.nextInt(1 + 1);
        } while(!isChangedActive);
    }


    public void initialSetup() {
        generateRandomInputs();
        generateRandomFunctionNodes();
        generateRandomOutputNodes();
        randomConnectFunctionNodes();
        randomConnectOutputNodes();
        checkActiveNodes();
    }
}
