package br.com.food4fit.service;

import br.com.food4fit.model.Banco;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BancoService {
	@GET("banco/")
	Call<Banco[]> lista();

	@POST("banco/")
	Call<Banco> salvar(@Body Banco dados);

	@PUT("banco/{id}")
	Call<Void>editar(@Body Banco dados, @Path("id") int id);

	@DELETE("banco/{id}")
	Call<Void>excluir(@Path("id") int id);
}
