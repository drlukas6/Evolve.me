package genetics.operations;

public class AddOperation extends Operation {
    public AddOperation(int operationId, int operationArity) {
        super(operationId, operationArity);
    }
    public AddOperation() {
        super(OperationId.ADDITION.getId(), 2);
    }

    @Override
    protected double execute(Double...inputs) {
        return inputs[0] + inputs[1];
    }
}
