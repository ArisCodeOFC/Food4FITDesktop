package br.com.food4fit.controller;

import java.util.Arrays;

import br.com.food4fit.Main;
import br.com.food4fit.component.MaskedTextField;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.helper.FormHelper;
import br.com.food4fit.model.Cidade;
import br.com.food4fit.model.Endereco;
import br.com.food4fit.model.Estado;
import br.com.food4fit.model.Fornecedor;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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

public class FornecedorController {
	private FormHelper formHelper = FormHelper.getInstance();
	private @FXML Pane paneConteudo;
	private @FXML TextField txtNomeFantasia, txtRazaoSocial, txtRepresentante, txtEmail, txtBairro, txtLogradouro, txtComplemento, txtNumero;
	private @FXML MaskedTextField txtInscricaoEstadual, txtCnpj, txtTelefone, txtCep;
	private @FXML TextArea txtObs;
	private @FXML ComboBox<Estado> comboEstado;
	private @FXML ComboBox<Cidade> comboCidade;
	private @FXML TableView<Fornecedor> tblFornecedor;
	private @FXML TableColumn<Fornecedor, String> colunaNome, colunaRazao, colunaCnpj, colunaEmail;
	private @FXML TableColumn<Fornecedor, Pane> colunaOpcoes;

	private @FXML void initialize() {
		paneConteudo.setVisible(false);;
		formHelper.addValidation(txtNomeFantasia, FormHelper.REQUIRED);
		formHelper.addValidation(txtRazaoSocial, FormHelper.REQUIRED);
		formHelper.addValidation(txtInscricaoEstadual, FormHelper.REQUIRED | FormHelper.VALID_MASK);
		formHelper.addValidation(txtCnpj, FormHelper.REQUIRED | FormHelper.VALID_MASK);
		formHelper.addValidation(txtTelefone, FormHelper.REQUIRED | FormHelper.VALID_MASK);
		formHelper.addValidation(txtRepresentante, FormHelper.REQUIRED);
		formHelper.addValidation(txtEmail, FormHelper.REQUIRED | FormHelper.VALID_EMAIL);
		formHelper.addValidation(comboEstado, FormHelper.REQUIRED);
		formHelper.addValidation(comboCidade, FormHelper.REQUIRED);
		formHelper.addValidation(txtCep, FormHelper.REQUIRED | FormHelper.VALID_MASK);
		formHelper.addValidation(txtBairro, FormHelper.REQUIRED);
		formHelper.addValidation(txtLogradouro, FormHelper.REQUIRED);
		formHelper.addValidation(txtNumero, FormHelper.REQUIRED);
		
		comboEstado.valueProperty().addListener((obs, oldval, newval) -> {
			comboCidade.setValue(null);
			if (newval != null) {
				listarCidades(((Estado) newval).getId());
			}
		});
		
		listarFornecedores();
		listarEstados();
	}

