package genetics.nodes;

import genetics.nodes.Node;
import genetics.nodes.NodeType;

import java.util.*;

public class OutputNode extends Node {
    private Node input;
    public OutputNode(NodeType nodeType, double output, Map<String, Integer> coordinates, String nodeId) {
        super(nodeType, output, coordinates, nodeId);
    }

    public OutputNode(Map<String, Integer> coordinates) {
        super(NodeType.OUTPUT, 0.0, coordinates, "o");
    }

    public Node getInput() {
        return input;
    }

    public void setInput(Node input) {
        this.input = input;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
        input.setActive(active);
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void execute() {
        output = input.getOutput();
    }
}
