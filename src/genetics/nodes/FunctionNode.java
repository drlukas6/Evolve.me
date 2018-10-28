package genetics.nodes;

import genetics.nodes.Node;
import genetics.nodes.NodeType;
import genetics.operations.*;

import java.util.*;

public class FunctionNode extends Node {
    private List<Node> inputs = new ArrayList<>();
    private Operation operation;
    private boolean active;

    public FunctionNode(NodeType nodeType, double output, Map<String, Integer> coordinates, String nodeId) {
        super(nodeType, output, coordinates, nodeId);
        Random r = new Random();
        this.operation = OperationFactory.getRandomOperation();
    }

    public FunctionNode(Map<String, Integer> coordinates) {
        super(NodeType.FUNCTION, 0.0, coordinates, "f");
        Random r = new Random();
        this.operation = OperationFactory.getRandomOperation();
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
        return nodeId + operation.getOperationId() + "(" + coordinates.get("x") + ", " + coordinates.get("y") + ")";
    }
}
