package br.com.food4fit.controller;

import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.model.UnidadeDeMedida;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnidadeDeMedidaController {
	@FXML
	private TableView tblUnidadeDdeMedida;

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
				colunaUnidadeMedida
						.setCellValueFactory(new PropertyValueFactory<UnidadeDeMedida, String>("unidadeMedida"));
				colunaSigla.setCellValueFactory(new PropertyValueFactory<UnidadeDeMedida, String>("sigla"));
				tblUnidadeDdeMedida.setItems(FXCollections.observableArrayList(arg1.body()));

			}

			@Override
			public void onFailure(Call<UnidadeDeMedida[]> arg0, Throwable arg1) {
				// TODO Auto-generated method stub

			}
		});
	}


	//Abrir o panel oculto
	@FXML
	void abrirConteudo() {
		paneConteudo.setStyle("visibility: true");
	}

	//Fecha o panel que foi aberto
	@FXML
	void fechaConteudo() {
		paneConteudo.setStyle("visibility: false");
	}

}
