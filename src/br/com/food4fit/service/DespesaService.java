package br.com.food4fit.service;

import java.util.List;

import br.com.food4fit.model.BaixaDespesa;
import br.com.food4fit.model.Despesa;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DespesaService {
	@GET("despesa")
	Call<Despesa[]> listar();
	
	@POST("despesa")
	Call<Despesa> inserir(@Body Despesa despesa);
	
	@POST("despesa/multiplo")
	Call<Void> inserir(@Body List<Despesa> despesas);
	
	@PUT("despesa/{id}")
	Call<Void> atualizar(@Path("id") int id, @Body Despesa despesa);
	
	@DELETE("despesa/{id}")
	Call<Void> excluir(@Path("id") int id);
	
	@POST("despesa/{id}/baixa")
	Call<Void> darBaixa(@Path("id") int id, @Body BaixaDespesa dados);
}
