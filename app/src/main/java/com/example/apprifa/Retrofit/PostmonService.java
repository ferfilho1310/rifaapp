package com.example.apprifa.Retrofit;

import com.example.apprifa.Models.Cliente;
import com.example.apprifa.Models.Cliente_cep;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface PostmonService {
        @GET("cep/find/{id}/json")
        Call<Cliente> cep(@Path("id") String cep);
    }

