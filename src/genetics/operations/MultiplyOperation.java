package genetics.operations;


public class MultiplyOperation extends Operation {
    public MultiplyOperation(int operationId, int operationArity) {
        super(operationId, operationArity);
    }

    public MultiplyOperation() {
        super(OperationId.MULTIPLICATION.getId(), 2);
    }

    @Override
    public double execute(Double... inputs) {
        return inputs[0] * inputs[1];
    }
}
