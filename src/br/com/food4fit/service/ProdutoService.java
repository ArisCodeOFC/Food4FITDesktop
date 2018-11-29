package br.com.food4fit.service;

import br.com.food4fit.model.Produto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProdutoService {
	@GET("produto")
	Call<Produto[]> listar();

	@POST("produto")
	Call<Produto> salvar(@Body Produto dados);

	@PUT("produto/{id}")
	Call<Void> editar(@Body Produto dados, @Path ("id") int id);

	@DELETE("produto/{id}")
	Call<Void> excluir(@Path ("id") int id);
}
