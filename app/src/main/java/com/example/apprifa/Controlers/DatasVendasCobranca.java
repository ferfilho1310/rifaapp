package com.example.apprifa.Controlers;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.apprifa.Helpers.AccessFirebase;
import com.example.apprifa.Models.Cliente;
import com.example.apprifa.Models.DataCobrancaVenda;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.apprifa.R;
import com.google.type.Date;

import java.util.Calendar;

public class DatasVendasCobranca extends AppCompatActivity {

    EditText ed_data_cobranca, ed_data_venda;

    String id_cliente_2;

    DataCobrancaVenda datasVendasCobranca = new DataCobrancaVenda();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datas_vendas_cobranca);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_data_venda);
        id_cliente_2 = getIntent().getExtras().getString("id_cliente");

        setTitle("Datas de venda e cobrança");

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

                                String data = i2 + "/" + (i1+1) + "/" + i;

                                Log.d("Data",data);

                                ed_data_venda.setText(data);

                            }
                        }, ano, mes, dia);

                        datePickerDialog.show();
                    }
                });

                inseri_data_cobranca.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatePickerDialog datePickerDialog = new DatePickerDialog(DatasVendasCobranca.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                                String data = i2 + "/" + (i1+1) + "/" + i;

                                Log.d("Data",data);

                                ed_data_cobranca.setText(data);

                            }
                        }, ano, mes, dia);

                        datePickerDialog.show();

                    }
                });

                cliente.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        datascobranca(custom_layout);

                    }
                });

                cliente.setNegativeButton("Cancelar",null);

                cliente.show();
            }
        });
    }

    public void datascobranca(View view) {

        datasVendasCobranca.setData_cobranca(ed_data_cobranca.getText().toString());
        datasVendasCobranca.setData_venda(ed_data_venda.getText().toString());

        new AccessFirebase().data_cobranca(id_cliente_2, datasVendasCobranca.getData_venda(), datasVendasCobranca.getData_venda());

    }

}
