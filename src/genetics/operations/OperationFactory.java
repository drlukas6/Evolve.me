package genetics.operations;

import java.util.List;
import java.util.Random;

public class OperationFactory {
    private static Random r = new Random();

    public static Operation getOperationWithId(int id) {
        return operationWithId(id);
    }

    public static Operation getRandomOperationFromGiven(List<Integer> givenOperations) {
        int operationId = givenOperations.get(r.nextInt(givenOperations.size()));
        return getOperationWithId(operationId);
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
