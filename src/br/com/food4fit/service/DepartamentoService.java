package br.com.food4fit.service;

import br.com.food4fit.model.Departamento;
import retrofit2.Call;
import retrofit2.http.GET;

public interface DepartamentoService {

	@GET("departamento")

	Call<Departamento[]> listar();
}
