package br.com.food4fit.controller;

import br.com.food4fit.Main;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.model.Funcionario;
import br.com.food4fit.model.UnidadeDeMedida;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

public class UnidadeDeMedidaController {
	@FXML
	private TableView tblUnidadeDeMedida;

	@FXML
	private TableColumn colunaOpc;

	@FXML
	private TableColumn colunaSigla;

	@FXML
	private TableColumn colunaUnidadeMedida;

	@FXML
	private TextField txtUnidadeDeMedida;

	@FXML
	private TextField txtSigla;

	@FXML
	private Pane paneConteudo;

	public void initialize() {
		paneConteudo.setStyle("visibility: false");

		listarUnidade();

		txtUnidadeDeMedida.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				txtUnidadeDeMedida.setStyle("-fx-background-color: #fff;" + "-fx-border-color:#9cc283");
				return;
			}
		});

		txtSigla.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				txtSigla.setStyle("-fx-background-color: #fff;" + "-fx-border-color:#9cc283");
				return;
			}
		});

	}

	// Metodo para salvar no banco

	@FXML
	void salvar() {
		if (txtUnidadeDeMedida.getText().isEmpty()) {
			txtUnidadeDeMedida.setStyle("-fx-background-color: #ed2121;" + "-fx-prompt-text-fill: #fff");
			return;
		}

		if (txtSigla.getText().isEmpty()) {
			txtSigla.setStyle("-fx-background-color: #ed2121;" + "-fx-prompt-text-fill: #fff");
			return;
		}

		String unidade = txtUnidadeDeMedida.getText();

		String sigla = txtSigla.getText();

		UnidadeDeMedida unidadeMedida = new UnidadeDeMedida();

		unidadeMedida.setUnidadeMedida(unidade);
		unidadeMedida.setSigla(sigla);

		if (txtUnidadeDeMedida.getUserData() == null) {
			Call<UnidadeDeMedida> retorno = new RetrofitConfig().getUnidadeDeMedida().salvar(unidadeMedida);

			retorno.enqueue(new Callback<UnidadeDeMedida>() {

				@Override
				public void onResponse(Call<UnidadeDeMedida> call, Response<UnidadeDeMedida> resposta) {
					if (resposta.code() == 500) {
						Main.showConfirmDialog("OK", "Erro", "Falha ao tentar conectar com o servidor",
								Alert.AlertType.WARNING);
					} else {

						UnidadeDeMedida medida = resposta.body();

						if (medida == null) {
							System.out.println("Deu ruim");
						} else {
							Platform.runLater(() -> {
								Main.showInfDialog("Sucesso", "", "Unidade de Medida cadastrada com secesso!!!");
								fechaConteudo();

								initialize();

							});
						}

					}

				}

				@Override
				public void onFailure(Call<UnidadeDeMedida> arg0, Throwable arg1) {
					// TODO Auto-generated method stub

				}
			});
		} else {
			int decisao = Main.showConfirmDialog("Sim", "Editar", "Deseja editar as informações?", Alert.AlertType.WARNING);

			if(decisao == 1){
				UnidadeDeMedida u = (UnidadeDeMedida) txtUnidadeDeMedida.getUserData();
				int id = u.getId();

				Call<Void> retorno = new RetrofitConfig().getUnidadeDeMedida().editar(unidadeMedida, id);

				retorno.enqueue(new Callback<Void>() {

					@Override
					public void onResponse(Call<Void> arg0, Response<Void> arg1) {
						if (arg1.code() == 500) {
							Main.showConfirmDialog("OK", "Erro", "Falha ao tentar conectar com o servidor",
									Alert.AlertType.WARNING);
						}else{
							Platform.runLater(() ->{
								fechaConteudo();
								initialize();
								txtUnidadeDeMedida.setUserData(null);
							});
						}

					}

					@Override
					public void onFailure(Call<Void> arg0, Throwable arg1) {
						// TODO Auto-generated method stub

					}
				});
			}

		}

	}

	public void listarUnidade(){
		Call<UnidadeDeMedida[]> retorno = new RetrofitConfig().getUnidadeDeMedida().lista();
		retorno.enqueue(new Callback<UnidadeDeMedida[]>() {

			@Override
			public void onResponse(Call<UnidadeDeMedida[]> arg0, Response<UnidadeDeMedida[]> arg1) {
				for (UnidadeDeMedida u : arg1.body()) {

					Image editImg = new Image(Main.class.getResource("assets/icons/editar-c.png").toString());

					Image cancelImg = new Image(Main.class.getResource("assets/icons/cancelar-c.png").toString());

					ImageView editView = new ImageView();
					editView.prefHeight(15);
					editView.prefWidth(15);
					editView.setImage(editImg);
					editView.setStyle("-fx-cursor: hand;");
					editView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
						// UnidadeDeMedida unidadeClass = new UnidadeDeMedida();
						// unidadeClass.setId(u.get);
						txtUnidadeDeMedida.setUserData(u);
						abrirConteudo();
						txtUnidadeDeMedida.setText(u.getUnidadeMedida());
						txtSigla.setText(u.getSigla());

						event.consume();
					});

					ImageView deleteView = new ImageView();
					deleteView.prefHeight(15);
					deleteView.prefWidth(15);
					deleteView.setImage(cancelImg);
					deleteView.setStyle("-fx-cursor: hand");
					deleteView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
						Call<Void> resultAcao = new RetrofitConfig().getUnidadeDeMedida().excluir(u.getId());
						resultAcao.enqueue(new Callback<Void>() {

							@Override
							public void onFailure(Call<Void> arg0, Throwable arg1) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onResponse(Call<Void> arg0, Response<Void> arg1) {
								if (arg1.code() == 500) {
									Main.showConfirmDialog("OK", "Erro", "Falha ao tentar excluir",
											Alert.AlertType.WARNING);
								} else {

									Platform.runLater(() -> {
										int result = Main.showConfirmDialog("OK", "Excluir", "Deseja excluir o item?",
												Alert.AlertType.WARNING);
										if (result == 1) {
											initialize();
										}

									});

								}

							}
						});

						event.consume();
					});

					HBox hBox = new HBox(editView, deleteView);
					hBox.setPrefHeight(15);
					hBox.setPrefWidth(15);
					hBox.setStyle("-fx-padding: 0 0 0 20; -fx-spacing:10;");

					u.setPaneOpcoes(hBox);
				}

				colunaUnidadeMedida
						.setCellValueFactory(new PropertyValueFactory<UnidadeDeMedida, String>("unidadeMedida"));
				colunaSigla.setCellValueFactory(new PropertyValueFactory<UnidadeDeMedida, String>("sigla"));
				colunaOpc.setCellValueFactory(new PropertyValueFactory<UnidadeDeMedida, Pane>("paneOpcoes"));
				tblUnidadeDeMedida.setItems(FXCollections.observableArrayList(arg1.body()));

			}

			@Override
			public void onFailure(Call<UnidadeDeMedida[]> arg0, Throwable arg1) {
				// TODO Auto-generated method stub

			}
		});
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

		txtUnidadeDeMedida.clear();
		txtSigla.clear();
	}

}
