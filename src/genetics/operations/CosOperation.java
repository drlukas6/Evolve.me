package genetics.operations;

public class CosOperation extends Operation {
    public CosOperation() {
        super(OperationId.COS.getId(), 1);
    }

    @Override
    public double execute(Double... inputs) {
        return (Math.cos(inputs[0]));
    }
}
