package gui.panes;

import javafx.geometry.Insets;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class BottomPane extends BorderPane {
    private ProgressBar trainingProgress = new ProgressBar();

    public BottomPane(Double prefWidth) {
        this.trainingProgress.setPrefWidth(prefWidth);

        this.setPrefWidth(prefWidth);
        this.setPadding(new Insets(5., 5., 5., 5.));

        this.setCenter(trainingProgress);
    }

    public void setProgress(Double progress) {
        this.trainingProgress.setProgress(progress);
    }

    public void updateProgressBy(Double progress) {
        this.trainingProgress.setProgress(this.trainingProgress.getProgress() + progress);
    }
}
