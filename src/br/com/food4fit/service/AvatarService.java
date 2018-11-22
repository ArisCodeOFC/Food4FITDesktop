package br.com.food4fit.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AvatarService {
	@POST("upload-desk.php")
	Call<String> enviar(@Body String avatar);
}
