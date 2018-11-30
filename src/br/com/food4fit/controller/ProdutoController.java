package br.com.food4fit.controller;

import java.util.Arrays;

import br.com.food4fit.Main;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.helper.FormHelper;
import br.com.food4fit.model.ClassificacaoProduto;
import br.com.food4fit.model.Produto;
import br.com.food4fit.model.UnidadeDeMedida;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
	private FormHelper formHelperProduto = FormHelper.getInstance();
	private FormHelper formHelperClassificacao = FormHelper.getInstance();

	private @FXML void initialize() {
		paneClassificacao.setStyle("visibility: false");
		paneProduto.setStyle("visibility: false");
		formHelperProduto.addValidation(txtNomeProduto, FormHelper.REQUIRED);
		formHelperProduto.addValidation(txtDescricao, FormHelper.REQUIRED);
		formHelperProduto.addValidation(comboClassificacao, FormHelper.REQUIRED);
		formHelperProduto.addValidation(comboUnidade, FormHelper.REQUIRED);
		formHelperClassificacao.addValidation(txtClassificacao, FormHelper.REQUIRED);
		listarClassificacao();
		listarProduto();
		listarUnidade();
	}

	// ******************************************************************
	// Produto
	private @FXML void salvarProduto() {
		if (formHelperProduto.validate()) {
			String produtoNome = txtNomeProduto.getText();
			String descricao = txtDescricao.getText();
			UnidadeDeMedida unidadade = (UnidadeDeMedida) comboUnidade.getValue();
			ClassificacaoProduto classificacao = (ClassificacaoProduto) comboClassificacao.getValue();

			Produto produto;

			if (formHelperProduto.getObjectData() != null) {
				produto = (Produto) formHelperProduto.getObjectData();
			} else {
				produto = new Produto();
			}

			produto.setTitulo(produtoNome);
			produto.setDescricao(descricao);
			produto.seClassificacao(classificacao);
			produto.setUnidadeMedida(unidadade);

			if (formHelperProduto.getObjectData() == null) {
				Call<Produto> ret = new RetrofitConfig().getProdutoService().salvar(produto);
				ret.enqueue(new Callback<Produto>() {
					@Override
					public void onResponse(Call<Produto> call, Response<Produto> response) {
						Platform.runLater(() -> {
							if (response.code() == 500) {
								Main.showErrorDialog("Erro", "Erro ao inserir produto",
										"N�o foi poss�vel inserir o produto, tente novamente mais tarde.",
										AlertType.ERROR);
							} else {
								montarPainelProduto(response.body());
								tblProduto.getItems().add(response.body());
								Main.showInfDialog("Sucesso", "", "Produto cadastrado com sucesso!!!");
								fecharProduto();
							}
						});

					}

					@Override
					public void onFailure(Call<Produto> call, Throwable t) {
						t.printStackTrace();
						Platform.runLater(() -> Main.showErrorDialog("Erro", "Erro ao inserir produto",
								"N�o foi poss�vel inserir o produto, tente novamente mais tarde.", AlertType.ERROR));

					}
				});
			} else {
				Call<Void> ret = new RetrofitConfig().getProdutoService().editar(produto, produto.getId());
				ret.enqueue(new Callback<Void>() {
					@Override
					public void onResponse(Call<Void> call, Response<Void> response) {
						Platform.runLater(() -> {
							if (response.code() == 500) {
								Main.showErrorDialog("Erro", "Erro ao atualizar produto",
										"N�o foi poss�vel atualizar o produto, tente novamente mais tarde.",
										AlertType.ERROR);
							} else {
								formHelperProduto.setObjectData(null);
								tblProduto.refresh();
								Main.showInfDialog("Sucesso", "", "Produto atualizado com sucesso!!!");
								fecharProduto();
							}
						});
					}

					@Override
					public void onFailure(Call<Void> call, Throwable t) {
						t.printStackTrace();
						Platform.runLater(() -> Main.showErrorDialog("Erro", "Erro ao atualizar produto",
								"N�o foi poss�vel atualizar o produto, tente novamente mais tarde.", AlertType.ERROR));
					}
				});
			}
		}
	}

	public void listarProduto() {
		Call<Produto[]> ret = new RetrofitConfig().getProdutoService().listar();
		ret.enqueue(new Callback<Produto[]>() {
			@Override
			public void onResponse(Call<Produto[]> call, Response<Produto[]> response) {
				if (response.code() == 500) {
					Platform.runLater(() -> Main.showErrorDialog("Erro", "Erro ao obter lista de produtos",
							"N�o foi poss�vel obter a lista de produtos, tente novamente mais tarde.",
							AlertType.ERROR));

				} else {
					montarTabelaProduto(response.body());
				}
			}

			@Override
			public void onFailure(Call<Produto[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> Main.showErrorDialog("Erro", "Erro ao obter lista de produtos",
						"N�o foi poss�vel obter a lista de produtos, tente novamente mais tarde.", AlertType.ERROR));

			}
		});
	}

	private void montarTabelaProduto(Produto[] produtos) {
		for (Produto produto : produtos) {
			montarPainelProduto(produto);
		}

		colunaProduto.setCellValueFactory(new PropertyValueFactory<Produto, String>("titulo"));
		colunaCodigo.setCellValueFactory(new PropertyValueFactory<Produto, Integer>("id"));
		colunaUnidade.setCellValueFactory(new PropertyValueFactory<Produto, String>("unidadeMedida"));
		colunaOpcProduto.setCellValueFactory(new PropertyValueFactory<>("paneOpcoes"));
		tblProduto.setItems(FXCollections.observableArrayList(produtos));
	}

	private void montarPainelProduto(Produto produto) {
		Image editImg = new Image(Main.class.getResource("assets/icons/editar-c.png").toString());
		Image cancelImg = new Image(Main.class.getResource("assets/icons/cancelar-c.png").toString());

		ImageView editView = new ImageView();
		editView.prefHeight(15);
		editView.prefWidth(15);
		editView.setImage(editImg);
		editView.setStyle("-fx-cursor: hand;");

		editView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			editarProduto(produto);
			event.consume();
		});

		ImageView deleteView = new ImageView();
		deleteView.prefHeight(15);
		deleteView.prefWidth(15);
		deleteView.setImage(cancelImg);
		deleteView.setStyle("-fx-cursor: hand");
		deleteView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			excluirProduto(produto);
		});

		HBox hBox = new HBox(editView, deleteView);
		hBox.setPrefHeight(15);
		hBox.setPrefWidth(15);
		hBox.setStyle("-fx-padding: 0 0 0 32; -fx-spacing:10;");
		produto.setPaneOpcoes(hBox);
	}

	public void listarUnidade() {
		comboUnidade.setCursor(Cursor.WAIT);
		Call<UnidadeDeMedida[]> ret = new RetrofitConfig().getUnidadeDeMedidaService().lista();
		ret.enqueue(new Callback<UnidadeDeMedida[]>() {
			@Override
			public void onResponse(Call<UnidadeDeMedida[]> call, Response<UnidadeDeMedida[]> response) {
				Platform.runLater(() -> {
					comboUnidade.setCursor(Cursor.HAND);
					if (response.code() == 500) {
						Main.showErrorDialog("Erro", "Erro ao obter lista de unidade de medida",
								"N�o foi poss�vel obter a lista de unidade de medida, tente novamente mais tarde.",
								AlertType.ERROR);
					} else {
						comboUnidade.setItems(FXCollections.observableArrayList(Arrays.asList(response.body())));
					}
				});

			}

			@Override
			public void onFailure(Call<UnidadeDeMedida[]> call, Throwable t) {
				Main.showErrorDialog("Erro", "Erro ao obter lista de unidade de medida",
						"N�o foi poss�vel obter a lista de unidade de medida, tente novamente mais tarde.",
						AlertType.ERROR);
			}
		});
	}

	private void editarProduto(Produto produto) {
		formHelperProduto.setObjectData(produto);
		txtNomeProduto.setText(produto.getTitulo());
		txtDescricao.setText(produto.getDescricao());
		comboClassificacao.setValue(produto.getClassificacao());
		comboUnidade.setValue(produto.getUnidadeMedida());
		abrirProduto();
	}

	private void excluirProduto(Produto produto) {
		int resposta = Main.showConfirmDialog("Excluir", "Excluir", "Deseja realmente excluir esse produto?",
				AlertType.CONFIRMATION);
		if (resposta == 1) {
			Call<Void> retorno = new RetrofitConfig().getProdutoService().excluir(produto.getId());
			retorno.enqueue(new Callback<Void>() {
				@Override
				public void onResponse(Call<Void> call, Response<Void> response) {
					Platform.runLater(() -> {
						if (response.code() == 500) {
							Main.showErrorDialog("Erro", "Erro ao excluir o produto",
									"N�o foi poss�vel excluir o produto, tente novamente mais tarde.", AlertType.ERROR);
						} else {
							tblProduto.getItems().remove(produto);
							Main.showInfDialog("Sucesso", "", "Produto exclu�do com sucesso!!!");
						}
					});
				}

				@Override
				public void onFailure(Call<Void> call, Throwable t) {
					t.printStackTrace();
					Platform.runLater(() -> {
						Main.showErrorDialog("Erro", "Erro ao excluir produto",
								"N�o foi poss�vel excluir o produto, tente novamente mais tarde.", AlertType.ERROR);
					});
				}
			});
		}
	}

	// ******************************************************************
	// Classifica��o do Produto
	private @FXML void salvarClassificacao() {
		if (formHelperClassificacao.validate()) {
			String classificacao = txtClassificacao.getText();

			ClassificacaoProduto classificacaoProduto;

			if (formHelperClassificacao.getObjectData() != null) {
				classificacaoProduto = (ClassificacaoProduto) formHelperClassificacao.getObjectData();
			} else {
				classificacaoProduto = new ClassificacaoProduto();
			}

			classificacaoProduto.setTitulo(classificacao);

			if (formHelperClassificacao.getObjectData() == null) {
				Call<ClassificacaoProduto> ret = new RetrofitConfig().getClassificacaoProdutoService()
						.salvar(classificacaoProduto);
				ret.enqueue(new Callback<ClassificacaoProduto>() {

					@Override
					public void onResponse(Call<ClassificacaoProduto> call, Response<ClassificacaoProduto> response) {
						Platform.runLater(() -> {
							if (response.code() == 500) {
								Main.showErrorDialog("Erro", "Erro ao inserir classifica��o de produto",
										"N�o foi poss�vel inserir o classifica��o de produto, tente novamente mais tarde.",
										AlertType.ERROR);
							} else {
								montarPainelClassificacao(response.body());
								tblClassificacao.getItems().add(response.body());
								Main.showInfDialog("Sucesso", "", "Classifica��o cadastrada com sucesso!!!");
								fecharClassificacao();
							}
						});
					}

					@Override
					public void onFailure(Call<ClassificacaoProduto> call, Throwable t) {
						t.printStackTrace();
						Platform.runLater(() -> Main.showErrorDialog("Erro", "Erro ao salvar classifica��o",
								"N�o foi poss�vel salvar a classifica��o, tente novamente mais tarde.",
								AlertType.ERROR));
					}
				});
			} else {
				Call<Void> ret = new RetrofitConfig().getClassificacaoProdutoService().editar(classificacaoProduto,
						classificacaoProduto.getId());
				ret.enqueue(new Callback<Void>() {
					@Override
					public void onResponse(Call<Void> call, Response<Void> response) {
						Platform.runLater(() -> {
							if (response.code() == 500) {
								Main.showErrorDialog("Erro", "Erro ao atualizar classifica��o",
										"N�o foi poss�vel atualizar o classifica��o, tente novamente mais tarde.",
										AlertType.ERROR);
							} else {
								formHelperClassificacao.setObjectData(null);
								tblClassificacao.refresh();
								Main.showInfDialog("Sucesso", "", "Classifica��o atualizado com sucesso!!!");
								fecharClassificacao();
							}
						});

					}

					@Override
					public void onFailure(Call<Void> call, Throwable t) {
						t.printStackTrace();
						Platform.runLater(() -> Main.showErrorDialog("Erro", "Erro ao editar classifica��es",
								"N�o foi poss�vel editar a classifica��o, tente novamente mais tarde.",
								AlertType.ERROR));
					}
				});
			}
		}
	}

	public void listarClassificacao() {
		Call<ClassificacaoProduto[]> retorno = new RetrofitConfig().getClassificacaoProdutoService().listar();
		retorno.enqueue(new Callback<ClassificacaoProduto[]>() {
			@Override
			public void onResponse(Call<ClassificacaoProduto[]> call, Response<ClassificacaoProduto[]> response) {
				if (response.code() == 500) {
					Platform.runLater(() -> Main.showErrorDialog("Erro", "Erro ao obter lista de classifica��es",
							"N�o foi poss�vel obter a lista de classifica��es, tente novamente mais tarde.",
							AlertType.ERROR));

				} else {
					montarTabelaClassificacao(response.body());
				}
			}

			@Override
			public void onFailure(Call<ClassificacaoProduto[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> Main.showErrorDialog("Erro", "Erro ao obter lista de classifica��es",
						"N�o foi poss�vel obter a lista de classifica��es, tente novamente mais tarde.",
						AlertType.ERROR));
			}
		});
	}

	private void montarTabelaClassificacao(ClassificacaoProduto[] classificacoes) {
		for (ClassificacaoProduto classificacao : classificacoes) {
			montarPainelClassificacao(classificacao);
		}

		colunaClassificacao.setCellValueFactory(new PropertyValueFactory<>("titulo"));
		colunaOpcClassificacao.setCellValueFactory(new PropertyValueFactory<>("paneOpcoes"));
		
		ObservableList<ClassificacaoProduto> collection = FXCollections.observableArrayList(classificacoes);
		tblClassificacao.setItems(collection);
		comboClassificacao.setItems(collection);
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
		formHelperClassificacao.setObjectData(classificacao);
		txtClassificacao.setText(classificacao.getTitulo());
		abrirClassficacao();
	}

	private void excluirClassificacao(ClassificacaoProduto classificacao) {
		int resposta = Main.showConfirmDialog("Excluir", "Excluir",
				"Deseja realmente excluir essa classifica��o de produto?", AlertType.CONFIRMATION);
		if (resposta == 1) {
			Call<Void> retorno = new RetrofitConfig().getClassificacaoProdutoService().excluir(classificacao.getId());
			retorno.enqueue(new Callback<Void>() {
				@Override
				public void onResponse(Call<Void> call, Response<Void> response) {
					Platform.runLater(() -> {
						if (response.code() == 500) {
							Main.showErrorDialog("Erro", "Erro ao excluir a classifica��o",
									"N�o foi poss�vel excluir a classifica��o, tente novamente mais tarde.",
									AlertType.ERROR);
						} else {
							tblClassificacao.getItems().remove(classificacao);
							Main.showInfDialog("Sucesso", "", "Classifica��o de produto exclu�da com sucesso!!!");
						}
					});
				}

				@Override
				public void onFailure(Call<Void> call, Throwable t) {
					t.printStackTrace();
					Platform.runLater(() -> {
						Main.showErrorDialog("Erro", "Erro ao excluir classifica��o",
								"N�o foi poss�vel excluir o classifica��o, tente novamente mais tarde.", AlertType.ERROR);
					});
				}
			});
		}
	}

	// ******************************************************************
	// Abrir o panel oculto
	private @FXML void abrirProduto() {
		paneProduto.setStyle("visibility: true");
	}

	private @FXML void abrirClassficacao() {
		paneClassificacao.setStyle("visibility: true");
	}

	// Fecha o panel que foi aberto
	private @FXML void fecharProduto() {
		formHelperProduto.setObjectData(null);
		paneProduto.setStyle("visibility: false");
		txtNomeProduto.clear();
		txtDescricao.clear();
		comboClassificacao.setValue(null);
		comboUnidade.setValue(null);
	}

	private @FXML void fecharClassificacao() {
		formHelperClassificacao.setObjectData(null);
		paneClassificacao.setStyle("visibility: false");
		txtClassificacao.clear();
	}
}