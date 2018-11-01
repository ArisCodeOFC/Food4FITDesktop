package br.com.food4fit.controller;

import java.io.IOException;
import java.util.Optional;

import org.omg.CORBA.INITIALIZE;

import br.com.food4fit.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PadraoLayoutController {

	@FXML
	private Pane main;

	@FXML
	private Label nomeTela;

	@FXML
	public void initialize() {
		mudarTela("Funcionarios", "Dashboard");
	}

	@FXML
	void menuHome() {
		mudarTela("Funcionarios", "Dashboard");

	}

	@FXML
	void sair() {

		int result = Main.showConfirmDialog("Sair", "Deseja sair da sua conta?");

		if (result == 1) {
			Main.abrirTela("Login");
		}

	}

	void mudarTela(String conteudo, String nome) {
		Pane xml;
		try {
			xml = FXMLLoader.load(PadraoLayoutController.class.getResource("../view/" + conteudo + ".fxml"));

			main.getChildren().clear();
			main.getChildren().add(xml);

			nomeTela.setText(nome);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}