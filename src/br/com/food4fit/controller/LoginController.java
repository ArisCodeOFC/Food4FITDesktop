package br.com.food4fit.controller;

import br.com.food4fit.Main;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.model.DadosFuncionario;
import br.com.food4fit.model.Funcionario;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginController {

    @FXML
    private TextField txt;

    @FXML
    private PasswordField txtSenha;

    @FXML
    void semConta() {
    	Main.abrirTela("MensagemLogin");
    }

    @FXML
    void login() {

    	String senha = txtSenha.getText();

    	int matricula = Integer.parseInt(txt.getText());

    	DadosFuncionario dadosFuncionario = new DadosFuncionario();

    	dadosFuncionario.setMatricula(matricula);
    	dadosFuncionario.setSenha(senha);

    	Call<Funcionario> retorno =  new RetrofitConfig().getFuncionarioService().login(dadosFuncionario);

    	retorno.enqueue(new Callback<Funcionario>() {

			@Override
			public void onResponse(Call<Funcionario> call, Response<Funcionario> response) {
				if(response.code() == 401 ){
					//mensagem pro usuario 'matricula ou senha incorreto'
				}else{

					Funcionario funcionario = response.body();
					if(funcionario == null){
						//mensagem pro usuario 'matricula ou senha incorreto'
					}else{
						Main.abrirTela("PadraoLayout");
					}

				}


			}

			@Override
			public void onFailure(Call<Funcionario> arg0, Throwable arg1) {
				//mensagem pro usuario
			}
		});

    	System.out.println(senha);

    }
}
