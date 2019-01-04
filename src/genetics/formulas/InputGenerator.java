package genetics.formulas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Un-necessary class primarily used for generating input and output numbers
 */
public class InputGenerator {
    public static Map<String, List<List<Double>>> inputData(double step, int rangeStart, int rangeEnd, int functionDimension) {
        List<List<Double>> inputs = new ArrayList<>();
        for(int i = 0; i < functionDimension; i++) {
            inputs.add(new ArrayList<>());
        }
        List<List<Double>> outputs = new ArrayList<>();
        outputs.add(new ArrayList<>());
        Map<String, List<List<Double>>> equationValues = new HashMap<>();
        double input1 = rangeStart;
        double input2 = rangeStart;
        while(input1 <= rangeEnd) {
            input2 = rangeStart;
            while(input2 <= rangeEnd) {
                double output = (Math.pow(input1, 3) / 5 + Math.pow(input2, 3) / 2 - input1 - input2);
                outputs.get(0).add(output);
                input2 += step;
            }
            input1 += step;
        }
        input1 = rangeStart;
        while(input1 <= rangeEnd) {
            input2 = rangeStart;
            while(input2 <= rangeEnd) {
                inputs.get(0).add(input1);
                inputs.get(1).add(input2);
                input2 += step;
            }
            input1 += step;
        }

        equationValues.put("input", inputs);
        equationValues.put("output", outputs);
        return equationValues;
    }
}