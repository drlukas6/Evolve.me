package genetics.operations;

public class DivideOperation extends Operation {
    public DivideOperation() {
        super(OperationId.DIVISION.getId(), 2);
    }

    @Override
    public double execute(Double... inputs) {
        return inputs[0] / inputs[1];
    }
}
