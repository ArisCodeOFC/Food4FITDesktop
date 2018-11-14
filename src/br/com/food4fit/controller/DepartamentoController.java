package br.com.food4fit.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class DepartamentoController {

	@FXML
	private Pane paneConteudo;

	@FXML
    private Pane paneButton;

	ToggleSwitch button = new ToggleSwitch(false);

	public void initialize() {

		paneConteudo.setStyle("visibility: false");

		paneButton.getChildren().add(button);
	}


	@FXML
    void salvar() {

    }


	@FXML
	void abrirConteudo() {
		paneConteudo.setStyle("visibility: true;");
	}

	@FXML
	void fechaConteudo() {
		paneConteudo.setStyle("visibility: false");
	}
}
