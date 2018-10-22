package genetics;

import genetics.abstractions.Node;
import genetics.abstractions.NodeType;
import genetics.operations.*;

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
    private Random r;

    public Network(int numberOfRows, int numberOfColumns, int numberOfInputs, int numberOfOutputs, int levelsBack) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.inputNodes = new ArrayList<>();
        this.functionNodes = new ArrayList<>();
        this.outputNodes = new ArrayList<>();
        this.numberOfInputs = numberOfInputs;
        this.numberOfOutputs = numberOfOutputs;
        this.levelsBack = levelsBack;
        this.r = new Random();
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
        for(List<FunctionNode> row: functionNodes) {
            for(FunctionNode node: row) {
                mutateFunctionNodeInputs(node, false);
                node.execute();
                System.out.println("Connected node at " + node + " with operation: " + node.getOperation().getOperationId() + " to node(s): " + node.getInputs());
            }
        }
    }

    public void randomConnectOutputNodes() {
        System.out.println("================\tConnecting Output nodes\t\t================");
        for (OutputNode node: outputNodes) {
            int randomRow;
            int randomCol;
//            Choosing a random number between 0 and number of function nodes + number of input nodes to make connecting output nodes proportional
            switch(r.nextInt((numberOfRows * numberOfColumns + numberOfInputs) + numberOfInputs)) {
                case 0:
                    randomRow = r.nextInt(numberOfInputs);
                    node.setInput(inputNodes.get(randomRow));
                    break;
                default:
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
        System.out.println("================\tSingle point mutation\t\t================");
        boolean isChangedActive = false;
        do {
            if(r.nextInt((numberOfColumns * numberOfRows + numberOfOutputs) + 1) > numberOfRows*numberOfColumns) {
                isChangedActive = mutateRandomOutputNode();
            }
            else {
                isChangedActive = mutateRandomFunctionNode();
            }
        } while(!isChangedActive);
        System.out.println("Single point mutation completed");
    }

    private boolean mutateRandomFunctionNode() {
        int randomColumn = r.nextInt(numberOfColumns);
        int randomRow = r.nextInt(numberOfRows);
        FunctionNode node = functionNodes.get(randomColumn).get(randomRow);
        switch (r.nextInt( 1 + 1)) {
            case 0:
                return mutateFunctionNodeInputs(node, true);
            default:
                return mutateFunctionNodeOperation(node);
        }
    }

    private boolean mutateFunctionNodeInputs(FunctionNode node, boolean shouldOutput) {
        int randomRow;
        int randomColumn;
        node.getInputs().clear();
        do {
            int maxColumn = getMaxColumn(node);
            int minColumn = getMinColumn(node);
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
        if(shouldOutput) {
            System.out.println("Changing Function node " + node + "; NEW CONNECTION(S): " + node.getInputs());
        }
        return node.isActive();
    }

    private boolean mutateFunctionNodeOperation(FunctionNode node) {
        int oprationId;
        boolean foundTargetArity = false;
        do {
            oprationId = r.nextInt(Operation.OPERATION_MAX + 1 - Operation.OPERATION_MIN) + Operation.OPERATION_MIN;
            Operation randomOperation = new AddOperation();
            switch (oprationId) {
                case 0:
                    randomOperation = new AddOperation();
                    break;
                case 1:
                    randomOperation = new SubstractOperation();
                    break;
                case 2:
                    randomOperation = new MultiplyOperation();
                    break;
                case 3:
                    randomOperation = new DivideOperation();
                    break;
                case 4:
                    randomOperation = new NegativeOperation();
                    break;
                case 5:
                    randomOperation = new SinOperation();
                    break;
            }
            if(randomOperation.getOperationArity() == node.getOperation().getOperationArity() && randomOperation.getOperationId() != node.getOperation().getOperationId()) {
                node.setOperation(randomOperation);
                foundTargetArity = true;
            }
        } while (!foundTargetArity);
        System.out.println("Changing Function node " + node + "; NEW OPERATION: " + node.getOperation().getOperationId());
        return node.isActive();
    }

    private boolean mutateRandomOutputNode() {
        int randomOutputNode = r.nextInt(numberOfOutputs + 1);
        int maxColumn = numberOfColumns - 1;
        int minColumn = maxColumn - levelsBack;
        int randomColumn = r.nextInt(maxColumn + 1 - minColumn) + minColumn;
        int randomRow = r.nextInt(numberOfRows + 1);
        OutputNode node = outputNodes.get(randomOutputNode);
        node.setInput(functionNodes.get(randomColumn).get(randomRow));
        System.out.println("Changing output node " + node + "; NEW CONNECTION: " + node.getInput());
        return node.isActive();
    }

    private int getMinColumn(Node from) {
        return from.getCoordinates().get("x") - 1 - levelsBack;
    }

    private int getMaxColumn(Node from) {
        return from.getCoordinates().get("x") - 1;
    }


    public void initialSetup() {
        generateRandomInputs();
        generateRandomFunctionNodes();
        generateRandomOutputNodes();
        randomConnectFunctionNodes();
        randomConnectOutputNodes();
        checkActiveNodes();
        singlePointMutation();
    }
}
