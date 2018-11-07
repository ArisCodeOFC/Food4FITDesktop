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
	void menuEstatisticas() {
		mudarTela("Funcionarios", "Estatísticas");
	}

	@FXML
	void menuFornecedor() {
		mudarTela("Funcionarios", "Fornecedores");
	}

	@FXML
	void menuFuncionario() {
		mudarTela("Funcionarios", "Funcionários");
	}

	@FXML
	void menuUsuario() {
		mudarTela("Funcionarios", "Usuários");
	}

	@FXML
	void menuNivel() {
		mudarTela("Funcionarios", "Nível de Acesso");
	}

	@FXML
	void menuDespesa() {
		mudarTela("Funcionarios", "Despesas");
	}

	@FXML
	void menuRelatorio() {
		mudarTela("Funcionarios", "Relários");
	}

	@FXML
	void menuBanco() {
		mudarTela("Funcionarios", "Banco");
	}

	@FXML
	void menuDepartamento() {
		mudarTela("Funcionarios", "Departamento");
	}

	@FXML
	void menuFaturamento() {
		mudarTela("Funcionarios", "Faturamento");
	}

	@FXML
	void menuUnidade() {
		mudarTela("UnidadeDeMedida", "Unidade de Medida");
	}

	@FXML
	void menuPerfil() {
		mudarTela("Funcionarios", "Meu Perfil");
	}

	@FXML
	void menuAjuda() {
		mudarTela("Funcionarios", "Ajuda");
	}

	@FXML
	void sair() {

		int result = Main.showConfirmDialog("Sim", "Sair", "Deseja sair da sua conta?", Alert.AlertType.WARNING);

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