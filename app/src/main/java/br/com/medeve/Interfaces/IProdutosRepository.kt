package br.com.medeve.Interfaces

import androidx.lifecycle.MutableLiveData
import br.com.medeve.Models.Produto
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

interface IProdutosRepository {

    fun salvarProdutos(produto: Produto, id: String)
    fun totalDevido(idUser: String)
    fun totalDevidoReturn(): MutableLiveData<QuerySnapshot>
    fun buscarProdutoscliente(id: String)
    fun buscarProdutosclienteReturn(): MutableLiveData<Query>
    fun salvarProdutosRetorno() : MutableLiveData<Boolean>
}