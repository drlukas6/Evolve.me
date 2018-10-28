package genetics.nodes;

import genetics.nodes.Node;
import genetics.nodes.NodeType;

import java.util.Map;

public class InputNode extends Node {
    private int operationId = -1;

    public InputNode(NodeType nodeType, double output, Map<String, Integer> coordinates, String nodeId) {
        super(nodeType, output, coordinates, nodeId);
    }

    public InputNode(Map<String, Integer> coordinates) {
        super(NodeType.INPUT, 0.0, coordinates, "i");
    }


    @Override
    public void setActive(boolean active) {
        this.active = active;
//        System.out.println("Node " + this + " is active: " + this.active);
    }


    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void execute() {
    }
}
