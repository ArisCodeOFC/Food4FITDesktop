package br.com.food4fit.service;

import br.com.food4fit.model.Cidade;
import br.com.food4fit.model.Estado;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EstadoService {

	@GET("estado")

	Call<Estado[]> listarEstado();

	@GET("estado/{id}/cidade")
	Call<Cidade[]> listarCidade(@Path("id") int id);
}
