package br.com.food4fit.service;

import br.com.food4fit.model.PedidoCompra;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PedidoCompraService {
	@GET("pedido-compra")
	Call<PedidoCompra[]> listar();
	
	@POST("pedido-compra")
	Call<PedidoCompra> inserir(@Body PedidoCompra pedido);
	
	@PUT("pedido-compra/{id}")
	Call<Void> atualizar(@Path("id") int id, @Body PedidoCompra pedido);
	
	@DELETE("pedido-compra/{id}")
	Call<Void> excluir(@Path("id") int id);
	
	@POST("pedido-compra/{id}/nota-fiscal")
	Call<Void> lancarNotaFiscal(@Path("id") int id, @Body PedidoCompra pedido);
}
