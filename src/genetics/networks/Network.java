package genetics.networks;

import constants.Coordinates;
import genetics.nodes.*;
import genetics.operations.*;

import java.util.*;

/**
 * Network class that defines all variables and methods that make it function
 */
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
    private int passThrough = 0;
    private double fitness = 999.9;
    private List<Double> calculatedOutputs = new ArrayList<>();
    private String networkDescriptor = "";
    private List<Integer> givenOperations;


    /**
     * Network constructor used by the network factory to create a random network
     *
     * @param numberOfRows number of rows of function nodes
     * @param numberOfColumns number of columns of function nodes
     * @param numberOfInputs number of inputs
     * @param numberOfOutputs number of outputs
     * @param levelsBack levels back
     * @param outputValues correct output values
     * @param inputValues input values
     * @param givenOperations operations that the network is allowed to use
     */
    Network(int numberOfRows, int numberOfColumns,
            int numberOfInputs, int numberOfOutputs,
            int levelsBack, List<Double> outputValues,
            List<List<Double>> inputValues, List<Integer> givenOperations) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.numberOfInputs = numberOfInputs;
        this.numberOfOutputs = numberOfOutputs;
        this.levelsBack = levelsBack;
        this.outputValues = outputValues;
        this.inputValues = inputValues;
        this.givenOperations = givenOperations;
    }


    /**
     * Network constructor used by the network factory to create a network from existing resources (eg. network descriptor string)
     *
     * @param numberOfRows number of rows of function nodes
     * @param numberOfColumns number of columns of function nodes
     * @param numberOfInputs number of inputs
     * @param numberOfOutputs number of outputs
     * @param levelsBack levels back
     * @param inputNodes existing input nodes
     * @param functionNodes existing function nodes
     * @param outputNodes existing output nodes
     * @param inputValues input values
     * @param outputValues correct output values
     * @param givenOperations operations that the network is allowed to use
     */
    Network(int numberOfRows, int numberOfColumns,
            int numberOfInputs, int numberOfOutputs,
            int levelsBack, List<InputNode> inputNodes,
            List<List<FunctionNode>> functionNodes, List<OutputNode> outputNodes,
            List<List<Double>> inputValues, List<Double> outputValues,
            List<Integer> givenOperations) {
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
        this.givenOperations = givenOperations;
    }

    public String getNetworkDescriptor() {
        return this.networkDescriptor;
    }

    public double getFitness() {
        return fitness;
    }


    /**
     * Creates empty input nodes
     */
    private void generateInputNodes() {
        for(int i = 0; i < numberOfInputs; i++) {
            Map<String, Integer> coordinates = new HashMap<>();
            coordinates.put(Coordinates.x, 0);
            coordinates.put(Coordinates.y, i);
            InputNode node = new InputNode(NodeType.INPUT, 0.0, coordinates, "i");
            inputNodes.add(node);
        }
    }


    private void populateInputNodes() {
        inputNodes.forEach(this::populateInputNode);
    }

    /**
     * Fetches the correct input value corresponding to the nodes positions
     * and assigns that value to the node
     *
     * @param inputNode node to populate with a value
     */
    private void populateInputNode(Node inputNode) {
        Map<String, Integer> nodeCoordinates = inputNode.getCoordinates();
        inputNode.setOutput(inputValues.get(nodeCoordinates.get("y")).get(passThrough));
    }

    private void generateRandomFunctionNodes() {
        for (int i = 0; i < numberOfColumns; i++) {
            functionNodes.add(new ArrayList<>());
            for (int j = 0; j < numberOfRows; j++) {
                Map<String, Integer> coordinates = new HashMap<>();
                coordinates.put(Coordinates.x, i);
                coordinates.put(Coordinates.y, j);
                functionNodes.get(i).add(new FunctionNode(coordinates, givenOperations));
            }
        }
    }

    private void generateRandomOutputNodes() {
        for (int i = 0; i < numberOfOutputs; i++) {
            Map<String, Integer> coordinates = new HashMap<>();
            coordinates.put("x", 0);
            coordinates.put("y", i);
            outputNodes.add(new OutputNode(coordinates));
        }
    }


    private void randomConnectFunctionNodes() {
        for(List<FunctionNode> col: functionNodes) {
            for(FunctionNode node: col) {
                mutateFunctionNodeInputs(node);
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

    /**
     * Calls node.execute() for each function node which calculates its output value to be used
     * in later columns or by output nodes
     */
    private void executeFunctionNodes() {
        for(List<FunctionNode> col: functionNodes) {
            for(FunctionNode node: col) {
                node.execute();
            }
        }
    }

    /**
     * Calls node.execute() for each output node which is later used to populate
     * network calculated output values
     */
    private void executeOutputNodes() {
        for(OutputNode node: outputNodes) {
            node.execute();
        }
    }

    /**
     * Sets node.active field for each function node depending if it is used in output calculation
     * Every node that gets its field changed automatically calls the same method on its parent node to do the same thing
     */
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

    /**
     * Mutates nodes until it mutates an active node
     */
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
        // [0, 2> => {0, 1}
        switch (r.nextInt( 2)) {
            case 0:
                return mutateFunctionNodeInputs(node);
            default:
                return mutateFunctionNodeOperation(node);
        }
    }

    private boolean mutateFunctionNodeInputs(FunctionNode node) {
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
            Operation randomOperation = OperationFactory.getRandomOperationFromGiven(givenOperations);
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

    /**
     * @param node node to check value for
     * @return min. column node can connect to based on the levels back value
     */
    private int getMinColumn(Node node) {
        return node.getCoordinates().get("x") - 1 - levelsBack;
    }

    private int getMaxColumn(Node node) {
        return node.getCoordinates().get("x") - 1;
    }

    /**
     * Calculates fitness based on calculated and given output values
     */
    private void calculateFitness() {
        double sum = 0.0;
        for(int i = 0; i < outputValues.size(); i++) {
            sum += Math.pow((outputValues.get(i) - calculatedOutputs.get(i)), 2);
        }
        fitness =  Math.sqrt(sum / (outputValues.size() - 2));
    }

    /**
     * completes one pass through the network with all input values in order
     */
    private void completeEpoch() {
        while(passThrough < outputValues.size()) {
            populateInputNodes();
            executeFunctionNodes();
            executeOutputNodes();
            calculatedOutputs.add(outputNodes.get(0).getOutput());
            ++passThrough;
        }
        passThrough = 0;
    }

    /**
     * Creates a network descriptor
     */
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

    // TODO: add new mutations
    public void mutateNetwork() {
        singlePointMutation();
    }

    /**
     * Combines all methods necessary for a one pass through the network.
     * Checks active nodes, completes an epoch and calculates fitness and than generates the network descriptor
     */
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

    /**
     * Prints out values compared to the given ones and a format for loading in mac grapher app
     */
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

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public int getNumberOfInputs() {
        return numberOfInputs;
    }

    public int getNumberOfOutputs() {
        return numberOfOutputs;
    }
}