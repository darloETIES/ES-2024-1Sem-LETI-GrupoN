package grupo.n.gestaodoterritorio.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

public class AlertWindowViewController {

    @FXML
    private Label messageLabel;

    @FXML
    private ProgressBar progressBar;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void setProgress(double progress) {
        progressBar.setProgress(progress);
    }

    public void close(){
        if(stage != null)
            stage.close();
    }

}
