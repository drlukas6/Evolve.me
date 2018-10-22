package genetics;

import genetics.abstractions.Node;
import genetics.abstractions.NodeType;
import genetics.operations.*;

import java.util.*;

public class FunctionNode extends Node {
    private List<Node> inputs;
    private Operation operation;

    public FunctionNode(NodeType nodeType, double output, Map<String, Integer> coordinates) {
        super(nodeType, output, coordinates);
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

    public List<Node> getInputs() {
        return inputs;
    }

    public Operation getOperation() {
        return operation;
    }


    @Override
    protected void execute() {

    }
}
