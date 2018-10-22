package genetics;

import genetics.abstractions.Node;
import genetics.abstractions.NodeType;

import java.util.Map;
import java.util.Set;

public class InputNode extends Node {
    private int operationId = -1;
    public InputNode(NodeType nodeType, double output, Map<String, Integer> coordinates) {
        super(nodeType, output, coordinates);
    }

    @Override
    protected void execute() {

    }
}
