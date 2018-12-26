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


    public CenterPane(Double prefWidth) {
        operationsStack.setSpacing(5.);
        networkOptionsStack.setSpacing(5.);

        numberOfRowsTextField.setPromptText("Rows");
        numberOfColumnsTextField.setPromptText("Columns");
        levelsBackTextField.setPromptText("Levels Back");
        maxGenerationsTextField.setPromptText("Max Generations");

        operationsStack.getChildren()
                .addAll(addOperation,
                        substractOperation,
                        divideOperation,
                        multiplyOperation,
                        sinOperation,
                        cosOperation,
                        squareOperation,
                        log10Operation,
                        negativeOperation,
                        new Label("If empty all selected"));

        networkOptionsStack.getChildren()
                .addAll(numberOfRowsTextField,
                        numberOfColumnsTextField,
                        levelsBackTextField,
                        maxGenerationsTextField,
                        new Label("All values default to 10"));

        this.setPrefWidth(prefWidth);
        this.setLeft(operationsStack);
        this.setRight(networkOptionsStack);
        this.setPadding(new Insets(5., 5., 5., 5.));
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

    public TextField getNumberOfRowsTextField() {
        return numberOfRowsTextField;
    }

    public TextField getNumberOfColumnsTextField() {
        return numberOfColumnsTextField;
    }

    public TextField getLevelsBackTextField() {
        return levelsBackTextField;
    }

    public TextField getMaxGenerationsTextField() {
        return maxGenerationsTextField;
    }
}
