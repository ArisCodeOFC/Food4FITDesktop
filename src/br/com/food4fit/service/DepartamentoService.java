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

//	@PUT("departamento/{id}")
//	Call<Void>editar(@Body Departamento dados, @Path("id") int id);

	@DELETE("departamentro/{id}")
	Call<Void>excluir(@Path("id") int id);
}
