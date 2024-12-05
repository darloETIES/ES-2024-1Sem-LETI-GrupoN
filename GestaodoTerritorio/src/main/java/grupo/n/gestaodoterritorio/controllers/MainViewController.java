package grupo.n.gestaodoterritorio.controllers;

import grupo.n.gestaodoterritorio.models.Owner;
import grupo.n.gestaodoterritorio.services.PropertiesService;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class MainViewController {

    //parametros para personalizações e animações da janela
    @FXML
    private AnchorPane sideLeftMenu;

    @FXML
    private VBox menuContent;

    @FXML
    private AnchorPane mainContentPanel;

    @FXML
    private AnchorPane contentPanel;

    @FXML
    private Button uploadFileBtn;

    @FXML
    private AnchorPane uploadPane;

    @FXML
    private AnchorPane uploadBackgroundPane;


    private static final double COLLAPSED_WIDTH = 45.0;
    private static final double EXPANDED_WIDTH = 110.0;

    @FXML
    private void initialize() {

        GaussianBlur gaussianBlur = new GaussianBlur();
        uploadBackgroundPane.setVisible(true);
        contentPanel.setEffect(gaussianBlur);

        uploadPane.setVisible(true);


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

    @FXML
    private void loadFile() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolha um ficheiro CSV para carregar");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        File file = fileChooser.showOpenDialog(mainContentPanel.getScene().getWindow());

        if(file != null){
            PropertiesService.getInstance().loadProperties(file.getAbsolutePath());
            Map<String, Owner> owners = PropertiesService.getInstance().getOwners();
            PropertiesService.getInstance().generateOwnersGraph(owners);
            // Ocultar o uploadPane após carregar o arquivo
            uploadPane.setVisible(false);
            uploadBackgroundPane.setVisible(false);
            contentPanel.setEffect(null);
            System.out.println("Ficheiro carregado com sucesso!");
        } else {
            // Opcional: Informar que o usuário cancelou a seleção
            System.out.println("Nenhum ficheiro foi selecionado.");
        }
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
