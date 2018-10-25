import genetics.Network;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        List<Double> inputs = new ArrayList<>();
//        inputs.add(0.1);
//        inputs.add(1.);
//        inputs.add(2.);
//        inputs.add(3.);
//        List<Double> outputs = new ArrayList<>();
//        outputs.add(0.);
//        outputs.add(1.83);
//        outputs.add(3.);
//        outputs.add(3.15);
////        Network network = new Network(3, 3, 3, 1, 1);
//        Network network = new Network(3, 3, 1, 1, 2, outputs, inputs);
//        network.executeNetwork();
        testingGround();
    }

    private static void testingGround() {
        String test = "f10(3,1)[i(0, 0), f(0, 1)]";
        System.out.println(test.indexOf("),")); // daje index zareza izmedu dva operatora
        System.out.println(test.indexOf("],")); // -1 jer ne nade
        String inputs = test.substring(test.indexOf("[") + 1, test.indexOf("]"));
        System.out.println(inputs);
        String coors = test.substring(test.indexOf("(") + 1, test.indexOf(")"));
        System.out.println(coors);
        String[] c = coors.split(",");
        System.out.println(c[0] + "   " + c[1]);
        String operation = test.substring(1, test.indexOf("("));
        System.out.println(operation);
        switch (test.substring(0, 1)) {
            case "i":
                System.out.println("ne");
                break;
            case "f":
                System.out.println("da");
                break;
        }
    }
}