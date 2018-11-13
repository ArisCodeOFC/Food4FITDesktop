package br.com.food4fit.service;



import br.com.food4fit.model.Cargo;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CargoService {

	@GET("cargo")

	Call<Cargo[]>  listar();

}
