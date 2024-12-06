package grupo.n.gestaodoterritorio.controllers;

import grupo.n.gestaodoterritorio.services.PropertiesService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class AreasPageViewController {
    @FXML
    private TextField parishField;

    @FXML
    private TextField countyField;

    @FXML
    private TextField districtField;

    @FXML
    private TextField ownerField;

    @FXML
    private AnchorPane modalPane;

    @FXML
    private Label modalTitleLabel;

    @FXML
    private Label modalParishLabel;

    @FXML
    private Label modalCountyLabel;

    @FXML
    private Label modalDistrictLabel;

    @FXML
    private Label modalAverageAreaLabel;

    @FXML
    private void handleSubmit() {
        //Obter os valores dos campos da GUI
        String parish = parishField.getText().trim();
        String county = countyField.getText().trim();
        String district = districtField.getText().trim();
        String owner = ownerField.getText().trim();

        // Validação de campos obrigatórios
        if (parish.isEmpty() || county.isEmpty() || district.isEmpty()) {
            showAlert("Erro", "Devem ser preenchidos todos os campos com '*'!");
            return;
        }




        // Determinar qual método executar
        if (owner.isEmpty()) {
            handleWithoutProprietario(parish, county, district);
        } else {
            handleWithProprietario(parish, county, district, owner);
        }


    }

    private void handleWithProprietario(String parish, String county, String district, String owner) {
        //Quando o proprietário é fornecido
        double averageArea = PropertiesService.getInstance().getAveragePropAreaByOwner(parish, county, district, owner);


        System.out.println("Com proprietário:");
        System.out.printf("Freguesia: %s, Concelho: %s, Distrito: %s, Proprietário: %s%n", parish, county, district, owner);
        showModal("Área Geográfica (Proprietário: " + owner + ")", parish, county, district, averageArea);

    }

    private void handleWithoutProprietario(String parish, String county, String district) {
        //Quando o proprietário não é fornecido
        double averageArea = PropertiesService.getInstance().getAverageArea(parish, county, district);


        System.out.println("Sem proprietário:");
        System.out.printf("Freguesia: %s, Concelho: %s, Distrito: %s%n", parish, county, district);
        showModal("Área Geográfica", parish, county, district, averageArea);
    }

    private void showModal(String title, String parish, String county, String district, double averageArea) {

        //Caso a área seja 0, mostra como inexistente
        if(averageArea <= 0){
            showAlert("Erro!", "Área inexistente!");
        }
        else {
            modalTitleLabel.setText(title);

            modalParishLabel.setText(parish);
            modalCountyLabel.setText(county);
            modalDistrictLabel.setText(district);

            // Arredondar a área média para 2 casas decimais
            String formattedArea = String.format("%.2f", averageArea);
            modalAverageAreaLabel.setText(formattedArea);

            // Exibir o modal
            modalPane.setVisible(true);
        }
    }

    @FXML
    private void closeModal() {
        //Fecha o modal
        modalPane.setVisible(false);
        System.out.println("Visibilidade do modal: " + modalPane.isVisible());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Label getModalTitleLabel(){
        return modalTitleLabel;
    }
}
