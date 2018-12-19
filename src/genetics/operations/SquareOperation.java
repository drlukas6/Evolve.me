package genetics.operations;

public class SquareOperation extends Operation {
    public SquareOperation() {
        super(OperationId.SQUARE.getId(), 1);
    }

    @Override
    public double execute(Double... inputs) {
        return Math.pow(inputs[0], 2);
    }
}
