package br.com.food4fit.controller;

import br.com.food4fit.Main;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.model.Departamento;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
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

public class DepartamentoController {
	@FXML
	private TableColumn colunaId;

	@FXML
	private TableColumn colunaStatus;

	@FXML
	private TableColumn colunaOpc;

	@FXML
	private TableColumn colunaNome;

	@FXML
	private TextField txtDepartamento;

	@FXML
	private Pane paneConteudo;

    @FXML
    private TableView tblFornecedor;

	@FXML
	private Pane paneButton;

	ToggleSwitch button = new ToggleSwitch(false);

	public void initialize() {

		paneConteudo.setStyle("visibility: false");
		paneButton.getChildren().add(button);

	}

	 @FXML
	 void salvar() {

	 }

//	@FXML
//	void salvar() {
//		String departamento = txtDepartamento.getText();
//
//		Departamento dep = new Departamento();
//		dep.setDepartamento(departamento);
//
//		if(txtDepartamento.getUserData() == null){
//			Call<Departamento> retorno = new RetrofitConfig().getDepartamentoService().salvar(dep);
//
//			retorno.enqueue(new Callback<Departamento>() {
//
//				@Override
//				public void onFailure(Call<Departamento> arg0, Throwable arg1) {
//					// TODO Auto-generated method stub
//
//				}
//
//				@Override
//				public void onResponse(Call<Departamento> arg0, Response<Departamento> arg1) {
//					if(arg1.code() == 500) {
//						Main.showConfirmDialog("OK", "Erro", "Falha ao tentar conectar com o servidor",
//						Alert.AlertType.WARNING);
//					}else{
//						Platform.runLater(() -> {
//							Main.showInfDialog("Sucesso", "", "Departamento cadastrado com secesso!!!");
//							fechaConteudo();
//
//							initialize();
//
//						});
//
//				}
//				}
//
//			});
//		}else{
//			int decisao = Main.showConfirmDialog("Sim", "Editar", "Deseja editar as informações?", Alert.AlertType.WARNING);
//
//						if(decisao == 1){
//							Departamento d = (Departamento) txtDepartamento.getUserData();
//							int id = d.getId();
//
//							Call<Void> retorno = new RetrofitConfig().getDepartamentoService().editar(dep, id);
//
//							retorno.enqueue(new Callback<Void>() {
//
//								@Override
//								public void onResponse(Call<Void> arg0, Response<Void> arg1) {
//									if (arg1.code() == 500) {
//										Main.showConfirmDialog("OK", "Erro", "Falha ao tentar conectar com o servidor",
//												Alert.AlertType.WARNING);
//									}else{
//										Platform.runLater(() ->{
//											fechaConteudo();
//											initialize();
//											txtDepartamento.setUserData(null);
//										});
//									}
//
//								}
//
//								@Override
//								public void onFailure(Call<Void> arg0, Throwable arg1) {
//									// TODO Auto-generated method stub
//
//								}
//							});
//						}
//		}
//
//
//
//	}

//	public void listar() {
//		Call<Departamento[]> retorno = new RetrofitConfig().getDepartamentoService().listar();
//
//		retorno.enqueue(new Callback<Departamento[]>() {
//
//			@Override
//			public void onResponse(Call<Departamento[]> arg0, Response<Departamento[]> arg1) {
//				for (Departamento d : arg1.body()) {
//
//					Image editImg = new Image(Main.class.getResource("assets/icons/editar-c.png").toString());
//
//					Image cancelImg = new Image(Main.class.getResource("assets/icons/cancelar-c.png").toString());
//
//					ImageView editView = new ImageView();
//					editView.prefHeight(15);
//					editView.prefWidth(15);
//					editView.setImage(editImg);
//					editView.setStyle("-fx-cursor: hand;");
//					editView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
//						txtDepartamento.setUserData(d);
//						abrirConteudo();
//						txtDepartamento.setText(d.getDepartamento());
//
//						event.consume();
//					});
//
//					ImageView deleteView = new ImageView();
//					deleteView.prefHeight(15);
//					deleteView.prefWidth(15);
//					deleteView.setImage(cancelImg);
//					deleteView.setStyle("-fx-cursor: hand");
//					deleteView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
//						Call<Void> resultAcao = new RetrofitConfig().getDepartamentoService().excluir(d.getId());
//						resultAcao.enqueue(new Callback<Void>() {
//
//							@Override
//							public void onFailure(Call<Void> arg0, Throwable arg1) {
//								// TODO Auto-generated method stub
//
//							}
//
//							@Override
//							public void onResponse(Call<Void> arg0, Response<Void> arg1) {
//								if (arg1.code() == 500) {
//									Main.showConfirmDialog("OK", "Erro", "Falha ao tentar excluir",
//											Alert.AlertType.WARNING);
//								} else {
//
//									Platform.runLater(() -> {
//										int result = Main.showConfirmDialog("OK", "Excluir", "Deseja excluir o item?",
//												Alert.AlertType.WARNING);
//										if (result == 1) {
//											initialize();
//										}
//
//									});
//
//								}
//
//							}
//						});
//
//						event.consume();
//					});
//
//					HBox hBox = new HBox(editView, deleteView);
//					hBox.setPrefHeight(15);
//					hBox.setPrefWidth(15);
//					hBox.setStyle("-fx-padding: 0 0 0 20; -fx-spacing:10;");
//
//					d.setPaneOpcoes(hBox);
//				}
//
//				colunaId.setCellValueFactory(new PropertyValueFactory<Departamento, String>("id"));
//				colunaNome.setCellValueFactory(new PropertyValueFactory<Departamento, String>("departamento"));
//				colunaOpc.setCellValueFactory(new PropertyValueFactory<Departamento, Pane>("paneOpcoes"));
//				tblFornecedor.setItems(FXCollections.observableArrayList(arg1.body()));
//
//			}
//
//			@Override
//			public void onFailure(Call<Departamento[]> arg0, Throwable arg1) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//	}

	// *************************************************************

	@FXML
	void abrirConteudo() {
		paneConteudo.setStyle("visibility: true;");
	}

	@FXML
	void fechaConteudo() {
		paneConteudo.setStyle("visibility: false");
	}
}
