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
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprifa.Adapters.Adapter_cliente;
import com.example.apprifa.Helpers.AccessFirebase;
import com.example.apprifa.Models.Cliente;
import com.example.apprifa.R;
import com.example.apprifa.Retrofit.RetrofitInit;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("Registered")
public class MainActivity extends AppCompatActivity {

    private static final int TIME_INTERVAL = 3000;//# milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    EditText ed_nome, ed_endereco, ed_numero, ed_bairro, ed_cidade, ed_estado, ed_cep;

    FloatingActionButton fab_cad_cliente;
    RecyclerView rc_produto;
    GridLayoutManager layout_manager_cliente;
    FirestoreRecyclerOptions firt_cad_clientes;

    Query query;

    FirebaseAuth db_users = FirebaseAuth.getInstance();

    FirebaseFirestore db_clientes = FirebaseFirestore.getInstance();
    CollectionReference cl_clientes = db_clientes.collection("cadastro_clientes")
            .document(db_users.getUid())
            .collection("cliente");

    Adapter_cliente adapter_cliente;

    Cliente cliente = new Cliente();
    AccessFirebase accessFirebase = new AccessFirebase();
    List<Cliente> ls_search_cliente;

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

                final AlertDialog.Builder cliente_dialog = new AlertDialog.Builder(MainActivity.this);
                final View custom_layout = getLayoutInflater().inflate(R.layout.dialog_cad_clientes, null);
                cliente_dialog.setView(custom_layout);

                ed_nome = custom_layout.findViewById(R.id.ed_nome);
                ed_endereco = custom_layout.findViewById(R.id.edend);
                ed_numero = custom_layout.findViewById(R.id.ed_numero);
                ed_bairro = custom_layout.findViewById(R.id.ed_bairro);
                ed_cidade = custom_layout.findViewById(R.id.ed_cidade);
                ed_estado = custom_layout.findViewById(R.id.ed_estado);
                ed_cep = custom_layout.findViewById(R.id.ed_cep);

                Button btn_cep = custom_layout.findViewById(R.id.btn_busca_cep);

                btn_cep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (ed_cep.length() < 8 || ed_cep.length() > 8) {
                            Toast.makeText(getApplicationContext(), "CEP inválido", Toast.LENGTH_LONG).show();
                            return;
                        }

                        Call<Cliente> call_cep = new RetrofitInit().getcep().cep(ed_cep.getText().toString());

                        call_cep.enqueue(new Callback<Cliente>() {
                            @Override
                            public void onResponse(Call<Cliente> call, Response<Cliente> response) {

                                if (response.isSuccessful()) {

                                    Cliente cliente_cep = response.body();

                                    String cl_endereco = cliente_cep.getLogradouro();
                                    String cl_bairro = cliente_cep.getBairro();
                                    String cl_cidade = cliente_cep.getCidade();
                                    String cl_estado = cliente_cep.getEstado();

                                    ed_endereco.setText(cl_endereco);
                                    ed_bairro.setText(cl_bairro);
                                    ed_cidade.setText(cl_cidade);
                                    ed_estado.setText(cl_estado);

                                }
                            }

                            @Override
                            public void onFailure(Call<Cliente> call, Throwable t) {

                                Toast.makeText(getApplicationContext(), "Erro ao consultar o CEP", Toast.LENGTH_LONG).show();
                                Log.e("Error", t.getMessage());
                            }
                        });
                    }
                });

                cliente_dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        cliente.setNome(ed_nome.getText().toString());
                        cliente.setLogradouro(ed_endereco.getText().toString());
                        cliente.setNumero(ed_numero.getText().toString());
                        cliente.setBairro(ed_bairro.getText().toString());
                        cliente.setCidade(ed_cidade.getText().toString());
                        cliente.setEstado(ed_estado.getText().toString());
                        cliente.setCep(ed_cep.getText().toString());

                        accessFirebase.salva_clientes(cliente.getNome(), cliente.getLogradouro(), cliente.getNumero()
                                , cliente.getBairro(), cliente.getCidade(), cliente.getCep(), cliente.getEstado());

                    }
                }).setNegativeButton("Cancelar", null);

                cliente_dialog.show();
            }
        });
    }

    @SuppressLint("WrongConstant")
    public void ler_dados_clientes() {

        query = cl_clientes.orderBy("nome", Query.Direction.ASCENDING);

        firt_cad_clientes = new FirestoreRecyclerOptions.Builder<Cliente>()
                .setQuery(query, Cliente.class)
                .build();

        adapter_cliente = new Adapter_cliente(firt_cad_clientes, MainActivity.this);
        layout_manager_cliente = new GridLayoutManager(MainActivity.this, 1);

        rc_produto.setLayoutManager(layout_manager_cliente);
        rc_produto.setHasFixedSize(true);
        rc_produto.setAdapter(adapter_cliente);

        adapter_cliente.setOnItemClicklistener(new Adapter_cliente.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Cliente cliente_snap = documentSnapshot.toObject(Cliente.class);
                String id_cliente = documentSnapshot.getId();

                Intent i_cliente = new Intent(getApplicationContext(), ProdutosCliente.class);
                i_cliente.putExtra("info_cliente", cliente_snap);
                i_cliente.putExtra("id_cliente", id_cliente);
                i_cliente.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i_cliente);

            }
        });
    }

    @SuppressLint("WrongConstant")
    public void seachview(String search) {

        query = cl_clientes.orderBy("nome").startAt(search).endAt(search + "\uf8ff");

        firt_cad_clientes = new FirestoreRecyclerOptions.Builder<Cliente>()
                .setQuery(query, Cliente.class)
                .build();

        adapter_cliente = new Adapter_cliente(firt_cad_clientes, MainActivity.this);
        layout_manager_cliente = new GridLayoutManager(MainActivity.this, 1);

        rc_produto.setHasFixedSize(true);
        rc_produto.setAdapter(adapter_cliente);

        adapter_cliente.setOnItemClicklistener(new Adapter_cliente.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Cliente cliente_snap = documentSnapshot.toObject(Cliente.class);
                String id_cliente = documentSnapshot.getId();

                Intent i_cliente = new Intent(getApplicationContext(), ProdutosCliente.class);
                i_cliente.putExtra("info_cliente", cliente_snap);
                i_cliente.putExtra("id_cliente", id_cliente);
                i_cliente.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i_cliente);

            }
        });

        // adapter_cliente.notifyDataSetChanged();
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

        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {

            Toast.makeText(MainActivity.this, "Toque novamente para sair", Toast.LENGTH_SHORT).show();

            mBackPressed = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchitem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchitem);

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

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
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {

            new AccessFirebase().sign_out_firebase(MainActivity.this);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

}