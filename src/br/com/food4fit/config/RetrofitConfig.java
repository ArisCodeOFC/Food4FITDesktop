package br.com.food4fit.config;

import br.com.food4fit.service.BancoService;
import br.com.food4fit.service.CargoService;
import br.com.food4fit.service.DepartamentoService;
import br.com.food4fit.service.EstadoService;
import br.com.food4fit.service.FuncionarioService;
import br.com.food4fit.service.UnidadeDeMedidaService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {

	private final Retrofit retrofit;

	public RetrofitConfig(){
		this.retrofit = new Retrofit.Builder()
				.baseUrl("http://142.44.189.41:3000/")
				.addConverterFactory(JacksonConverterFactory.create())
				.build();
	}

	public FuncionarioService getFuncionarioService(){
		return this.retrofit.create(FuncionarioService.class);
	}


	public UnidadeDeMedidaService getUnidadeDeMedida(){
		return this.retrofit.create(UnidadeDeMedidaService.class);
	}

	public CargoService getCargoService(){
		return this.retrofit.create(CargoService.class);
	}

	public DepartamentoService getDepartamentoService(){
		return this.retrofit.create(DepartamentoService.class);
	}

	public EstadoService getEstadoService(){
		return this.retrofit.create(EstadoService.class);
	}

	public BancoService getBancoServece(){
		return this.retrofit.create(BancoService.class);
	}

}
