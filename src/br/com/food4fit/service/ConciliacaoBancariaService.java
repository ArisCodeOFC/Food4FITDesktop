package br.com.food4fit.service;

import br.com.food4fit.model.ConciliacaoBancaria;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ConciliacaoBancariaService {
	@GET("conciliacao-bancaria/{id}")
	Call<ConciliacaoBancaria[]> listar(@Path("id") int id);
	
	@POST("conciliacao-bancaria/{tipo}/{id}")
	Call<Void> conciliar(@Path("tipo") int tipo, @Path("id") int id);
}
