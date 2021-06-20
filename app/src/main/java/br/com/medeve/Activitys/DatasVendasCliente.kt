package br.com.medeve.Activitys

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.medeve.Adapters.AdapterDataCobranca
import br.com.medeve.Fragment.CadastrarDataVendaClienteFragment
import br.com.medeve.Models.Cliente
import br.com.medeve.Models.DataCobrancaVenda
import br.com.medeve.R
import br.com.medeve.ViewModels.DataVendasClienteViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_datas_vendas_cliente.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DatasVendasCliente : AppCompatActivity(), View.OnClickListener {

    val dataVendasClienteViewModel: DataVendasClienteViewModel by viewModel()

    var rcOptionsDatasVendaCobranca: FirestoreRecyclerOptions<DataCobrancaVenda>? = null

    var cliente: Cliente? = null

    var adapter_datas: AdapterDataCobranca? = null

    var idClient: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datas_vendas_cliente)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        title = "Datas de venda e cobrança"

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)

        cliente = intent?.extras?.getParcelable("info_cliente")
        idClient = intent?.extras?.getString("id_cliente")

        txt_nome_cliente_extra.text = cliente!!.nome
        txt_telefone_extra.text = cliente!!.telefone

        MobileAds.initialize(this, "ca-app-pub-2528240545678093~1740905001")

        val adRequest = AdRequest.Builder()
            .addTestDevice("B6D5B7288C97DD6A90A5F0E267BADDA5")
            .build()

        adView_datas.loadAd(adRequest)

        buscaDatasCliente(idClient!!)

        fab_data_venda.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_data_venda -> {
                val fragmentCadastroDatasCobrancaVenda = CadastrarDataVendaClienteFragment()
                fragmentCadastroDatasCobrancaVenda.show(
                    supportFragmentManager.beginTransaction(),
                    "Data de vendas e cobranca"
                )
            }
        }
    }

    fun buscaDatasCliente(idClient: String) {
        dataVendasClienteViewModel.buscaDatasCliente(idClient)

        dataVendasClienteViewModel.buscaDatasVendaCobranca().observe(this, {

            rcOptionsDatasVendaCobranca = FirestoreRecyclerOptions.Builder<DataCobrancaVenda>()
                .setQuery(it, DataCobrancaVenda::class.java)
                .build()

            adapter_datas = AdapterDataCobranca(rcOptionsDatasVendaCobranca!!, this)
            rc_data_cobranca.adapter = adapter_datas
            rc_data_cobranca.layoutManager = GridLayoutManager(
                this, 2
            )
            rc_data_cobranca.setHasFixedSize(true)

            adapter_datas!!.setOnItemClicklistener { documentSnapshot, position ->

                val id_data = documentSnapshot.id
                val i_data = Intent(applicationContext, ProdutosCliente::class.java)
                i_data.putExtra("id_data_compra", id_data)
                i_data.putExtra("dados_cliente", cliente)
                startActivity(i_data)
            }
            adapter_datas?.startListening()
        })
    }

    override fun onStart() {
        super.onStart()
        adapter_datas?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter_datas?.stopListening()
    }

}