package br.com.food4fit.service;

import br.com.food4fit.model.Departamento;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DepartamentoService {
	@GET("departamento")
	Call<Departamento[]> listar();

	@POST("departamento")
	Call<Departamento> salvar(@Body Departamento dados);

	@DELETE("departamento/{id}")
	Call<Void> excluir(@Path("id") int id);
	
	@PUT("departamento/{id}")
	Call<Void> atualizar(@Path("id") int id, @Body Departamento departamento);
}
