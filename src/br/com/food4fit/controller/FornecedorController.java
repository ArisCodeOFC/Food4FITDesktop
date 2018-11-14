package br.com.food4fit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class FornecedorController {
	@FXML
	private TextField txtEndereco;

	@FXML
	private AnchorPane conteudo;

	@FXML
	private TableColumn colunaOpc;

	@FXML
	private TableColumn colunaRazao;

	@FXML
	private ComboBox comboEstado;

	@FXML
	private TextField txtTel;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txtCodigo;

	@FXML
	private TableColumn colunaCpf;

	@FXML
	private Pane paneConteudo;

	@FXML
	private TextField txtCnpj;

	@FXML
	private TableView tblFornecedor;

	@FXML
	private TableColumn colunaEmail;

	@FXML
	private TextField txtNomeFantasia;

	@FXML
	private TextField txtRepresentante;

	@FXML
	private TextArea txtObs;

	@FXML
	private TextField txtInsc;

	@FXML
	private TextField txtCidade;

	@FXML
	private TextField txtCep;

	@FXML
	private TableColumn colunaNome;

	@FXML
	private TextField txtRazao;

	@FXML
	private TextField txtBairro;

	public void initialize() {
		paneConteudo.setStyle("visibility: false");
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
