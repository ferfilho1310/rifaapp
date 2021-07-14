package br.com.medeve.Repository

import androidx.lifecycle.MutableLiveData
import br.com.medeve.Interfaces.IDataVendasClienteRepository
import br.com.medeve.Models.DataCobrancaVenda
import br.com.medeve.Util.Constantes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

class DatasVendasClienteRepository : IDataVendasClienteRepository {

    val buscaDatasVendasClienteRepository: MutableLiveData<Query> =
        MutableLiveData()
    val buscaDatasClienteFiltrandoRepository: MutableLiveData<Query> =
        MutableLiveData()
    val deleteDatasVendasCliente : MutableLiveData<Boolean> =
        MutableLiveData()
    val saveData : MutableLiveData<Int> =
        MutableLiveData()

    var firebaseAuthDatasVendasCliente = FirebaseAuth.getInstance()
    var salvarDatasVendasCliente = FirebaseFirestore.getInstance().collection("datas_cobranca")

    var collectionDatasVendasCliente = FirebaseFirestore.getInstance().collection("datas_cobranca")
        .document(firebaseAuthDatasVendasCliente.uid!!)
        .collection("data_de_cobraca")

    var cl_clientes: CollectionReference = FirebaseFirestore.getInstance().collection("produtos_cliente")
        .document(firebaseAuthDatasVendasCliente.uid!!)
        .collection("produtos")

    var query: Query? = null

    override fun buscarDatasClienteRepository(idCliente: String) {
        query = collectionDatasVendasCliente.whereEqualTo("id_data", idCliente)
            .orderBy("data_venda", Query.Direction.DESCENDING)
        buscaDatasVendasClienteRepository.postValue(query)
    }

    override fun adicionarDatasCobrancaCliente(
        idCliente: String,
        datasVendasCliente: DataCobrancaVenda
    ) {
        val map: MutableMap<String, Any> = HashMap()

        map["id_data"] = idCliente
        map["data_venda"] = datasVendasCliente.data_venda
        map["data_cobranca"] = datasVendasCliente.data_cobranca

        salvarDatasVendasCliente.document(firebaseAuthDatasVendasCliente.uid!!)
            .collection("data_de_cobraca")
            .add(map).addOnSuccessListener { documentReferent ->
                saveData.postValue(Constantes.ClienteRepository.CLIENTE_SALVO_SUCESSO)
            }.addOnFailureListener { failure ->
                saveData.postValue(Constantes.ClienteRepository.FALHA_AO_SALVAR_CLIENTE)
            }
    }

    override fun buscaDatasClienteFiltrandoRepository(
        idCliente: String,
        dataDigitadaFiltro: String
    ) {
        query = collectionDatasVendasCliente.whereEqualTo("id_data", idCliente)
            .orderBy("data_venda").startAt(dataDigitadaFiltro).endAt(dataDigitadaFiltro + "\uf8ff")
        buscaDatasClienteFiltrandoRepository.postValue(query)
    }

    override fun excluirDataVendasCliente(documentReference : DocumentReference){
        cl_clientes
            .whereEqualTo("id", documentReference.id)
            .get()
            .addOnCompleteListener { task ->
                val queryDocumentSnapshot = task.result
                if (!queryDocumentSnapshot!!.isEmpty) {
                    deleteDatasVendasCliente.postValue(false)
                } else {
                    deleteDatasVendasCliente.postValue(true)
                    documentReference.delete()
                }
            }
    }

    override fun excluirDataVendasClienteMutableLiveData(): MutableLiveData<Boolean> {
        return deleteDatasVendasCliente
    }

    override fun saveData(): MutableLiveData<Int> {
        return saveData
    }

    override fun buscaDatasVendasClienteMutableLiveData() : MutableLiveData<Query>  {
        return buscaDatasVendasClienteRepository
    }

    override fun buscaDatasClienteFiltrandoRepositoryMutableLiveData(): MutableLiveData<Query> {
        return buscaDatasClienteFiltrandoRepository
    }




}