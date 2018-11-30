package br.com.food4fit.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.food4fit.Main;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.model.Funcionario;
import br.com.food4fit.model.FuncionarioUsuario;
import br.com.food4fit.model.Permissao;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioController implements Controller {
	private Funcionario funcionario;
	private @FXML Label lblNome, lblEmail, lblCargo;
	private @FXML ImageView imageFuncionario;
	private @FXML VBox vboxPermissoes;
	private @FXML PasswordField txtSenha, txtSenhaConfirmacao;
	
	@Override
	public void init(Object... params) {
		funcionario = (Funcionario) params[0];

		lblNome.setText(funcionario.getNome());
		lblEmail.setText(funcionario.getEmail());
		lblCargo.setText(funcionario.getCargo().getCargo());
		imageFuncionario.setImage(new Image("http://www.food4fit.com/" + funcionario.getAvatar()));
		listarPermissoes();
	}
	
	private @FXML void salvar() {
		if (!txtSenha.getText().equals(txtSenhaConfirmacao.getText())) {
			Main.showErrorDialog("Erro", "Senhas não correspondem", "As senhas digitadas não correspondem.", AlertType.ERROR);
		} else {
			FuncionarioUsuario usuario = new FuncionarioUsuario();
			usuario.setSenha(txtSenha.getText());
			usuario.setPermissoes(new ArrayList<>());
			
			for (Node node : vboxPermissoes.getChildren()) {
				if (node instanceof CheckBox) {
					CheckBox checkbox = (CheckBox) node;
					if (!checkbox.isDisabled() && checkbox.isSelected() && checkbox.getUserData() != null) {
						Permissao permissao = (Permissao) checkbox.getUserData();
						usuario.getPermissoes().add(permissao);
					}
				}
			}
			
			new RetrofitConfig().getFuncionarioService().atualizarDados(usuario, funcionario.getId()).enqueue(new Callback<Void>() {
				@Override
				public void onResponse(Call<Void> call, Response<Void> response) {
					Platform.runLater(() -> {
						if (response.code() == 500) {
							Main.showErrorDialog("Erro", "Erro ao atualizar dados", "Não foi possível atualizar os dados, tente novamente mais tarde.", AlertType.ERROR);
						} else {
							Main.showInfDialog("Sucesso", "", "Dados atualizados com sucesso!!!");
							cancelar();
						}
					});
				}
				
				@Override
				public void onFailure(Call<Void> call, Throwable t) {
					t.printStackTrace();
					Platform.runLater(() -> {
						Main.showErrorDialog("Erro", "Erro ao atualizar dados", "Não foi possível atualizar os dados, tente novamente mais tarde.", AlertType.ERROR);
					});
				}
			});
		}
	}
	
	private @FXML void cancelar() {
		Main.getHome().mudarTela("Funcionarios", "Funcionários");
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
			if (funcionario.getCargo().getPermissoes().contains(permissao)) {
				checkbox.setDisable(true);
				checkbox.setSelected(true);
			} else if (funcionario.getPermissoes().contains(permissao) ) {
				checkbox.setSelected(true);
			}
			
			vboxPermissoes.getChildren().add(checkbox);
		});
		
		Separator separator = new Separator();
		separator.setPrefWidth(vboxPermissoes.getWidth());
		vboxPermissoes.getChildren().add(separator);
		vboxPermissoes.getChildren().add(new Label("Permissões Web"));
		listaPermissoes.stream().filter(permissao -> permissao.isWeb()).forEachOrdered(permissao -> {
			CheckBox checkbox = new CheckBox(permissao.getDescricao());
			checkbox.setUserData(permissao);
			if (funcionario.getCargo().getPermissoes().contains(permissao)) {
				checkbox.setDisable(true);
				checkbox.setSelected(true);
			} else if (funcionario.getPermissoes().contains(permissao) ) {
				checkbox.setSelected(true);
			}
			
			vboxPermissoes.getChildren().add(checkbox);
		});
	}
}
