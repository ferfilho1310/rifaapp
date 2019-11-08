package br.com.medeve.Retrofit;

import br.com.medeve.Models.Cliente;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface PostmonService {
    @GET("cep/find/{id}/json")
    Call<Cliente> cep(@Path("id") String cep);
}

