package genetics.operations;

public class SubstractOperation extends Operation {
    public SubstractOperation(int operationId, int operationArity) {
        super(operationId, operationArity);
    }

    public SubstractOperation() {
        super(OperationId.SUBSTRACTION.getId(), 2);
    }

    @Override
    protected double execute(Double... inputs) {
        return inputs[0] - inputs[1];
    }
}
