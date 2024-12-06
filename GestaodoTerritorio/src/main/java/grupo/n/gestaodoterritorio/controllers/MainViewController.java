package grupo.n.gestaodoterritorio.controllers;

import grupo.n.gestaodoterritorio.models.Owner;
import grupo.n.gestaodoterritorio.services.PropertiesService;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
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

    @FXML
    private StackPane loadingIndicator;

    private static final double COLLAPSED_WIDTH = 45.0;
    private static final double EXPANDED_WIDTH = 110.0;

    private File file; // Usado maioritariamente em BlackBoxTesting

    /**
     * Prepara a interação com o utilizador (começando na janela de upload do ficheiro)
     */
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

    /**
     * Método responsável por carregar o ficheiro
     * @throws Exception
     */
    @FXML
    private void loadFile() throws Exception {
        File file;
        if(this.file != null) { //verificar se o ficheiro inicialmente está vazio
            file = this.file;
        }
        else{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Escolha um ficheiro CSV para carregar");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("CSV Files", "*.csv")
            );

            file = fileChooser.showOpenDialog(mainContentPanel.getScene().getWindow());
        }

        if (file != null) {
            // Adiciona uma animação de carregamento enquanto o processamento ocorre
            startLoadingAnimation();

            // Processo em uma thread separada para não travar a interface
            new Thread(() -> {
                try {
                    PropertiesService.getInstance().loadProperties(file.getAbsolutePath());
                    Map<String, Owner> owners = PropertiesService.getInstance().getOwners();
                    PropertiesService.getInstance().generateOwnersGraph(owners);

                    // Atualiza interface no JavaFX Application Thread
                    javafx.application.Platform.runLater(() -> {
                        stopLoadingAnimation();
                        uploadPane.setVisible(false);
                        uploadBackgroundPane.setVisible(false);
                        contentPanel.setEffect(null);
                        System.out.println("Ficheiro carregado com sucesso!");
                    });
                } catch (Exception e) {
                    javafx.application.Platform.runLater(() -> {
                        stopLoadingAnimation();
                        showError("Erro ao carregar o ficheiro: " + e.getMessage());
                    });
                }
            }).start();
        } else {
            System.out.println("Nenhum ficheiro foi selecionado.");
        }
    }

    /**
     * Começo da animação de carregamento
     */
    private void startLoadingAnimation() {
        loadingIndicator.setVisible(true);
    }

    /**
     * Fecho da animação de carregamento
     */
    private void stopLoadingAnimation() {
        loadingIndicator.setVisible(false);
    }

    /**
     * Mostra mensagem de erro
     * @param message
     */
    private void showError(String message) {
        //mostra uma mensagem de erro
        System.err.println(message);
    }

    /**
     * Método que carrega a view do visualizador de grafos pela seleção do botão correspondente
     */
    @FXML
    private void loadGraphPageView() {
        loadContent("/grupo/n/gestaodoterritorio/graph-page-view.fxml");
    }

    /**
     * Método que carrega a view das áreas geográficas pela seleção do botão correspondente
     */
    @FXML
    private void loadAreasPageView() {
        loadContent("/grupo/n/gestaodoterritorio/areas-page-view.fxml");
    }

    /**
     * Método que carrega a view das trocas sugeridas pela seleção do botão correspondente
     */
    @FXML
    private void loadSuggestedTradesPageView() {
        loadContent("/grupo/n/gestaodoterritorio/sug-trades-page-view.fxml");
    }

    /**
     * Carrega a view selecionada no menu lateral em uma AnchorPane
     * @param fxmlPath Caminho para a view
     */
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


    /**
     * Método para expandir o menu com o uso de uma animação
     */
    private void expandMenu() {
        Timeline expandTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(sideLeftMenu.prefWidthProperty(), COLLAPSED_WIDTH, Interpolator.EASE_IN)),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(sideLeftMenu.prefWidthProperty(), EXPANDED_WIDTH, Interpolator.EASE_IN))
        );

        expandTimeline.play();
    }
    /**
     * Método para colapsar o menu com o uso de uma animação
     */
    private void collapseMenu() {
        Timeline collapseTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(sideLeftMenu.prefWidthProperty(), EXPANDED_WIDTH, Interpolator.EASE_OUT)),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(sideLeftMenu.prefWidthProperty(), COLLAPSED_WIDTH, Interpolator.EASE_OUT))
        );


        collapseTimeline.play();
    }

    /**
     * Fecha a aplicação
     */
    @FXML
    private void closeApp() {
        System.exit(0);
    }

    /**
     * Define o valor de file
     * @param file Novo valor de file
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Obtém o botão de upload do ficheiro
     * @return Botão de upload do ficheiro
     */
    public Button getUploadFileBtn(){
        return uploadFileBtn;
    }

    /**
     * Obtém o painel de upload do ficheiro
     * @return Painel de upload do ficheiro
     */
    public AnchorPane getUploadPane(){
        return uploadPane;
    };

    /**
     * Obtém o background de upload do ficheiro
     * @return Background de upload do ficheiro
     */
    public AnchorPane getUploadBackgroundPane(){
        return uploadBackgroundPane;
    }

    /**
     * Obtém o painel com a barra de carregamento de upload do ficheiro
     * @return Painel com a barra de carregamento de upload do ficheiro
     */
    public StackPane getLoadingIndicator(){
        return loadingIndicator;
    }

    /**
     * Obtém o painel do conteúdo principal
     * @return Painel do conteúdo principal
     */
    public AnchorPane getContentPanel() {
        return contentPanel;
    }
}
