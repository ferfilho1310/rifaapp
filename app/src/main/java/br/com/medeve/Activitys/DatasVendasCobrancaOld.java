/*
package br.com.medeve.Activitys;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import br.com.medeve.Adapters.AdapterViewEmpty;
import br.com.medeve.Models.Cliente;
import br.com.medeve.R;
import br.com.medeve.Adapters.AdapterDataCobranca;
import br.com.medeve.Helpers.AccessFirebase;
import br.com.medeve.Models.DataCobrancaVenda;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;

public class DatasVendasCobrancaOld extends AppCompatActivity {

    FloatingActionButton fab;
    Query query;
    FirestoreRecyclerOptions<DataCobrancaVenda> rc_options_datas;
    AdView adView_vendas;
    SearchView searchView;
    TextView nome_cliente_extra, telefone_extra,no_data;

    private String id_cliente_2;

    Cliente cliente;

    DataCobrancaVenda datasVendasCobranca = new DataCobrancaVenda();

    FirebaseAuth db_users = FirebaseAuth.getInstance();

    FirebaseFirestore db_datas = FirebaseFirestore.getInstance();
    CollectionReference cl_datas = db_datas.collection("datas_cobranca")
            .document(db_users.getUid())
            .collection("data_de_cobraca");

    RecyclerView rc_datas;
    AdapterDataCobranca adapter_datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datas_vendas_cliente);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab_data_venda);
        rc_datas = findViewById(R.id.rc_data_cobranca);
        adView_vendas = findViewById(R.id.adView_datas);
        nome_cliente_extra = findViewById(R.id.txt_nome_cliente_extra);
        telefone_extra = findViewById(R.id.txt_telefone_extra);
        no_data = findViewById(R.id.no_data_de_vendas);

        setTitle("Datas de venda e cobrança");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        id_cliente_2 = getIntent().getExtras().getString("id_cliente");

        cliente = getIntent().getExtras().getParcelable("info_cliente");
        nome_cliente_extra.setText(cliente.getNome());
        telefone_extra.setText(cliente.getTelefone());

        MobileAds.initialize(DatasVendasCobrancaOld.this, "ca-app-pub-2528240545678093~1740905001");

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("B6D5B7288C97DD6A90A5F0E267BADDA5")
                .build();

        adView_vendas.loadAd(adRequest);

        ler_dados_firestore_datas();

        fab_cad_data_clientes();

        AdapterViewEmpty adapterViewEmpty = new AdapterViewEmpty(no_data,rc_datas);
        adapter_datas.registerAdapterDataObserver(adapterViewEmpty);
    }

    public void fab_cad_data_clientes() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder cliente = new AlertDialog.Builder(DatasVendasCobrancaOld.this);
                final View custom_layout = getLayoutInflater().inflate(R.layout.dialog_data_cobranca, null);
                cliente.setTitle("Informe as datas:");
                cliente.setView(custom_layout);

                ImageButton inseri_data_venda = custom_layout.findViewById(R.id.btn_data_venda);
                ImageButton inseri_data_cobranca = custom_layout.findViewById(R.id.btn_data_cobranca);

                final EditText ed_data_cobranca = custom_layout.findViewById(R.id.ed_data_cobranca);
                final EditText ed_data_venda = custom_layout.findViewById(R.id.ed_data_venda);

                Calendar calendar = Calendar.getInstance();
                final int dia = calendar.get(Calendar.DAY_OF_MONTH);
                final int mes = calendar.get(Calendar.MONTH);
                final int ano = calendar.get(Calendar.YEAR);

                btn_data_venda.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatePickerDialog datePickerDialog = new DatePickerDialog(DatasVendasCobrancaOld.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                                String data = i2 + "/" + (i1 + 1) + "/" + i;

                                Log.d("Data", data);

                                ed_data_venda.setText(data);

                            }
                        }, ano, mes, dia);

                        datePickerDialog.show();
                    }
                });

                Calendar calendar_cobranca = Calendar.getInstance();
                final int dia_cobranca = calendar_cobranca.get(Calendar.DAY_OF_MONTH);
                final int mes_cobranca = calendar_cobranca.get(Calendar.MONTH);
                final int ano_cobranca = calendar_cobranca.get(Calendar.YEAR);

                btn_data_cobranca.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatePickerDialog datePickerDialog = new DatePickerDialog(DatasVendasCobrancaOld.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                                String data = i2 + "/" + (i1 + 1) + "/" + i;

                                ed_data_cobranca.setText(data);

                            }
                        }, ano_cobranca, mes_cobranca, dia_cobranca);

                        datePickerDialog.show();
                    }
                });

                cliente.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        datascobranca(custom_layout);

                    }
                });
                cliente.setNegativeButton("Cancelar", null);

                cliente.show();
            }
        });
    }

    public void datascobranca(View view) {

        EditText ed_data_cobranca = view.findViewById(R.id.ed_data_cobranca);
        EditText ed_data_venda = view.findViewById(R.id.ed_data_venda);

        datasVendasCobranca.setData_cobranca(ed_data_cobranca.getText().toString());
        datasVendasCobranca.setData_venda(ed_data_venda.getText().toString());

        AccessFirebase.getinstance().data_cobranca(id_cliente_2, datasVendasCobranca.getData_venda(), datasVendasCobranca.getData_cobranca());

    }

    @SuppressLint("WrongConstant")
    public void ler_dados_firestore_datas() {

        query = cl_datas.whereEqualTo("id_data", id_cliente_2)
                .orderBy("data_venda", Query.Direction.DESCENDING);

        rc_options_datas = new FirestoreRecyclerOptions.Builder<DataCobrancaVenda>()
                .setQuery(query, DataCobrancaVenda.class)
                .build();

        adapter_datas = new AdapterDataCobranca(rc_options_datas, DatasVendasCobrancaOld.this);
        rc_datas.setAdapter(adapter_datas);
        rc_datas.setLayoutManager(new LinearLayoutManager(DatasVendasCobrancaOld.this, LinearLayoutManager.VERTICAL, false));
        rc_datas.setHasFixedSize(true);

        adapter_datas.setOnItemClicklistener(new AdapterDataCobranca.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                String id_data = documentSnapshot.getId();

                Intent i_data = new Intent(getApplicationContext(), ProdutosCliente.class);
                i_data.putExtra("id_data_compra", id_data);
                i_data.putExtra("dados_cliente",cliente);
                startActivity(i_data);

            }
        });
    }

    public void search_datas_firestore(String search_datas) {

        query = cl_datas.whereEqualTo("id_data", id_cliente_2)
                .orderBy("data_venda").startAt(search_datas).endAt(search_datas + "\uf8ff");

        rc_options_datas = new FirestoreRecyclerOptions.Builder<DataCobrancaVenda>()
                .setQuery(query, DataCobrancaVenda.class)
                .build();

        adapter_datas = new AdapterDataCobranca(rc_options_datas, DatasVendasCobrancaOld.this);
        rc_datas.setAdapter(adapter_datas);
        rc_datas.setLayoutManager(new LinearLayoutManager(DatasVendasCobrancaOld.this, LinearLayoutManager.VERTICAL, false));
        rc_datas.setHasFixedSize(true);

        adapter_datas.setOnItemClicklistener(new AdapterDataCobranca.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                String id_data = documentSnapshot.getId();

                Intent i_data = new Intent(getApplicationContext(), ProdutosCliente.class);
                Bundle data = new Bundle();
                data.putString("id_data_compra", id_data);
                i_data.putExtras(data);
                startActivity(i_data);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_datas, menu);

        MenuItem searchitem = menu.findItem(R.id.search);

        searchView = (SearchView) MenuItemCompat.getActionView(searchitem);

        searchView.setQueryHint("Digite a data da venda");
        searchView.setIconified(true);
        searchView.setFocusable(true);
        searchView.setInputType(InputType.TYPE_CLASS_DATETIME);

        searchitem.expandActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                if (!s.trim().isEmpty()) {
                    search_datas_firestore(s);
                    adapter_datas.startListening();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if (s.trim().isEmpty()) {
                    search_datas_firestore(s);
                    adapter_datas.startListening();
                }

                return false;
            }
        });
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter_datas.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter_datas.stopListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(), CadastroClienteActView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent i_cad_user = new Intent(DatasVendasCobrancaOld.this, CadastroClienteActView.class);
                i_cad_user.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i_cad_user);
                finish();

                break;
            default:
                break;
        }
        return true;
    }

}
*/
