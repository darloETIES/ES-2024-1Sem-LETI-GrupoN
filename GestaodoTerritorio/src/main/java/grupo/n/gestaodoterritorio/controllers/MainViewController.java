package grupo.n.gestaodoterritorio.controllers;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class MainViewController {


    //parametros para personalizações e animações da janela
    @FXML
    private AnchorPane sideLeftMenu;

    @FXML
    private VBox menuContent;

    @FXML
    private AnchorPane mainContentPanel;


    private static final double COLLAPSED_WIDTH = 45.0;
    private static final double EXPANDED_WIDTH = 110.0;

    @FXML
    private void initialize() {
        // Configura os eventos de entrada e saída do cursor
        sideLeftMenu.setOnMouseEntered(event -> expandMenu());
        sideLeftMenu.setOnMouseExited(event -> collapseMenu());

        // Oculta o texto inicialmente
        menuContent.getChildren().forEach(node -> {
            if (node instanceof Label) {
                node.setOpacity(0);  // Faz o texto começar invisível
            }
        });
    }

    //métodos que carregam a view escolhida pelo menu lateral
    @FXML
    private void loadGraphPageView() {
        loadContent("/grupo/n/gestaodoterritorio/graph-page-view.fxml");
    }

    @FXML
    private void loadAreasPageView() {
        loadContent("/grupo/n/gestaodoterritorio/areas-page-view.fxml");
    }

    @FXML
    private void loadSuggestedTradesPageView() {
        loadContent("/grupo/n/gestaodoterritorio/sug_trades_page_view.fxml");
    }

    private void loadContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane content = loader.load(); // Carregar o conteúdo do arquivo FXML
            mainContentPanel.getChildren().clear(); // Limpar o painel principal
            mainContentPanel.getChildren().add(content); // Adicionar o novo conteúdo
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar o arquivo FXML: " + fxmlPath);
        }
    }



    //metodos para personalizações e animações da janela (expandir e colapsar com animação)
    private void expandMenu() {
        Timeline expandTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(sideLeftMenu.prefWidthProperty(), COLLAPSED_WIDTH, Interpolator.EASE_IN)),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(sideLeftMenu.prefWidthProperty(), EXPANDED_WIDTH, Interpolator.EASE_IN))
        );

        expandTimeline.play();
    }

    private void collapseMenu() {
        Timeline collapseTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(sideLeftMenu.prefWidthProperty(), EXPANDED_WIDTH, Interpolator.EASE_OUT)),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(sideLeftMenu.prefWidthProperty(), COLLAPSED_WIDTH, Interpolator.EASE_OUT))
        );


        collapseTimeline.play();
    }

    @FXML
    private void closeApp() {
        System.exit(0);
    }
}
