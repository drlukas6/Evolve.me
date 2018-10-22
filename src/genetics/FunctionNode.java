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
        int operationId  =r.nextInt((Operation.OPERATION_MAX - Operation.OPERATION_MIN) + 1) + Operation.OPERATION_MIN;
        switch (operationId) {
            case 0:
                this.operation = new AddOperation();
                break;
            case 1:
                this.operation = new SubstractOperation();
                break;
            case 2:
                this.operation = new MultiplyOperation();
                break;
            case 3:
                this.operation = new DivideOperation();
                break;
            case 4:
                this.operation = new NegativeOperation();
                break;
        }
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
