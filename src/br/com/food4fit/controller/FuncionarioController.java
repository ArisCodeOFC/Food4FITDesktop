package br.com.food4fit.controller;

import java.util.Date;

import br.com.food4fit.Main;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.model.Funcionario;
import br.com.food4fit.model.UnidadeDeMedida;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FuncionarioController {
	@FXML
	private AnchorPane conteudo;

	@FXML
	private TableView tblFuncionario;

	@FXML
	private TableColumn colunaEmail;

	@FXML
	private TableColumn colunaOpc;

	@FXML
	private TableColumn colunaCargo;

	@FXML
	private TableColumn colunaAdmissao;

	@FXML
	private TableColumn colunaMatricula;

	@FXML
	private TableColumn colunaNome;

	@FXML
	private Pane paneConteudo;


	public void initialize() {
		paneConteudo.setStyle("visibility: false");

		Call<Funcionario[]> retorno = new RetrofitConfig().getFuncionarioService().lista();
		retorno.enqueue(new Callback<Funcionario[]>() {

			@Override
			public void onResponse(Call<Funcionario[]> arg0, Response<Funcionario[]> arg1) {
				for(Funcionario f : arg1.body()){
					Image editImg = new Image(Main.class.getResource("assets/icons/editar-c.png").toString());

					Image cancelImg = new Image(Main.class.getResource("assets/icons/cancelar-c.png").toString());

					ImageView editView = new ImageView();
					editView.prefHeight(15);
					editView.prefWidth(15);
					editView.setImage(editImg);
					editView.setStyle("-fx-cursor: hand;");
					editView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
						abrirConteudo();


						event.consume();
					});

					ImageView deleteView = new ImageView();
					deleteView.prefHeight(15);
					deleteView.prefWidth(15);
					deleteView.setImage(cancelImg);
					deleteView.setStyle("-fx-cursor: hand");
					deleteView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
						Call<Void> retorno = new RetrofitConfig().getFuncionarioService().excluir(f.getId());
						retorno.enqueue(new Callback<Void>() {

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


					});

					HBox hBox = new HBox(editView, deleteView);
					hBox.setPrefHeight(15);
					hBox.setPrefWidth(15);
					hBox.setStyle("-fx-padding: 0 0 0 32; -fx-spacing:10;");

					f.setPaneOpcoes(hBox);

				}

				colunaNome.setCellValueFactory(new PropertyValueFactory<Funcionario, String>("nomeCompleto"));
				colunaMatricula.setCellValueFactory(new PropertyValueFactory<Funcionario, String>("matricula"));
				colunaEmail.setCellValueFactory(new PropertyValueFactory<Funcionario, String>("email"));
				colunaCargo.setCellValueFactory(new PropertyValueFactory<Funcionario, String>("cargo"));
				colunaAdmissao.setCellValueFactory(new PropertyValueFactory<Funcionario, Date>("dataFormatada"));
				colunaOpc.setCellValueFactory(new PropertyValueFactory<UnidadeDeMedida, Pane>("paneOpcoes"));
				tblFuncionario.setItems(FXCollections.observableArrayList(arg1.body()));
			}

			@Override
			public void onFailure(Call<Funcionario[]> arg0, Throwable arg1) {
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

	}

}
