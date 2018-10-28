import genetics.networks.Network;

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
//        Network network = new Network("i(0, 0);-f8(0, 0)[i(0, 0)];f6(0, 1)[i(0, 0)];f7(0, 2)[i(0, 0)];f6(1, 0)[i(0, 0)];f4(1, 1)[i(0, 0)];f1(1, 2)[i(0, 0), f8(0, 0)];f0(2, 0)[f1(1, 2), f7(0, 2)];f7(2, 1)[f4(1, 1)];f2(2, 2)[f8(0, 0), f8(0, 0)];-o(0, 0)f7(2, 1);-3-3");
    }
}