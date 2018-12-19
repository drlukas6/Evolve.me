package genetics.operations;


public class NegativeOperation extends Operation{
    public NegativeOperation() {
        super(OperationId.NEGATIVE.getId(), 1);
    }

    @Override
    public double execute(Double... inputs) {
        return -inputs[0];
    }
}
