import genetics.formulas.InputGenerator;
import genetics.networks.Network;
import genetics.organism.Organism;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, List<Double>> values = InputGenerator.inputData(0.5, 0, 20);
        List<Double> inputs = values.get("input");
        List<Double> outputs = values.get("output");
        Organism organism = new Organism(20, 31, 31, 31, 1, 1, inputs, outputs, 1000);
        organism.startTraining();
        organism.testBestNetwork();
    }
}