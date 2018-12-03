package br.com.food4fit.service;

import br.com.food4fit.model.RelatorioDespesaVencida;
import br.com.food4fit.model.RelatorioReceitaReceber;
import br.com.food4fit.model.RelatorioReceitaRecebida;
import br.com.food4fit.model.RelatorioDespesaPaga;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RelatorioService {
	@GET("relatorio/despesas-vencidas")
	Call<RelatorioDespesaVencida[]> listarDespesasVencidas(@Query("dataInicial") long dataInicial, @Query("dataFinal") long dataFinal);	
	
	@GET("relatorio/despesas-pagas")
	Call<RelatorioDespesaPaga[]> listarDespesasPagas(@Query("dataInicial") long dataInicial, @Query("dataFinal") long dataFinal);
	
	@GET("relatorio/receitas-receber")
	Call<RelatorioReceitaReceber[]> listarReceitasReceber(@Query("dataInicial") long dataInicial, @Query("dataFinal") long dataFinal);	
	
	@GET("relatorio/receitas-recebidas")
	Call<RelatorioReceitaRecebida[]> listarReceitasRecebidas(@Query("dataInicial") long dataInicial, @Query("dataFinal") long dataFinal);
}
