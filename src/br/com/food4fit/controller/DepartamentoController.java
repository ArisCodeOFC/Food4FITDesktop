package br.com.food4fit.controller;

import java.util.Arrays;

import br.com.food4fit.Main;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.helper.FormHelper;
import br.com.food4fit.model.Departamento;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Alert.AlertType;
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
	private FormHelper formHelper = FormHelper.getInstance();
	private @FXML TableColumn<Departamento, Integer> colunaId;
	private @FXML TableColumn<Departamento, Pane> colunaOpc;
	private @FXML TableColumn<Departamento, String> colunaNome;
	private @FXML TextField txtDepartamento;
	private @FXML Pane paneConteudo;
    private @FXML TableView<Departamento> tblDepartamento;

	private @FXML void initialize() {
		paneConteudo.setVisible(false);
		formHelper.addValidation(txtDepartamento, FormHelper.REQUIRED);
		listarDepartamentos();
	}

	private @FXML void salvar() {
		if (formHelper.validate()) {
			Departamento departamento;
			if (formHelper.getObjectData() != null) {
				departamento = (Departamento) formHelper.getObjectData();
			} else {
				departamento = new Departamento();
			}
			
			departamento.setDepartamento(txtDepartamento.getText());
			
			if (formHelper.getObjectData() == null) {
				new RetrofitConfig().getDepartamentoService().salvar(departamento).enqueue(
					new Callback<Departamento>() {
						@Override
						public void onResponse(Call<Departamento> call, Response<Departamento> response) {
							Platform.runLater(() -> {
								if (response.code() == 500) {
									Main.showErrorDialog("Erro", "Erro ao inserir departamento", "Não foi possível inserir o departamento, tente novamente mais tarde.", AlertType.ERROR);
								} else {
									montarPainel(response.body());
									tblDepartamento.getItems().add(response.body());
									Main.showInfDialog("Sucesso", "", "Departamento cadastrado com sucesso!!!");
									fecharConteudo();
								}
							});
						}
						
						@Override
						public void onFailure(Call<Departamento> call, Throwable t) {
							t.printStackTrace();
							Platform.runLater(() -> 
								Main.showErrorDialog("Erro", "Erro ao inserir departamento", "Não foi possível inserir o departamento, tente novamente mais tarde.", AlertType.ERROR)
							);
						}
					}
				);
				
			} else {
				new RetrofitConfig().getDepartamentoService().atualizar(departamento.getId(), departamento).enqueue(
					new Callback<Void>() {
						@Override
						public void onResponse(Call<Void> call, Response<Void> response) {
							Platform.runLater(() -> {
								if (response.code() == 500) {
									Main.showErrorDialog("Erro", "Erro ao atualizar departamento", "Não foi possível atualizar o departamento, tente novamente mais tarde.", AlertType.ERROR);
								} else {
									formHelper.setObjectData(null);
									tblDepartamento.refresh();
									Main.showInfDialog("Sucesso", "", "Departamento atualizado com sucesso!!!");
									fecharConteudo();
								}
							});
						}
						
						@Override
						public void onFailure(Call<Void> call, Throwable t) {
							t.printStackTrace();
							Platform.runLater(() -> 
								Main.showErrorDialog("Erro", "Erro ao atualizar departamento", "Não foi possível atualizar o departamento, tente novamente mais tarde.", AlertType.ERROR)
							);
						}
					}
				);
			}
		}
	}
	 
	private @FXML void abrirConteudo() {
		paneConteudo.setVisible(true);
	}

	private @FXML void fecharConteudo() {
		formHelper.setObjectData(null);
		paneConteudo.setVisible(false);
		txtDepartamento.clear();
	}
	
	private void listarDepartamentos() {
		Call<Departamento[]> retorno = new RetrofitConfig().getDepartamentoService().listar();
		retorno.enqueue(new Callback<Departamento[]>() {
			@Override
			public void onResponse(Call<Departamento[]> call, Response<Departamento[]> response) {
				if (response.code() == 500) {
					Main.showErrorDialog("Erro", "Erro ao obter lista de departamentos", "Não foi possível obter a lista de departamentos, tente novamente mais tarde.", AlertType.ERROR);
				} else {
					montarTabela(response.body());
				}
			}
			
			@Override
			public void onFailure(Call<Departamento[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> {
					Main.showErrorDialog("Erro", "Erro ao obter lista de departamentos", "Não foi possível obter a lista de departamentos, tente novamente mais tarde.", AlertType.ERROR);
				});
			}
		});
	}
	
	private void montarPainel(Departamento departamento) {
		ImageView imgEditar = new ImageView(new Image(Main.class.getResource("/br/com/food4fit/assets/icons/editar-c.png").toString()));
		imgEditar.prefHeight(15);
		imgEditar.prefWidth(15);
		imgEditar.setCursor(Cursor.HAND);
		imgEditar.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			editarDepartamento(departamento);
		});
		
		ImageView imgExcluir = new ImageView(new Image(Main.class.getResource("/br/com/food4fit/assets/icons/cancelar-c.png").toString()));
		imgExcluir.prefHeight(15);
		imgExcluir.prefWidth(15);
		imgExcluir.setCursor(Cursor.HAND);
		imgExcluir.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			excluirDepartamento(departamento);
		});
		
		HBox hBox = new HBox(imgEditar, imgExcluir);
		hBox.setPrefHeight(15);
		hBox.setPrefWidth(15);
		hBox.setSpacing(10);
		hBox.setPadding(new Insets(0, 0, 0, 32));
		departamento.setPaneOpcoes(hBox);
	}
	
	private void montarTabela(Departamento[] departamentos) {
		for (Departamento departamento : departamentos) {
			montarPainel(departamento);
		}

		colunaId.setCellValueFactory(new PropertyValueFactory<Departamento, Integer>("id"));
		colunaNome.setCellValueFactory(new PropertyValueFactory<Departamento, String>("departamento"));
		colunaOpc.setCellValueFactory(new PropertyValueFactory<Departamento, Pane>("paneOpcoes"));
		tblDepartamento.setItems(FXCollections.observableArrayList(Arrays.asList(departamentos)));
	}
	
	private void excluirDepartamento(Departamento departamento) {
		int resposta = Main.showConfirmDialog("Excluir", "Excluir", "Deseja realmente excluir este departamento?", AlertType.CONFIRMATION);
		if (resposta == 1) {
			new RetrofitConfig().getDepartamentoService().excluir(departamento.getId()).enqueue(
				new Callback<Void>() {
					@Override
					public void onResponse(Call<Void> call, Response<Void> response) {
						Platform.runLater(() -> {
							if (response.code() == 500) {
								Main.showErrorDialog("Erro", "Erro ao excluir departamento", "Não foi possível excluir o departamento, tente novamente mais tarde.", AlertType.ERROR);
							} else {
								tblDepartamento.getItems().remove(departamento);
								Main.showInfDialog("Sucesso", "", "Departamento excluído com sucesso!!!");
							}
						});
					}
					
					@Override
					public void onFailure(Call<Void> call, Throwable t) {
						t.printStackTrace();
						Platform.runLater(() -> {
							Main.showErrorDialog("Erro", "Erro ao excluir departamento", "Não foi possível excluir o departamento, tente novamente mais tarde.", AlertType.ERROR);
						});
					}
				}
			);
		}
	}
	
	private void editarDepartamento(Departamento departamento) {
		formHelper.setObjectData(departamento);
		txtDepartamento.setText(departamento.getDepartamento());
		abrirConteudo();
	}
}
