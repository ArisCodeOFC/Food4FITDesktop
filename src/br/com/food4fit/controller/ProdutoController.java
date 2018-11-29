package br.com.food4fit.controller;

import br.com.food4fit.Main;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.model.Cargo;
import br.com.food4fit.model.ClassificacaoProduto;
import br.com.food4fit.model.Produto;
import br.com.food4fit.model.UnidadeDeMedida;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdutoController {
	private @FXML TableView<Produto> tblProduto;
	private @FXML TableColumn<Produto, String> colunaProduto, colunaUnidade;
	private @FXML TableColumn<Produto, Pane> colunaOpcProduto;
	private @FXML TableColumn<Produto, Integer> colunaCodigo;
	private @FXML TableView<ClassificacaoProduto> tblClassificacao;
	private @FXML TableColumn<ClassificacaoProduto, String> colunaClassificacao;
	private @FXML TableColumn<ClassificacaoProduto, Pane> colunaOpcClassificacao;
	private @FXML Pane paneClassificacao, paneProduto;
	private @FXML TextField txtNomeProduto, txtClassificacao;
	private @FXML ComboBox<ClassificacaoProduto> comboClassificacao;
	private @FXML ComboBox<UnidadeDeMedida> comboUnidade;
	private @FXML TextArea txtDescricao;


	private @FXML void initialize() {
		paneClassificacao.setStyle("visibility: false");
		paneProduto.setStyle("visibility: false");
	}

	//******************************************************************
	//Produto
	private @FXML void salvarProduto() {

	}

	public void listarProduto(){

	}

	public void listarUnidade(){

	}

	public void listarClassificacaoProduto(){

	}
	//******************************************************************
	//Classificação do Produto
	private @FXML void salvarEspecificacao() {

	}

	public void listarClassificacao(){
		Call<ClassificacaoProduto[]> retorno = new RetrofitConfig().getClassificacaoProdutoService().listar();
		retorno.enqueue(new Callback<ClassificacaoProduto[]>() {
			@Override
			public void onResponse(Call<ClassificacaoProduto[]> call, Response<ClassificacaoProduto[]> response) {
				if (response.code() == 500) {
					Platform.runLater(() ->
						Main.showErrorDialog("Erro", "Erro ao obter lista de classificações", "Não foi possível obter a lista de classificações, tente novamente mais tarde.", AlertType.ERROR)
					);

				} else {
					montarTabelaClassificacao(response.body());
				}
			}

			@Override
			public void onFailure(Call<ClassificacaoProduto[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() ->
					Main.showErrorDialog("Erro", "Erro ao obter lista de classificações", "Não foi possível obter a lista de classificações, tente novamente mais tarde.", AlertType.ERROR)
				);
			}
		});
	}


	private void montarTabelaClassificacao(ClassificacaoProduto[] classificacoes) {
		for (ClassificacaoProduto classificacao : classificacoes) {
			montarPainelClassificacao(classificacao);
		}

		colunaClassificacao.setCellValueFactory(new PropertyValueFactory<ClassificacaoProduto, String>("titulo"));
	}


	private void montarPainelClassificacao(ClassificacaoProduto classificacao) {
		Image editImg = new Image(Main.class.getResource("assets/icons/editar-c.png").toString());
		Image cancelImg = new Image(Main.class.getResource("assets/icons/cancelar-c.png").toString());

		ImageView editView = new ImageView();
		editView.prefHeight(15);
		editView.prefWidth(15);
		editView.setImage(editImg);
		editView.setStyle("-fx-cursor: hand;");

		editView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			editarClassificacao(classificacao);
			event.consume();
		});

		ImageView deleteView = new ImageView();
		deleteView.prefHeight(15);
		deleteView.prefWidth(15);
		deleteView.setImage(cancelImg);
		deleteView.setStyle("-fx-cursor: hand");
		deleteView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			excluirClassificacao(classificacao);
		});

		HBox hBox = new HBox(editView, deleteView);
		hBox.setPrefHeight(15);
		hBox.setPrefWidth(15);
		hBox.setStyle("-fx-padding: 0 0 0 32; -fx-spacing:10;");
		classificacao.setPaneOpcoes(hBox);
	}

	private void editarClassificacao(ClassificacaoProduto classificacao) {
		//formHelper.setObjectData(classificacao);
		abrirClassficacao();
	}

	private void excluirClassificacao(ClassificacaoProduto classificacao) {
		int resposta = Main.showConfirmDialog("Excluir", "Excluir", "Deseja realmente excluir essa classificação de produto?", AlertType.CONFIRMATION);
		if (resposta == 1) {
			Call<Void> retorno = new RetrofitConfig().getClassificacaoProdutoService().excluir(classificacao.getId());
			retorno.enqueue(new Callback<Void>() {
				@Override
				public void onResponse(Call<Void> call, Response<Void> response) {
					Platform.runLater(() -> {
						if (response.code() == 500) {
							Main.showErrorDialog("Erro", "Erro ao excluir a classificação", "Não foi possível excluir a classificação, tente novamente mais tarde.", AlertType.ERROR);
						} else {
							tblClassificacao.getItems().remove(classificacao);
							Main.showInfDialog("Sucesso", "", "Classificação de produto excluída com secesso!!!");
						}
					});
				}

				@Override
				public void onFailure(Call<Void> call, Throwable t) {
					t.printStackTrace();
					Platform.runLater(() -> {
						Main.showErrorDialog("Erro", "Erro ao excluir funcionário", "Não foi possível excluir o funcionário, tente novamente mais tarde.", AlertType.ERROR);
					});
				}
			});
		}
	}

	//******************************************************************
	// Abrir o panel oculto
	private @FXML void abrirProduto() {
		paneProduto.setStyle("visibility: true");
	}

	private @FXML void abrirClassficacao() {
		paneClassificacao.setStyle("visibility: true");
	}

	// Fecha o panel que foi aberto
	private @FXML void fecharProduto() {
		paneProduto.setStyle("visibility: false");
		txtNomeProduto.clear();
		txtDescricao.clear();
		comboClassificacao.setValue(null);
		comboUnidade.setValue(null);
	}

	private @FXML void fecharEspecificacao() {
		paneClassificacao.setStyle("visibility: false");
		txtClassificacao.clear();
	}
}