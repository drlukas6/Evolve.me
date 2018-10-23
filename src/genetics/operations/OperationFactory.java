package genetics.operations;

import java.util.Random;

public class OperationFactory {
    private static Random r = new Random();

    public static Operation getRandomOperation() {
        return operationWithId(r.nextInt(Operation.OPERATION_MAX + 1));
    }

    public static Operation getOperationWithId(int id) {
        return operationWithId(id);
    }

    private static Operation operationWithId(int id) {
        switch(id) {
            case 0:
                return new AddOperation();
            case 1:
                return new SubstractOperation();
            case 2:
                return new MultiplyOperation();
            case 3:
                return new DivideOperation();
            case 4:
                return new NegativeOperation();
            case 5:
                return new SinOperation();
            case 6:
                return new CosOperation();
            case 7:
                return new SquareOperation();
            default:
                return new Log10Operation();
        }
    }
}
