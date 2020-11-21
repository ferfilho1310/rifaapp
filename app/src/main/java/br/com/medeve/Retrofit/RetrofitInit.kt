package br.com.medeve.Retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInit {
    private val retrofit: Retrofit

    fun getcep(): PostmonService {
        return retrofit.create(PostmonService::class.java)
    }

    init {
        retrofit = Retrofit.Builder()
                .baseUrl("http://ws.matheuscastiglioni.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
}