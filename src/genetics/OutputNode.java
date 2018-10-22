package genetics;

import genetics.abstractions.Node;
import genetics.abstractions.NodeType;

import java.util.*;

public class OutputNode extends Node {
    private List<Node> inputs;
    public OutputNode(NodeType nodeType, double output, Map<String, Integer> coordinates, String nodeId) {
        super(nodeType, output, coordinates, nodeId);
        this.inputs = new ArrayList<>();
    }

    @Override
    protected void execute() {

    }
}
