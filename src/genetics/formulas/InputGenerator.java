package genetics.formulas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputGenerator {
    public static Map<String, List<Double>> inputData(double step, int rangeStart, int rangeEnd) {
        List<Double> inputs = new ArrayList<>();
        List<Double> outputs = new ArrayList<>();
        Map<String, List<Double>> equationValues = new HashMap<>();
        double input = (double)rangeStart;
        while (input <= (double) rangeEnd) {
            inputs.add(input);
            double value = Math.log(input + 1) + Math.log(Math.pow(input, 2) + 1);
            outputs.add(value);
            input += step;
        }
        equationValues.put("input", inputs);
        equationValues.put("output", outputs);
        return equationValues;
    }
}
