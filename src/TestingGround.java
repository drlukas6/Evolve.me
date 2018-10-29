import genetics.networks.Network;
import genetics.networks.NetworkFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class test {
    private int a;

    public test(int a) {
        this.a = a;
    }

    public int getA() {
        return a;
    }

    @Override
    public String toString() {
        return "" + a;
    }
}

public class TestingGround {
    public static void main(String[] args) {
        Comparator<test> testc = Comparator.comparing(test::getA);
        test t = new test(5);
        test t1 = new test(1);
        test t2 = new test(7);
        List<test> testList = new ArrayList<>();
        testList.add(t);
        testList.add(t1);
        testList.add(t2);
        for(test te: testList) {
            System.out.println(te);
        }
        testList.sort(testc);
        for(test te: testList) {
            System.out.println(te);
        }
    }

}
