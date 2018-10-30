package br.com.food4fit.config;

import br.com.food4fit.service.FuncionarioService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {

	private final Retrofit retrofit;

	public RetrofitConfig(){
		this.retrofit = new Retrofit.Builder()
				.baseUrl("http://localhost:3000/")
				.addConverterFactory(JacksonConverterFactory.create())
				.build();
	}

	public FuncionarioService getFuncionarioService(){
		return this.retrofit.create(FuncionarioService.class);
	}



}
