package br.com.medeve.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.medeve.Models.DataCobrancaVenda
import br.com.medeve.Repository.DatasVendasClienteRepository
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.runBlocking

class DataVendasClienteViewModel(val datasVendasClienteRepository: DatasVendasClienteRepository) :
    ViewModel() {

    init {
        buscaDatasVendaCobranca()
    }

    fun adicionarDatasVendaCliente(
        idCliente: String?,
        dataDigitadaFiltro: DataCobrancaVenda
    ) {
        if (idCliente != null) {
            datasVendasClienteRepository.adicionarDatasCobrancaCliente(
                idCliente,
                dataDigitadaFiltro
            )
        }
    }

    fun deleteDataVendaCliente(existeProdutoRelacionadoDataVenda: DocumentReference){
        datasVendasClienteRepository.excluirDataVendasCliente(existeProdutoRelacionadoDataVenda)
    }

    fun buscaDatasCliente(idCliente: String){
        datasVendasClienteRepository.buscarDatasClienteRepository(idCliente)
    }

    fun buscaDatasVendaCobranca() : MutableLiveData<Query> {
        return datasVendasClienteRepository.buscaDatasVendasClienteMutableLiveData()
    }

    fun deleteDataVendaClienteMutableLiveData() : MutableLiveData<Boolean>{
        return datasVendasClienteRepository.excluirDataVendasClienteMutableLiveData()
    }

}