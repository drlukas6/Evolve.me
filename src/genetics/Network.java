package genetics;

import genetics.abstractions.Node;
import genetics.abstractions.NodeType;
import genetics.operations.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private String networkDescriptor = new String();

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

    public Network(String networkDescriptor) {
        String[] networkParts = networkDescriptor.split(" | ");
        String[] inputDescriptors = networkParts[0].split(";");
        String[] functionDescriptors = networkParts[1].split(";");
        String[] outputDescriptors = networkParts[2].split(";");
    }

    public String getNetworkDescriptor() {
        return this.networkDescriptor;
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
                System.out.println("Connected node " + node + " to node(s): " + node.getInputs());
            }
        }
    }

    private void randomConnectOutputNodes() {
        System.out.println("========================\tConnecting Output nodes\t\t========================");
        for (OutputNode node: outputNodes) {
            int randomRow;
            int randomCol;
            randomRow = r.nextInt(numberOfRows);
            randomCol = r.nextInt(numberOfColumns);
            node.setInput(functionNodes.get(randomCol).get(randomRow));
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

    public void generateNetworkDescriptor() {
        String descriptor = new String();
        for(InputNode node: inputNodes) {
            descriptor += node + ";";
        }
        descriptor += "| ";
        for(List<FunctionNode> col: functionNodes) {
            for(FunctionNode node: col) {
                descriptor += node + node.getInputs().toString() + ";";
            }
        }
        descriptor += "| ";
        for(OutputNode node: outputNodes) {
            descriptor += node + node.getInput().toString() + ";";
        }
        this.networkDescriptor = descriptor;
    }

    private void parseNodeDescriptor(String descriptor) {
        NodeType nodeType = getNodeTypeFromDescriptor(descriptor);
        Operation operation = getNodeOperationFromDescriptor(descriptor);
    }

    private NodeType getNodeTypeFromDescriptor(String descriptor) {
        switch (descriptor.substring(0, 1)) {
            case "i":
                return NodeType.INPUT;
            case "f":
                return NodeType.FUNCTION;
            default:
                return NodeType.OUTPUT;
        }
    }

    private Operation getNodeOperationFromDescriptor(String descriptor) {
        return OperationFactory
                .getOperationWithId(
                        Integer.parseInt(
                            descriptor.substring(1, descriptor.indexOf("(")))
                );
    }

    private Map<String, Integer> getParsedCoordinates(String stringCoordinates) {
        Map<String, Integer> coordinates = new HashMap<>();
        String[] coors = stringCoordinates
                .substring(stringCoordinates.indexOf("(") + 1, stringCoordinates.indexOf(")"))
                .split(",");
        coordinates.put("x", Integer.parseInt(coors[0]));
        coordinates.put("y", Integer.parseInt(coors[1]));
        return coordinates;
    }

    private List<Node> getNodeInputsFromDescriptor(String descriptor) {
        List<Node> inputs = new ArrayList<>();
        String rawInputs = descriptor.substring(descriptor.indexOf("[") + 1, descriptor.indexOf("]"));
        int indexOfSeparator = rawInputs.indexOf("),");
        switch(indexOfSeparator) {
            case -1:
                inputs.add(getNodeWithDescriptor(rawInputs));
                break;
            default:
                String[] rawInputsArray = rawInputs.split("\\),");
                for(String unparsedInput: rawInputsArray) {
                    inputs.add(getNodeWithDescriptor(unparsedInput));
                }
                break;
        }
        return inputs;
    }

    Node getNodeWithDescriptor(String singleDescriptor) {
        switch(singleDescriptor.substring(0, 1)) {
            case "i":
                return inputNodes.get(Integer.parseInt(singleDescriptor.substring(4, 5)));
            default:
                String unparsedCoordinates = singleDescriptor.substring(singleDescriptor.indexOf("(") + 1, singleDescriptor.indexOf(")"));
                String[] coors = unparsedCoordinates.split(", ");
                return functionNodes.get(Integer.parseInt(coors[0])).get(Integer.parseInt(coors[1]));
        }
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
        generateNetworkDescriptor();
        System.out.println(networkDescriptor);
        try {
            Files.write(Paths.get("./networkDescriptor.txt"), networkDescriptor.getBytes());
        } catch(Exception e) {
            System.err.println(e);
        }
    }


}
