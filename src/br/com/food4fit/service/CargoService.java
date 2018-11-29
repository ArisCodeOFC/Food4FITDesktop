package br.com.food4fit.service;



import br.com.food4fit.model.Cargo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CargoService {
	@GET("cargo")
	Call<Cargo[]> listar();

	@POST("cargo")
	Call<Cargo> inserir(@Body Cargo cargo);
	
	@DELETE("cargo/{id}")
	Call<Void> excluir(@Path("id") int id);
	
	@PUT("cargo/{id}")
	Call<Void> atualizar(@Path("id") int id, @Body Cargo cargo);
}
