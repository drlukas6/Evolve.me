package gui.panes;

import gui.networkInfo.NetworkInfo;
import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;


public class LeftPane extends BorderPane implements NetworkInfoProvider{
    private TextArea inputsTextArea = new TextArea();

    public LeftPane(Double prefWidth) {
        inputsTextArea.setPromptText("Inputs here");

        this.setPrefWidth(prefWidth);
        this.setPadding(new Insets(5., 5., 5., 5.));

        this.setCenter(inputsTextArea);
    }

    @Override
    public NetworkInfo getNetworkInfo() {
        NetworkInfo networkInfoParser = new NetworkInfo(inputsTextArea.getText());
        if(networkInfoParser.getDimension() < 0) {
            String text = this.inputsTextArea.getText();
            this.inputsTextArea.setText(networkInfoParser.getStatus() + "\n\n" + text);
            return null;
        }
        return networkInfoParser;
    }


}
