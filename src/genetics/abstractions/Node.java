package genetics.abstractions;

import java.util.Map;
import java.util.Set;

public abstract class Node {
    protected NodeType nodeType;
    protected double output;
    protected Map<String, Integer> coordinates;

    public Node(NodeType nodeType, double output, Map<String, Integer> coordinates) {
        this.nodeType = nodeType;
        this.output = output;
        this.coordinates = coordinates;
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
        return 8 * coordinates.get("x") + 10 * coordinates.get("y") + 1000*(int) output;
    }

    @Override
    public boolean equals(Object obj) {
        Node rhs = (Node) obj;
        if(rhs == null)
            return false;
        if (rhs.coordinates.get("x") == this.coordinates.get("x") && rhs.coordinates.get("y") == this.coordinates.get("y"))
            return true;
        return false;
    }

    @Override
    public String toString() {
        return " (" + coordinates.get("x") + ", " + coordinates.get("y") + ") ";
    }

    protected abstract void execute();
}
