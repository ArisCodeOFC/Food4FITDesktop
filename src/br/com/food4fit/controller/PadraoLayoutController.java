package br.com.food4fit.controller;

import java.util.Optional;

import br.com.food4fit.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PadraoLayoutController {

    @FXML
    private Label nomeTela;


	@FXML
    void menuHome(ActionEvent event) {
		System.out.println(event.getSource());

    }

	  @FXML
	    void sair() {

		   PadraoLayoutController.showConfirmDialog("Sair", "Deseja sair da sua conta?");


	    }


	  public static Optional<ButtonType> showConfirmDialog(String titulo, String texto){
			Alert dialogo = new Alert(Alert.AlertType.WARNING);
          ButtonType btnSim = new ButtonType("Sim");
          ButtonType btnNao = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

          dialogo.setTitle(titulo);
          dialogo.setHeaderText(texto);
          dialogo.getButtonTypes().setAll(btnSim,  btnNao);

          Stage stage = (Stage) dialogo.getDialogPane().getScene().getWindow();

          // Adiciona uma imagen no icone do dialog
          stage.getIcons().add(new Image(Main.class.getResource("assets/icons/favicon.png").toString()));
          Optional<ButtonType> result = dialogo.showAndWait();

           System.out.println(result);

          return result;
          //dialogo.showAndWait().ifPresent(b -> {if (b == btnSim) {Main.abrirTela("Login");}}

		}
}
