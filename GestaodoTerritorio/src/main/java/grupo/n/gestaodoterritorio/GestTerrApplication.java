package grupo.n.gestaodoterritorio;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import grupo.n.gestaodoterritorio.services.PropertiesService;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.controlsfx.glyphfont.FontAwesome;

import java.io.IOException;

public class GestTerrApplication extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws IOException {

        try{
            PropertiesService.getInstance().loadProperties("GestaodoTerritorio/src/main/resources/Madeira-Moodle-1.1.csv");
        } catch (Exception e){
            e.printStackTrace();
            System.err.println("Erro ao carregar o ficheiro CSV!");
            System.exit(1); //Sair da aplicação se falhar
        }

        FXMLLoader fxmlLoader = new FXMLLoader(GestTerrApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 577, 371);

        //Remover a barra acima na aplicação javaFx
        stage.initStyle(javafx.stage.StageStyle.UNDECORATED);

        // Adicionar eventos para arrastar a janela
        scene.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        scene.setOnMouseDragged(event -> {
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