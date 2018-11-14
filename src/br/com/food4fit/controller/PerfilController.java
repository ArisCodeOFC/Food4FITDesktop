package br.com.food4fit.controller;

import br.com.food4fit.Main;
import br.com.food4fit.model.Funcionario;
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
	private Label txtDtNasc;

	@FXML
	private Label txtDtEfetiva;

	@FXML
	private Label txtCpf;

	@FXML
	private ImageView imgFuncionario;

	@FXML
	private Label txtMatricula;

	@FXML
	private Label txtRg;

	@FXML
	private Pane paneConteudoPerfil;

	public Funcionario funcionario = Main.getPerfil();

	public void initialize() {
		paneConteudo.setStyle("visibility: false");
		paneConteudoPerfil.setStyle("visibility: false");
		txtNome.setText(funcionario.getNomeCompleto());
		txtEmail.setText(funcionario.getEmail());
		txtMatricula.setText(String.valueOf(funcionario.getMatricula()));
		txtDtNasc.setText(String.valueOf(funcionario.getDataNasciFormatada()));
		//txtDtEfetiva.setText(String.valueOf(funcionario.getDataAdmissaoFormatada()));

		txtCpf.setText(funcionario.getCpf());
		txtRg.setText(funcionario.getRg());
		txtSalario.setText(String.valueOf(funcionario.getSalario()));

	}

	public void listaPerfil() {

	}

	@FXML
	void salvar() {

	}

	@FXML
	void salvarPerfil() {

	}

	// Abrir o panel oculto

	@FXML
	void abrirConteudo() {
		paneConteudo.setStyle("visibility: true;");
	}

	@FXML
	void abrirConteudoPerfil() {
		paneConteudoPerfil.setStyle("visibility: true;");
	}

	// Fecha o panel que foi aberto
	@FXML
	void fechaConteudo() {
		paneConteudo.setStyle("visibility: false");

		txtSenhaUm.clear();
		txtSenhaDois.clear();
	}

	@FXML
	void fechaConteudoPerfil() {
		paneConteudoPerfil.setStyle("visibility: false");
	}
}
