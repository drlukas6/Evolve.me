import genetics.networks.Network;
import genetics.organism.Organism;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Double> inputs = new ArrayList<>();
        inputs.add(2.4889);
        inputs.add(3.8255);
        inputs.add(4.9972);
        inputs.add(6.0764);
        inputs.add(7.5674);
        List<Double> outputs = new ArrayList<>();
        outputs.add(1.4645);
        outputs.add(2.1731);
        outputs.add(2.6514);
        outputs.add(3.0261);
        outputs.add(3.4777);
        Organism organism = new Organism(20, 31, 31, 5, 1, 1, inputs, outputs, 20);
        organism.startTraining();
    }
}