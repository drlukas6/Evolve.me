package genetics.networks;

import genetics.nodes.*;
import genetics.operations.Operation;
import genetics.operations.OperationFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkFactory {
    private  List<InputNode> inputNodes = new ArrayList<>();
    private  List<List<FunctionNode>> functionNodes = new ArrayList<>();
    private  List<OutputNode> outputNodes = new ArrayList<>();

    private  List<Double> outputValues;
    private  List<List<Double>> inputValues;
    private int numberOfRows;
    private int numberOfColumns;
    private int numberOfInputs;
    private int numberOfOutputs;
    private int levelsBack;
    private List<Integer> givenOperations;

    public NetworkFactory(List<Double> outputValues, List<List<Double>> inputValues,
                          int numberOfRows, int numberOfColumns,
                          int numberOfInputs, int numberOfOutputs,
                          int levelsBack, List<Integer> givenOperations) {
        this.outputValues = outputValues;
        this.inputValues = inputValues;
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.numberOfInputs = numberOfInputs;
        this.numberOfOutputs = numberOfOutputs;
        this.levelsBack = levelsBack;
        this.givenOperations = givenOperations;
    }

    public Network createRandomNetwork() {
        return new Network(numberOfRows, numberOfColumns, numberOfInputs, numberOfOutputs, levelsBack, outputValues, inputValues, givenOperations);
    }

    public Network createNetworkWithDescriptor(String networkDescriptor) {
        inputNodes = new ArrayList<>();
        functionNodes = new ArrayList<>();
        outputNodes = new ArrayList<>();

        String[] networkParts = networkDescriptor.split("-");

        String[] unparsedInputs = networkParts[0].split(";");
        int numberOfInputs = unparsedInputs.length;

        String[] unparsedFunctionNodes = networkParts[1].split(";");

        String[] unparsedOutputs = networkParts[2].split(";");
        int numberOfOutputs = unparsedOutputs.length;

        int numberOfRows = Integer.parseInt(networkParts[3]);
        int numberOfColumns = Integer.parseInt(networkParts[4]);
        int levelsBack = Integer.parseInt(networkParts[5]);

        for(int i = 0; i < numberOfInputs; i++) {
            Map<String, Integer> inputNodeCoordinates = new HashMap<>();
            inputNodeCoordinates.put("x", 0);
            inputNodeCoordinates.put("y", i);
            inputNodes.add(new InputNode(inputNodeCoordinates));
        }

        for(int i = 0; i < numberOfColumns; i ++) {
            functionNodes.add(new ArrayList<>());
        }

        parseFunctionNodes(unparsedFunctionNodes);
        parseOutputNodes(unparsedOutputs);

        return new Network(numberOfRows, numberOfColumns,
                numberOfInputs, numberOfOutputs,
                levelsBack, inputNodes,
                functionNodes, outputNodes,
                inputValues, outputValues, givenOperations);
    }

    public Network combineNetworks(Network lhs, Network rhs) {
        String networkDescriptorLhs = lhs.getNetworkDescriptor();
        String[] lhsNetworkParts = networkDescriptorLhs.split("-");
        String lhsInputs = lhsNetworkParts[0];
        String[] lhsFunctions = lhsNetworkParts[1].split(";");
        String[] lhsOutputs = lhsNetworkParts[2].split(";");
        String networkDescriptorRhs = rhs.getNetworkDescriptor();
        String[] rhsNetworkParts = networkDescriptorRhs.split("-");
        String[] rhsFunctions = rhsNetworkParts[1].split(";");
        String[] rhsOutputs = rhsNetworkParts[2].split(";");

        int splitter =lhsFunctions.length / 2;

        StringBuilder descriptor = new StringBuilder(lhsInputs + "-");

        for(int i = 0; i < splitter; i++) {
            descriptor.append(lhsFunctions[i]).append(";");
        }
        for(int i = splitter; i < rhsFunctions.length; i++) {
            descriptor.append(rhsFunctions[i]).append(";");
        }

        descriptor.append("-");

        if(lhs.getFitness() < rhs.getFitness()) {
            for (String lhsOutput : lhsOutputs) descriptor.append(lhsOutput).append(";");
        }
        else {
            for (String rhsOutput : rhsOutputs) descriptor.append(rhsOutput).append(";");
        }

        descriptor.append("-").append(lhsNetworkParts[3]).append("-").append(lhsNetworkParts[4]).append("-").append(lhsNetworkParts[5]);

        return createNetworkWithDescriptor(descriptor.toString());
    }

    private void parseOutputNodes(String[] unparsedOutputNodes) {
        for(String unparsedOutputNode: unparsedOutputNodes) {
            String shortInputDescription = unparsedOutputNode.substring(unparsedOutputNode.indexOf("f"));
            Map<String, Integer> coordinates = getNodeCoordinates(unparsedOutputNode);
            OutputNode outputNode = new OutputNode(coordinates);
            Node outputInput = getNodeWithDescription(shortInputDescription);
            outputNode.setInput(outputInput);
            outputNodes.add(outputNode);
        }
    }

    private void parseFunctionNodes(String[] unparsedFunctionNodes) {
        for(String unparsedFunctionNode: unparsedFunctionNodes) {
            String shortNodeDescriptor = unparsedFunctionNode.substring(0, unparsedFunctionNode.indexOf(")") + 1);
            Operation nodeOperation = getNodeOperation(unparsedFunctionNode);
            Map<String, Integer> coordinates = getNodeCoordinates(shortNodeDescriptor);
            List<Node> nodeInputs = getNodeInputs(unparsedFunctionNode);
            FunctionNode node = new FunctionNode(coordinates, givenOperations);
            node.setOperation(nodeOperation);
            node.setInputs(nodeInputs);
            functionNodes.get(coordinates.get("x")).add(node);
        }
    }

    private Operation getNodeOperation(String nodeDescriptor) {
        int operationId = Integer.parseInt(nodeDescriptor.substring(1, 2));
        return OperationFactory.getOperationWithId(operationId);
    }

//    shortNodeDescriptor = f2(0, 0)
    private Map<String, Integer> getNodeCoordinates(String shortNodeDescriptor) {
        Map<String, Integer> coordinates = new HashMap<>();
        String[] unparsedCoordinates = shortNodeDescriptor.substring(shortNodeDescriptor.indexOf("(") + 1, shortNodeDescriptor.indexOf(")")).split(", ");
        coordinates.put("x", Integer.parseInt(unparsedCoordinates[0]));
        coordinates.put("y", Integer.parseInt(unparsedCoordinates[1]));
        return coordinates;
    }

    private List<Node> getNodeInputs(String nodeDescriptor) {
        List<Node> nodeInputs = new ArrayList<>();
        String unparsedNodeInputs = nodeDescriptor.substring(nodeDescriptor.indexOf("[") + 1, nodeDescriptor.indexOf("]"));
        int indexOfSeparator = unparsedNodeInputs.indexOf("),");
        if(indexOfSeparator == -1){
            Node nodeInput = getNodeWithDescription(unparsedNodeInputs);
            nodeInputs.add(nodeInput);
        }
        else {
            String nodeInput1 = unparsedNodeInputs.substring(unparsedNodeInputs.indexOf("[") + 1, unparsedNodeInputs.indexOf("),") + 1);
            String nodeInput2 = unparsedNodeInputs.substring(unparsedNodeInputs.indexOf("),") + 3);
            String[] splitNodeInputs = {nodeInput1, nodeInput2};
            for (String shortNodeDescription: splitNodeInputs) {
                Node nodeInput = getNodeWithDescription(shortNodeDescription);
                nodeInputs.add(nodeInput);
            }
        }
        return nodeInputs;
    }

    private Node getNodeWithDescription(String shortNodeDescription) {
        Map<String, Integer> inputNodeCoordinates = getNodeCoordinates(shortNodeDescription);
        Node node;
        switch (shortNodeDescription.substring(0, 1)) {
            case "i":
                node = inputNodes.get(inputNodeCoordinates.get("y"));
                break;
            default:
                node = functionNodes.get(inputNodeCoordinates.get("x")).get(inputNodeCoordinates.get("y"));
                break;
        }
        return node;
    }

}
