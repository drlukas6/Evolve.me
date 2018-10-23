package genetics;

import genetics.abstractions.Node;
import genetics.abstractions.NodeType;
import genetics.operations.*;

import java.util.*;

public class FunctionNode extends Node {
    private List<Node> inputs;
    private Operation operation;
    private boolean active;

    public FunctionNode(NodeType nodeType, double output, Map<String, Integer> coordinates, String nodeId) {
        super(nodeType, output, coordinates, nodeId);
        Random r = new Random();
        this.operation = OperationFactory.getRandomOperation();
        this.inputs = new ArrayList<>();
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

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    protected void execute() {
        switch(operation.getOperationArity()) {
            case 1:
                output = operation.execute(inputs.get(0).getOutput());
                break;
            case 2:
                output = operation.execute(inputs.get(0).getOutput(), inputs.get(1).getOutput());
                break;
        }
    }
}