	private @FXML void salvar() {
		if (formHelper.validate()) {
			Fornecedor fornecedor;
			if (formHelper.getObjectData() != null) {
				fornecedor = (Fornecedor) formHelper.getObjectData();
			} else {
				fornecedor = new Fornecedor();
			}
			
			fornecedor.setNomeFantasia(txtNomeFantasia.getText());
			fornecedor.setRazaoSocial(txtRazaoSocial.getText());
			fornecedor.setInscricaoEstadual(txtInscricaoEstadual.getText());
			fornecedor.setCnpj(txtCnpj.getText());
			fornecedor.setTelefone(txtTelefone.getText());
			fornecedor.setRepresentante(txtRepresentante.getText());
			fornecedor.setEmail(txtEmail.getText());
			fornecedor.setObservacao(txtObs.getText());
			
			Endereco endereco = fornecedor.getEndereco() == null ? new Endereco() : fornecedor.getEndereco();
			endereco.setCidade(comboCidade.getValue());
			endereco.setEstado(comboEstado.getValue());
			endereco.setBairro(txtBairro.getText());
			endereco.setLogradouro(txtLogradouro.getText());
			endereco.setNumero(txtNumero.getText());
			endereco.setComplemento(txtComplemento.getText());
			endereco.setCep(txtCep.getText());
			fornecedor.setEndereco(endereco);
			
			if (formHelper.getObjectData() == null) {
				new RetrofitConfig().getFornecedorService().inserir(fornecedor).enqueue(
					new Callback<Fornecedor>() {
						@Override
						public void onResponse(Call<Fornecedor> call, Response<Fornecedor> response) {
							Platform.runLater(() -> {
								if (response.code() == 500) {
									Main.showErrorDialog("Erro", "Erro ao inserir fornecedor", "Não foi possível inserir o fornecedor, tente novamente mais tarde.", AlertType.ERROR);
								} else {
									montarPainel(response.body());
									tblFornecedor.getItems().add(response.body());
									Main.showInfDialog("Sucesso", "", "Fornecedor cadastrado com sucesso!!!");
									fecharConteudo();
								}
							});
						}
						
						@Override
						public void onFailure(Call<Fornecedor> call, Throwable t) {
							t.printStackTrace();
							Platform.runLater(() -> 
								Main.showErrorDialog("Erro", "Erro ao inserir fornecedor", "Não foi possível inserir o fornecedor, tente novamente mais tarde.", AlertType.ERROR)
							);
						}
					}
				);
				
			} else {
				new RetrofitConfig().getFornecedorService().atualizar(fornecedor.getId(), fornecedor).enqueue(
					new Callback<Void>() {
						@Override
						public void onResponse(Call<Void> call, Response<Void> response) {
							Platform.runLater(() -> {
								if (response.code() == 500) {
									Main.showErrorDialog("Erro", "Erro ao atualizar fornecedor", "Não foi possível atualizar o fornecedor, tente novamente mais tarde.", AlertType.ERROR);
								} else {
									formHelper.setObjectData(null);
									tblFornecedor.refresh();
									Main.showInfDialog("Sucesso", "", "Fornecedor atualizado com sucesso!!!");
									fecharConteudo();
								}
							});
						}
						
						@Override
						public void onFailure(Call<Void> call, Throwable t) {
							t.printStackTrace();
							Platform.runLater(() -> 
								Main.showErrorDialog("Erro", "Erro ao atualizar fornecedor", "Não foi possível atualizar o fornecedor, tente novamente mais tarde.", AlertType.ERROR)
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
		txtNomeFantasia.setText("");
		txtRazaoSocial.setText("");
		txtInscricaoEstadual.setPlainText("");
		txtCnpj.setPlainText("");
		txtRepresentante.setText("");
		txtEmail.setText("");
		txtTelefone.setPlainText("");
		txtObs.setText("");
		comboEstado.setValue(null);
		comboCidade.setValue(null);
		txtBairro.setText("");
		txtLogradouro.setText("");
		txtNumero.setText("");
		txtCep.setPlainText("");
		txtComplemento.setText("");
	}
	
	private void listarFornecedores() {
		new RetrofitConfig().getFornecedorService().listar().enqueue(new Callback<Fornecedor[]>() {
			@Override
			public void onResponse(Call<Fornecedor[]> call, Response<Fornecedor[]> response) {
				if (response.code() == 500) {
					Platform.runLater(() -> 
						Main.showErrorDialog("Erro", "Erro ao obter lista de fornecedores", "Não foi possível obter a lista de fornecedores, tente novamente mais tarde.", AlertType.ERROR)
					);
					
				} else {
					montarTabela(response.body());
				}
			}
			
			@Override
			public void onFailure(Call<Fornecedor[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> 
					Main.showErrorDialog("Erro", "Erro ao obter lista de fornecedores", "Não foi possível obter a lista de fornecedores, tente novamente mais tarde.", AlertType.ERROR)
				);
			}
		});
	}
	
	private void montarPainel(Fornecedor fornecedor) {
		ImageView imgEditar = new ImageView(new Image(Main.class.getResource("/br/com/food4fit/assets/icons/editar-c.png").toString()));
		imgEditar.prefHeight(15);
		imgEditar.prefWidth(15);
		imgEditar.setCursor(Cursor.HAND);
		imgEditar.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			editarFornecedor(fornecedor);
		});
		
		ImageView imgExcluir = new ImageView(new Image(Main.class.getResource("/br/com/food4fit/assets/icons/cancelar-c.png").toString()));
		imgExcluir.prefHeight(15);
		imgExcluir.prefWidth(15);
		imgExcluir.setCursor(Cursor.HAND);
		imgExcluir.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			excluirFornecedor(fornecedor);
		});
		
		HBox hBox = new HBox(imgEditar, imgExcluir);
		hBox.setPrefHeight(15);
		hBox.setPrefWidth(15);
		hBox.setSpacing(10);
		hBox.setPadding(new Insets(0, 0, 0, 32));
		fornecedor.setPaneOpcoes(hBox);
	}
	
	private void montarTabela(Fornecedor[] fornecedores) {
		for (Fornecedor fornecedor : fornecedores) {
			montarPainel(fornecedor);
		}

		colunaNome.setCellValueFactory(new PropertyValueFactory<Fornecedor, String>("nomeFantasia"));
		colunaRazao.setCellValueFactory(new PropertyValueFactory<Fornecedor, String>("razaoSocial"));
		colunaCnpj.setCellValueFactory(new PropertyValueFactory<Fornecedor, String>("cnpj"));
		colunaEmail.setCellValueFactory(new PropertyValueFactory<Fornecedor, String>("email"));
		colunaOpcoes.setCellValueFactory(new PropertyValueFactory<Fornecedor, Pane>("paneOpcoes"));
		tblFornecedor.setItems(FXCollections.observableArrayList(Arrays.asList(fornecedores)));
	}
	
	private void listarEstados() {
		comboEstado.setCursor(Cursor.WAIT);
		new RetrofitConfig().getEstadoService().listarEstado().enqueue(new Callback<Estado[]>() {
			@Override
			public void onResponse(Call<Estado[]> call, Response<Estado[]> response) {
				Platform.runLater(() -> {
					comboEstado.setCursor(Cursor.HAND);
					if (response.code() == 500) {
						Main.showErrorDialog("Erro", "Erro ao obter lista de estados", "Não foi possível obter a lista de estados, tente novamente mais tarde.", AlertType.ERROR);
					} else {
						comboEstado.setItems(FXCollections.observableArrayList(Arrays.asList(response.body())));
					}
				});
			}
			
			@Override
			public void onFailure(Call<Estado[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> {
					comboEstado.setCursor(Cursor.HAND);
					Main.showErrorDialog("Erro", "Erro ao obter lista de estados", "Não foi possível obter a lista de estados, tente novamente mais tarde.", AlertType.ERROR);
				});
			}
		});
	}
	
	private void listarCidades(int id) {
		comboCidade.setCursor(Cursor.WAIT);
		new RetrofitConfig().getEstadoService().listarCidade(id).enqueue(new Callback<Cidade[]>() {
			@Override
			public void onResponse(Call<Cidade[]> call, Response<Cidade[]> response) {
				comboCidade.setCursor(Cursor.HAND);
				if (response.code() == 500) {
					Main.showErrorDialog("Erro", "Erro ao obter lista de cidades", "Não foi possível obter a lista de cidades, tente novamente mais tarde.", AlertType.ERROR);
				} else {
					comboCidade.setItems(FXCollections.observableArrayList(Arrays.asList(response.body())));
				}
			}
			
			@Override
			public void onFailure(Call<Cidade[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> {
					comboCidade.setCursor(Cursor.HAND);
					Main.showErrorDialog("Erro", "Erro ao obter lista de cidades", "Não foi possível obter a lista de cidades, tente novamente mais tarde.", AlertType.ERROR);
				});
			}
		});
	}
	
	private void excluirFornecedor(Fornecedor fornecedor) {
		int resposta = Main.showConfirmDialog("Excluir", "Excluir", "Deseja realmente excluir este fornecedor?", AlertType.CONFIRMATION);
		if (resposta == 1) {
			new RetrofitConfig().getFornecedorService().excluir(fornecedor.getId()).enqueue(
				new Callback<Void>() {
					@Override
					public void onResponse(Call<Void> call, Response<Void> response) {
						Platform.runLater(() -> {
							if (response.code() == 500) {
								Main.showErrorDialog("Erro", "Erro ao excluir fornecedor", "Não foi possível excluir o fornecedor, tente novamente mais tarde.", AlertType.ERROR);
							} else {
								tblFornecedor.getItems().remove(fornecedor);
								Main.showInfDialog("Sucesso", "", "Fornecedor excluído com sucesso!!!");
							}
						});
					}
					
					@Override
					public void onFailure(Call<Void> call, Throwable t) {
						t.printStackTrace();
						Platform.runLater(() -> {
							Main.showErrorDialog("Erro", "Erro ao excluir fornecedor", "Não foi possível excluir o fornecedor, tente novamente mais tarde.", AlertType.ERROR);
						});
					}
				}
			);
		}
	}
	
	private void editarFornecedor(Fornecedor fornecedor) {
		txtNomeFantasia.setText(fornecedor.getNomeFantasia());
		txtRazaoSocial.setText(fornecedor.getRazaoSocial());
		txtInscricaoEstadual.setPlainText(fornecedor.getInscricaoEstadual());
		txtCnpj.setPlainText(fornecedor.getCnpj());
		txtRepresentante.setText(fornecedor.getRepresentante());
		txtEmail.setText(fornecedor.getEmail());
		txtTelefone.setPlainText(fornecedor.getTelefone());
		txtObs.setText(fornecedor.getObservacao());
		comboEstado.setValue(fornecedor.getEndereco().getEstado());
		comboCidade.setValue(fornecedor.getEndereco().getCidade());
		txtBairro.setText(fornecedor.getEndereco().getBairro());
		txtLogradouro.setText(fornecedor.getEndereco().getLogradouro());
		txtNumero.setText(fornecedor.getEndereco().getNumero());
		txtCep.setPlainText(fornecedor.getEndereco().getCep());
		txtComplemento.setText(fornecedor.getEndereco().getComplemento());
		formHelper.setObjectData(fornecedor);
		abrirConteudo();
	}
}
