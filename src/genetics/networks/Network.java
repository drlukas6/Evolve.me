package genetics.networks;

import genetics.nodes.*;
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
    private List<List<Double>> inputValues;
    private int generation = 0;
    private int passThrough = 0;
    private double fitness = 999.9;
    private List<Double> calculatedOutputs = new ArrayList<>();
    private String networkDescriptor = "";

    public Network(int numberOfRows, int numberOfColumns, int numberOfInputs,
                   int numberOfOutputs, int levelsBack,
                   List<Double> outputValues, List<List<Double>> inputValues) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.numberOfInputs = numberOfInputs;
        this.numberOfOutputs = numberOfOutputs;
        this.levelsBack = levelsBack;
        this.outputValues = outputValues;
        this.inputValues = inputValues;
    }

    public Network(int numberOfRows, int numberOfColumns,
                   int numberOfInputs, int numberOfOutputs,
                   int levelsBack, List<InputNode> inputNodes,
                   List<List<FunctionNode>> functionNodes, List<OutputNode> outputNodes,
                   List<List<Double>> inputValues, List<Double> outputValues) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.numberOfInputs = numberOfInputs;
        this.numberOfOutputs = numberOfOutputs;
        this.levelsBack = levelsBack;
        this.inputNodes = inputNodes;
        this.functionNodes = functionNodes;
        this.outputNodes = outputNodes;
        this.inputValues = inputValues;
        this.outputValues = outputValues;
    }

    public String getNetworkDescriptor() {
        return this.networkDescriptor;
    }

    public double getFitness() {
        return fitness;
    }

    private void generateInputNodes() {
        for(int i = 0; i < numberOfInputs; i++) {
            Map<String, Integer> coordinates = new HashMap<>();
            coordinates.put("x", 0);
            coordinates.put("y", i);
            InputNode node = new InputNode(NodeType.INPUT, 0.0, coordinates, "i");
            inputNodes.add(node);
        }
    }

    private void populateInputNodes(boolean isRandom) {

        inputNodes.stream().forEach(node -> populateInputNode(isRandom, node));
    }

    private void populateInputNode(boolean isRandom, Node inputNode) {
        if(isRandom)
            inputNode.setOutput(r.nextDouble());
        else {
            Map<String, Integer> nodeCoordinates = inputNode.getCoordinates();
            inputNode.setOutput(inputValues.get(nodeCoordinates.get("y")).get(passThrough));
        }
    }

    private void generateRandomFunctionNodes() {
        for (int i = 0; i < numberOfColumns; i++) {
            functionNodes.add(new ArrayList<>());
            for (int j = 0; j < numberOfRows; j++) {
                Map<String, Integer> coordinates = new HashMap<>();
                coordinates.put("x", i);
                coordinates.put("y", j);
                functionNodes.get(i).add(new FunctionNode(NodeType.FUNCTION, 0.0, coordinates, "f"));
            }
        }
    }

    private void generateRandomOutputNodes() {
        for (int i = 0; i < numberOfOutputs; i++) {
            Map<String, Integer> coordinates = new HashMap<>();
            coordinates.put("x", 0);
            coordinates.put("y", i);
            outputNodes.add(new OutputNode(NodeType.OUTPUT, 0.0, coordinates, "o"));
        }
    }


    private void randomConnectFunctionNodes() {
        for(List<FunctionNode> col: functionNodes) {
            for(FunctionNode node: col) {
                mutateFunctionNodeInputs(node, false);
            }
        }
    }

    private void randomConnectOutputNodes() {
        for (OutputNode node: outputNodes) {
            int randomRow;
            int randomCol;
            randomRow = r.nextInt(numberOfRows);
            randomCol = r.nextInt(numberOfColumns);
            node.setInput(functionNodes.get(randomCol).get(randomRow));
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

    public void checkActiveNodes() {
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
        boolean isChangedActive;
        do {
            if(r.nextInt((numberOfColumns * numberOfRows + numberOfOutputs) + 1) > numberOfRows*numberOfColumns) {
                isChangedActive = mutateRandomOutputNode();
            }
            else {
                isChangedActive = mutateRandomFunctionNode();
            }
        } while(!isChangedActive);
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
        return node.isActive();
    }

    private boolean mutateRandomOutputNode() {
        int randomOutputNode = r.nextInt(numberOfOutputs);
        int randomColumn = r.nextInt(numberOfColumns);
        int randomRow = r.nextInt(numberOfRows);
        OutputNode node = outputNodes.get(randomOutputNode);
        node.setInput(functionNodes.get(randomColumn).get(randomRow));
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
    }

    private void completeEpoch() {
        while(passThrough < outputValues.size()) {
            populateInputNodes(false);
            executeFunctionNodes();
            executeOutputNodes();
            calculatedOutputs.add(outputNodes.get(0).getOutput());
            ++passThrough;
        }
        passThrough = 0;
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

    private void generateNetworkDescriptor() {
        StringBuilder descriptor = new StringBuilder();
        for(InputNode node: inputNodes) {
            descriptor.append(node).append(";");
        }
        descriptor.append("-");
        for(List<FunctionNode> col: functionNodes) {
            for(FunctionNode node: col) {
                descriptor.append(node).append(node.getInputs().toString()).append(";");
            }
        }
        descriptor.append("-");
        for(OutputNode node: outputNodes) {
            descriptor.append(node).append(node.getInput().toString()).append(";");
        }
        descriptor.append("-").append(numberOfColumns).append("-").append(numberOfRows).append("-").append(levelsBack);
        this.networkDescriptor = descriptor.toString();
    }



    public void executeInitialNetwork() {
        generateInputNodes();
        generateRandomFunctionNodes();
        randomConnectFunctionNodes();
        generateRandomOutputNodes();
        randomConnectOutputNodes();
        checkActiveNodes();
        completeEpoch();
        calculateFitness();
        generateNetworkDescriptor();
        System.out.println("======================================================");
        System.out.println(networkDescriptor);
        System.out.println(fitness);
        System.out.println("------------------------------------------------------");
    }

    public void mutateNetwork() {
        singlePointMutation();
    }

    public void executeNetwork() {
        checkActiveNodes();
        completeEpoch();
        calculateFitness();
        generateNetworkDescriptor();
        System.out.println("======================================================");
        System.out.println(networkDescriptor);
        System.out.println(fitness);
        System.out.println("------------------------------------------------------");
    }

    public void testNetworkPerformances() {
        completeEpoch();
        System.out.println("GA-OUTPUT\tREAL-OUTPUT\tDIFFERENCE");
        for(int i = 0; i < inputValues.get(0).size(); i++) {
            System.out.format("%7.3f\t\t%7.3f\t\t%7.3f\n", calculatedOutputs.get(i), outputValues.get(i), Math.abs(outputValues.get(i) - calculatedOutputs.get(i)));
        }
        System.out.println("\n\nFORMAT FOR LOADING IN GRAPHER");
        for(int i = 0; i < inputValues.get(0).size(); i++) {
            for(List<Double> dimension: inputValues) {
                System.out.format("%7.3f\t", dimension.get(i));
            }
            System.out.format("%7.3f\n", calculatedOutputs.get(i));
        }
    }

    public String getStats() {
        return "NETWORK: \n" + getNetworkDescriptor() + "\nFITNESS:" + fitness + "\n";
    }



}