package genetics.nodes;


import java.util.Map;

public class InputNode extends Node {
    public InputNode(NodeType nodeType, double output, Map<String, Integer> coordinates, String nodeId) {
        super(nodeType, output, coordinates, nodeId);
    }

    public InputNode(Map<String, Integer> coordinates) {
        super(NodeType.INPUT, 0.0, coordinates, "i");
    }


    @Override
    public void setActive(boolean active) {
        this.active = active;
    }


    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void execute() {
    }
}
