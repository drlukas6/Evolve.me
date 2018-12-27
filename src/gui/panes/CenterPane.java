package gui.panes;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class CenterPane extends BorderPane {
    private VBox operationsStack = new VBox();
    private VBox networkOptionsStack = new VBox();

    private CheckBox addOperation = new CheckBox("Add");
    private CheckBox substractOperation = new CheckBox("Substract");
    private CheckBox divideOperation = new CheckBox("Divide");
    private CheckBox multiplyOperation = new CheckBox("Multiply");
    private CheckBox sinOperation = new CheckBox("Sin");
    private CheckBox cosOperation = new CheckBox("Cos");
    private CheckBox squareOperation = new CheckBox("Square");
    private CheckBox log10Operation = new CheckBox("Log10");
    private CheckBox negativeOperation = new CheckBox("Negative");

    private TextField numberOfRowsTextField = new TextField();
    private TextField numberOfColumnsTextField = new TextField();
    private TextField levelsBackTextField = new TextField();
    private TextField maxGenerationsTextField = new TextField();
    private TextField numberOfNetworksTextField = new TextField();

    private Label statusLabel = new Label();


    public CenterPane(Double prefWidth) {
        operationsStack.setSpacing(5.);
        networkOptionsStack.setSpacing(5.);

        numberOfRowsTextField.setPromptText("Rows");
        numberOfColumnsTextField.setPromptText("Columns");
        levelsBackTextField.setPromptText("Levels Back");
        maxGenerationsTextField.setPromptText("Max Generations");
        numberOfNetworksTextField.setPromptText("No. Networks");

        operationsStack.getChildren()
                .addAll(addOperation,
                        substractOperation,
                        multiplyOperation,
                        divideOperation,
                        negativeOperation,
                        sinOperation,
                        cosOperation,
                        squareOperation,
                        log10Operation,
                        new Label("If empty all selected"));

        networkOptionsStack.getChildren()
                .addAll(numberOfRowsTextField,
                        numberOfColumnsTextField,
                        levelsBackTextField,
                        maxGenerationsTextField,
                        numberOfNetworksTextField,
                        new Label("All values default to 10"));

        statusLabel.setText("");

        this.setPrefWidth(prefWidth);
        this.setLeft(operationsStack);
        this.setRight(networkOptionsStack);
        this.setBottom(statusLabel);
        this.setPadding(new Insets(5., 5., 5., 5.));
    }

    public void setStatusText(String text) {
        this.statusLabel.setText(text);
    }

    public void clearStatusText() {
        this.statusLabel.setText("");
    }

    public CheckBox[] getAllCheckboxes() {
        CheckBox[] checkBoxes = {addOperation,
                                substractOperation,
                                divideOperation,
                                multiplyOperation,
                                sinOperation,
                                cosOperation,
                                squareOperation,
                                log10Operation,
                                negativeOperation};

        return checkBoxes;
    }

    public Boolean areNoOperationsSelected() {
        for(CheckBox checkBox: getAllCheckboxes()) {
            if (checkBox.isSelected()) return false;
        }
        return true;
    }

    public int getNumberOfRows() throws NumberFormatException {
        return Integer.parseInt(numberOfRowsTextField.getText());
    }

    public int getNumberOfColumns() throws NumberFormatException {
        return Integer.parseInt(numberOfColumnsTextField.getText());
    }


    public int getLevelsBack() throws NumberFormatException {
        return Integer.parseInt(levelsBackTextField.getText());
    }

    public int getMaxGenerations() throws NumberFormatException {
        return Integer.parseInt(maxGenerationsTextField.getText());
    }

    public int getNumberOfNetworks() throws NumberFormatException {
        return Integer.parseInt(numberOfNetworksTextField.getText());
    }
}
