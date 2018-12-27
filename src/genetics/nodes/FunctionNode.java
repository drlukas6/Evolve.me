package genetics.nodes;

import constants.ConstCoordinates;
import genetics.operations.*;

import java.util.*;

public class FunctionNode extends Node {
    private List<Node> inputs = new ArrayList<>();
    private Operation operation;
    private boolean active;
    Random r = new Random();

    public FunctionNode(NodeType nodeType, double output, Map<String, Integer> coordinates, String nodeId, List<Integer> givenOperations) {
        super(nodeType, output, coordinates, nodeId);
        this.operation = OperationFactory.getOperationWithId(givenOperations.get(r.nextInt(givenOperations.size())));
    }

    public FunctionNode(Map<String, Integer> coordinates, List<Integer> givenOperations) {
        super(NodeType.FUNCTION, 0.0, coordinates, "f");
        this.operation = OperationFactory.getOperationWithId(givenOperations.get(r.nextInt(givenOperations.size())));
    }


    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
        for(Node inputNode: inputs) {
            inputNode.setActive(active);
        }
    }

    public List<Node> getInputs() {
        return inputs;
    }

    public void setInputs(List<Node> inputs) {
        this.inputs = inputs;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public void execute() {
        switch(operation.getOperationArity()) {
            case 1:
                output = operation.execute(inputs.get(0).getOutput());
                break;
            case 2:
                output = operation.execute(inputs.get(0).getOutput(), inputs.get(1).getOutput());
                break;
        }
    }

    @Override
    public String toString() {
        return nodeId + operation.getOperationId() + "(" + coordinates.get(ConstCoordinates.x) + ", " + coordinates.get(ConstCoordinates.y) + ")";
    }
}
