package grupo.n.gestaodoterritorio.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class SuggestedTradesPageViewController {

    @FXML
    private AnchorPane detailsPanel; // Painel de detalhes da troca

    @FXML
    private AnchorPane backgroundPane;

    @FXML
    private HBox tradeButton; // Botão para ver detalhes da troca

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        // Inicialmente, o painel de detalhes está oculto
        detailsPanel.setVisible(false);

        // Mostrar o painel de detalhes ao clicar no botão de troca
        tradeButton.setOnMouseClicked(event -> detailsPanel.setVisible(true));

        // Ocultar o painel de detalhes ao clicar no botão "Voltar"
        backButton.setOnAction(event -> detailsPanel.setVisible(false));
    }
}