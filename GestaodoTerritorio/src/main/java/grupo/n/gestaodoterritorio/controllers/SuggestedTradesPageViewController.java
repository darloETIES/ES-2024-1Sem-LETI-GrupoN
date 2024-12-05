package grupo.n.gestaodoterritorio.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import grupo.n.gestaodoterritorio.Graph;
import grupo.n.gestaodoterritorio.Owner;
import grupo.n.gestaodoterritorio.Proposal;
import grupo.n.gestaodoterritorio.services.PropertiesService;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.List;

public class SuggestedTradesPageViewController {


    PropertiesService propertiesService = PropertiesService.getInstance();
    private List<Proposal> proposalList = new ArrayList<>();

    @FXML
    private VBox tradeListContainer; // Lista de propostas no painel esquerdo

    @FXML
    private AnchorPane detailsPanel; // Painel de detalhes da troca

    @FXML
    private Label ownerSourceInTitle, ownerTargetInTitle;

    @FXML
    private Label sourceOwnerLabel1, targetOwnerLabel1, sourceOwnerLabel2, targetOwnerLabel2;
    @FXML
    private Label sp1Label1, sp2Label1, tp1Label1, tp2Label1, sp1Label2, sp2Label2, tp1Label2, tp2Label2;
    @FXML
    private Button backButton;



    @FXML
    public void initialize() {

        loadTradeList();

        //Personalização
        // Inicialmente, o painel de detalhes está oculto
        detailsPanel.setVisible(false);

        // Mostrar o painel de detalhes ao clicar no botão de troca
        //tradeButton.setOnMouseClicked(event -> detailsPanel.setVisible(true));

        // Ocultar o painel de detalhes ao clicar no botão "Voltar"
        backButton.setOnAction(event -> detailsPanel.setVisible(false));
    }

    private void loadTradeList() {

        SimpleGraph<Owner, DefaultEdge> ownersGraph = PropertiesService.getInstance().getGraphOwners();
        proposalList = propertiesService.getExchSuggestions(ownersGraph);

        tradeListContainer.getChildren().clear();

        for (Proposal proposal : proposalList) {
            HBox tradeButton = new HBox();
            tradeButton.setSpacing(10);
            tradeButton.setOnMouseClicked(event -> showDetails(proposal));

            tradeButton.setAlignment(Pos.CENTER);

            Label sourceLabel = new Label("Proprietário " + proposal.getSource().getOwnerID());
            sourceLabel.setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-font-family: 'Microsoft JhengHei UI'");


            Label targetLabel = new Label("Proprietário " + proposal.getTarget().getOwnerID());
            targetLabel.setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-font-family: 'Microsoft JhengHei UI'");

            FontAwesomeIconView exchangeIcon = new FontAwesomeIconView(FontAwesomeIcon.EXCHANGE);
            exchangeIcon.setGlyphSize(20);
            exchangeIcon.setFill(Color.WHITE);

            tradeButton.getChildren().addAll(sourceLabel, exchangeIcon, targetLabel);

            tradeListContainer.getChildren().add(tradeButton);
        }
    }

    private void showDetails(Proposal proposal) {
        detailsPanel.setVisible(true);

        ownerSourceInTitle.setText("Proprietário " + proposal.getSource().getOwnerID());
        ownerTargetInTitle.setText("Proprietário " + proposal.getTarget().getOwnerID());

        //Cenário 1
        sourceOwnerLabel1.setText("(Propietário " + proposal.getSource().getOwnerID() + " fica com as props.)");
        targetOwnerLabel1.setText("(Propietário " + proposal.getTarget().getOwnerID() + " fica com as props.)");
        sp1Label1.setText("Propriedade " + proposal.getSp1().getObjectId());
        tp1Label1.setText("Propriedade " + proposal.getTp1().getObjectId());
        sp2Label1.setText("Propriedade " + proposal.getSp2().getObjectId());
        tp2Label1.setText("Propriedade " + proposal.getTp2().getObjectId());

        //Cenário 2
        sourceOwnerLabel2.setText("(Propietário " + proposal.getSource().getOwnerID() + " fica com as props.)");
        targetOwnerLabel2.setText("(Propietário " + proposal.getTarget().getOwnerID() + " fica com as props.)");
        sp2Label2.setText("Propriedade " + proposal.getSp2().getObjectId());
        tp2Label2.setText("Propriedade " + proposal.getTp2().getObjectId());
        sp1Label2.setText("Propriedade " + proposal.getSp1().getObjectId());
        tp1Label2.setText("Propriedade " + proposal.getTp1().getObjectId());


    }
}