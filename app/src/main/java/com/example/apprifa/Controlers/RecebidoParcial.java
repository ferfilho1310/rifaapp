package com.example.apprifa.Controlers;

import android.content.DialogInterface;
import android.os.Bundle;

import com.example.apprifa.Helpers.AccessFirebase;
import com.example.apprifa.Models.RecebidoParcialModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.apprifa.R;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RecebidoParcial extends AppCompatActivity {

    RecyclerView rc_recebido_parcial;
    AdView ad_recebido_parcial;
    FloatingActionButton fab_recebido_parcial;

    AccessFirebase accessFirebase = new AccessFirebase();
    RecebidoParcialModel recebidoParcialModel = new RecebidoParcialModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recebido_parcial);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rc_recebido_parcial = findViewById(R.id.rc_recebido_parcial);
        ad_recebido_parcial = findViewById(R.id.ad_recebido_parcial);
        fab_recebido_parcial = findViewById(R.id.fab_recebido_parcial);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        FirebaseInstanceId.getInstance();

        MobileAds.initialize(RecebidoParcial.this, "ca-app-pub-2528240545678093~1740905001");

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("435EC5F610664462653ADEB2D6B1026B")
                .build();

        ad_recebido_parcial.loadAd(adRequest);

        fab_recebido_parcial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder altr_receb_parcial = new AlertDialog.Builder(RecebidoParcial.this);
                altr_receb_parcial.setTitle("Informe o valor recebido");
                final View dialgo_receb_parc = getLayoutInflater().inflate(R.layout.dialog_cad_recebido_parcial, null);
                altr_receb_parcial.setView(dialgo_receb_parc);

                altr_receb_parcial.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SimpleDateFormat format_date = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = new Date();
                        String data = format_date.format(date);

                        EditText ed_valor_recebido = dialgo_receb_parc.findViewById(R.id.ed_dialog_recebido_parcial);

                        recebidoParcialModel.setData(data);
                        recebidoParcialModel.setValor_recebido(ed_valor_recebido.getText().toString());

                        //accessFirebase.salva_recebido_parcial(recebidoParcialModel.getValor_recebido(),);


                    }
                }).setNegativeButton("Cancelar", null);

                altr_receb_parcial.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
