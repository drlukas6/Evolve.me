package genetics.operations;

public class SquareOperation extends Operation {
    public SquareOperation(int operationId, int operationArity) {
        super(operationId, operationArity);
    }

    public SquareOperation() {
        super(OperationId.SQUARE.getId(), 1);
    }

    @Override
    public double execute(Double... inputs) {
        return Math.pow(inputs[0], 2);
    }
}
