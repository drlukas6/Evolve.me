import genetics.networks.Network;
import genetics.networks.NetworkFactory;

import java.util.Arrays;

public class TestingGround {
    public static void main(String[] args) {
        String networkDescriptor = "i(0, 0);-f5(0, 0)[i(0, 0)];f8(0, 1)[i(0, 0)];f7(0, 2)[i(0, 0)];f8(1, 0)[f7(0, 2)];f2(1, 1)[i(0, 0), i(0, 0)];f7(1, 2)[i(0, 0)];f7(2, 0)[f7(0, 2)];f8(2, 1)[f8(0, 1)];f7(2, 2)[f7(1, 2)];-o(0, 0)f8(0, 1);-3-3-2";
        Network network = NetworkFactory.createNetworkWithDescriptor(networkDescriptor);
    }

}
