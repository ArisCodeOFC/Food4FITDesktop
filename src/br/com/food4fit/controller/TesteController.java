package br.com.food4fit.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class TesteController {

	@FXML
	private Pane botao;

	@FXML
	private AnchorPane conteudo;

	ToggleSwitch button = new ToggleSwitch(false);

	public void initialize() {
		botao.getChildren().add(button);
	}
}
