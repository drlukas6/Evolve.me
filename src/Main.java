import genetics.Network;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Double> inputs = new ArrayList<>();
        inputs.add(0.1);
        inputs.add(1.);
        inputs.add(2.);
        inputs.add(3.);
        List<Double> outputs = new ArrayList<>();
        outputs.add(0.);
        outputs.add(1.83);
        outputs.add(3.);
        outputs.add(3.15);
//        Network network = new Network(3, 3, 3, 1, 1);
        Network network = new Network(3, 3, 1, 1, 2, outputs, inputs);
        network.executeNetwork();
    }
}