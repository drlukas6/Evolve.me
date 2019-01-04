package genetics.nodes;

import java.util.Map;
import java.util.Objects;

/**
 * Abstract class that describes basic look and functionality of a node
 */
public abstract class Node {
    private NodeType nodeType;
    double output;
    Map<String, Integer> coordinates;
    String nodeId;
    boolean active;

    Node(NodeType nodeType, double output, Map<String, Integer> coordinates, String nodeId) {
        this.nodeType = nodeType;
        this.output = output;
        this.coordinates = coordinates;
        this.nodeId = nodeId;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public double getOutput() {
        return output;
    }

    public Map<String, Integer> getCoordinates() {
        return coordinates;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeType, output, coordinates, nodeId, active);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() == Node.class) {
            Node rhs = (Node) obj;
            return (rhs.coordinates.get("x").equals(this.coordinates.get("x")) && rhs.coordinates.get("y").equals(this.coordinates.get("y")));
        }
        return false;
    }

    @Override
    public String toString() {
        return nodeId + "(" + coordinates.get("x") + ", " + coordinates.get("y") + ")";
    }

    public abstract void execute();
    public abstract void setActive(boolean active);
    public abstract boolean isActive();
}
