package br.com.medeve.Controlers;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import br.com.medeve.R;
import br.com.medeve.Adapters.AdapterCliente;
import br.com.medeve.Helpers.AccessFirebase;
import br.com.medeve.Models.Cliente;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdRequest;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.iid.FirebaseInstanceId;

@SuppressLint("Registered")
public class CadastroCliente extends AppCompatActivity {

    private static final int TIME_INTERVAL = 3000; //Intervalo de tempo para click no botão de voltar para sair do aplicativo
    private long mBackPressed;

    FloatingActionButton fab_cad_cliente;
    RecyclerView rc_produto;
    GridLayoutManager layout_manager_cliente;
    FirestoreRecyclerOptions firt_cad_clientes;
    AdView adView;

    Query query;

    FirebaseAuth db_users = FirebaseAuth.getInstance();

    FirebaseFirestore db_clientes = FirebaseFirestore.getInstance();
    CollectionReference cl_clientes = db_clientes.collection("cadastro_clientes")
            .document(db_users.getUid())
            .collection("cliente");

    AdapterCliente adapter_cliente;

    Cliente cliente = new Cliente();
    AccessFirebase accessFirebase = new AccessFirebase();

    SearchView searchView;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rc_produto = findViewById(R.id.rc_cad_clientes);
        fab_cad_cliente = findViewById(R.id.fab_cad_clientes);
        adView = findViewById(R.id.adView);

        FirebaseInstanceId.getInstance();

        FirebaseApp.initializeApp(CadastroCliente.this);

        setTitle("Dados dos clientes");

        MobileAds.initialize(CadastroCliente.this, "ca-app-pub-2528240545678093~1740905001");

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        adView.loadAd(adRequest);

        ler_dados_clientes();

        fab_cad_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                accessFirebase.cadastro_cliente(view, cliente, CadastroCliente.this);

            }
        });
    }

    @SuppressLint("WrongConstant")
    public void ler_dados_clientes() {

        query = cl_clientes.orderBy("nome", Query.Direction.ASCENDING);

        firt_cad_clientes = new FirestoreRecyclerOptions.Builder<Cliente>()
                .setQuery(query, Cliente.class)
                .build();

        adapter_cliente = new AdapterCliente(firt_cad_clientes, CadastroCliente.this);
        layout_manager_cliente = new GridLayoutManager(CadastroCliente.this, 1);

        rc_produto.setLayoutManager(layout_manager_cliente);
        rc_produto.setHasFixedSize(true);
        rc_produto.setAdapter(adapter_cliente);

        adapter_cliente.setOnItemClicklistener(new AdapterCliente.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Cliente cliente_snap = documentSnapshot.toObject(Cliente.class);
                String id_cliente = documentSnapshot.getId();

                Intent i_cliente = new Intent(getApplicationContext(), DatasVendasCobranca.class);
                i_cliente.putExtra("info_cliente", cliente_snap);
                i_cliente.putExtra("id_cliente", id_cliente);
                startActivity(i_cliente);

            }
        });
    }

    @SuppressLint("WrongConstant")
    public void seachview(String search) {

        query = cl_clientes.orderBy("nome_maiusculo").startAt(search.toUpperCase()).endAt(search.toUpperCase() + "\uf8ff");

        firt_cad_clientes = new FirestoreRecyclerOptions.Builder<Cliente>()
                .setQuery(query, Cliente.class)
                .build();

        adapter_cliente = new AdapterCliente(firt_cad_clientes, CadastroCliente.this);
        layout_manager_cliente = new GridLayoutManager(CadastroCliente.this, 1);

        rc_produto.setHasFixedSize(true);
        rc_produto.setAdapter(adapter_cliente);

        adapter_cliente.setOnItemClicklistener(new AdapterCliente.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Cliente cliente_snap = documentSnapshot.toObject(Cliente.class);
                String id_cliente = documentSnapshot.getId();

                Intent i_cliente = new Intent(CadastroCliente.this, DatasVendasCobranca.class);
                i_cliente.putExtra("info_cliente", cliente_snap);
                i_cliente.putExtra("id_cliente", id_cliente);
                startActivity(i_cliente);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter_cliente.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter_cliente.stopListening();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchitem = menu.findItem(R.id.search);

        searchView = (SearchView) MenuItemCompat.getActionView(searchitem);

        searchView.setQueryHint("Digite o nome do cliente");
        searchView.setIconified(true);
        searchView.setFocusable(true);

        searchitem.expandActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                if (!s.trim().isEmpty()) {
                    seachview(s);
                    adapter_cliente.startListening();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

               if (s.trim().isEmpty()) {
                    seachview(s);
                    adapter_cliente.startListening();
                }
                return false;
            }
        });
        return true;
    }


    @Override
    public void onBackPressed() {

        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();


            return;
        } else {

            Toast.makeText(CadastroCliente.this, "Toque novamente para sair", Toast.LENGTH_SHORT).show();

            mBackPressed = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {

            AlertDialog.Builder alert_exit = new AlertDialog.Builder(CadastroCliente.this);
            alert_exit.setMessage("Você deseja realmente sair ?");

            alert_exit.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    new AccessFirebase().sign_out_firebase(CadastroCliente.this);

                }
            }).setNegativeButton("Cancelar", null);

            alert_exit.show();

            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}