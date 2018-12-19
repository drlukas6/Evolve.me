package commandLine;

import genetics.formulas.InputGenerator;
import genetics.networks.Network;
import genetics.organism.Organism;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException, FileNotFoundException {
        Map<String, List<List<Double>>> values = InputGenerator.inputData(0.5, -10, 10, 2);
        List<List<Double>> inputs = values.get("input");
        List<List<Double>> outputs = values.get("output");
        String organismId = new Date().toString().replace(" ", "-");
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(String.format("Organisms/organism-%s.cgp", organismId)), StandardCharsets.UTF_8))) {
            Organism organism = new Organism(20, 3, 3, 2, 2, 1, inputs, outputs.get(0), 20, writer);
            organism.startTraining();
            organism.testBestNetwork();
        }

    }
}