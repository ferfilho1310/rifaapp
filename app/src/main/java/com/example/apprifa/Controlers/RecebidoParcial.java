package com.example.apprifa.Controlers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.apprifa.Adapters.AdapterRecebidosParcial;
import com.example.apprifa.Helpers.AccessFirebase;
import com.example.apprifa.Models.RecebidoParcialModel;
import com.example.apprifa.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RecebidoParcial extends AppCompatActivity {

    String id_produto;

    FloatingActionButton fab_recebido_parcial;
    RecyclerView rc_recebido_parcial;

    FirestoreRecyclerOptions fro_recebido;
    Query recebido;
    AdapterRecebidosParcial adapter_recebidos_parcial;

    AdView ad_recebido_parcial;

    RecebidoParcialModel recebidoParcial = new RecebidoParcialModel();

    FirebaseAuth db_users = FirebaseAuth.getInstance();

    FirebaseFirestore db_recebido = FirebaseFirestore.getInstance();
    CollectionReference cl_recebido_parcial = db_recebido.collection("recebido_partcial")
            .document(db_users.getUid())
            .collection("recebido_parcial");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recebido_parcial);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        fab_recebido_parcial = findViewById(R.id.fab_recebido_parcial);
        rc_recebido_parcial = findViewById(R.id.rc_recebido_parcial);
        ad_recebido_parcial = findViewById(R.id.adView_recebido_parcial);

        setTitle("Valor Recebido Parcial");

        MobileAds.initialize(RecebidoParcial.this, "ca-app-pub-2528240545678093~1740905001");

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("435EC5F610664462653ADEB2D6B1026B")
                .build();

        ad_recebido_parcial.loadAd(adRequest);

        id_produto = getIntent().getExtras().getString("id_recebido_parcial");


        ler_dados_firebase_recebido_parcial();

        fab_recebido_parcial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                AlertDialog.Builder alrt_valor_recebido = new AlertDialog.Builder(RecebidoParcial.this);
                View view1 = getLayoutInflater().inflate(R.layout.dialog_cad_recebido_parcial, null);
                alrt_valor_recebido.setTitle("Informe o valor recebido");
                alrt_valor_recebido.setView(view1);

                final EditText ed_valor_recebido = view1.findViewById(R.id.ed_dialog_recebido_parcial);

                alrt_valor_recebido.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SimpleDateFormat form_data = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = new Date();
                        String data = form_data.format(date);

                        recebidoParcial.setData(data);
                        recebidoParcial.setValor_recebido(ed_valor_recebido.getText().toString());


                        new AccessFirebase().salva_recebido_parcial(recebidoParcial.getValor_recebido(), id_produto, recebidoParcial.getData());

                    }

                }).setNegativeButton("Cancelar", null);
                alrt_valor_recebido.show();
            }
        });
    }

    public void ler_dados_firebase_recebido_parcial() {

        recebido = cl_recebido_parcial.whereEqualTo("id", id_produto);

        fro_recebido = new FirestoreRecyclerOptions.Builder<RecebidoParcialModel>()
                .setQuery(recebido, RecebidoParcialModel.class)
                .build();

        adapter_recebidos_parcial = new AdapterRecebidosParcial(fro_recebido, RecebidoParcial.this);

        rc_recebido_parcial.setLayoutManager(new LinearLayoutManager(RecebidoParcial.this,LinearLayoutManager.VERTICAL,false));
        rc_recebido_parcial.setAdapter(adapter_recebidos_parcial);
        rc_recebido_parcial.hasFixedSize();

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter_recebidos_parcial.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter_recebidos_parcial.stopListening();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:

                onBackPressed();

                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
