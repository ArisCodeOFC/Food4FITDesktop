package br.com.food4fit.service;

import br.com.food4fit.model.DadosFuncionario;
import br.com.food4fit.model.Funcionario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FuncionarioService {

	@POST("funcionario/login")

	//<Funcionario> -> o que esta sendo recebido
	//login -> criando metodo
	//(@Body DadosFuncionario dados) -> o que esta sendo enviado
	Call<Funcionario> login(@Body DadosFuncionario dados);

}
