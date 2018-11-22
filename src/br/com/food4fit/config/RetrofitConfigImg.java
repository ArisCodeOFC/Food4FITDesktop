package br.com.food4fit.config;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import br.com.food4fit.service.AvatarService;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter.Factory;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.ScalarsConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfigImg {
	private final Retrofit retrofit;

	public RetrofitConfigImg(){
		this.retrofit = new Retrofit.Builder()
				.baseUrl("http://localhost/inf4t/Allan/Food-4FitWEB-Procedure-master/")
				.addConverterFactory(new Factory() {
					private final MediaType MEDIA_TYPE = MediaType.parse("text/plain");

					@Override
					public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations,
							Annotation[] methodAnnotations, Retrofit retrofit) {
						if (String.class.equals(type)) {
				            return new Converter<String, RequestBody>() {
				                @Override
				                public RequestBody convert(String value) throws IOException {
				                    return RequestBody.create(MEDIA_TYPE, value);
				                }
				            };
				        }
				        return null;
					}

					@Override
					public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
							Retrofit retrofit) {
						if (String.class.equals(type)) {
				            return new Converter<ResponseBody, String>() {
				                @Override
				                public String convert(ResponseBody value) throws IOException
				                {
				                    return value.string();
				                }
				            };
				        }
				        return null;
					}
				})
				.build();
	}

	public AvatarService getAvatarService(){
		return this.retrofit.create(AvatarService.class);
	}
}
