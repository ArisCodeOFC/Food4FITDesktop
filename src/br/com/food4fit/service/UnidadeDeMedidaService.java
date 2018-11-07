package br.com.food4fit.service;

import org.intellij.lang.annotations.JdkConstants.PatternFlags;

import br.com.food4fit.model.UnidadeDeMedida;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UnidadeDeMedidaService {

	@GET("unidade-medida/")

	Call<UnidadeDeMedida[]> lista();


	@POST("unidade-medida/")

	Call<UnidadeDeMedida> salvar(@Body UnidadeDeMedida dados);


	@PUT("unidade-medida/{id}")

	Call<Void> editar(@Body UnidadeDeMedida dados, @Path("id") int id);


	@DELETE("unidade-medida/{id}")

	Call<Void> excluir(@Path("id") int id);
}
