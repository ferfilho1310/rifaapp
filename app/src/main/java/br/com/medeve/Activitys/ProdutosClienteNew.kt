package br.com.medeve.Activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.medeve.Adapters.AdapterProdutosCliente
import br.com.medeve.Fragment.CadastroProdutoFragment
import br.com.medeve.Models.Cliente
import br.com.medeve.Models.Produto
import br.com.medeve.R
import br.com.medeve.ViewModels.ProdutosViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_datas_vendas_cliente.*
import kotlinx.android.synthetic.main.activity_produtos_cliente_new.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProdutosClienteNew : AppCompatActivity(), View.OnClickListener {

    val produtosViewModel: ProdutosViewModel by viewModel()

    var firt_cad_clientes: FirestoreRecyclerOptions<Produto>? = null
    var adapter_produtos_cliente: AdapterProdutosCliente? = null

    var id_data: String? = null
    var cliente: Cliente? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produtos_cliente_new)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        title = "Produtos do Cliente"

        val receber = intent
        val data = receber.extras

        if (data != null) {
            id_data = data.getString("id_data_compra")
        }

        try {
            cliente = intent.extras!!.getParcelable("dados_cliente")
            txt_nome_cliente_extra.text = cliente?.nome
            txt_telefone_extra_produto.text = cliente?.telefone
        } catch (e: Exception) {
            Log.i("TAG", "Erro a obter nome e telefone")
        }

        loadAds()
        listeners()
        setObservers()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_produto_cliente -> {
                val cadastroProdutoFragment = CadastroProdutoFragment()
                cadastroProdutoFragment.show(supportFragmentManager.beginTransaction(), "Produtos Cliente")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        adapter_produtos_cliente?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter_produtos_cliente?.stopListening()
    }

    fun listeners() {
        fab_produto_cliente.setOnClickListener(this)
    }

    fun setObservers() {
        produtosViewModel.buscarProdutosCliente(id_data!!)

        produtosViewModel.buscaProdutosClienteRetorno().observe(this, {
            firt_cad_clientes = FirestoreRecyclerOptions.Builder<Produto>()
                .setQuery(it, Produto::class.java)
                .build()

            adapter_produtos_cliente =
                AdapterProdutosCliente(firt_cad_clientes!!, this@ProdutosClienteNew)
            rc_produto_cliente.layoutManager = LinearLayoutManager(
                this@ProdutosClienteNew,
                LinearLayoutManager.VERTICAL,
                false
            )

            rc_produto_cliente.setHasFixedSize(true)
            rc_produto_cliente.adapter = adapter_produtos_cliente
            adapter_produtos_cliente?.setOnItemClicklistener { documentSnapshot, _ ->

                val produto = documentSnapshot.toObject(Produto::class.java)
                val i_recebido_parcial = Intent(applicationContext, RecebidoParcial::class.java)
                i_recebido_parcial.putExtra("id_recebido_parcial", documentSnapshot.id)
                i_recebido_parcial.putExtra("info_produto", produto)
                startActivity(i_recebido_parcial)
            }
            adapter_produtos_cliente?.startListening()
        })
    }

    fun loadAds() {
        MobileAds.initialize(this@ProdutosClienteNew, "ca-app-pub-2528240545678093~1740905001")
        val adRequest = AdRequest.Builder().build()
        adView_produtos.loadAd(adRequest)
    }
}