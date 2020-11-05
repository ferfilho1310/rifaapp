package br.com.medeve.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import br.com.medeve.Models.Produto;
import br.com.medeve.R;
import br.com.medeve.Adapters.AdapterRecebidosParcial;
import br.com.medeve.Helpers.AccessFirebase;

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
    Produto produto;

    FloatingActionButton fab_recebido_parcial;
    RecyclerView rc_recebido_parcial;

    FirestoreRecyclerOptions fro_recebido;
    Query recebido;
    AdapterRecebidosParcial adapter_recebidos_parcial;

    AdView ad_recebido_parcial;

    br.com.medeve.Models.RecebidoParcial recebidoParcial = new br.com.medeve.Models.RecebidoParcial();

    FirebaseAuth db_users = FirebaseAuth.getInstance();

    FirebaseFirestore db_recebido = FirebaseFirestore.getInstance();
    CollectionReference cl_recebido_parcial = db_recebido.collection("recebido_partcial")
            .document(db_users.getUid())
            .collection("recebido_parcial");

    TextView valor_produto, quantidade_produto;

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
        valor_produto = findViewById(R.id.txt_vlr_produto);
        quantidade_produto = findViewById(R.id.txt_quant_produto);

        setTitle("Valor Recebido Parcial");

        MobileAds.initialize(RecebidoParcial.this, "ca-app-pub-2528240545678093~1740905001");

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        ad_recebido_parcial.loadAd(adRequest);

        id_produto = getIntent().getExtras().getString("id_recebido_parcial");

        try {
            produto = getIntent().getExtras().getParcelable("info_produto");
            valor_produto.setText(produto.getValor());
            quantidade_produto.setText(produto.getQuantidade());
        } catch (Exception e) {
            Log.i("Erro", "Erro ao excluir o valor recebido parcial", e);
        }

        ler_dados_firebase_recebido_parcial();

        fab_recebido_parcial();
    }

    public void fab_recebido_parcial() {

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

                        @SuppressLint("SimpleDateFormat") SimpleDateFormat form_data = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = new Date();
                        String data = form_data.format(date);

                        recebidoParcial.setData(data);
                        recebidoParcial.setValor_recebido(ed_valor_recebido.getText().toString());

                        AccessFirebase.getinstance().salva_recebido_parcial(recebidoParcial.getValor_recebido(), id_produto, recebidoParcial.getData());

                    }

                }).setNegativeButton("Cancelar", null);
                alrt_valor_recebido.show();
            }
        });

    }

    public void ler_dados_firebase_recebido_parcial() {

        recebido = cl_recebido_parcial.whereEqualTo("id", id_produto);

        fro_recebido = new FirestoreRecyclerOptions.Builder<br.com.medeve.Models.RecebidoParcial>()
                .setQuery(recebido, br.com.medeve.Models.RecebidoParcial.class)
                .build();

        adapter_recebidos_parcial = new AdapterRecebidosParcial(fro_recebido, RecebidoParcial.this);

        rc_recebido_parcial.setLayoutManager(new LinearLayoutManager(RecebidoParcial.this, LinearLayoutManager.VERTICAL, false));
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
