package br.com.food4fit.service;

import br.com.food4fit.model.UnidadeDeMedida;
import retrofit2.Call;
import retrofit2.http.GET;

public interface UnidadeDeMedidaService {

	@GET("unidade-medida/")

	Call<UnidadeDeMedida[]> lista();
}
