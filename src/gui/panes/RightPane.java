package gui.panes;

import gui.networkInfo.NetworkInfo;
import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class RightPane extends BorderPane implements NetworkInfoProvider {
    private TextArea outputsTextArea = new TextArea();

    public RightPane(Double prefWidth) {
        outputsTextArea.setPromptText("Outputs here");

        this.setPrefWidth(prefWidth);
        this.setPadding(new Insets(5., 5., 5., 5.));

        this.setCenter(outputsTextArea);
    }

    @Override
    public NetworkInfo getNetworkInfo() {
        NetworkInfo networkInfoParser = new NetworkInfo(outputsTextArea.getText());
        if(networkInfoParser.getDimension() < 0) {
            String text = this.outputsTextArea.getText();
            this.outputsTextArea.setText(networkInfoParser.getStatus() + "\n\n" + text);
            return null;
        }
        return networkInfoParser;
    }
}
