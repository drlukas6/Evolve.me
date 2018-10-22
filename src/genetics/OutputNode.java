package genetics;

import genetics.abstractions.Node;
import genetics.abstractions.NodeType;

import java.util.*;

public class OutputNode extends Node {
    private Node input;
    public OutputNode(NodeType nodeType, double output, Map<String, Integer> coordinates, String nodeId) {
        super(nodeType, output, coordinates, nodeId);
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
    protected void execute() {
        output = input.getOutput();
    }
}
