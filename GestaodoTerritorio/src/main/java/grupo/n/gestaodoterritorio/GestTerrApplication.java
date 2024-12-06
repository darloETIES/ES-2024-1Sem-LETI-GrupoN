package grupo.n.gestaodoterritorio;

import grupo.n.gestaodoterritorio.models.Owner;
import grupo.n.gestaodoterritorio.services.PropertiesService;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class GestTerrApplication extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Inicia a aplicação
     * @param stage Janela a carregar
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(GestTerrApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 577, 371);

        //Remover a barra acima na aplicação javaFx
        stage.initStyle(javafx.stage.StageStyle.UNDECORATED);

        AnchorPane windowBar = (AnchorPane) scene.lookup(".window-bar");

        // Adicionar eventos para arrastar a janela
        windowBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        windowBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        //stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void closeApp() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }
}