package br.com.food4fit.controller;

import java.util.Arrays;

import br.com.food4fit.Main;
import br.com.food4fit.component.MaskedTextField;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.helper.FormHelper;
import br.com.food4fit.model.Banco;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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

public class BancoController {
	private FormHelper formHelper = FormHelper.getInstance();
    private @FXML MaskedTextField txtAgencia;
    private @FXML TextField txtConta, txtBanco;
    private @FXML TableColumn<Banco, Pane> colunaOpc;
    private @FXML TableColumn<Banco, String> colunaBanco, colunaAgencia, colunaConta;
    private @FXML TableView<Banco> tblBanco;
    
    private @FXML void initialize() {
    	formHelper.addValidation(txtConta, FormHelper.REQUIRED);
    	formHelper.addValidation(txtBanco, FormHelper.REQUIRED);
    	formHelper.addValidation(txtAgencia, FormHelper.REQUIRED | FormHelper.VALID_MASK);
    	listarBancos();
    }
    
    private @FXML void salvar() {
    	if (formHelper.validate()) {
    		Banco banco;
    		if (formHelper.getObjectData() != null) {
    			banco = (Banco) formHelper.getObjectData();
    		} else {
    			banco = new Banco();
    		}
    		
    		banco.setAgencia(txtAgencia.getText());
    		banco.setConta(txtConta.getText());
    		banco.setBanco(txtBanco.getText());
    		
    		if (formHelper.getObjectData() == null) {
    			new RetrofitConfig().getBancoService().inserir(banco).enqueue(
					new Callback<Banco>() {
						@Override
						public void onResponse(Call<Banco> call, Response<Banco> response) {
							Platform.runLater(() -> {
								if (response.code() == 500) {
									Main.showErrorDialog("Erro", "Erro ao inserir banco", "Não foi possível inserir o banco, tente novamente mais tarde.", AlertType.ERROR);
								} else {
									montarPainel(response.body());
									tblBanco.getItems().add(response.body());
									Main.showInfDialog("Sucesso", "", "Banco cadastrado com sucesso!!!");
									fecharConteudo();
								}
							});
						}
						
						@Override
						public void onFailure(Call<Banco> call, Throwable t) {
							t.printStackTrace();
							Platform.runLater(() -> 
								Main.showErrorDialog("Erro", "Erro ao inserir banco", "Não foi possível inserir o banco, tente novamente mais tarde.", AlertType.ERROR)
							);
						}
					}
				);
    			
    		} else {
    			new RetrofitConfig().getBancoService().atualizar(banco.getId(), banco).enqueue(
					new Callback<Void>() {
						@Override
						public void onResponse(Call<Void> call, Response<Void> response) {
							Platform.runLater(() -> {
								if (response.code() == 500) {
									Main.showErrorDialog("Erro", "Erro ao atualizar banco", "Não foi possível atualizar o banco, tente novamente mais tarde.", AlertType.ERROR);
								} else {
									formHelper.setObjectData(null);
									tblBanco.refresh();
									Main.showInfDialog("Sucesso", "", "Banco atualizado com sucesso!!!");
									fecharConteudo();
								}
							});
						}
						
						@Override
						public void onFailure(Call<Void> call, Throwable t) {
							t.printStackTrace();
							Platform.runLater(() -> 
								Main.showErrorDialog("Erro", "Erro ao atualizar banco", "Não foi possível atualizar o banco, tente novamente mais tarde.", AlertType.ERROR)
							);
						}
					}
				);
    		}
    	}
    }
    
    private @FXML void fecharConteudo() {
    	formHelper.setObjectData(null);
    	txtConta.clear();
    	txtBanco.clear();
    	txtAgencia.clear();
    }
    
    private void listarBancos() {
    	new RetrofitConfig().getBancoService().listar().enqueue(
    		new Callback<Banco[]>() {
				@Override
				public void onResponse(Call<Banco[]> call, Response<Banco[]> response) {
					if (response.code() == 500) {
						Main.showErrorDialog("Erro", "Erro ao obter lista de bancos", "Não foi possível obter a lista de bancos, tente novamente mais tarde.", AlertType.ERROR);
					} else {
						montarTabela(response.body());
					}
				}
				
				@Override
				public void onFailure(Call<Banco[]> call, Throwable t) {
					t.printStackTrace();
					Platform.runLater(() -> {
						Main.showErrorDialog("Erro", "Erro ao obter lista de bancos", "Não foi possível obter a lista de bancos, tente novamente mais tarde.", AlertType.ERROR);
					});
				}
			}
		);
    }
    
    private void montarPainel(Banco banco) {
    	ImageView imgEditar = new ImageView(new Image(Main.class.getResource("/br/com/food4fit/assets/icons/editar-c.png").toString()));
		imgEditar.prefHeight(15);
		imgEditar.prefWidth(15);
		imgEditar.setCursor(Cursor.HAND);
		imgEditar.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			editarBanco(banco);
		});
		
		ImageView imgExcluir = new ImageView(new Image(Main.class.getResource("/br/com/food4fit/assets/icons/cancelar-c.png").toString()));
		imgExcluir.prefHeight(15);
		imgExcluir.prefWidth(15);
		imgExcluir.setCursor(Cursor.HAND);
		imgExcluir.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			excluirBanco(banco);
		});
		
		HBox hBox = new HBox(imgEditar, imgExcluir);
		hBox.setSpacing(10);
		hBox.setAlignment(Pos.CENTER);
		banco.setPaneOpcoes(hBox);
    }
    
    private void montarTabela(Banco[] bancos) {
    	for (Banco banco : bancos) {
    		montarPainel(banco);
    	}
    	
    	colunaBanco.setCellValueFactory(new PropertyValueFactory<Banco, String>("banco"));
    	colunaAgencia.setCellValueFactory(new PropertyValueFactory<Banco, String>("agencia"));
		colunaConta.setCellValueFactory(new PropertyValueFactory<Banco, String>("conta"));
		colunaOpc.setCellValueFactory(new PropertyValueFactory<Banco, Pane>("paneOpcoes"));
		tblBanco.setItems(FXCollections.observableArrayList(Arrays.asList(bancos)));
    }
    
    private void excluirBanco(Banco banco) {
    	int resposta = Main.showConfirmDialog("Excluir", "Excluir", "Deseja realmente excluir este banco?", AlertType.CONFIRMATION);
		if (resposta == 1) {
			new RetrofitConfig().getBancoService().excluir(banco.getId()).enqueue(
				new Callback<Void>() {
					@Override
					public void onResponse(Call<Void> call, Response<Void> response) {
						Platform.runLater(() -> {
							if (response.code() == 500) {
								Main.showErrorDialog("Erro", "Erro ao excluir banco", "Não foi possível excluir o banco, tente novamente mais tarde.", AlertType.ERROR);
							} else {
								tblBanco.getItems().remove(banco);
								Main.showInfDialog("Sucesso", "", "Banco excluído com sucesso!!!");
							}
						});
					}
					
					@Override
					public void onFailure(Call<Void> call, Throwable t) {
						t.printStackTrace();
						Platform.runLater(() -> {
							Main.showErrorDialog("Erro", "Erro ao excluir banco", "Não foi possível excluir o banco, tente novamente mais tarde.", AlertType.ERROR);
						});
					}
				}
			);
		}
    }
    
    private void editarBanco(Banco banco) {
    	formHelper.setObjectData(banco);
		txtAgencia.setPlainText(banco.getAgencia());
		txtConta.setText(banco.getConta());
		txtBanco.setText(banco.getBanco());
    }
}
