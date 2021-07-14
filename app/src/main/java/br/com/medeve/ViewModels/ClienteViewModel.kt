package br.com.medeve.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.medeve.Models.Cliente
import br.com.medeve.Repository.ClienteRepository
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.runBlocking

class ClienteViewModel(val clienteRepository: ClienteRepository) : ViewModel() {

    init {
        runBlocking {
            buscarClienteRepository()
        }
    }

    fun buscarClienteRepository() {
        clienteRepository.queryFireStoreBuscaCliente()
    }

    fun saveCliente(cliente: Cliente) {
        clienteRepository.salvarCliente(cliente)
    }

    fun buscaClienteWithSearchView(nomeCliente: String) {
        clienteRepository.queryFireStoreSearchCliente(nomeCliente)
    }

    fun deleteUsuario(documentReference: DocumentReference) {
        clienteRepository.deleteCliente(documentReference)
    }

    fun saveCliente(): MutableLiveData<Int> {
        return clienteRepository.saveClientMutableLiveData()
    }

    fun buscaClienteSemSearChView(): MutableLiveData<Query> {
        return clienteRepository.buscaClienteFireStoreWithSearch()
    }

    fun buscaClienteComSearchView(): MutableLiveData<Query> {
        return clienteRepository.buscaClienteFireStoreWithOutSearch()
    }

    fun deleteClienteMutableLiveData() : MutableLiveData<Boolean> {
        return clienteRepository.deleteUsuarioMutableLiveData()
    }

}