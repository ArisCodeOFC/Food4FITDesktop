package br.com.food4fit.controller;

import java.util.Arrays;

import br.com.food4fit.Main;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.helper.FormHelper;
import br.com.food4fit.model.Cargo;
import br.com.food4fit.model.ClassificacaoProduto;
import br.com.food4fit.model.Estado;
import br.com.food4fit.model.Funcionario;
import br.com.food4fit.model.Produto;
import br.com.food4fit.model.UnidadeDeMedida;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
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
	private FormHelper formHelperProduto = FormHelper.getInstance();
	private FormHelper formHelperClassificacao = FormHelper.getInstance();

	private @FXML void initialize() {
		paneClassificacao.setStyle("visibility: false");
		paneProduto.setStyle("visibility: false");
		formHelperProduto.addValidation(txtNomeProduto, FormHelper.REQUIRED);
		formHelperClassificacao.addValidation(txtClassificacao, FormHelper.REQUIRED);
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
					public void onResponse(Call<Produto> arg0, Response<Produto> arg1) {
						Platform.runLater(() -> {
							if (arg1.code() == 500) {
								Main.showErrorDialog("Erro", "Erro ao inserir produto",
										"Não foi possível inserir o produto, tente novamente mais tarde.",
										AlertType.ERROR);
							} else {
								montarPainelProduto(arg1.body());
								tblProduto.getItems().add(arg1.body());
								Main.showInfDialog("Sucesso", "", "Produto cadastrado com secesso!!!");
								fecharProduto();
							}
						});

					}

					@Override
					public void onFailure(Call<Produto> arg0, Throwable arg1) {
						arg1.printStackTrace();
						Platform.runLater(() -> Main.showErrorDialog("Erro", "Erro ao inserir produto",
								"Não foi possível inserir o produto, tente novamente mais tarde.", AlertType.ERROR));

					}
				});
			} else {
				Call<Void> ret = new RetrofitConfig().getProdutoService().editar(produto, produto.getId());
				ret.enqueue(new Callback<Void>() {
					@Override
					public void onResponse(Call<Void> arg0, Response<Void> arg1) {
						Platform.runLater(() -> {
							if (arg1.code() == 500) {
								Main.showErrorDialog("Erro", "Erro ao atualizar produto",
										"Não foi possível atualizar o funcionário, tente novamente mais tarde.",
										AlertType.ERROR);
							} else {
								formHelperProduto.setObjectData(null);
								tblProduto.refresh();
								Main.showInfDialog("Sucesso", "", "Produto atualizado com secesso!!!");
								fecharProduto();
							}
						});
					}

					@Override
					public void onFailure(Call<Void> arg0, Throwable arg1) {
						arg1.printStackTrace();
						Platform.runLater(() -> Main.showErrorDialog("Erro", "Erro ao atualizar produto",
								"Não foi possível atualizar o produto, tente novamente mais tarde.", AlertType.ERROR));
					}
				});
			}
		}
	}

	public void listarProduto() {
		Call<Produto[]> ret = new RetrofitConfig().getProdutoService().listar();
		ret.enqueue(new Callback<Produto[]>() {
			@Override
			public void onResponse(Call<Produto[]> arg0, Response<Produto[]> arg1) {
				if (arg1.code() == 500) {
					Platform.runLater(() -> Main.showErrorDialog("Erro", "Erro ao obter lista de produtos",
							"Não foi possível obter a lista de produtos, tente novamente mais tarde.",
							AlertType.ERROR));

				} else {
					montarTabelaProduto(arg1.body());
				}
			}

			@Override
			public void onFailure(Call<Produto[]> arg0, Throwable arg1) {
				arg1.printStackTrace();
				Platform.runLater(() -> Main.showErrorDialog("Erro", "Erro ao obter lista de produtos",
						"Não foi possível obter a lista de produtos, tente novamente mais tarde.", AlertType.ERROR));

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
			public void onResponse(Call<UnidadeDeMedida[]> arg0, Response<UnidadeDeMedida[]> arg1) {
				Platform.runLater(() -> {
					comboUnidade.setCursor(Cursor.HAND);
					if (arg1.code() == 500) {
						Main.showErrorDialog("Erro", "Erro ao obter lista de unidade de medida",
								"Não foi possível obter a lista de unidade de medida, tente novamente mais tarde.",
								AlertType.ERROR);
					} else {
						comboUnidade.setItems(FXCollections.observableArrayList(Arrays.asList(arg1.body())));
					}
				});

			}

			@Override
			public void onFailure(Call<UnidadeDeMedida[]> arg0, Throwable arg1) {
				Main.showErrorDialog("Erro", "Erro ao obter lista de unidade de medida",
						"Não foi possível obter a lista de unidade de medida, tente novamente mais tarde.",
						AlertType.ERROR);
			}
		});
	}

	public void listarClassificacaoProduto() {
		comboClassificacao.setCursor(Cursor.WAIT);
		Call<ClassificacaoProduto[]> ret = new RetrofitConfig().getClassificacaoProdutoService().listar();
		ret.enqueue(new Callback<ClassificacaoProduto[]>() {
			@Override
			public void onResponse(Call<ClassificacaoProduto[]> arg0, Response<ClassificacaoProduto[]> arg1) {
				Platform.runLater(() -> {
					comboClassificacao.setCursor(Cursor.HAND);
					if (arg1.code() == 500) {
						Main.showErrorDialog("Erro", "Erro ao obter lista de classificação de produto",
								"Não foi possível obter a lista de classificação de produto, tente novamente mais tarde.",
								AlertType.ERROR);
					} else {
						comboClassificacao.setItems(FXCollections.observableArrayList(Arrays.asList(arg1.body())));
					}
				});

			}

			@Override
			public void onFailure(Call<ClassificacaoProduto[]> arg0, Throwable arg1) {
				Main.showErrorDialog("Erro", "Erro ao obter lista de classificação de produto",
						"Não foi possível obter a lista de classificação de produto, tente novamente mais tarde.",
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
		abrirClassficacao();
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
									"Não foi possível excluir o produto, tente novamente mais tarde.", AlertType.ERROR);
						} else {
							tblProduto.getItems().remove(produto);
							Main.showInfDialog("Sucesso", "", "Produto excluído com secesso!!!");
						}
					});
				}

				@Override
				public void onFailure(Call<Void> call, Throwable t) {
					t.printStackTrace();
					Platform.runLater(() -> {
						Main.showErrorDialog("Erro", "Erro ao excluir funcionário",
								"Não foi possível excluir o funcionário, tente novamente mais tarde.", AlertType.ERROR);
					});
				}
			});
		}
	}

	// ******************************************************************
	// Classificação do Produto
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
					public void onResponse(Call<ClassificacaoProduto> arg0, Response<ClassificacaoProduto> response) {
						Platform.runLater(() -> {
							if (response.code() == 500) {
								Main.showErrorDialog("Erro", "Erro ao inserir classificação de produto",
										"Não foi possível inserir o classificação de produto, tente novamente mais tarde.",
										AlertType.ERROR);
							} else {
								montarPainelClassificacao(response.body());
								tblClassificacao.getItems().add(response.body());
								Main.showInfDialog("Sucesso", "", "Funcionário cadastrado com secesso!!!");
								fecharClassificacao();
							}
						});
					}

					@Override
					public void onFailure(Call<ClassificacaoProduto> arg0, Throwable arg1) {
						arg1.printStackTrace();
						Platform.runLater(() -> Main.showErrorDialog("Erro", "Erro ao salvar classificação",
								"Não foi possível salvar a classificação, tente novamente mais tarde.",
								AlertType.ERROR));
					}
				});
			} else {
				Call<Void> ret = new RetrofitConfig().getClassificacaoProdutoService().editar(classificacaoProduto,
						classificacaoProduto.getId());
				ret.enqueue(new Callback<Void>() {
					@Override
					public void onResponse(Call<Void> arg0, Response<Void> arg1) {
						Platform.runLater(() -> {
							if (arg1.code() == 500) {
								Main.showErrorDialog("Erro", "Erro ao atualizar funcionário",
										"Não foi possível atualizar o funcionário, tente novamente mais tarde.",
										AlertType.ERROR);
							} else {
								formHelperClassificacao.setObjectData(null);
								tblClassificacao.refresh();
								Main.showInfDialog("Sucesso", "", "Funcionário atualizado com secesso!!!");
								fecharClassificacao();
							}
						});

					}

					@Override
					public void onFailure(Call<Void> arg0, Throwable arg1) {
						arg1.printStackTrace();
						Platform.runLater(() -> Main.showErrorDialog("Erro", "Erro ao editar classificações",
								"Não foi possível editar a classificação, tente novamente mais tarde.",
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
					Platform.runLater(() -> Main.showErrorDialog("Erro", "Erro ao obter lista de classificações",
							"Não foi possível obter a lista de classificações, tente novamente mais tarde.",
							AlertType.ERROR));

				} else {
					montarTabelaClassificacao(response.body());
				}
			}

			@Override
			public void onFailure(Call<ClassificacaoProduto[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> Main.showErrorDialog("Erro", "Erro ao obter lista de classificações",
						"Não foi possível obter a lista de classificações, tente novamente mais tarde.",
						AlertType.ERROR));
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
		formHelperClassificacao.setObjectData(classificacao);
		txtClassificacao.setText(classificacao.getTitulo());
		abrirClassficacao();
	}

	private void excluirClassificacao(ClassificacaoProduto classificacao) {
		int resposta = Main.showConfirmDialog("Excluir", "Excluir",
				"Deseja realmente excluir essa classificação de produto?", AlertType.CONFIRMATION);
		if (resposta == 1) {
			Call<Void> retorno = new RetrofitConfig().getClassificacaoProdutoService().excluir(classificacao.getId());
			retorno.enqueue(new Callback<Void>() {
				@Override
				public void onResponse(Call<Void> call, Response<Void> response) {
					Platform.runLater(() -> {
						if (response.code() == 500) {
							Main.showErrorDialog("Erro", "Erro ao excluir a classificação",
									"Não foi possível excluir a classificação, tente novamente mais tarde.",
									AlertType.ERROR);
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
						Main.showErrorDialog("Erro", "Erro ao excluir funcionário",
								"Não foi possível excluir o funcionário, tente novamente mais tarde.", AlertType.ERROR);
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
		paneProduto.setStyle("visibility: false");
		txtNomeProduto.clear();
		txtDescricao.clear();
		comboClassificacao.setValue(null);
		comboUnidade.setValue(null);
	}

	private @FXML void fecharClassificacao() {
		paneClassificacao.setStyle("visibility: false");
		txtClassificacao.clear();
	}
}