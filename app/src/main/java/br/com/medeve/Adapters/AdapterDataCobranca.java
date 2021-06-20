package br.com.medeve.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.medeve.R;
import br.com.medeve.Models.DataCobrancaVenda;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class AdapterDataCobranca extends FirestoreRecyclerAdapter<DataCobrancaVenda, AdapterDataCobranca.ViewHolder_datas> {

    OnItemClickListener listener;
    Context context;

    FirebaseAuth db_users = FirebaseAuth.getInstance();

    //Criando instancia do banco de dados para salvar os produtos na coleção "produtos"

    FirebaseFirestore db_clientes = FirebaseFirestore.getInstance();
    CollectionReference cl_clientes = db_clientes.collection("produtos_cliente")
            .document(db_users.getUid())
            .collection("produtos");


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdapterDataCobranca(@NonNull FirestoreRecyclerOptions<DataCobrancaVenda> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder_datas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_venda_cliente_item, parent, false);

        return new ViewHolder_datas(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder_datas viewHolder_datas, int i, @NonNull DataCobrancaVenda datasVendasCobranca) {

        viewHolder_datas.datavenda.setText(datasVendasCobranca.getData_venda());
        viewHolder_datas.datacobranca.setText(datasVendasCobranca.getData_cobranca());

        viewHolder_datas.excluir_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                AlertDialog.Builder alert_datas = new AlertDialog.Builder(context);
                alert_datas.setMessage("Deseja realmente exlcuir a data ?");

                alert_datas.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        excluir_data(viewHolder_datas.getAdapterPosition(),view);

                    }
                }).setNegativeButton("Cancelar", null);

                alert_datas.show();
            }
        });
    }

    public void excluir_data(int i, final View view) {

        final DocumentReference documentReference = getSnapshots().getSnapshot(i).getReference();

        cl_clientes
                .whereEqualTo("id", documentReference.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        QuerySnapshot queryDocumentSnapshot = task.getResult();

                        if (!queryDocumentSnapshot.isEmpty()) {

                            Snackbar.make(view,"Há vendas associadas a esta data.",Snackbar.LENGTH_LONG)
                                    .show();

                        } else {

                            documentReference.delete();
                        }
                    }
                });
    }

    public class ViewHolder_datas extends RecyclerView.ViewHolder {

        TextView datacobranca, datavenda;
        ImageButton excluir_data;

        public ViewHolder_datas(@NonNull View itemView) {
            super(itemView);

            datacobranca = itemView.findViewById(R.id.txt_data_cobranca);
            datavenda = itemView.findViewById(R.id.txt_data_venda);
            excluir_data = itemView.findViewById(R.id.btn_excluir_data);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {

                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);

    }

    public void setOnItemClicklistener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
