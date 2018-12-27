package gui.networkInfo;

import java.util.ArrayList;
import java.util.List;

public class NetworkInfo {
    private List<List<Double>> values;
    private int dimension;
    private String status;

    private static final String NO_INPUTS = "NO VALUE \nGIVEN";
    private static final String PARSING_PROBLEM = "NUMBER PARSING \nERROR";
    private static final String INPUT_NON_EXISTANT = "ERROR IN GIVEN \nVALUES";
    private static final String INTERNAL_ERROR = "INTERNAL ERROR";

    public NetworkInfo(String rawInput) {
        rawInput = rawInput.trim();
        this.values = new ArrayList<>();
        if (rawInput.isEmpty()) {
            this.values = null;
            this.dimension = -1;
            this.status = NO_INPUTS;
        }
        else {
            String[] rows = rawInput.split("\n");
            dimension = rows[0].split("\\s+").length;
            for(int i = 0; i < dimension; i++) {
                try {
                    values.add(new ArrayList<>());
                } catch (NullPointerException e) {
                    this.values = null;
                    this.dimension = -1;
                    this.status = INTERNAL_ERROR;
                    return;
                }

            }
            for(String line: rows) {
                String[] rawNumbers = line.split("\\s+");
                for(int i = 0; i < rawNumbers.length; i++) {
                    if(rawNumbers.length < dimension) {
                        this.values = null;
                        this.dimension = -1;
                        this.status = INPUT_NON_EXISTANT;
                    }
                    try {
                        Double number = Double.parseDouble(rawNumbers[i]);
                        values.get(i).add(number);
                    } catch (NumberFormatException e) {
                        this.values = null;
                        this.dimension = -1;
                        this.status = PARSING_PROBLEM;
                        return;
                    } catch (NullPointerException e) {
                        this.values = null;
                        this.dimension = -1;
                        this.status = INPUT_NON_EXISTANT;
                        return;
                    }
                }
            }
        }
    }

    public List<List<Double>> getValues() {
        return values;
    }

    public int getDimension() {
        return dimension;
    }

    public String getStatus() {
        return status;
    }
}
