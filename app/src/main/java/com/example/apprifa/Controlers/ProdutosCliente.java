package com.example.apprifa.Controlers;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.apprifa.Adapters.Adapter_produtos_cliente;
import com.example.apprifa.Helpers.AccessFirebase;

import com.example.apprifa.Models.Produto;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.apprifa.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
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
    AdView adView_produtos;

    FirebaseAuth db_users = FirebaseAuth.getInstance();

    //Criando instacia do banco de dados para salvar os produtos na coleção "produtos"

    FirebaseFirestore db_clientes = FirebaseFirestore.getInstance();
    CollectionReference cl_clientes = db_clientes.collection("produtos_cliente")
            .document(db_users.getUid())
            .collection("produtos");

    Adapter_produtos_cliente adapter_produtos_cliente;

    TextView teste_soma, recebido_produtos;

    Produto produto = new Produto();
    AccessFirebase accessFirebase = new AccessFirebase();

    String id_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos_cliente);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rc_prod_cliente = findViewById(R.id.rc_produto_cliente);
        fb_prod_cliente = findViewById(R.id.fab_produto_cliente);
        teste_soma = findViewById(R.id.txt_soma);
        adView_produtos = findViewById(R.id.adView_produtos);
        recebido_produtos = findViewById(R.id.txt_recebido);

        //Métodos para aparecer o botão "back" na action bar customizavel
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        //Recuperando o ID do item vindo da classe "DataVendasCobranca"

        Intent receber = getIntent();
        Bundle data = receber.getExtras();

        if (data != null) {
            id_data = data.getString("id_data_compra");
        }
        setTitle("Produtos do Cliente");

        MobileAds.initialize(ProdutosCliente.this, "ca-app-pub-2528240545678093~1740905001");

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("435EC5F610664462653ADEB2D6B1026B")
                .build();

        adView_produtos.loadAd(adRequest);

        //Instacia das classes para ler dados salvos
        // no banco e somar o valor devido de cada cliente

        ler_dados_clientes();
        soma_total();
        recebido();

        //new Assync_somas().execute();

        fb_prod_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder cliente = new AlertDialog.Builder(ProdutosCliente.this);
                final View custom_layout = getLayoutInflater().inflate(R.layout.dialog_produto_cliente, null);
                cliente.setTitle("Informe os dados do produto:");
                cliente.setView(custom_layout);

                final EditText nm_produto = custom_layout.findViewById(R.id.ed_nome_produto);
                final EditText vl_produto = custom_layout.findViewById(R.id.ed_preco_produto);
                final EditText qtd_produto = custom_layout.findViewById(R.id.ed_quantidade);

                cliente.setPositiveButton("OK", null)
                        .setNegativeButton("Cancelar", null);

                final AlertDialog valida_campo = cliente.create();
                valida_campo.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialogInterface) {

                        Button btn_ok_dlg = valida_campo.getButton(AlertDialog.BUTTON_POSITIVE);

                        btn_ok_dlg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (nm_produto.getText().length() == 0) {
                                    nm_produto.setError("Informe o nome do produto");
                                } else if (qtd_produto.getText().length() == 0) {
                                    qtd_produto.setError("Informe o valor do produto");
                                } else if (vl_produto.getText().length() == 0) {
                                    vl_produto.setError("Inforne a quantidade do produto");
                                } else {
                                    salva_produto_cliente(custom_layout);
                                    soma_total();
                                    dialogInterface.dismiss();
                                }
                            }
                        });
                    }
                });

                valida_campo.show();
            }
        });
    }

    //classe que pega o valor digitado pelo usuário e salva no banco de dados

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
        produto.setRecebido(false);
        produto.setDevolvido(false);

        //Metodo para salvar os dados do produto do cliente no banco de dados

        accessFirebase.salva_produtos(produto.getData(), produto.getNomedoproduto(), produto.getQuantidade(),
                produto.getValor(), produto.getTotal(), id_data, produto.getRecebido(), produto.getDevolvido());

    }

    //Classe para ler dados do banco
    @SuppressLint("WrongConstant")
    public void ler_dados_clientes() {

        //Query será executada para filtrar os dados de acordo
        //com o ID da data seleciona vindo da classe "DatasVendasCobranca"
        //e ordenando pelo nome do produto adquirido pelo cliente

        query = cl_clientes
                .whereEqualTo("id", id_data)
                .orderBy("nomedoproduto", Query.Direction.DESCENDING);

        firt_cad_clientes = new FirestoreRecyclerOptions.Builder<Produto>()
                .setQuery(query, Produto.class)
                .build();

        //Instacia do adapter do produtos para carregar
        // os itens vindo do banco do banco no formado de linhas na vertical

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

    //Classe para percorrer todos os produtos salvos no banco
    //e somar o valor devido de cada cliente.

    public void soma_total() {

        cl_clientes.whereEqualTo("id", id_data)
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

    public void recebido() {

        cl_clientes.whereEqualTo("id", id_data)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        float soma = 0;

                        List<Float> ls_resultado = new ArrayList<>();

                        QuerySnapshot queryDocumentSnapshots = task.getResult();

                        for (Produto produto : queryDocumentSnapshots.toObjects(Produto.class)) {

                            if (produto.getRecebido() != false) {

                                float i = Float.parseFloat(produto.getTotal());

                                ls_resultado.add(i);
                            }
                        }

                        for (int i = 0; i < ls_resultado.size(); i++) {

                            soma = (soma + ls_resultado.get(i));
                            recebido_produtos.setText(String.valueOf(soma));

                        }
                    }
                });
    }

    //Classe para inflar o menu customizado

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_produtos, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                onBackPressed();

                break;
            case R.id.somar:
                soma_total();
                recebido();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}