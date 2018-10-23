package genetics.operations;

public abstract class Operation {
    protected int operationId;
    protected int operationArity;

    public Operation(int operationId, int operationArity) {
        this.operationId = operationId;
        this.operationArity = operationArity;
    }

    public int getOperationId() {
        return operationId;
    }

    public int getOperationArity() {
        return operationArity;
    }

    public abstract double execute(Double...inputs);


    public static final int OPERATION_MAX = 8;
    public static final int OPERATION_MIN = 0;

    public enum OperationId {
        ADDITION(0),
        SUBSTRACTION(1),
        MULTIPLICATION(2),
        DIVISION(3),
        NEGATIVE(4),
        SIN(5),
        COS(6),
        SQUARE(7),
        LOG10(8);

        private int id;

        OperationId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}