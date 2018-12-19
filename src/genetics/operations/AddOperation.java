package genetics.operations;

public class AddOperation extends Operation {
    public AddOperation() {
        super(OperationId.ADDITION.getId(), 2);
    }

    @Override
    public double execute(Double...inputs) {
        return inputs[0] + inputs[1];
    }
}
