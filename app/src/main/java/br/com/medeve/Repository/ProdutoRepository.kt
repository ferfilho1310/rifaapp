package br.com.medeve.Repository

import androidx.lifecycle.MutableLiveData
import br.com.medeve.Interfaces.IProdutosRepository
import br.com.medeve.Models.Produto
import br.com.medeve.Util.Constantes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import java.util.*

class ProdutoRepository : IProdutosRepository {

    val totaldevido: MutableLiveData<QuerySnapshot> = MutableLiveData()
    val buscarProdutosCliente: MutableLiveData<Query> = MutableLiveData()
    val firebaseSaveProduto: MutableLiveData<Boolean> = MutableLiveData()

    var firebaseAuth = FirebaseAuth.getInstance()

    var produtosCliente: CollectionReference =
        FirebaseFirestore.getInstance().collection("produtos_cliente")
            .document(firebaseAuth.uid!!)
            .collection("produtos")

    var query: Query? = null

    override fun salvarProdutos(produto: Produto, id: String) {

        val map: MutableMap<String, Any> = HashMap()

        map["id"] = id
        map["nomedoproduto"] = produto.nomedoproduto
        map["quantidade"] = produto.quantidade
        map["valor"] = produto.valor
        map["total"] = produto.total
        map["data"] = produto.data
        map["recebido"] = produto.recebido
        map["devolvido"] = produto.devolvido

        produtosCliente.add(map).addOnSuccessListener { documentReferent ->
            firebaseSaveProduto.postValue(true)
        }.addOnFailureListener { failure ->
            firebaseSaveProduto.postValue(false)
        }
    }

    override fun totalDevido(idUser: String) {
        produtosCliente.whereEqualTo("id", idUser)
            .get()
            .addOnCompleteListener { task ->
                val queryDocumentSnapshots = task.result
                totaldevido.postValue(queryDocumentSnapshots)
            }
    }

    override fun buscarProdutoscliente(id: String) {
        query = produtosCliente
            .whereEqualTo("id", id)
            .orderBy("nomedoproduto", Query.Direction.DESCENDING)
        buscarProdutosCliente.postValue(query)
    }

    override fun buscarProdutosclienteReturn(): MutableLiveData<Query> {
        return buscarProdutosCliente
    }

    override fun salvarProdutosRetorno(): MutableLiveData<Boolean> {
        return firebaseSaveProduto
    }

    override fun totalDevidoReturn(): MutableLiveData<QuerySnapshot> {
        return totaldevido
    }


}