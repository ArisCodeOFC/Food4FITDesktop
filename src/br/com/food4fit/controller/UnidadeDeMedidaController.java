package br.com.food4fit.controller;

import br.com.food4fit.Main;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.helper.FormHelper;
import br.com.food4fit.model.UnidadeDeMedida;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

public class UnidadeDeMedidaController {
	private FormHelper formHelper = FormHelper.getInstance();
	private @FXML TableView<UnidadeDeMedida> tblUnidadeDeMedida;
	private @FXML TableColumn<UnidadeDeMedida, Pane> colunaOpc;
	private @FXML TableColumn<UnidadeDeMedida, String> colunaSigla, colunaUnidadeMedida;
	private @FXML TextField txtUnidadeDeMedida, txtSigla;
	private @FXML Pane paneConteudo;

	private @FXML void initialize() {
		paneConteudo.setStyle("visibility: false");
		formHelper.addValidation(txtSigla, FormHelper.REQUIRED);
		formHelper.addValidation(txtUnidadeDeMedida, FormHelper.REQUIRED);
		listarUnidade();
	}

	// Metodo para salvar no banco
	private @FXML void salvar() {
		if (formHelper.validate()) {
			String unidade = txtUnidadeDeMedida.getText();
			String sigla = txtSigla.getText();
			
			UnidadeDeMedida unidadeMedida;
			if (formHelper.getObjectData() != null) {
				unidadeMedida = (UnidadeDeMedida) formHelper.getObjectData();
			} else {
				unidadeMedida = new UnidadeDeMedida();
			}
			
			unidadeMedida.setUnidadeMedida(unidade);
			unidadeMedida.setSigla(sigla);

			if (formHelper.getObjectData() == null) {
				Call<UnidadeDeMedida> retorno = new RetrofitConfig().getUnidadeDeMedidaService().salvar(unidadeMedida);
				retorno.enqueue(new Callback<UnidadeDeMedida>() {
					@Override
					public void onResponse(Call<UnidadeDeMedida> call, Response<UnidadeDeMedida> response) {
						Platform.runLater(() -> {
							if (response.code() == 500) {
								Main.showErrorDialog("Erro", "Erro ao inserir unidade de medida", "Não foi possível inserir a unidade de medida, tente novamente mais tarde.", AlertType.ERROR);
							} else {
								montarPainel(response.body());
								tblUnidadeDeMedida.getItems().add(response.body());
								Main.showInfDialog("Sucesso", "", "Unidade de medida cadastrada com secesso!!!");
								fecharConteudo();
							}
						});
					}
					
					@Override
					public void onFailure(Call<UnidadeDeMedida> call, Throwable t) {
						t.printStackTrace();
						Platform.runLater(() -> 
							Main.showErrorDialog("Erro", "Erro ao inserir unidade de medida", "Não foi possível inserir a unidade de medida, tente novamente mais tarde.", AlertType.ERROR)
						);
					}
				});
				
			} else {
				Call<Void> retorno = new RetrofitConfig().getUnidadeDeMedidaService().editar(unidadeMedida, unidadeMedida.getId());
				retorno.enqueue(new Callback<Void>() {
					@Override
					public void onResponse(Call<Void> call, Response<Void> response) {
						Platform.runLater(() -> {
							if (response.code() == 500) {
								Main.showErrorDialog("Erro", "Erro ao atualizar unidade de medida", "Não foi possível atualizar a unidade de medida, tente novamente mais tarde.", AlertType.ERROR);
							} else {
								formHelper.setObjectData(null);
								tblUnidadeDeMedida.refresh();
								Main.showInfDialog("Sucesso", "", "Unidade de medida atualizada com secesso!!!");
								fecharConteudo();
							}
						});
					}
					
					@Override
					public void onFailure(Call<Void> call, Throwable t) {
						t.printStackTrace();
						Platform.runLater(() -> 
							Main.showErrorDialog("Erro", "Erro ao atualizar unidade de medida", "Não foi possível atualizar a unidade de medida, tente novamente mais tarde.", AlertType.ERROR)
						);
					}
				});
			}
		}
	}

