package br.com.food4fit.service;

import br.com.food4fit.model.Cargo;
import br.com.food4fit.model.ClassificacaoProduto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ClassificacaoProdutoService {

	@GET("classificacao-produto")
	Call<ClassificacaoProduto[]>  listar();

	@POST("classificacao-produto")
	Call<ClassificacaoProduto> salvar(@Body ClassificacaoProduto dados);

	@PUT("classificacao-produto/{id}")
	Call<Void> editar(@Body ClassificacaoProduto dados,@Path ("id") int id);

	@DELETE("classificacao-produto/{id}")
	Call<Void> excluir(@Path ("id") int id);
}
