package br.com.medeve.Retrofit

import br.com.medeve.Models.Cep
import retrofit2.http.GET
import retrofit2.http.Path

interface ICepService {

    @GET("{id}/json")
    suspend fun cep(@Path("id") cep: String?): Cep
}