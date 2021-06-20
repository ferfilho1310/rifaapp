package br.com.medeve.Repository

import androidx.lifecycle.MutableLiveData
import br.com.medeve.Interfaces.IDataVendasClienteRepository
import br.com.medeve.Models.DataCobrancaVenda
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

class DatasVendasClienteRepository : IDataVendasClienteRepository {

    val buscaDatasVendasClienteRepository: MutableLiveData<Query> =
        MutableLiveData()
    val buscaDatasClienteFiltrandoRepository: MutableLiveData<Query> =
        MutableLiveData()

    var firebaseAuthDatasVendasCliente = FirebaseAuth.getInstance()
    var db_datas_cobranca = FirebaseFirestore.getInstance().collection("datas_cobranca")

    var dbDatasVendasClienteInstace = FirebaseFirestore.getInstance()
    var collectionDatasVendasCliente = dbDatasVendasClienteInstace.collection("datas_cobranca")
        .document(firebaseAuthDatasVendasCliente.uid!!)
        .collection("data_de_cobraca")

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

        db_datas_cobranca.document(firebaseAuthDatasVendasCliente.uid!!)
            .collection("data_de_cobraca")
            .add(map)
    }

    override fun buscaDatasClienteFiltrandoRepository(
        idCliente: String,
        dataDigitadaFiltro: String
    ) {
        query = collectionDatasVendasCliente.whereEqualTo("id_data", idCliente)
            .orderBy("data_venda").startAt(dataDigitadaFiltro).endAt(dataDigitadaFiltro + "\uf8ff")
        buscaDatasClienteFiltrandoRepository.postValue(query)
    }

    override fun buscaDatasVendasClienteMutableLiveData() : MutableLiveData<Query>  {
        return buscaDatasVendasClienteRepository
    }

    override fun buscaDatasClienteFiltrandoRepositoryMutableLiveData(): MutableLiveData<Query> {
        return buscaDatasClienteFiltrandoRepository
    }


}