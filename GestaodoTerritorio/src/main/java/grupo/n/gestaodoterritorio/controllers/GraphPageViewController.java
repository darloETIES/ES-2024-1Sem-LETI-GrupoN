package grupo.n.gestaodoterritorio.controllers;

import grupo.n.gestaodoterritorio.models.Graph;
import grupo.n.gestaodoterritorio.models.Owner;
import grupo.n.gestaodoterritorio.models.Property;
import grupo.n.gestaodoterritorio.services.PropertiesService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;

import java.io.File;

public class GraphPageViewController {

    @FXML
    private ToggleButton btnPropriedades;

    @FXML
    private ToggleButton btnProprietarios;

    @FXML
    private Button btnReset;

    @FXML
    private ToggleGroup graphButtons;

    @FXML
    private ImageView graphImageView;

    @FXML
    private ScrollPane imageScrollPane;

    @FXML
    private VBox noImageContainer; // Mensagem de imagem não carregada

    private double zoomFactor = 1.0;

    private double initialMouseX;
    private double initialMouseY;

    @FXML
    public void initialize() {

        // Configurar zoom com rolagem do mouse
        imageScrollPane.addEventFilter(ScrollEvent.SCROLL, this::handleZoom);

        // Configurar pan com clique e arraste
        graphImageView.setOnMousePressed(this::startDragging);
        graphImageView.setOnMouseDragged(this::dragImage);

        // Mudar o cursor para uma "mão" quando arrastar
        graphImageView.setOnMouseEntered(event -> {
            graphImageView.getScene().setCursor(javafx.scene.Cursor.HAND);
        });
        graphImageView.setOnMouseExited(event -> {
            graphImageView.getScene().setCursor(javafx.scene.Cursor.DEFAULT);
        });

        // Controle de toggle dos botões
        graphButtons = new ToggleGroup();

        btnPropriedades.setToggleGroup(graphButtons);
        btnProprietarios.setToggleGroup(graphButtons);

        // Ouvir mudanças nos botões de alternância
        graphButtons.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle != null) {
                ToggleButton selectedButton = (ToggleButton) newToggle;

                String imgName = "";

                if (selectedButton == btnPropriedades) {
                    imgName = "Properties"; // Nome da imagem para "propriedades"
                } else if (selectedButton == btnProprietarios) {
                    imgName = "Owners"; // Nome da imagem para "proprietários"
                }

                // Chama o método para carregar ou gerar a imagem
                setImage(imgName);
            } else {
                System.out.println("Nenhum botão selecionado.");
                // Remove a imagem da visualização
                graphImageView.setImage(null);
                noImageContainer.setVisible(true); // Exibir a mensagem
                btnReset.setVisible(false); // Ocultar botão

            }
        });

    }

    // Método para verificar se a imagem existe no diretório /images
    public boolean imageExists(String imageName) {
        File imageFile = new File("GestaodoTerritorio/src/main/resources/images/grafo" + imageName + ".png");
        return imageFile.exists();
    }

    // Método para carregar ou gerar a imagem
    @FXML
    public void setImage(String imgName) {
        File imageFile = new File("GestaodoTerritorio/src/main/resources/images/grafo" + imgName + ".png");
        // Verificar se a imagem já existe
        if (!imageExists(imgName)) {
            // Se a imagem não existir, gerar o grafo
            if (imgName.equals("Properties")) {
                Graph<Property> propertyGraph = new Graph<>();
                propertyGraph.createGraph(PropertiesService.getInstance().getProperties());
                propertyGraph.drawGraph(imgName); // Gerar grafo de propriedades
            } else if (imgName.equals("Owners")) {
                Graph<Owner> ownerGraph = new Graph<>();
                ownerGraph.createGraph(PropertiesService.getInstance().getOwners());
                ownerGraph.drawGraph(imgName); // Gerar grafo de proprietários
            }
        }

        // Carregar a imagem gerada
        String filePath = imageFile.toURI().toString();
        Image image = new Image(filePath);

        // Verificar se a imagem foi carregada corretamente
        if (image.isError()) {
            // Caso haja erro ao carregar, o botão de reset deve ficar invisível
            graphImageView.setImage(null);
            btnReset.setVisible(false);
            noImageContainer.setVisible(true);
        } else {
            // Se a imagem for carregada corretamente, mostra o botão de reset
            graphImageView.setImage(image);
            btnReset.setVisible(true);
            noImageContainer.setVisible(false);
        }
    }

    @FXML
    public void handleReset() {
        // Resetando a posição da imagem
        graphImageView.setTranslateX(0);
        graphImageView.setTranslateY(0);

        // Resetando o zoom da imagem
        zoomFactor = 1.0;
        graphImageView.setScaleX(zoomFactor);
        graphImageView.setScaleY(zoomFactor);
    }

    // Método de zoom: aumentar/diminuir a imagem com rolagem do mouse
    private void handleZoom(ScrollEvent event) {
        if (event.getDeltaY() > 0) {
            zoomFactor *= 1.1; // Zoom in
        } else {
            zoomFactor /= 1.1; // Zoom out
        }

        // Ajustar a escala da imagem
        graphImageView.setScaleX(zoomFactor);
        graphImageView.setScaleY(zoomFactor);

        event.consume();  // Impedir que o evento seja propagado
    }

    // Iniciar o movimento da imagem com o mouse
    private void startDragging(MouseEvent event) {
        initialMouseX = event.getSceneX();
        initialMouseY = event.getSceneY();
    }

    // Mover a imagem enquanto o mouse é arrastado
    private void dragImage(MouseEvent event) {
        double deltaX = event.getSceneX() - initialMouseX;
        double deltaY = event.getSceneY() - initialMouseY;

        // Mover a imagem
        graphImageView.setTranslateX(graphImageView.getTranslateX() + deltaX);
        graphImageView.setTranslateY(graphImageView.getTranslateY() + deltaY);

        // Atualizar a posição inicial
        initialMouseX = event.getSceneX();
        initialMouseY = event.getSceneY();
    }

    @FXML
    public void handleSubmit() {
        // Definir o nome da imagem com base no botão pressionado
        String imgName = "";

        if (btnPropriedades.isSelected()) {
            imgName = "Properties";
        } else if (btnProprietarios.isSelected()) {
            imgName = "Owners";
        }

        // Verifique se a imagem já existe ou gere-a
        setImage(imgName);
    }



    public ToggleButton getBtnPropriedades(){
        return btnPropriedades;
    }

    public ToggleButton getBtnProprietarios(){
        return btnProprietarios;
    }

    public ImageView getGraphImageView(){
        return graphImageView;
    }

    public VBox getNoImageContainer(){
        return noImageContainer;
    }


}