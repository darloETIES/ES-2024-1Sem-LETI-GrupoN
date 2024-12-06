package grupo.n.gestaodoterritorio.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.File;
import java.net.URL;

import static java.lang.Thread.sleep;
import static javafx.application.Application.launch;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(ApplicationExtension.class)
class MainViewControllerTest {

    private MainViewController controller;
    private Stage stage;
    private boolean uploaded;

    @Start
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupo/n/gestaodoterritorio/main-view.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        this.stage = stage;
        stage.setScene(new Scene(root));
        stage.show();
        stage.setResizable(false);
        stage.toFront(); //trazer a janela para a frente
        uploaded = false;
    }

    // Método para preparar o arquivo simulado
    public void prepareFile() {
        // Simula o arquivo a ser carregado no método `loadFile`
        URL resourceUrl = getClass().getClassLoader().getResource("Madeira-Moodle-1.1.csv");
        String stringPath = resourceUrl.getPath();
        File testFile = new File(stringPath);
        controller.setFile(testFile); // Método criado apenas para teste
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Preparar o arquivo para o primeiro teste
        prepareFile();
    }

    @Test
    public void testUploadAndLoading(FxRobot robot) throws InterruptedException {
        // 1. Verificar o estado inicial dos elementos
        StackPane loadingIndicator = robot.lookup("#loadingIndicator").queryAs(StackPane.class);
        assertFalse(loadingIndicator.isVisible(), "O indicador de carregamento deve estar oculto inicialmente.");

        // 2. Verificar se o botão de upload está visível antes de clicar
        Node uploadFileBtn = robot.lookup("#uploadFileBtn").query();
        assertNotNull(uploadFileBtn, "O botão de upload deve existir.");
        assertTrue(uploadFileBtn.isVisible(), "O botão de upload deve estar visível.");

        // 3. Clicar no botão de upload
        robot.clickOn("#uploadFileBtn");

        // 4. Verificar se o indicador de carregamento aparece
        assertTrue(loadingIndicator.isVisible(), "O indicador de carregamento deve aparecer após o clique no botão.");

        // 5. Aguarda até o indicador de carregamento desaparecer
        waitUntilNodeIsInvisible(robot.lookup("#loadingIndicator").queryAs(StackPane.class), 20000);

        // 6. Verificar se o painel de upload desapareceu
        AnchorPane uploadPane = robot.lookup("#uploadPane").queryAs(AnchorPane.class);
        assertFalse(uploadPane.isVisible(), "O painel de upload deve estar oculto após o carregamento.");
        uploaded = true;
    }

    public void waitUntilNodeIsInvisible(Node node, int timeoutMillis) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        while (node.isVisible()) {
            if (System.currentTimeMillis() - startTime > timeoutMillis) {
                throw new RuntimeException("Timeout waiting for node to become invisible.");
            }
            Thread.sleep(100); // Verifica a cada 100ms
        }
    }

    public void hideUploadElements(){
        controller.getLoadingIndicator().setVisible(false);
        controller.getUploadFileBtn().setVisible(false);
        controller.getUploadPane().setVisible(false);
        controller.getUploadBackgroundPane().setVisible(false);
        controller.getContentPanel().setEffect(null);
    }

    @BeforeEach
    public void setUpTestSideMenuOptions(){
        prepareFile();
    }

    @Test
    public void testSideMenuButtons(FxRobot robot) throws InterruptedException {
        hideUploadElements();

        // Aguardar para garantir que a interface tenha sido completamente renderizada
        robot.sleep(200);

        // Verifica o estado inicial da barra lateral
        AnchorPane sideLeftMenu = robot.lookup("#side-left-menu").queryAs(AnchorPane.class);
        assertNotNull(sideLeftMenu, "O sideLeftMenu não foi encontrado.");
        assertEquals(45.0, sideLeftMenu.getPrefWidth(), "O menu lateral não tem a largura esperada inicialmente.");

        // Simula o hover sobre o menu para expandir
        robot.moveTo("#side-left-menu");
        robot.sleep(200); // Aguarde a animação
        assertEquals(110.0, sideLeftMenu.getPrefWidth(), "O menu lateral não expandiu após o hover.");

        // Simula o clique no botão de 'Grafos'
        robot.clickOn("#graph-option-btn");
        // Aqui você pode adicionar uma asserção para verificar se a ação associada ao botão foi realizada corretamente
        // Exemplo: assertTrue(controller.isGraphPageLoaded(), "A página de grafos não foi carregada.");

        // Simula o clique no botão de 'Áreas'
        robot.clickOn("#area-option-btn");
        // Verifique se a página correta foi carregada ou se algum outro efeito esperado ocorreu
        // Exemplo: assertTrue(controller.isAreasPageLoaded(), "A página de áreas não foi carregada.");

        // Simula o clique no botão de 'Trocas'
        robot.clickOn("#sug-trade-option-btn");
        // Exemplo: assertTrue(controller.isSuggestedTradesPageLoaded(), "A página de trocas não foi carregada.");

        // Simula a saída do hover para colapsar o menu
        robot.moveTo("#mainContentPanel");
        robot.sleep(200); // Aguarde a animação
        assertEquals(45.0, sideLeftMenu.getPrefWidth(), "O menu lateral não colapsou após o mouse sair.");
    }
}