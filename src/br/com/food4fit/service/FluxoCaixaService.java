package br.com.food4fit.service;

import br.com.food4fit.model.FluxoCaixa;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FluxoCaixaService {
	@GET("fluxo-caixa")
	Call<FluxoCaixa[]> listar(@Query("dataInicial") long dataInicial, @Query("dataFinal") long dataFinal);
}
