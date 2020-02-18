package br.com.medeve.Controlers;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import br.com.medeve.Adapters.AdapterViewEmpty;
import br.com.medeve.Models.Cliente;
import br.com.medeve.R;
import br.com.medeve.Adapters.AdapterProdutosCliente;
import br.com.medeve.Helpers.AccessFirebase;

import br.com.medeve.Models.Produto;

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

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.internal.FederatedSignInActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
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
    TextView nome_cliente, telefone_cliente, no_data_produtos;

    FirebaseAuth db_users = FirebaseAuth.getInstance();

    //Criando instacia do banco de dados para salvar os produtos na coleção "produtos"

    FirebaseFirestore db_clientes = FirebaseFirestore.getInstance();
    CollectionReference cl_clientes = db_clientes.collection("produtos_cliente")
            .document(db_users.getUid())
            .collection("produtos");

    AdapterProdutosCliente adapter_produtos_cliente;

    TextView teste_soma, recebido_produtos, a_receber;

    Produto produto = new Produto();

    String id_data;
    Cliente cliente;

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
        //a_receber = findViewById(R.id.valor_a_receber);
        nome_cliente = findViewById(R.id.txt_nome_cliente_extra_produto);
        telefone_cliente = findViewById(R.id.txt_telefone_extra_produto);
        no_data_produtos = findViewById(R.id.no_data_produtos_cliente);

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

        try {
            cliente = getIntent().getExtras().getParcelable("dados_cliente");
            nome_cliente.setText(cliente.getNome());
            telefone_cliente.setText(cliente.getTelefone());
        } catch (Exception e) {
            Log.i("TAG", "Erro a obter nome e telefone");
        }

        setTitle("Produtos do Cliente");

        MobileAds.initialize(ProdutosCliente.this, "ca-app-pub-2528240545678093~1740905001");

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        adView_produtos.loadAd(adRequest);

        //Instacia das classes para ler dados salvos
        // no banco e somar o valor devido de cada cliente

        ler_dados_clientes();
        soma_total();
        recebido();
        fab_cad_produto_cliente();
        //valor_a_receber();

        AdapterViewEmpty adapterViewEmpty = new AdapterViewEmpty(no_data_produtos, rc_prod_cliente);
        adapter_produtos_cliente.registerAdapterDataObserver(adapterViewEmpty);
    }

    public void fab_cad_produto_cliente() {

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
                                    //valor_a_receber();
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

        @SuppressLint("SimpleDateFormat") SimpleDateFormat format_date = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String data = format_date.format(date);

        EditText nm_produto = custom_layout.findViewById(R.id.ed_nome_produto);
        EditText vl_produto = custom_layout.findViewById(R.id.ed_preco_produto);
        EditText qtd_produto = custom_layout.findViewById(R.id.ed_quantidade);

        produto.setNomedoproduto(nm_produto.getText().toString());
        produto.setQuantidade(qtd_produto.getText().toString());
        produto.setValor(vl_produto.getText().toString());
        produto.setTotal(String.valueOf(Float.parseFloat(qtd_produto.getText().toString())
                * Float.parseFloat(vl_produto.getText().toString())));
        produto.setData(data);
        produto.setRecebido(false);
        produto.setDevolvido(false);

        //Metodo para salvar os dados do produto do cliente no banco de dados

        AccessFirebase.getinstance().salva_produtos(produto.getData(), produto.getNomedoproduto(), produto.getQuantidade(),
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

        adapter_produtos_cliente = new AdapterProdutosCliente(firt_cad_clientes, ProdutosCliente.this);
        rc_prod_cliente.setLayoutManager(new LinearLayoutManager(ProdutosCliente.this, LinearLayoutManager.VERTICAL, false));
        rc_prod_cliente.setHasFixedSize(true);
        rc_prod_cliente.setAdapter(adapter_produtos_cliente);
        adapter_produtos_cliente.setOnItemClicklistener(new AdapterProdutosCliente.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Produto produto = documentSnapshot.toObject(Produto.class);

                final Intent i_recebido_parcial = new Intent(getApplicationContext(), RecebidoParcial.class);
                i_recebido_parcial.putExtra("id_recebido_parcial", documentSnapshot.getId());
                i_recebido_parcial.putExtra("info_produto", produto);
                startActivity(i_recebido_parcial);

            }
        });

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
                        String zero = "0.00";

                        List<Float> ls_resultado = new ArrayList<>();

                        QuerySnapshot queryDocumentSnapshots = task.getResult();

                        for (Produto produto : queryDocumentSnapshots.toObjects(Produto.class)) {

                            float i = Float.parseFloat(produto.getTotal());

                            ls_resultado.add(i);
                        }

                        for (int i = 0; i < ls_resultado.size(); i++) {

                            soma = (soma + ls_resultado.get(i));
                        }

                        NumberFormat format_soma = new DecimalFormat("0.##");

                        teste_soma.setText(format_soma.format(soma));
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
                        String zero = "0.00";

                        List<Float> ls_resultado = new ArrayList<>();

                        QuerySnapshot queryDocumentSnapshots = task.getResult();

                        for (Produto produto : queryDocumentSnapshots.toObjects(Produto.class)) {

                            if (produto.getRecebido() != false) {

                                float i = Float.parseFloat(produto.getTotal());

                                ls_resultado.add(i);
                            } else {

                                recebido_produtos.setText(zero);

                            }
                        }

                        for (int i = 0; i < ls_resultado.size(); i++) {

                            soma = (soma + ls_resultado.get(i));
                        }

                        NumberFormat format_recebido = new DecimalFormat("0.##");

                        recebido_produtos.setText(format_recebido.format(soma));
                    }
                });
    }

    /*public void valor_a_receber(){

        cl_clientes.whereEqualTo("id", id_data)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        float soma_total = 0;
                        float soma_recebido = 0;
                        float soma_devolvido = 0;
                        float areceber;

                        List<Float> recebido = new ArrayList<>();
                        List<Float> total = new ArrayList<>();
                        List<Float> devolvido = new ArrayList<>();

                        QuerySnapshot queryDocumentSnapshots = task.getResult();

                        for (Produto produto_total : queryDocumentSnapshots.toObjects(Produto.class)) {

                            float i = Float.parseFloat(produto_total.getTotal());

                            total.add(i);

                        }

                        for (Produto produto_recebido : queryDocumentSnapshots.toObjects(Produto.class)) {

                            if (produto_recebido.getRecebido() != false) {

                                float j = Float.parseFloat(produto_recebido.getTotal());
                                recebido.add(j);
                            }
                        }

                        for (Produto produto_devolvido : queryDocumentSnapshots.toObjects(Produto.class)) {

                            if (produto_devolvido.getDevolvido() != false) {

                                float f = Float.parseFloat(produto_devolvido.getTotal());
                                devolvido.add(f);
                            }
                        }

                        for (int j = 0; j < total.size(); j++) {

                            soma_total = (soma_total + total.get(j));
                        }

                        for (int i = 0; i < recebido.size(); i++) {

                            soma_recebido = (soma_recebido + recebido.get(i));
                        }

                        for (int f = 0; f < devolvido.size(); f++) {

                            soma_devolvido = (soma_devolvido + devolvido.get(f));
                        }

                        areceber = (soma_total - soma_recebido) - soma_devolvido;

                        NumberFormat format_a_receber = new DecimalFormat("0.##");

                        a_receber.setText(format_a_receber.format(areceber));
                    }
                });
    }
*/
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
                //valor_a_receber();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}