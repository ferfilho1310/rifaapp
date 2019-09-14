package com.example.apprifa.Controlers;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.example.apprifa.Retrofit.PostmonService;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressLint("Registered")
public class MainActivity extends AppCompatActivity {


    FloatingActionButton fab_cad_cliente;
    RecyclerView rc_produto;

    FirestoreRecyclerOptions firt_cad_clientes;

    Query query;

    FirebaseAuth db_users = FirebaseAuth.getInstance();

    FirebaseFirestore db_clientes = FirebaseFirestore.getInstance();
    CollectionReference cl_clientes = db_clientes.collection("cadastro de clientes")
            .document(db_users.getUid())
            .collection("cliente");

    Adapter_cliente adapter_cliente;

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

                final AlertDialog.Builder cliente_dialog = new AlertDialog.Builder(MainActivity.this);
                final View custom_layout = getLayoutInflater().inflate(R.layout.dialog_cad_clientes, null);
                cliente_dialog.setView(custom_layout);

                Button btn_cep = custom_layout.findViewById(R.id.btn_busca_cep);

                btn_cep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        EditText ed_cep = custom_layout.findViewById(R.id.ed_cep);

                        final EditText ed_endereco_dialog = custom_layout.findViewById(R.id.ed_end);
                        final EditText ed_bairro = custom_layout.findViewById(R.id.ed_bairro);
                        final EditText ed_cidade = custom_layout.findViewById(R.id.ed_cidade);
                        final EditText ed_estado = custom_layout.findViewById(R.id.ed_estado);
                        final EditText ed_numero = custom_layout.findViewById(R.id.ed_numero);

                        cliente.setCep(ed_cep.getText().toString());

                        Retrofit cep_busca = new Retrofit.Builder()
                                .baseUrl("http://ws.matheuscastiglioni.com.br/ws/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        PostmonService service = cep_busca.create(PostmonService.class);

                        Call<Cliente> call_cep = service.cep(ed_cep.getText().toString());

                        call_cep.enqueue(new Callback<Cliente>() {
                            @Override
                            public void onResponse(Call<Cliente> call, Response<Cliente> response) {

                                if (response.isSuccessful()) {

                                    Cliente cliente_cep = response.body();

                                    String cl_numero = cliente_cep.getNumero();
                                    String cl_endereco = cliente_cep.getLogradouro();
                                    String cl_bairro = cliente_cep.getBairro();
                                    String cl_cidade = cliente_cep.getCidade();
                                    String cl_estado = cliente_cep.getEstado();

                                    Log.d("Endereco", cl_bairro + "\n" + cl_endereco + "\n" + cl_cidade + "\n" + cl_estado);

                                    ed_endereco_dialog.setText(cl_endereco);
                                    ed_bairro.setText(cl_bairro);
                                    ed_cidade.setText(cl_cidade);
                                    ed_estado.setText(cl_estado);
                                    ed_numero.setText(cl_numero);

                                    Toast.makeText(getApplicationContext(), "Dados encotrados", Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<Cliente> call, Throwable t) {

                                Toast.makeText(getApplicationContext(), "Erro ao buscar os dados", Toast.LENGTH_LONG).show();

                            }
                        });

                    }
                });

                cliente_dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        salva_dados_cliente(custom_layout);

                    }
                }).setNegativeButton("Cancelar", null);

                cliente_dialog.show();
            }
        });

    }

    public void salva_dados_cliente(View view) {

        EditText ed_nome = view.findViewById(R.id.ed_nome);
        EditText ed_endereco = view.findViewById(R.id.ed_end);
        EditText ed_numero = view.findViewById(R.id.ed_numero);
        EditText ed_bairro = view.findViewById(R.id.ed_bairro);
        EditText ed_cidade = view.findViewById(R.id.ed_cidade);
        EditText ed_estado = view.findViewById(R.id.ed_estado);

        cliente.setNome(ed_nome.getText().toString());
        cliente.setLogradouro(ed_endereco.getText().toString());
        cliente.setNumero(ed_numero.getText().toString());
        cliente.setBairro(ed_bairro.getText().toString());
        cliente.setCidade(ed_cidade.getText().toString());
        cliente.setEstado(ed_estado.getText().toString());

        new AccessFirebase().salva_clientes(cliente.getNome(), cliente.getLogradouro(), cliente.getNumero()
                , cliente.getBairro(), cliente.getCidade(), cliente.getCep(), cliente.getEstado());

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

        query = cl_clientes.orderBy("nome", Query.Direction.ASCENDING);

        firt_cad_clientes = new FirestoreRecyclerOptions.Builder<Cliente>()
                .setQuery(query, Cliente.class)
                .build();

        adapter_cliente = new Adapter_cliente(firt_cad_clientes);

        rc_produto.setAdapter(adapter_cliente);
        adapter_cliente.notifyDataSetChanged();
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchitem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchitem);

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                seachview(s.toLowerCase());
                adapter_cliente.startListening();

                return false;
            }
        });
        return true;
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

        }

        return super.onOptionsItemSelected(item);
    }

}