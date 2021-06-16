package br.com.medeve.Repository

import androidx.lifecycle.MutableLiveData
import br.com.medeve.Interfaces.IClienteRepository
import br.com.medeve.Models.Cliente
import br.com.medeve.Util.Constantes
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import java.util.*

class ClienteRepository : IClienteRepository {

    val firebaseSaveCliente: MutableLiveData<Int> = MutableLiveData()
    val firabseQueryBuscaCliente: MutableLiveData<Query> = MutableLiveData()
    val firebaseQueryBuscaClienteWithSearchView : MutableLiveData<Query> = MutableLiveData()
    val deleteCliente : MutableLiveData<Boolean> = MutableLiveData()

    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var firestoreCadastroClienteCollection =
        FirebaseFirestore.getInstance().collection("cadastro_clientes")

    var db_clientes = FirebaseFirestore.getInstance()

    var cl_clientes = db_clientes.collection("cadastro_clientes")
        .document(firebaseAuth.uid!!)
        .collection("cliente")

    var cl_datas: CollectionReference = db_clientes.collection("datas_cobranca")
        .document(firebaseAuth.uid!!)
        .collection("data_de_cobraca")

    var query: Query? = null

    override fun salvarCliente(cliente: Cliente) {
        val map: MutableMap<String, Any> = HashMap()

        map["nome"] = cliente.nome
        map["nome_maiusculo"] = cliente.nome.toUpperCase()
        map["logradouro"] = cliente.logradouro
        map["numero"] = cliente.numero
        map["bairro"] = cliente.bairro
        map["cidade"] = cliente.cidade
        map["cep"] = cliente.cep
        map["estado"] = cliente.estado
        map["telefone"] = cliente.telefone

        firestoreCadastroClienteCollection.document(firebaseAuth.uid!!).collection("cliente")
            .add(map).addOnSuccessListener { documentReferent ->
                firebaseSaveCliente.postValue(Constantes.ClienteRepository.CLIENTE_SALVO_SUCESSO)
            }.addOnFailureListener { failure ->
                firebaseSaveCliente.postValue(Constantes.ClienteRepository.FALHA_AO_SALVAR_CLIENTE)
            }
    }

    fun queryFireStoreBuscaCliente() {
        query = cl_clientes.orderBy("nome", Query.Direction.ASCENDING)
        firabseQueryBuscaCliente.postValue(query)
    }

    fun queryFireStoreSearchCliente(nomeCliente: String) {
        query = cl_clientes.orderBy("nome_maiusculo").startAt(nomeCliente.toUpperCase())
            .endAt(nomeCliente.toUpperCase() + "\uf8ff");
        firebaseQueryBuscaClienteWithSearchView.postValue(query)
    }

    fun deleteCliente(documentReference : DocumentReference){
        cl_datas.whereEqualTo("id_data", documentReference.id)
            .get()
            .addOnCompleteListener { task: Task<QuerySnapshot?> ->
                val queryDocumentSnapshot = task.result
                if (!queryDocumentSnapshot!!.isEmpty) {
                    deleteCliente.postValue(false)
                } else {
                    documentReference.delete()
                    deleteCliente.postValue(true)
                }
            }
    }

    fun saveClientMutableLiveData(): MutableLiveData<Int> {
        return firebaseSaveCliente
    }

    fun buscaClienteFireStoreWithOutSearch(): MutableLiveData<Query> {
        return firabseQueryBuscaCliente
    }

    fun buscaClienteFireStoreWithSearch(): MutableLiveData<Query> {
        return firebaseQueryBuscaClienteWithSearchView
    }

    fun deleteUsuarioMutableLiveData() : MutableLiveData<Boolean> {
        return deleteCliente
    }

}