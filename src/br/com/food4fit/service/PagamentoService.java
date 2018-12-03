package br.com.food4fit.service;

import br.com.food4fit.model.BaixaPagamento;
import br.com.food4fit.model.Pagamento;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PagamentoService {
	@GET("pagamento")
	Call<Pagamento[]> listar();
	
	@POST("pagamento/{id}/baixa")
	Call<Void> darBaixa(@Path("id") int id, @Body BaixaPagamento dados);
}
