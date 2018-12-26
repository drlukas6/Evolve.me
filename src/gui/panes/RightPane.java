package gui.panes;

import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class RightPane extends BorderPane {
    private TextArea outputsTextArea = new TextArea();

    public RightPane(Double prefWidth) {
        outputsTextArea.setPromptText("Outputs here");

        this.setPrefWidth(prefWidth);
        this.setPadding(new Insets(5., 5., 5., 5.));

        this.setCenter(outputsTextArea);
    }
}
