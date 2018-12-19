package genetics.operations;

public class SinOperation extends Operation {
    public SinOperation() {
        super(OperationId.SIN.getId(), 1);
    }

    @Override
    public double execute(Double... inputs) {
        return (Math.toDegrees(inputs[0]));
    }
}
