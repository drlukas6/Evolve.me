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
        if(inputs[0] - 0.0 <= 0.0001) {
            return Math.log10(0.0001);
        }
        return Math.log10(Math.abs(inputs[0]));
    }
}
