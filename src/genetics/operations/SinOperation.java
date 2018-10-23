package genetics.operations;

public class SinOperation extends Operation {
    public SinOperation(int operationId, int operationArity) {
        super(operationId, operationArity);
    }

    public SinOperation() {
        super(OperationId.SIN.getId(), 1);
    }

    @Override
    public double execute(Double... inputs) {
        return (Math.sin(Math.toDegrees(inputs[0])));
    }
}
