package br.com.food4fit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class PerfilController {
	@FXML
	private PasswordField txtSenhaUm;

	@FXML
	private PasswordField txtSenhaDois;

	@FXML
	private Text txtNome;

	@FXML
	private Text txtEmail;

	@FXML
	private Label txtSalario;

	@FXML
	private Pane paneConteudo;

	@FXML
	private Label txtGenero;

	@FXML
	private Label txtDtNasci;

	@FXML
	private Label txtDtEfetiva;

	@FXML
	private Label txtCpfv;

	@FXML
	private ImageView imgFuncionario;

	@FXML
	private Label txtMatricula;

	@FXML
	private Label txtRg;

	public void initialize() {
		paneConteudo.setStyle("visibility: false");
	}

	@FXML
	void salvar() {

	}

	// Abrir o panel oculto
	@FXML
	void abrirConteudo() {
		paneConteudo.setStyle("visibility: true;");
	}

	// Fecha o panel que foi aberto
	@FXML
	void fechaConteudo() {
		paneConteudo.setStyle("visibility: false");

		txtSenhaUm.clear();
		txtSenhaDois.clear();
	}
}
