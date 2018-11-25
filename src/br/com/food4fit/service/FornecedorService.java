package br.com.food4fit.service;

import br.com.food4fit.model.Fornecedor;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface FornecedorService {
	@GET("fornecedor")
	Call<Fornecedor[]> listar();
	
	@POST("fornecedor")
	Call<Fornecedor> inserir(@Body Fornecedor fornecedor);

	@DELETE("fornecedor/{id}")
	Call<Void> excluir(@Path("id") int id);
	
	@PUT("fornecedor/{id}")
	Call<Void> atualizar(@Path("id") int id, @Body Fornecedor fornecedor);
}
