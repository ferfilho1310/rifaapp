package com.example.apprifa.Controlers;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprifa.Adapters.Adapter_cliente;
import com.example.apprifa.Helpers.AccessFirebase;
import com.example.apprifa.Models.Cliente;
import com.example.apprifa.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

@SuppressLint("Registered")
public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab_cad_cliente;
    RecyclerView rc_produto;
    SearchView searchView;

    FirestoreRecyclerOptions firt_cad_clientes;

    Query query;

    FirebaseAuth db_users = FirebaseAuth.getInstance();

    FirebaseFirestore db_clientes = FirebaseFirestore.getInstance();
    CollectionReference cl_clientes = db_clientes.collection("cadastro de clientes")
            .document(db_users.getUid())
            .collection("cliente");

    Adapter_cliente adapter_cliente;
    List<Cliente> ls_cl;

    Cliente cliente = new Cliente();

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rc_produto = findViewById(R.id.rc_cad_clientes);
        fab_cad_cliente = findViewById(R.id.fab_cad_clientes);

        setTitle("Clientes Cadastrados");

        ler_dados_clientes();

        fab_cad_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder cliente = new AlertDialog.Builder(MainActivity.this);
                final View custom_layout = getLayoutInflater().inflate(R.layout.dialog_cad_clientes, null);
                cliente.setView(custom_layout);

                cliente.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        salva_dados_cliente(custom_layout);

                    }
                }).setNegativeButton("Cancelar", null);

                cliente.show();
            }
        });

    }

    public void salva_dados_cliente(View view) {

        EditText ed_nome = view.findViewById(R.id.ed_nome);
        EditText ed_endereco = view.findViewById(R.id.ed_endereco);
        EditText ed_numero = view.findViewById(R.id.ed_numero);
        EditText ed_bairro = view.findViewById(R.id.ed_bairro);
        EditText ed_cidade = view.findViewById(R.id.ed_cidade);

        cliente.setNome(ed_nome.getText().toString());
        cliente.setEndereco(ed_endereco.getText().toString());
        cliente.setNumero(ed_numero.getText().toString());
        cliente.setBairro(ed_bairro.getText().toString());
        cliente.setCidade(ed_cidade.getText().toString());

        ls_cl.add(cliente);

        new AccessFirebase().salva_clientes(cliente.getNome(), cliente.getEndereco(), cliente.getNumero()
                , cliente.getBairro(), cliente.getCidade());

    }

    @SuppressLint("WrongConstant")
    public void ler_dados_clientes() {

        query = cl_clientes.orderBy("nome", Query.Direction.ASCENDING);

        firt_cad_clientes = new FirestoreRecyclerOptions.Builder<Cliente>()
                .setQuery(query, Cliente.class)
                .build();

        adapter_cliente = new Adapter_cliente(firt_cad_clientes);
        rc_produto.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        rc_produto.setHasFixedSize(true);
        rc_produto.setAdapter(adapter_cliente);

        adapter_cliente.setOnItemClicklistener(new Adapter_cliente.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Cliente cliente = documentSnapshot.toObject(Cliente.class);

                Intent i_cliente = new Intent(getApplicationContext(), ProdutosCliente.class);
                i_cliente.putExtra("info_cliente", cliente);
                startActivity(i_cliente);

            }
        });

    }

    @SuppressLint("WrongConstant")
    public void seachview(String search) {

        query = cl_clientes.whereEqualTo("nome",search).orderBy("nome",Query.Direction.ASCENDING).startAt(search).endAt(search + "\uf8ff");

        Log.d("Retorno Search", search);

        adapter_cliente = new Adapter_cliente(firt_cad_clientes,ls_cl);
        rc_produto.setAdapter(adapter_cliente);

        adapter_cliente.setOnItemClicklistener(new Adapter_cliente.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Cliente cliente = documentSnapshot.toObject(Cliente.class);

                Intent i_cliente = new Intent(getApplicationContext(), ProdutosCliente.class);
                i_cliente.putExtra("info_cliente", cliente);
                startActivity(i_cliente);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchitem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchitem);

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setQueryHint("Pesquisar");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                seachview(s);
                adapter_cliente.startListening();

                Log.d("Retorno", s);

                return false;
            }
        });
        return true;
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
    public void onBackPressed() {
        if (searchView.isIconified()) {
            searchView.setIconified(true);
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            new AccessFirebase().sign_out_firebase(MainActivity.this);
            return true;
        } else if (id == R.id.search){

        }

        return super.onOptionsItemSelected(item);
    }


}
