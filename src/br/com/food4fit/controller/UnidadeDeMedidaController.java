package br.com.food4fit.controller;

import br.com.food4fit.Main;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.model.Icons;
import br.com.food4fit.model.UnidadeDeMedida;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
		Call<UnidadeDeMedida[]> retorno = new RetrofitConfig().getUnidadeDeMedida().lista();
		retorno.enqueue(new Callback<UnidadeDeMedida[]>() {

			@Override
			public void onResponse(Call<UnidadeDeMedida[]> arg0, Response<UnidadeDeMedida[]> arg1) {


				//colunaOpc.getCellValueFactory(new Image(Main.class.getResource("assets/icons/favicon.png").toString()));
				//colunaOpc.setCellValueFactory((new ImageView(new Image(Icons i = new Icons(Main.class.getResource("assets/icons/favicon.png").toString()))));Main.class.getResource("assets/icons/favicon.png").toString()))));

				colunaUnidadeMedida
						.setCellValueFactory(new PropertyValueFactory<UnidadeDeMedida, String>("unidadeMedida"));
				colunaSigla.setCellValueFactory(new PropertyValueFactory<UnidadeDeMedida, String>("sigla"));



				tblUnidadeDeMedida.setItems(FXCollections.observableArrayList(arg1.body()));

			}

			@Override
			public void onFailure(Call<UnidadeDeMedida[]> arg0, Throwable arg1) {
				// TODO Auto-generated method stub

			}
		});

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

	}

	// Abrir o panel oculto
	@FXML
	void abrirConteudo() {
		paneConteudo.setStyle("visibility: true; -fx-background-color: #dcdcdc");
	}

	// Fecha o panel que foi aberto
	@FXML
	void fechaConteudo() {
		paneConteudo.setStyle("visibility: false");

		txtUnidadeDeMedida.clear();
		txtSigla.clear();
	}

}
