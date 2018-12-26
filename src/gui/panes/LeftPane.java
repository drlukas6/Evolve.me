package gui.panes;

import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;


public class LeftPane extends BorderPane {
    private TextArea inputsTextArea = new TextArea();

    public LeftPane(Double prefWidth) {
        inputsTextArea.setPromptText("Inputs here");

        this.setPrefWidth(prefWidth);
        this.setPadding(new Insets(5., 5., 5., 5.));

        this.setCenter(inputsTextArea);
    }
}
