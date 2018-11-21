package br.com.food4fit.controller;

import java.io.IOException;

import br.com.food4fit.Main;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.model.DadosFuncionario;
import br.com.food4fit.model.Funcionario;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginController {

	@FXML
	private TextField txtMatricula;

	@FXML
	private PasswordField txtSenha;

	@FXML
	void semConta() {
		Main.abrirTela("MensagemLogin");
	}

	@FXML
	void initialize() {
		Platform.runLater(() -> {
			Main.abrirTela("PadraoLayout");
		});

		txtMatricula.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				txtMatricula.setStyle("-fx-background-color: #fff;" + "-fx-border-color:#9cc283");
				return;
			}
		});

		txtSenha.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				txtSenha.setStyle("-fx-background-color: #fff;" + "-fx-border-color:#9cc283");
				return;
			}
		});
	}

	@FXML
	void login() {
//		Platform.runLater(() -> {
//			Main.abrirTela("PadraoLayout");
//		});


		if (txtMatricula.getText() == null || txtMatricula.getText().isEmpty()) {
			txtMatricula.setStyle("-fx-background-color: #ed2121;" + "-fx-prompt-text-fill: #fff");
			return;
		}

		if (txtSenha.getText() == null || txtSenha.getText().isEmpty()) {
			txtSenha.setStyle("-fx-background-color: #ed2121;" + "-fx-prompt-text-fill: #fff");
			return;
		}

		String senha = txtSenha.getText();

		int matricula = Integer.parseInt(txtMatricula.getText());

		DadosFuncionario dadosFuncionario = new DadosFuncionario();

		dadosFuncionario.setMatricula(matricula);
		dadosFuncionario.setSenha(senha);

		Call<Funcionario> retorno = new RetrofitConfig().getFuncionarioService().login(dadosFuncionario);

		retorno.enqueue(new Callback<Funcionario>() {

			@Override
			public void onResponse(Call<Funcionario> call, Response<Funcionario> response) {

				if (response.code() == 401) {
					// mensagem pro usuario 'matricula ou senha incorreto'
					Platform.runLater(() -> {

						Main.showErrorDialog("Erro", "Falha ao tentar realizar o login",
								"Matricula ou senha incorretos", Alert.AlertType.WARNING);
						txtSenha.clear();

					});

				} else if (response.code() == 500) {
					// erro de sql
					System.out.println("Erro ao conectar com o Banco de Dados");
				} else {

					Funcionario funcionario = response.body();
					if (funcionario == null) {
						// mensagem pro usuario 'matricula ou senha incorreto'
					} else {
						Platform.runLater(() -> {
							Main.setPerfil(funcionario);
							Main.abrirTela("PadraoLayout");
						});
					}

				}

			}

			@Override
			public void onFailure(Call<Funcionario> arg0, Throwable arg1) {
				// mensagem pro usuario quando nao estiver conectado a internet

			}
		});

	}

}
