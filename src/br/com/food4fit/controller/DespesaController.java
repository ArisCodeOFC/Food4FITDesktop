package br.com.food4fit.controller;

import br.com.food4fit.Main;
import br.com.food4fit.component.MaskedTextField;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.helper.FormHelper;
import br.com.food4fit.model.Despesa;
import br.com.food4fit.model.Fornecedor;
import br.com.food4fit.model.Funcionario;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DespesaController {
	private FormHelper formHelper = FormHelper.getInstance();
	private @FXML Pane paneConteudo;
	private @FXML RadioButton rdoFuncionario, rdoFornecedor;
	private @FXML ComboBox<Funcionario> comboFuncionario;
	private @FXML ComboBox<Fornecedor> comboFornecedor;
	private @FXML TableView<Despesa> tblDespesa;
	private @FXML TableColumn<Despesa, String> colunaTipoDespesa, colunaNome, colunaDataEmissao, colunaDataVencimento, colunaValor;
	private @FXML TableColumn<Despesa, Pane> colunaOpcoes;
	private @FXML ToggleGroup toggleTipo;
	private @FXML MaskedTextField txtDataEmissao, txtDataVencimento;
	private @FXML TextField txtValor;
	private @FXML TextArea txtDetalhes;

    private @FXML void initialize() {
		paneConteudo.setVisible(false);
		
		formHelper.addValidation(toggleTipo, FormHelper.REQUIRED);
		formHelper.addValidation(comboFornecedor, FormHelper.REQUIRED | FormHelper.VISIBLE_ONLY);
		formHelper.addValidation(comboFuncionario, FormHelper.REQUIRED | FormHelper.VISIBLE_ONLY);
		formHelper.addValidation(txtDataEmissao, FormHelper.REQUIRED | FormHelper.VALID_MASK | FormHelper.VALID_DATE);
		formHelper.addValidation(txtDataVencimento, FormHelper.REQUIRED | FormHelper.VALID_MASK | FormHelper.VALID_DATE);
		formHelper.addValidation(txtValor, FormHelper.REQUIRED | FormHelper.VALID_DOUBLE);
		formHelper.addValidation(txtDetalhes, FormHelper.REQUIRED);
		
		comboFornecedor.managedProperty().bind(comboFornecedor.visibleProperty());
		comboFornecedor.visibleProperty().bind(rdoFornecedor.selectedProperty());
		comboFuncionario.managedProperty().bind(comboFuncionario.visibleProperty());
		comboFuncionario.visibleProperty().bind(rdoFuncionario.selectedProperty());
		listarFornecedores();
		listarFuncionarios();
		listarDespesas();
    }

	private @FXML void salvar() {
		if (formHelper.validate()) {
			
		}
	}

	private @FXML void abrirConteudo() {
		paneConteudo.setStyle("visibility: true;");
	}

	private @FXML void fecharConteudo() {
		paneConteudo.setStyle("visibility: false");
	}
	
	private void listarFornecedores() {
		new RetrofitConfig().getFornecedorService().listar().enqueue(new Callback<Fornecedor[]>() {
			@Override
			public void onResponse(Call<Fornecedor[]> call, Response<Fornecedor[]> response) {
				if (response.code() == 500) {
					Platform.runLater(() -> 
						Main.showErrorDialog("Erro", "Erro ao obter lista de fornecedores", "Não foi possível obter a lista de fornecedores, tente novamente mais tarde.", AlertType.ERROR)
					);
					
				} else {
					comboFornecedor.setItems(FXCollections.observableArrayList(response.body()));
				}
			}
			
			@Override
			public void onFailure(Call<Fornecedor[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> 
					Main.showErrorDialog("Erro", "Erro ao obter lista de fornecedores", "Não foi possível obter a lista de fornecedores, tente novamente mais tarde.", AlertType.ERROR)
				);
			}
		});
	}

	private void listarFuncionarios() {
		Call<Funcionario[]> retorno = new RetrofitConfig().getFuncionarioService().lista();
		retorno.enqueue(new Callback<Funcionario[]>() {
			@Override
			public void onResponse(Call<Funcionario[]> call, Response<Funcionario[]> response) {
				if (response.code() == 500) {
					Platform.runLater(() -> 
						Main.showErrorDialog("Erro", "Erro ao obter lista de funcionários", "Não foi possível obter a lista de funcionários, tente novamente mais tarde.", AlertType.ERROR)
					);
					
				} else {
					comboFuncionario.setItems(FXCollections.observableArrayList(response.body()));
				}
			}
			
			@Override
			public void onFailure(Call<Funcionario[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> 
					Main.showErrorDialog("Erro", "Erro ao obter lista de funcionários", "Não foi possível obter a lista de funcionários, tente novamente mais tarde.", AlertType.ERROR)
				);
			}
		});
	}
	
	private void listarDespesas() {
		new RetrofitConfig().getDespesaService().listar().enqueue(new Callback<Despesa[]>() {
			@Override
			public void onResponse(Call<Despesa[]> call, Response<Despesa[]> response) {
				Platform.runLater(() -> {
					if (response.code() == 500) {
						Main.showErrorDialog("Erro", "Erro ao obter lista de despesas", "Não foi possível obter a lista de despesas, tente novamente mais tarde.", AlertType.ERROR);
					} else {
						montarTabela(response.body());
					}
				});
			}
			
			@Override
			public void onFailure(Call<Despesa[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> 
					Main.showErrorDialog("Erro", "Erro ao obter lista de despesas", "Não foi possível obter a lista de despesas, tente novamente mais tarde.", AlertType.ERROR)
				);
			}
		});
	}
	
	private void montarPainel(Despesa despesa) {
		ImageView imgEditar = new ImageView(new Image(Main.class.getResource("/br/com/food4fit/assets/icons/editar-c.png").toString()));
		imgEditar.prefHeight(15);
		imgEditar.prefWidth(15);
		imgEditar.setCursor(Cursor.HAND);
		imgEditar.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			
		});
		
		ImageView imgExcluir = new ImageView(new Image(Main.class.getResource("/br/com/food4fit/assets/icons/cancelar-c.png").toString()));
		imgExcluir.prefHeight(15);
		imgExcluir.prefWidth(15);
		imgExcluir.setCursor(Cursor.HAND);
		imgExcluir.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			
		});
		
		HBox hBox = new HBox(imgEditar, imgExcluir);
		hBox.setPrefHeight(15);
		hBox.setPrefWidth(15);
		hBox.setSpacing(10);
		hBox.setPadding(new Insets(0, 0, 0, 32));
		despesa.setPaneOpcoes(hBox);
	}
	
	private void montarTabela(Despesa[] despesas) {
		for (Despesa despesa : despesas) {
			montarPainel(despesa);
		}

		colunaTipoDespesa.setCellValueFactory(new PropertyValueFactory<Despesa, String>("tipo"));
		colunaNome.setCellValueFactory(new PropertyValueFactory<Despesa, String>("nome"));
		colunaDataEmissao.setCellValueFactory(new PropertyValueFactory<Despesa, String>("dataEmissaoFormatada"));
		colunaDataVencimento.setCellValueFactory(new PropertyValueFactory<Despesa, String>("dataVencimentoFormatada"));
		colunaValor.setCellValueFactory(new PropertyValueFactory<Despesa, String>("valorFormatado"));
		colunaOpcoes.setCellValueFactory(new PropertyValueFactory<Despesa, Pane>("paneOpcoes"));
		tblDespesa.setItems(FXCollections.observableArrayList(despesas));
	}
}
