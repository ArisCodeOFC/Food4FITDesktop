package br.com.food4fit.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.food4fit.Main;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.helper.FormHelper;
import br.com.food4fit.model.Cargo;
import br.com.food4fit.model.Permissao;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NivelAcessoController {
	private FormHelper formHelper = FormHelper.getInstance();
	private @FXML VBox vboxPermissoes;
	private @FXML TextField txtTitulo;
	private @FXML TableView<Cargo> tblCargos;
	private @FXML TableColumn<Cargo, Integer> colunaId;
	private @FXML TableColumn<Cargo, String> colunaCargo;
	private @FXML TableColumn<Cargo, Pane> colunaOpcoes;
	
	private @FXML void initialize() {
		formHelper.addValidation(txtTitulo, FormHelper.REQUIRED);
		listarCargos();
		listarPermissoes();
	}
	
	private void listarCargos() {
		new RetrofitConfig().getCargoService().listar().enqueue(new Callback<Cargo[]>() {
			@Override
			public void onResponse(Call<Cargo[]> call, Response<Cargo[]> response) {
				Platform.runLater(() -> {
					if (response.code() == 500) {
						Main.showErrorDialog("Erro", "Erro ao obter lista de cargos", "Não foi possível obter a lista de cargos, tente novamente mais tarde.", AlertType.ERROR);
					} else {
						montarTabela(response.body());
					}
				});
			}
			
			@Override
			public void onFailure(Call<Cargo[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> {
					Main.showErrorDialog("Erro", "Erro ao obter lista de cargos", "Não foi possível obter a lista de cargos, tente novamente mais tarde.", AlertType.ERROR);
				});
			}
		});
	}
	
	private void listarPermissoes() {
		new RetrofitConfig().getPermissaoService().listar().enqueue(new Callback<Permissao[]>() {
			@Override
			public void onResponse(Call<Permissao[]> call, Response<Permissao[]> response) {
				Platform.runLater(() -> {
					if (response.code() == 500) {
						Main.showErrorDialog("Erro", "Erro ao obter lista de permissões", "Não foi possível obter a lista de permissões, tente novamente mais tarde.", AlertType.ERROR);
					} else {
						preencherPermissoes(response.body());
					}
				});
			}
			
			@Override
			public void onFailure(Call<Permissao[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> {
					Main.showErrorDialog("Erro", "Erro ao obter lista de permissões", "Não foi possível obter a lista de permissões, tente novamente mais tarde.", AlertType.ERROR);
				});
			}
		});
	}
	
	private void preencherPermissoes(Permissao[] permissoes) {
		List<Permissao> listaPermissoes = Arrays.asList(permissoes);
		
		vboxPermissoes.getChildren().add(new Label("Permissões Desktop"));
		listaPermissoes.stream().filter(permissao -> !permissao.isWeb()).forEachOrdered(permissao -> {
			CheckBox checkbox = new CheckBox(permissao.getDescricao());
			checkbox.setUserData(permissao);
			vboxPermissoes.getChildren().add(checkbox);
		});
		
		Separator separator = new Separator();
		separator.setPrefWidth(vboxPermissoes.getWidth());
		vboxPermissoes.getChildren().add(separator);
		vboxPermissoes.getChildren().add(new Label("Permissões Web"));
		listaPermissoes.stream().filter(permissao -> permissao.isWeb()).forEachOrdered(permissao -> {
			CheckBox checkbox = new CheckBox(permissao.getDescricao());
			checkbox.setUserData(permissao);
			vboxPermissoes.getChildren().add(checkbox);
		});
	}
	
	private @FXML void salvar() {
		if (formHelper.validate()) {
			Cargo cargo;
			if (formHelper.getObjectData() != null) {
				cargo = (Cargo) formHelper.getObjectData();
			} else {
				cargo = new Cargo();
			}
			
			cargo.setCargo(txtTitulo.getText());
			cargo.setPermissoes(new ArrayList<>());
			
			for (Node node : vboxPermissoes.getChildren()) {
				if (node instanceof CheckBox) {
					CheckBox checkbox = (CheckBox) node;
					if (checkbox.isSelected() && checkbox.getUserData() != null) {
						Permissao permissao = (Permissao) checkbox.getUserData();
						cargo.getPermissoes().add(permissao);
					}
				}
			}
			
			if (formHelper.getObjectData() == null) {
				new RetrofitConfig().getCargoService().inserir(cargo).enqueue(new Callback<Cargo>() {
					@Override
					public void onResponse(Call<Cargo> call, Response<Cargo> response) {
						Platform.runLater(() -> {
							if (response.code() == 500) {
								Main.showErrorDialog("Erro", "Erro ao salvar cargo", "Não foi possível salvar o cargo, tente novamente mais tarde.", AlertType.ERROR);
							} else {
								montarPainel(response.body());
								tblCargos.getItems().add(response.body());
								Main.showInfDialog("Sucesso", "", "Cargo cadastrado com sucesso!!!");
								limpar();
							}
						});
					}
					
					@Override
					public void onFailure(Call<Cargo> call, Throwable t) {
						t.printStackTrace();
						Platform.runLater(() -> {
							Main.showErrorDialog("Erro", "Erro ao salvar cargo", "Não foi possível salvar o cargo, tente novamente mais tarde.", AlertType.ERROR);
						});
					}
				});
				
			} else {
				new RetrofitConfig().getCargoService().atualizar(cargo.getId(), cargo).enqueue(new Callback<Void>() {
					@Override
					public void onResponse(Call<Void> call, Response<Void> response) {
						Platform.runLater(() -> {
							if (response.code() == 500) {
								Main.showErrorDialog("Erro", "Erro ao atualizar cargo", "Não foi possível atualizar o cargo, tente novamente mais tarde.", AlertType.ERROR);
							} else {
								formHelper.setObjectData(null);
								tblCargos.refresh();
								Main.showInfDialog("Sucesso", "", "Cargo atualizado com sucesso!!!");
								limpar();
							}
						});
					}
					
					@Override
					public void onFailure(Call<Void> call, Throwable t) {
						t.printStackTrace();
						Platform.runLater(() -> {
							Main.showErrorDialog("Erro", "Erro ao atualizar cargo", "Não foi possível atualizar o cargo, tente novamente mais tarde.", AlertType.ERROR);
						});
					}
				});
			}
		}
	}
	
	private @FXML void limpar() {
		formHelper.setObjectData(null);
		txtTitulo.clear();
		for (Node node : vboxPermissoes.getChildren()) {
			if (node instanceof CheckBox) {
				CheckBox checkbox = (CheckBox) node;
				checkbox.setSelected(false);
			}
		}
	}
	
	private void montarPainel(Cargo cargo) {
		ImageView imgEditar = new ImageView(new Image(Main.class.getResource("/br/com/food4fit/assets/icons/editar-c.png").toString()));
		imgEditar.prefHeight(15);
		imgEditar.prefWidth(15);
		imgEditar.setCursor(Cursor.HAND);
		imgEditar.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			editarCargo(cargo);
		});
		
		ImageView imgExcluir = new ImageView(new Image(Main.class.getResource("/br/com/food4fit/assets/icons/cancelar-c.png").toString()));
		imgExcluir.prefHeight(15);
		imgExcluir.prefWidth(15);
		imgExcluir.setCursor(Cursor.HAND);
		imgExcluir.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			excluirCargo(cargo);
		});
		
		HBox hBox = new HBox(imgEditar, imgExcluir);
		hBox.setSpacing(10);
		hBox.setAlignment(Pos.CENTER);
		cargo.setPaneOpcoes(hBox);
	}
	
	private void montarTabela(Cargo[] cargos) {
		for (Cargo cargo : cargos) {
			montarPainel(cargo);
		}
		
		colunaId.setCellValueFactory(new PropertyValueFactory<Cargo, Integer>("id"));
		colunaCargo.setCellValueFactory(new PropertyValueFactory<Cargo, String>("cargo"));
		colunaOpcoes.setCellValueFactory(new PropertyValueFactory<Cargo, Pane>("paneOpcoes"));
		tblCargos.setItems(FXCollections.observableArrayList(Arrays.asList(cargos)));
	}
	
	private void excluirCargo(Cargo cargo) {
		int resposta = Main.showConfirmDialog("Excluir", "Excluir", "Deseja realmente excluir este cargo?", AlertType.CONFIRMATION);
		if (resposta == 1) {
			new RetrofitConfig().getCargoService().excluir(cargo.getId()).enqueue(new Callback<Void>() {
				@Override
				public void onResponse(Call<Void> call, Response<Void> response) {
					Platform.runLater(() -> {
						if (response.code() == 500) {
							Main.showErrorDialog("Erro", "Erro ao excluir cargo", "Não foi possível excluir o cargo, tente novamente mais tarde.", AlertType.ERROR);
						} else {
							tblCargos.getItems().remove(cargo);
							Main.showInfDialog("Sucesso", "", "Cargo excluído com sucesso!!!");
						}
					});
				}
				
				@Override
				public void onFailure(Call<Void> call, Throwable t) {
					t.printStackTrace();
					Platform.runLater(() -> {
						Main.showErrorDialog("Erro", "Erro ao excluir cargo", "Não foi possível excluir o cargo, tente novamente mais tarde.", AlertType.ERROR);
					});
				}
			});
		}
	}
	
	private void editarCargo(Cargo cargo) {
		formHelper.setObjectData(cargo);
		txtTitulo.setText(cargo.getCargo());
		for (Node node : vboxPermissoes.getChildren()) {
			if (node instanceof CheckBox) {
				CheckBox checkbox = (CheckBox) node;
				if (checkbox.getUserData() != null) {
					Permissao permissao = (Permissao) checkbox.getUserData();
					checkbox.setSelected(cargo.getPermissoes().contains(permissao));
				}
			}
		}
	}
}
