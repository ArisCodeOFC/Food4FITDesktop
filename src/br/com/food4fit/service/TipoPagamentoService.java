package br.com.food4fit.service;

import br.com.food4fit.model.TipoPagamento;
import retrofit2.Call;
import retrofit2.http.GET;

public interface TipoPagamentoService {
	@GET("tipo-pagamento")
	Call<TipoPagamento[]> listar();
}
