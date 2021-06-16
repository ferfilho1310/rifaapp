package br.com.medeve.Repository

import br.com.medeve.Retrofit.ICepService

class CepRepository(val IGetCepService : ICepService) {
    suspend fun getCep(id : String) = IGetCepService.cep(id)
}