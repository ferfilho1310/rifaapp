package br.com.medeve.Interfaces

import androidx.lifecycle.MutableLiveData
import br.com.medeve.Models.DataCobrancaVenda
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query

interface IDataVendasClienteRepository {

    fun buscarDatasClienteRepository(idCliente: String)
    fun adicionarDatasCobrancaCliente(idCliente: String, datasVendasCliente: DataCobrancaVenda)
    fun buscaDatasClienteFiltrandoRepository(idCliente: String, dataDigitadaFiltro: String)
    fun buscaDatasVendasClienteMutableLiveData(): MutableLiveData<Query>
    fun buscaDatasClienteFiltrandoRepositoryMutableLiveData(): MutableLiveData<Query>
    fun excluirDataVendasCliente(documentReference : DocumentReference)
    fun excluirDataVendasClienteMutableLiveData() :MutableLiveData<Boolean>
}