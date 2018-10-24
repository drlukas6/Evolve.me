package genetics;

import genetics.abstractions.Node;
import genetics.abstractions.NodeType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InputNode extends Node {
    private int operationId = -1;

    public InputNode(NodeType nodeType, double output, Map<String, Integer> coordinates, String nodeId) {
        super(nodeType, output, coordinates, nodeId);
    }

//    public InputNode(String nodeDescriptor) {
//        String coordinatesText = nodeDescriptor.substring(nodeDescriptor.indexOf("(") + 1, nodeDescriptor.indexOf(")"));
//        String[] coordinatesArray = coordinatesText.split(", ");
//        Map<String, Integer> coordinates = new HashMap<>();
//        coordinates.put("x", Integer.parseInt(coordinatesArray[0]));
//        coordinates.put("y", Integer.parseInt(coordinatesArray[1]));
//        super(NodeType.INPUT, 0.0, coordinates, "i");
//    }

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
    protected void execute() {
    }
}
