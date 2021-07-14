package br.com.medeve.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.medeve.Models.Produto
import br.com.medeve.Repository.ProdutoRepository
import com.google.firebase.firestore.Query
import kotlinx.coroutines.runBlocking

class ProdutosViewModel(val produtoRepository: ProdutoRepository) : ViewModel() {

    fun salvarProdutos(produto: Produto, id: String) {
        produtoRepository.salvarProdutos(produto, id)
    }

    fun totalDevido(id: String) {
        produtoRepository.totalDevido(id)
    }

    fun buscarProdutosCliente(id: String) {
        produtoRepository.buscarProdutoscliente(id)
    }

    fun buscaProdutosClienteRetorno(): MutableLiveData<Query> {
        return produtoRepository.buscarProdutosclienteReturn()
    }

    fun salvarProdutosRetorno() : MutableLiveData<Boolean> {
        return produtoRepository.salvarProdutosRetorno()
    }
}