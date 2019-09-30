package com.example.apprifa.Controlers;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.apprifa.Adapters.Adapter_Data_Cobranca;
import com.example.apprifa.Helpers.AccessFirebase;
import com.example.apprifa.Models.Cliente;
import com.example.apprifa.Models.DataCobrancaVenda;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.apprifa.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.type.Date;

import java.util.Calendar;

public class DatasVendasCobranca extends AppCompatActivity {

    EditText ed_data_cobranca, ed_data_venda;

    Query query;
    FirestoreRecyclerOptions<DataCobrancaVenda> rc_options_datas;

    String id_cliente_2;

    DataCobrancaVenda datasVendasCobranca = new DataCobrancaVenda();

    FirebaseAuth db_users = FirebaseAuth.getInstance();

    FirebaseFirestore db_datas = FirebaseFirestore.getInstance();
    CollectionReference cl_datas = db_datas.collection("datas_cobranca")
            .document(db_users.getUid())
            .collection("data_de_cobraca");

    RecyclerView rc_datas;
    Adapter_Data_Cobranca adapter_datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datas_vendas_cobranca);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_data_venda);
        id_cliente_2 = getIntent().getExtras().getString("id_cliente");
        rc_datas = findViewById(R.id.rc_data_cobranca);

        setTitle("Datas de venda e cobrança");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        FirebaseApp.initializeApp(DatasVendasCobranca.this);

        ler_dados_firestore_datas();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder cliente = new AlertDialog.Builder(DatasVendasCobranca.this);
                final View custom_layout = getLayoutInflater().inflate(R.layout.dialog_data_cobranca, null);
                cliente.setView(custom_layout);

                Button inseri_data_venda = custom_layout.findViewById(R.id.btn_data_venda);
                Button inseri_data_cobranca = custom_layout.findViewById(R.id.btn_data_cobranca);

                ed_data_cobranca = custom_layout.findViewById(R.id.ed_data_cobranca);
                ed_data_venda = custom_layout.findViewById(R.id.ed_data_venda);

                Calendar calendar = Calendar.getInstance();
                final int dia = calendar.get(Calendar.DAY_OF_MONTH);
                final int mes = calendar.get(Calendar.MONTH);
                final int ano = calendar.get(Calendar.YEAR);

                inseri_data_venda.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatePickerDialog datePickerDialog = new DatePickerDialog(DatasVendasCobranca.this, new DatePickerDialog.OnDateSetListener() {
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

                inseri_data_cobranca.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatePickerDialog datePickerDialog = new DatePickerDialog(DatasVendasCobranca.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                                String data = i2 + "/" + (i1 + 1) + "/" + i;

                                Log.d("Data", data);

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

        datasVendasCobranca.setData_cobranca(ed_data_cobranca.getText().toString());
        datasVendasCobranca.setData_venda(ed_data_venda.getText().toString());

        new AccessFirebase().data_cobranca(id_cliente_2, datasVendasCobranca.getData_venda(), datasVendasCobranca.getData_venda());

    }

    public void ler_dados_firestore_datas() {

        query = cl_datas.whereEqualTo("id",id_cliente_2);

        rc_options_datas = new FirestoreRecyclerOptions.Builder<DataCobrancaVenda>()
                .setQuery(query,DataCobrancaVenda.class)
                .build();

        adapter_datas = new Adapter_Data_Cobranca(rc_options_datas);
        rc_datas.setAdapter(adapter_datas);
        rc_datas.setLayoutManager(new LinearLayoutManager(DatasVendasCobranca.this,LinearLayoutManager.VERTICAL,false));
        rc_datas.setHasFixedSize(true);

        adapter_datas.setOnItemClicklistener(new Adapter_Data_Cobranca.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                String id_data = documentSnapshot.getId();
                Intent i_data = new Intent(getApplicationContext(),ProdutosCliente.class);
                i_data.putExtra("id_data_compra",id_data);
                startActivity(i_data);

            }
        });
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

    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i_cad_user = new Intent(getApplicationContext(), MainActivity.class);//ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(i_cad_user);//O efeito ao ser pressionado do botão (no caso abre a activity)
                finish();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:
                break;
        }
        return true;
    }

}
