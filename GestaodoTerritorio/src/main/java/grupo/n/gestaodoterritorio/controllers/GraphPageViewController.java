package grupo.n.gestaodoterritorio.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class GraphPageViewController {

    @FXML
    private ToggleButton btnPropriedades;

    @FXML
    private ToggleButton btnProprietarios;

    private ToggleGroup graphButtons;

    @FXML
    public void initialize() {
        graphButtons = new ToggleGroup();

        btnPropriedades.setToggleGroup(graphButtons);
        btnProprietarios.setToggleGroup(graphButtons);

        graphButtons.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle != null) {
                ToggleButton selectedButton = (ToggleButton) newToggle;

                // Tudo o que o botão faz vai aqui
            } else {
                System.out.println("Nenhum botão selecionado.");
            }
        });
    }
}
