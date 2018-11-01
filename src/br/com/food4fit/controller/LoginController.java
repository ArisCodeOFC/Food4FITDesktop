package br.com.food4fit.controller;

import java.io.IOException;

import br.com.food4fit.Main;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.model.DadosFuncionario;
import br.com.food4fit.model.Funcionario;
import javafx.application.Platform;
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
    void login() {



    	if(txtMatricula.getText() == null || txtMatricula.getText().isEmpty()){
    		txtMatricula.setStyle("-fx-background-color: #D50000");
    		System.out.println("Favor preencher a matricula");
    		return;
    	}

    	if(txtSenha.getText() == null || txtSenha.getText().isEmpty()){
    		txtSenha.setStyle("-fx-background-color: #D50000");
    		System.out.println("Favor preencher a senha");
    		return;
    	}

    	String senha = txtSenha.getText();

    	int matricula = Integer.parseInt(txtMatricula.getText());


    	DadosFuncionario dadosFuncionario = new DadosFuncionario();

    	dadosFuncionario.setMatricula(matricula);
    	dadosFuncionario.setSenha(senha);

    	Call<Funcionario> retorno =  new RetrofitConfig().getFuncionarioService().login(dadosFuncionario);

    	retorno.enqueue(new Callback<Funcionario>() {

			@Override
			public void onResponse(Call<Funcionario> call, Response<Funcionario> response) {



				if(response.code() == 401 ){
					//mensagem pro usuario 'matricula ou senha incorreto'
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Falha");
					alert.setHeaderText("Look, an Error Dialog");
					alert.setContentText("Ooops, there was an error!");

					alert.showAndWait();
					System.out.println("Matricula ou senha incoretos");
				}else if(response.code() == 500){
					//erro de sql
					System.out.println("Erro ao conectar com o Banco de Dados");
				}else{

					Funcionario funcionario = response.body();
					if(funcionario == null){
						//mensagem pro usuario 'matricula ou senha incorreto'
					}else{
						Platform.runLater(() -> {
							Main.abrirTela("PadraoLayout");

						});
					}

				}


			}

			@Override
			public void onFailure(Call<Funcionario> arg0, Throwable arg1) {
				//mensagem pro usuario quando nao estiver conectado a internet

			}
		});

    }

}
