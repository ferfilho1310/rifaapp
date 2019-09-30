package com.example.apprifa.Controlers;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.apprifa.Adapters.Adapter_produtos_cliente;
import com.example.apprifa.Helpers.AccessFirebase;
import com.example.apprifa.Models.Cliente;
import com.example.apprifa.Models.Produto;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.apprifa.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProdutosCliente extends AppCompatActivity {

    RecyclerView rc_prod_cliente;
    FloatingActionButton fb_prod_cliente;

    FirestoreRecyclerOptions firt_cad_clientes;
    Query query;

    FirebaseAuth db_users = FirebaseAuth.getInstance();

    FirebaseFirestore db_clientes = FirebaseFirestore.getInstance();
    CollectionReference cl_clientes = db_clientes.collection("produtos_cliente")
            .document(db_users.getUid())
            .collection("produtos");

    Adapter_produtos_cliente adapter_produtos_cliente;

    TextView teste_soma;

    private Cliente cliente;
    Produto produto = new Produto();
    AccessFirebase accessFirebase = new AccessFirebase();

    String nome, id_cliente_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos_cliente);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rc_prod_cliente = findViewById(R.id.rc_produto_cliente);
        fb_prod_cliente = findViewById(R.id.fab_produto_cliente);
        teste_soma = findViewById(R.id.txt_soma);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        cliente = getIntent().getExtras().getParcelable("info_cliente");
        id_cliente_2 = getIntent().getExtras().getString("id_cliente");

        nome = cliente.getNome();

        getSupportActionBar().setTitle(nome);
        ler_dados_clientes();
        soma_total();


        fb_prod_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder cliente = new AlertDialog.Builder(ProdutosCliente.this);
                final View custom_layout = getLayoutInflater().inflate(R.layout.dialog_produto_cliente, null);
                cliente.setView(custom_layout);

                cliente.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        salva_produto_cliente(custom_layout);
                        soma_total();

                    }
                }).setNegativeButton("Cancelar", null);

                cliente.show();
            }


        });

    }

    private void salva_produto_cliente(View custom_layout) {

        SimpleDateFormat format_date = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String data = format_date.format(date);

        EditText nm_produto = custom_layout.findViewById(R.id.ed_nome_produto);
        EditText vl_produto = custom_layout.findViewById(R.id.ed_preco_produto);
        EditText qtd_produto = custom_layout.findViewById(R.id.ed_quantidade);

        produto.setNomedoproduto(nm_produto.getText().toString());
        produto.setQuantidade(qtd_produto.getText().toString());
        produto.setValor(vl_produto.getText().toString());
        produto.setTotal(String.valueOf(Float.parseFloat(qtd_produto.getText().toString()) * Float.parseFloat(vl_produto.getText().toString())));
        produto.setData(data);

        accessFirebase.salva_produtos(produto.getData(), produto.getNomedoproduto(), produto.getQuantidade(), produto.getValor(), produto.getTotal(), id_cliente_2);

    }

    @SuppressLint("WrongConstant")
    public void ler_dados_clientes() {

        query = cl_clientes
                .whereEqualTo("id", id_cliente_2)
                .orderBy("nomedoproduto", Query.Direction.DESCENDING);

        firt_cad_clientes = new FirestoreRecyclerOptions.Builder<Produto>()
                .setQuery(query, Produto.class)
                .build();

        adapter_produtos_cliente = new Adapter_produtos_cliente(firt_cad_clientes, ProdutosCliente.this);
        rc_prod_cliente.setLayoutManager(new LinearLayoutManager(ProdutosCliente.this, LinearLayoutManager.VERTICAL, false));
        rc_prod_cliente.setHasFixedSize(true);
        rc_prod_cliente.setAdapter(adapter_produtos_cliente);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter_produtos_cliente.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter_produtos_cliente.stopListening();
    }

    public void soma_total() {

        cl_clientes.whereEqualTo("id", id_cliente_2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        float soma = 0;

                        List<Float> ls_resultado = new ArrayList<>();

                        QuerySnapshot queryDocumentSnapshots = task.getResult();

                        for (Produto produto : queryDocumentSnapshots.toObjects(Produto.class)) {

                            float i = Float.parseFloat(produto.getTotal());

                            ls_resultado.add(i);

                        }

                        for (int i = 0; i < ls_resultado.size(); i++) {

                            soma = (soma + ls_resultado.get(i));
                            teste_soma.setText(String.valueOf(soma));

                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_produtos, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();

        if (i == android.R.id.home) {

            startActivity(new Intent(getApplicationContext(), DatasVendasCobranca.class));
            finish();
            return true;

        } else if (i == R.id.somar) {

            soma_total();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
