package br.com.food4fit.service;

import br.com.food4fit.model.Permissao;
import retrofit2.Call;
import retrofit2.http.GET;

public interface PermissaoService {
	@GET("permissao")
	Call<Permissao[]> listar();
}
