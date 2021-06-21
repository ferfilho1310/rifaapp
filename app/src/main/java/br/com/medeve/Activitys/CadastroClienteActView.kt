package br.com.medeve.Activitys

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.medeve.Adapters.AdapterCliente
import br.com.medeve.Fragment.CadastrarDadoClienteFragment
import br.com.medeve.Helpers.IntentHelper
import br.com.medeve.Models.Cliente
import br.com.medeve.R
import br.com.medeve.ViewModels.CepViewModel
import br.com.medeve.ViewModels.ClienteViewModel
import br.com.medeve.ViewModels.UsuarioViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CadastroClienteActView : AppCompatActivity(), View.OnClickListener {

    private val TIME_INTERVAL = 3000

    private var mBackPressed: Long = 0

    val clienteViewModel: ClienteViewModel by viewModel()
    val cepViewModel: CepViewModel by viewModel()

    var adapter_cliente: AdapterCliente? = null
    var layoutManagerCadastroCliente: LinearLayoutManager? = null
    var firestoreRecyclerOptions: FirestoreRecyclerOptions<Cliente>? = null

    var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        title = "Dados dos clientes"

        loadAds()
        buscarClientes()

        fab_cad_clientes.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_cad_clientes -> {
                val cadastrarDadoClienteFragment = CadastrarDadoClienteFragment()
                cadastrarDadoClienteFragment.show(supportFragmentManager.beginTransaction(), "Tag")
            }
        }
    }

    fun loadAds() {
        MobileAds.initialize(
            this@CadastroClienteActView,
            "ca-app-pub-2528240545678093~1740905001"
        )

        val adRequest = AdRequest.Builder()
            .addTestDevice("B6D5B7288C97DD6A90A5F0E267BADDA5")
            .build()

        adView.loadAd(adRequest)
    }

    fun buscarClientes() {

        clienteViewModel.buscaClienteSemSearChView().observe(this, {
            firestoreRecyclerOptions = FirestoreRecyclerOptions.Builder<Cliente>()
                .setQuery(it, Cliente::class.java)
                .build()

            adapter_cliente =
                AdapterCliente(firestoreRecyclerOptions!!, this, cepViewModel, clienteViewModel)
            layoutManagerCadastroCliente =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            rc_cad_clientes.apply {
                layoutManager = layoutManagerCadastroCliente
                setHasFixedSize(true)
                adapter = adapter_cliente
            }

            adapter_cliente!!.setOnItemClicklistener { documentSnapshot, position ->

                val snapShotCliente = documentSnapshot.toObject(Cliente::class.java)
                val idCliente = documentSnapshot.id
                val iCliente = Intent(applicationContext, DatasVendasCliente::class.java)
                iCliente.putExtra("info_cliente", snapShotCliente)
                iCliente.putExtra("id_cliente", idCliente)
                startActivity(iCliente)

            }

            adapter_cliente?.startListening()
        })
    }

    fun buscaClienteSearchView(nomeCliente: String) {

        clienteViewModel.buscaClienteWithSearchView(nomeCliente)

        clienteViewModel.buscaClienteComSearchView().observe(this, {
            firestoreRecyclerOptions = FirestoreRecyclerOptions.Builder<Cliente>()
                .setQuery(it, Cliente::class.java)
                .build()

            adapter_cliente =
                AdapterCliente(firestoreRecyclerOptions!!, this, cepViewModel, clienteViewModel)
            layoutManagerCadastroCliente =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            rc_cad_clientes.apply {
                layoutManager = layoutManagerCadastroCliente
                adapter = adapter_cliente
            }

            adapter_cliente!!.setOnItemClicklistener { documentSnapshot, position ->
                val snapShotCliente = documentSnapshot.toObject(Cliente::class.java)
                val idCliente = documentSnapshot.id
                val iCliente = Intent(applicationContext, DatasVendasCliente::class.java)
                iCliente.putExtra("info_cliente", snapShotCliente)
                iCliente.putExtra("id_cliente", idCliente)
                startActivity(iCliente)
            }
            adapter_cliente?.startListening()
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchitem = menu!!.findItem(R.id.search)
        searchView = MenuItemCompat.getActionView(searchitem) as SearchView

        searchitem.expandActionView()

        searchView?.apply {
            queryHint = "Digite o nome do cliente"
            isIconified = true
            isFocusable = true

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(s: String): Boolean {

                    return false
                }

                override fun onQueryTextChange(s: String): Boolean {
                    if (s.trim().isEmpty()) {
                        buscaClienteSearchView(s)
                        adapter_cliente?.startListening()
                    } else {
                        buscaClienteSearchView(s)
                        adapter_cliente?.startListening()
                    }
                    return false
                }
            })
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_settings) {
            val alertExit = AlertDialog.Builder(this)
            alertExit.setMessage("Você deseja realmente sair ?")
            alertExit.setPositiveButton(
                "Ok"
            ) { _, i ->
                FirebaseAuth.getInstance().signOut()
                IntentHelper.intentWithFinish(this, EntrarUsuarioActView::class.java)
            }.setNegativeButton("Cancelar", null)
            alertExit.show()
            return true
        }
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed()
            return
        } else {
            Toast.makeText(
                this,
                "Toque novamente para sair",
                Toast.LENGTH_SHORT
            ).show()
            mBackPressed = System.currentTimeMillis()
        }
    }

    override fun onStart() {
        super.onStart()
        adapter_cliente?.startListening()
        clienteViewModel.buscarClienteRepository()
        clienteViewModel.buscaClienteWithSearchView("")
    }

    override fun onStop() {
        super.onStop()
        adapter_cliente?.stopListening()
    }

}