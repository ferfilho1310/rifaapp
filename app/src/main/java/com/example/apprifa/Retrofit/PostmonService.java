package com.example.apprifa.Retrofit;

import com.example.apprifa.Models.Cliente;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface PostmonService {
        @GET("{cep}")
        Call<Cliente> cep(@Path("cep") String cep);
    }

