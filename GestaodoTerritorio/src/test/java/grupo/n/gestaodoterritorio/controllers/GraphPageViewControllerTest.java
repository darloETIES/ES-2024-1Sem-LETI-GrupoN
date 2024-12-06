package grupo.n.gestaodoterritorio.controllers;

import grupo.n.gestaodoterritorio.services.PropertiesService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ApplicationExtension.class)
class GraphPageViewControllerTest {

    private static final String PROPERTIES_IMAGE_CLASSPATH = "/grupo/n/gestaodoterritorio/images/grafoProperties.png";
    private static final String OWNERS_IMAGE_CLASSPATH = "/grupo/n/gestaodoterritorio/images/grafoOwners.png";

    private PropertiesService propertiesService;
    private GraphPageViewController controller;
    private Stage stage;

    @Start
    void start(Stage stage) throws IOException {
        propertiesService = PropertiesService.getInstance();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupo/n/gestaodoterritorio/graph-page-view.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        this.stage = stage;
        stage.setScene(new Scene(root));
        stage.show();
        stage.setResizable(false);
        stage.toFront(); // Trazer a janela para frente
    }

    @BeforeEach
    void prepareFile() throws Exception {
        // Carregar o arquivo CSV de propriedades
        URL resourceUrl = getClass().getClassLoader().getResource("Madeira-Moodle-1.1.csv");
        assertNotNull(resourceUrl, "Arquivo CSV de propriedades não encontrado no classpath.");
        String stringPath = resourceUrl.getPath();
        propertiesService.loadProperties(stringPath);

        if (getClass().getResource(OWNERS_IMAGE_CLASSPATH) == null) {
            System.out.println("Imagem de proprietários não encontrada. Gerando...");
            controller.setImage("Owners"); // Gera a imagem, se não encontrada
        } else {
            System.out.println("Imagem de proprietários já existe.");
        }
    }

    @Test
    void testInitialize() {
        // Testa se os componentes estão corretamente inicializados
        assertNotNull(controller.getBtnPropriedades(), "O botão 'Propriedades' não foi inicializado.");
        assertNotNull(controller.getBtnProprietarios(), "O botão 'Proprietários' não foi inicializado.");
        assertNotNull(controller.getGraphImageView(), "A imagem do grafo não foi inicializada.");
    }

    @BeforeEach
    void prepareFileForOwners() throws Exception {
        // Carregar o arquivo CSV de propriedades
        URL resourceUrl = getClass().getClassLoader().getResource("Madeira-Moodle-1.1.csv");
        assertNotNull(resourceUrl, "Arquivo CSV de propriedades não encontrado no classpath.");
        String stringPath = resourceUrl.getPath();
        propertiesService.loadProperties(stringPath);

        if (getClass().getResource(OWNERS_IMAGE_CLASSPATH) == null) {
            System.out.println("Imagem de proprietários não encontrada. Gerando...");
            controller.setImage("Owners"); // Gera a imagem, se não encontrada
        } else {
            System.out.println("Imagem de proprietários já existe.");
        }
    }

    @Test
    void testSelectProprietariosButton() {
        controller.getBtnProprietarios().fire();
        controller.handleSubmit();
    }

    /*
    @BeforeEach
    void prepareFileForProperties() throws Exception {
        // Carregar o arquivo CSV de propriedades
        URL resourceUrl = getClass().getClassLoader().getResource("Madeira-Moodle-1.1.csv");
        assertNotNull(resourceUrl, "Arquivo CSV de propriedades não encontrado no classpath.");
        String stringPath = resourceUrl.getPath();
        propertiesService.loadProperties(stringPath);

        if (getClass().getResource(PROPERTIES_IMAGE_CLASSPATH) == null) {
            System.out.println("Imagem de propriedades não encontrada. Gerando...");
            controller.setImage("Properties"); // Gera a imagem, se não encontrada
        } else {
            System.out.println("Imagem de propriedades já existe.");
        }
    }


    @Test
    void testSelectPropriedadesButton() {
        controller.getBtnPropriedades().fire();
        controller.handleSubmit();
    }


     */
    @Test
    void testResetButton() {
        controller.setImage("Owners");
        controller.getGraphImageView().setTranslateX(50);
        controller.getGraphImageView().setTranslateY(50);
        controller.getGraphImageView().setScaleX(2.0);
        controller.getGraphImageView().setScaleY(2.0);

        // Chama o método de reset
        controller.handleReset();

        // Verifica se o reset funcionou corretamente
        assertEquals(0, controller.getGraphImageView().getTranslateX(), "A posição X não foi resetada para 0.");
        assertEquals(0, controller.getGraphImageView().getTranslateY(), "A posição Y não foi resetada para 0.");
        assertEquals(1.0, controller.getGraphImageView().getScaleX(), "O zoom X não foi resetado para 1.0.");
        assertEquals(1.0, controller.getGraphImageView().getScaleY(), "O zoom Y não foi resetado para 1.0.");
    }


    @Test
    void testNoImageLoadedBehavior() {
        controller.getBtnPropriedades().setSelected(false);
        controller.getBtnProprietarios().setSelected(false);

        controller.setImage(""); // Limpa a imagem

        // Verifica que não há imagem carregada
        assertNull(controller.getGraphImageView().getImage(), "A imagem não deveria estar carregada.");
        assertTrue(controller.getNoImageContainer().isVisible(), "A mensagem de 'imagem não carregada' deveria estar visível.");
    }
}