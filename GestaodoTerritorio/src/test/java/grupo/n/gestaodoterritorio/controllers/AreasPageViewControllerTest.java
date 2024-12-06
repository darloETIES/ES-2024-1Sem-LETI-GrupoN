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
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import java.io.File;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class AreasPageViewControllerTest {
    private static PropertiesService propertiesService; // Tornar estático para reutilizar
    private AreasPageViewController controller;

    @Start
    void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupo/n/gestaodoterritorio/areas-page-view.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
        stage.setResizable(false);
        stage.toFront(); // Trazer a janela para frente
    }

    @BeforeAll
    static void prepareFile() throws Exception {
        // Simula o arquivo a ser carregado no método `loadFile`
        propertiesService = PropertiesService.getInstance();
        URL resourceUrl = AreasPageViewControllerTest.class.getClassLoader().getResource("Madeira-Moodle-1.1.csv");
        if (resourceUrl == null) {
            throw new IllegalArgumentException("Arquivo Madeira-Moodle-1.1.csv não encontrado.");
        }
        String stringPath = resourceUrl.getPath();
        File testFile = new File(stringPath);
        propertiesService.loadProperties(testFile.getPath());
    }

    // Testar a validação de campos obrigatórios
    @Test
    void testRequiredFieldsValidation(FxRobot robot) {
        robot.clickOn("#parishField");
        robot.write(""); // Deixa o campo vazio

        robot.clickOn("#countyField");
        robot.write(""); // Deixa o campo vazio

        robot.clickOn("#districtField");
        robot.write(""); // Deixa o campo vazio

        robot.clickOn("#ownerField");
        robot.write("5");

        robot.clickOn("#submitButton"); // Clique no botão de submit

        // Verifica que o alerta de erro foi mostrado
        assertTrue(robot.lookup(".alert").tryQuery().isPresent(), "Alerta não foi exibido!");
    }

    // Testar o comportamento quando todos os campos estão preenchidos corretamente, sem o proprietário
    @Test
    void testSubmitWithoutOwner(FxRobot robot) {
        // Preenche os campos
        robot.clickOn("#parishField").write("Arco da Calheta");
        robot.clickOn("#countyField").write("Calheta");
        robot.clickOn("#districtField").write("Ilha da Madeira (Madeira)");
        robot.clickOn("#ownerField").write(""); // Proprietário em branco

        robot.clickOn("#submitButton"); // Clique no botão de submit

        // Verifica se o modal foi exibido
        assertTrue(robot.lookup("#modalPane").tryQuery().isPresent(), "Modal não foi exibido!");
        assertEquals("Área Geográfica", controller.getModalTitleLabel().getText(), "Título do modal não está correto.");
    }

    // Testar o comportamento quando todos os campos estão preenchidos corretamente, com o proprietário
    @Test
    void testSubmitWithOwner(FxRobot robot) {
        // Preenche os campos
        robot.clickOn("#parishField").write("Arco da Calheta");
        robot.clickOn("#countyField").write("Calheta");
        robot.clickOn("#districtField").write("Ilha da Madeira (Madeira)");
        robot.clickOn("#ownerField").write("50"); // Proprietário fornecido

        robot.clickOn("#submitButton"); // Clique no botão de submit

        // Verifica se o modal foi exibido
        assertTrue(robot.lookup("#modalPane").tryQuery().isPresent(), "Modal não foi exibido!");
        assertEquals("Área Geográfica (Proprietário: 50)", controller.getModalTitleLabel().getText(), "Título do modal não está correto.");
    }

    // Testar o comportamento quando a área é 0, ou inexistente
    @Test
    void testSubmitWithZeroArea(FxRobot robot) {
        // Preenche os campos com valores que resultam em área inexistente
        robot.clickOn("#parishField").write("S. Domingos de Benfica");
        robot.clickOn("#countyField").write("Lisboa");
        robot.clickOn("#districtField").write("Lisboa");
        robot.clickOn("#ownerField").write(""); // Proprietário em branco

        robot.clickOn("#submitButton"); // Clique no botão de submit

        // Verifica se o alerta de erro foi mostrado
        assertTrue(robot.lookup(".alert").tryQuery().isPresent(), "Alerta não foi exibido!");
    }

    @Test
    void testCloseModal(FxRobot robot) {
        // Abre o modal
        robot.clickOn("#parishField").write("Arco da Calheta");
        robot.clickOn("#countyField").write("Calheta");
        robot.clickOn("#districtField").write("Ilha da Madeira (Madeira)");
        robot.clickOn("#ownerField").write("2");

        robot.clickOn("#submitButton"); // Abre o modal

        // Fecha o modal
        robot.clickOn("#closeModalButton");
    }
}