package br.com.medeve.Retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val retrofit: Retrofit? = null
val URL: String = "https://viacep.com.br/ws/"

fun getcepService(retrofit: Retrofit) = retrofit.create(ICepService::class.java)!!


fun retrofitBuilder(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}