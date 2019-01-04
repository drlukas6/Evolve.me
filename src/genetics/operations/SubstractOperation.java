package genetics.operations;

public class SubstractOperation extends Operation {
    public SubstractOperation() {
        super(OperationId.SUBTRACTION.getId(), 2);
    }

    @Override
    public double execute(Double... inputs) {
        return inputs[0] - inputs[1];
    }
}
