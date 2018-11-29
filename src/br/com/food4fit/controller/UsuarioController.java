package br.com.food4fit.controller;

import java.util.Arrays;
import java.util.List;

import br.com.food4fit.Main;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.model.Funcionario;
import br.com.food4fit.model.Permissao;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Alert.AlertType;
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
	
	@Override
	public void init(Object... params) {
		funcionario = (Funcionario) params[0];
		lblNome.setText(funcionario.getNome());
		lblEmail.setText(funcionario.getEmail());
		lblCargo.setText(funcionario.getCargo().getCargo());
		imageFuncionario.setImage(new Image("http://www.food4fit.com/" + funcionario.getAvatar()));
		listarPermissoes();
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
			}
			
			vboxPermissoes.getChildren().add(checkbox);
		});
	}
}
