package br.com.food4fit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class DespesaController {

	 @FXML
	    private TableColumn<?, ?> colunaDtEmissao;

	    @FXML
	    private ToggleGroup despesa;

	    @FXML
	    private TableColumn<?, ?> colunaOpc;

	    @FXML
	    private TextField txtDtVencimento;

	    @FXML
	    private Pane paneConteudo;

	    @FXML
	    private AnchorPane conteudo;

	    @FXML
	    private TableColumn<?, ?> colunaValor;

	    @FXML
	    private TextField txtDtEmissao;

	    @FXML
	    private TableView<?> tblDespesa;

	    @FXML
	    private RadioButton rdoFornecedor;

	    @FXML
	    private TableColumn<?, ?> colunaTipoDespesa;

	    @FXML
	    private TextArea txtDetalhes;

	    @FXML
	    private ComboBox<?> comboNome;

	    @FXML
	    private TextField txtValor;

	    @FXML
	    private TableColumn<?, ?> colunaDtVencimento;

	    @FXML
	    private RadioButton rdoFuncionario;

	    @FXML
	    private TableColumn<?, ?> colunaNome;


	    private @FXML void initialize() {
			paneConteudo.setStyle("visibility: false");
	    }


	    @FXML
	    void salvar() {

	    }


		// Abrir o panel oculto
		private @FXML void abrirConteudo() {
			paneConteudo.setStyle("visibility: true;");
		}

		// Fecha o panel que foi aberto
		private @FXML void fecharConteudo() {
			paneConteudo.setStyle("visibility: false");


		}
}
