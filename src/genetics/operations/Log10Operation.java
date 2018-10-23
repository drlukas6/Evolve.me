package genetics.operations;

public class Log10Operation extends Operation {

    public Log10Operation(int operationId, int operationArity) {
        super(operationId, operationArity);
    }

    public Log10Operation() {
        super(OperationId.LOG10.getId(), 1);
    }

    @Override
    public double execute(Double... inputs) {
        return Math.log10(inputs[0]);
    }
}