	private void listarUnidade(){
		Call<UnidadeDeMedida[]> retorno = new RetrofitConfig().getUnidadeDeMedidaService().lista();
		retorno.enqueue(new Callback<UnidadeDeMedida[]>() {
			@Override
			public void onResponse(Call<UnidadeDeMedida[]> call, Response<UnidadeDeMedida[]> response) {
				if (response.code() == 500) {
					Platform.runLater(() -> 
					Main.showErrorDialog("Erro", "Erro ao obter lista de unidades de medida", "Não foi possível obter a lista de unidades de medida, tente novamente mais tarde.", AlertType.ERROR)
					);
					
				} else {
					montarTabela(response.body());
				}
			}
			
			@Override
			public void onFailure(Call<UnidadeDeMedida[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> 
					Main.showErrorDialog("Erro", "Erro ao obter lista de unidades de medida", "Não foi possível obter a lista de unidades de medida, tente novamente mais tarde.", AlertType.ERROR)
				);
			}
		});
	}

	// Abrir o panel oculto
	private @FXML void abrirConteudo() {
		paneConteudo.setStyle("visibility: true;");
	}

	// Fecha o panel que foi aberto
	private @FXML void fecharConteudo() {
		paneConteudo.setStyle("visibility: false");
		txtUnidadeDeMedida.clear();
		txtSigla.clear();
	}
	
	private void montarPainel(UnidadeDeMedida unidade) {
		Image editImg = new Image(Main.class.getResource("assets/icons/editar-c.png").toString());
		Image cancelImg = new Image(Main.class.getResource("assets/icons/cancelar-c.png").toString());

		ImageView editView = new ImageView();
		editView.prefHeight(15);
		editView.prefWidth(15);
		editView.setImage(editImg);
		editView.setStyle("-fx-cursor: hand;");
		editView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			formHelper.setObjectData(unidade);
			abrirConteudo();
			txtUnidadeDeMedida.setText(unidade.getUnidadeMedida());
			txtSigla.setText(unidade.getSigla());
			event.consume();
		});

		ImageView deleteView = new ImageView();
		deleteView.prefHeight(15);
		deleteView.prefWidth(15);
		deleteView.setImage(cancelImg);
		deleteView.setStyle("-fx-cursor: hand");
		deleteView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			int resposta = Main.showConfirmDialog("Excluir", "Excluir", "Deseja realmente excluir esta unidade de medida?", AlertType.CONFIRMATION);
			if (resposta == 1) {
				Call<Void> resultAcao = new RetrofitConfig().getUnidadeDeMedidaService().excluir(unidade.getId());
				resultAcao.enqueue(new Callback<Void>() {
					@Override
					public void onResponse(Call<Void> call, Response<Void> response) {
						Platform.runLater(() -> {
							if (response.code() == 500) {
								Main.showErrorDialog("Erro", "Erro ao excluir unidade de medida", "Não foi possível excluir a unidade de medida, tente novamente mais tarde.", AlertType.ERROR);
							} else {
								tblUnidadeDeMedida.getItems().remove(unidade);
								Main.showInfDialog("Sucesso", "", "Unidade de medida excluída com secesso!!!");
							}
						});
					}
					
					@Override
					public void onFailure(Call<Void> call, Throwable t) {
						t.printStackTrace();
						Platform.runLater(() -> {
							Main.showErrorDialog("Erro", "Erro ao excluir unidade de medida", "Não foi possível excluir a unidade de medida, tente novamente mais tarde.", AlertType.ERROR);
						});
					}
				});
			}

			event.consume();
		});

		HBox hBox = new HBox(editView, deleteView);
		hBox.setPrefHeight(15);
		hBox.setPrefWidth(15);
		hBox.setStyle("-fx-padding: 0 0 0 20; -fx-spacing:10;");
		unidade.setPaneOpcoes(hBox);
	}

	private void montarTabela(UnidadeDeMedida[] unidades) {
		for (UnidadeDeMedida unidade : unidades) {
			montarPainel(unidade);
		}

		colunaUnidadeMedida.setCellValueFactory(new PropertyValueFactory<UnidadeDeMedida, String>("unidadeMedida"));
		colunaSigla.setCellValueFactory(new PropertyValueFactory<UnidadeDeMedida, String>("sigla"));
		colunaOpc.setCellValueFactory(new PropertyValueFactory<UnidadeDeMedida, Pane>("paneOpcoes"));
		tblUnidadeDeMedida.setItems(FXCollections.observableArrayList(unidades));
	}
}
