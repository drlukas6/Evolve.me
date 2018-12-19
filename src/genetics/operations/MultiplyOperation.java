package genetics.operations;


public class MultiplyOperation extends Operation {
    public MultiplyOperation() {
        super(OperationId.MULTIPLICATION.getId(), 2);
    }

    @Override
    public double execute(Double... inputs) {
        return inputs[0] * inputs[1];
    }
}
