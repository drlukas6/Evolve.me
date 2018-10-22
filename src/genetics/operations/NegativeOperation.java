package genetics.operations;


public class NegativeOperation extends Operation{
    public NegativeOperation(int operationId, int operationArity) {
        super(operationId, operationArity);
    }

    public NegativeOperation() {
        super(OperationId.NEGATIVE.getId(), 1);
    }

    @Override
    protected double execute(Double... inputs) {
        return -inputs[0];
    }
}
