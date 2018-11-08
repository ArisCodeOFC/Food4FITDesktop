package br.com.food4fit.service;

import br.com.food4fit.model.DadosFuncionario;
import br.com.food4fit.model.Funcionario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface FuncionarioService {

	@GET("funcionario/")
	Call<Funcionario[]> lista();



	@POST("funcionario/login")

	//<Funcionario> -> o que esta sendo recebido
	//login -> criando metodo
	//(@Body DadosFuncionario dados) -> o que esta sendo enviado
	Call<Funcionario> login(@Body DadosFuncionario dados);



	@DELETE("funcionario/{id}")

	Call<Void> excluir(@Path ("id")int id);

	@PUT("funcionario/{id}")

	Call<Void> editar(@Body Funcionario dados, @Path ("id") int id);

}
