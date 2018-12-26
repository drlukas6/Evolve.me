package gui.panes;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class TopPane extends BorderPane {
    private Label titleLabel = new Label("Evolve.me");
    private Button trainButton = new Button("Train");

    public TopPane(Double prefWidth) {
        titleLabel.setFont(new Font(25));
        trainButton.setPrefHeight(25);

        this.setPrefWidth(prefWidth);
        this.setPadding(new Insets(5., 5., 5., 5.));

        this.setLeft(titleLabel);
        this.setRight(trainButton);
    }
}
