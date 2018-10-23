package genetics;

import genetics.abstractions.Node;
import genetics.abstractions.NodeType;
import genetics.operations.*;

import java.util.*;

public class Network {
    private List<InputNode> inputNodes = new ArrayList<>();
    private List<List<FunctionNode>> functionNodes = new ArrayList<>();
    private List<OutputNode> outputNodes = new ArrayList<>();
    private int numberOfRows;
    private int numberOfColumns;
    private int numberOfInputs;
    private int numberOfOutputs;
    private int levelsBack;
    private Random r = new Random();
    private List<Double> outputValues;
    private List<Double> inputValues;
    private int generation = 0;
    private int passThrough = 0;
    private double fitness = 999.9;
    private List<Double> calculatedOutputs = new ArrayList<>();

    public Network(int numberOfRows, int numberOfColumns, int numberOfInputs,
                   int numberOfOutputs, int levelsBack) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.numberOfInputs = numberOfInputs;
        this.numberOfOutputs = numberOfOutputs;
        this.levelsBack = levelsBack;
    }

    public Network(int numberOfRows, int numberOfColumns, int numberOfInputs,
                   int numberOfOutputs, int levelsBack,
                   List<Double> outputValues, List<Double> inputValues) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.numberOfInputs = numberOfInputs;
        this.numberOfOutputs = numberOfOutputs;
        this.levelsBack = levelsBack;
        this.outputValues = outputValues;
        this.inputValues = inputValues;
    }

    private void generateInputNodes() {
        for(int i = 0; i < numberOfInputs; i++) {
            Map<String, Integer> coordinates = new HashMap<>();
            coordinates.put("x", 0);
            coordinates.put("y", i);
            InputNode node = new InputNode(NodeType.INPUT, 0.0, coordinates, "i");
            inputNodes.add(node);
        }
        System.out.println("========================\tGenerated " + inputNodes.size() + " Input nodes\t\t========================");
        System.out.println(inputNodes);
    }

    private void populateInputNodes(boolean isRandom) {
        inputNodes.stream().forEach(node -> {
            populateInputNode(isRandom, node);
        });
    }

    private void populateInputNode(boolean isRandom, Node inputNode) {
        if(isRandom) {
            inputNode.setOutput(r.nextDouble());
        }
        else {
            inputNode.setOutput(inputValues.get(passThrough));
        }
    }

    private void generateRandomFunctionNodes() {
        int numberOfNodes = 0;
        for (int i = 0; i < numberOfColumns; i++) {
            functionNodes.add(new ArrayList<>());
            for (int j = 0; j < numberOfRows; j++) {
                numberOfNodes++;
                Map<String, Integer> coordinates = new HashMap<>();
                coordinates.put("x", i);
                coordinates.put("y", j);
                functionNodes.get(i).add(new FunctionNode(NodeType.FUNCTION, 0.0, coordinates, "f"));
            }
        }
        System.out.println("========================\tGenerated " + numberOfNodes + " Function nodes\t========================");
        System.out.println(functionNodes);
    }

    private void generateRandomOutputNodes() {
        for (int i = 0; i < numberOfOutputs; i++) {
            Map<String, Integer> coordinates = new HashMap<>();
            coordinates.put("x", 0);
            coordinates.put("y", i);
            outputNodes.add(new OutputNode(NodeType.OUTPUT, 0.0, coordinates, "o"));
        }
        System.out.println("========================\tGenerated " + outputNodes.size() + " Output nodes\t========================");
        System.out.println(outputNodes);
    }


    private void randomConnectFunctionNodes() {
        System.out.println("========================\tConnecting function nodes\t========================");
        for(List<FunctionNode> col: functionNodes) {
            for(FunctionNode node: col) {
                mutateFunctionNodeInputs(node, false);
//                node.execute();
                System.out.println("Connected node at " + node + " with operation: " + node.getOperation().getOperationId() + " to node(s): " + node.getInputs());
            }
        }
    }

    private void randomConnectOutputNodes() {
        System.out.println("========================\tConnecting Output nodes\t\t========================");
        for (OutputNode node: outputNodes) {
            int randomRow;
            int randomCol;
            int numberOfFunctionNodes = numberOfRows * numberOfColumns;
//            Choosing a random number between 0 and number of function nodes + number of input nodes to make connecting output nodes proportional
//            if(r.nextInt((numberOfFunctionNodes + numberOfInputs) + numberOfInputs) > numberOfFunctionNodes) {
//                randomRow = r.nextInt(numberOfInputs);
//                node.setInput(inputNodes.get(randomRow));
//            }
//            else {
//                randomRow = r.nextInt(numberOfRows);
//                randomCol = r.nextInt(numberOfColumns);
//                node.setInput(functionNodes.get(randomCol).get(randomRow));
//            }
            randomRow = r.nextInt(numberOfRows);
            randomCol = r.nextInt(numberOfColumns);
            node.setInput(functionNodes.get(randomCol).get(randomRow));
//            node.execute();
            System.out.println("Connected output node at " + node + " to node: " + node.getInput());
        }
    }

    private void executeFunctionNodes() {
        for(List<FunctionNode> col: functionNodes) {
            for(FunctionNode node: col) {
                node.execute();
            }
        }
    }

    private void executeOutputNodes() {
        for(OutputNode node: outputNodes) {
            node.execute();
        }
    }

    private void checkActiveNodes() {
        System.out.println("========================\tMarking selected nodes\t\t========================");
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
        System.out.println("========================\tSingle point mutation\t\t========================");
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
                randomRow = r.nextInt(inputNodes.size());
                foundNode = inputNodes.get(randomRow);
            }
            else {
                randomRow = r.nextInt(numberOfRows);
                foundNode = functionNodes.get(randomColumn).get(randomRow);
            }
            if(node != foundNode) {
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
            Operation randomOperation = OperationFactory.getOperationWithId(oprationId);
            if(randomOperation.getOperationArity() == node.getOperation().getOperationArity() && randomOperation.getOperationId() != node.getOperation().getOperationId()) {
                node.setOperation(randomOperation);
                foundTargetArity = true;
            }
        } while (!foundTargetArity);
        System.out.println("Changing Function node " + node + "; NEW OPERATION: " + node.getOperation().getOperationId());
        return node.isActive();
    }

    private boolean mutateRandomOutputNode() {
        int randomOutputNode = r.nextInt(numberOfOutputs);
        int maxColumn = numberOfColumns - 1;
        int minColumn = maxColumn - levelsBack;
        int randomColumn = r.nextInt(maxColumn + 1 - minColumn) + minColumn;
        int randomRow = r.nextInt(numberOfRows);
        OutputNode node = outputNodes.get(randomOutputNode);
        node.setInput(functionNodes.get(randomColumn).get(randomRow));
        System.out.println("Changing output node " + node + "; NEW CONNECTION: " + node.getInput());
        return node.isActive();
    }

    private int getMinColumn(Node node) {
        return node.getCoordinates().get("x") - 1 - levelsBack;
    }

    private int getMaxColumn(Node node) {
        return node.getCoordinates().get("x") - 1;
    }

    private void calculateFitness() {
        double sum = 0.0;
        for(int i = 0; i < outputValues.size(); i++) {
            sum += Math.pow((outputValues.get(i) - calculatedOutputs.get(i)), 2);
        }
        fitness =  Math.sqrt(sum / (outputValues.size() - 2));
        System.out.println("========================\tFitness calculation\t\t\t========================");
        System.out.println("FITNESS: " + fitness);
    }

    private void completeEpoch() {
        while(passThrough < inputValues.size()) {
            populateInputNodes(false);
            executeFunctionNodes();
            executeOutputNodes();
            calculatedOutputs.add(outputNodes.get(0).getOutput());
            ++passThrough;
        }
    }


    public void randomInitialSetup() {
        generateInputNodes();
        populateInputNodes(true);
        generateRandomFunctionNodes();
        generateRandomOutputNodes();
        randomConnectFunctionNodes();
        randomConnectOutputNodes();
        executeFunctionNodes();
        executeOutputNodes();
        checkActiveNodes();
        singlePointMutation();
    }


    public void executeNetwork() {
        generateInputNodes();
        generateRandomFunctionNodes();
        randomConnectFunctionNodes();
        generateRandomOutputNodes();
        randomConnectOutputNodes();
        checkActiveNodes();
        completeEpoch();
        calculateFitness();
    }
}
