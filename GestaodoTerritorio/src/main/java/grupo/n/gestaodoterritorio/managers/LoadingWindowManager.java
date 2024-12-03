package grupo.n.gestaodoterritorio.managers;

import grupo.n.gestaodoterritorio.controllers.AlertWindowViewController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoadingWindowManager {
    private static Stage loadingStage;
    private static AlertWindowViewController controller;

    public static void showLoadingWindow(String message) {
        if (loadingStage == null) {
            try {
                FXMLLoader loader = new FXMLLoader(LoadingWindowManager.class.getResource("/fxml/alert-window-view.fxml"));
                Scene scene = new Scene(loader.load());

                controller = loader.getController();
                loadingStage = new Stage();
                loadingStage.setScene(scene);
                loadingStage.initModality(Modality.APPLICATION_MODAL);
                loadingStage.setResizable(false);

                controller.setStage(loadingStage);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        Platform.runLater(() -> {
            controller.setMessage(message);
            controller.setProgress(-1); // Indeterminate mode
            loadingStage.show();
        });
    }

    public static void updateProgress(double progress) {
        if (controller != null) {
            Platform.runLater(() -> controller.setProgress(progress));
        }
    }

    public static void closeLoadingWindow() {
        if (loadingStage != null) {
            Platform.runLater(() -> {
                loadingStage.close();
                loadingStage = null;
            });
        }
    }
}
