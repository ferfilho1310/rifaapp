package br.com.medeve.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.medeve.R;
import br.com.medeve.Models.Produto;
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

import java.util.HashMap;

import java.util.Map;


public class AdapterProdutosCliente extends FirestoreRecyclerAdapter<Produto, AdapterProdutosCliente.Viewholder_prod_cliente> {


    OnItemClickListener listener;
    Context context;

    FirebaseAuth db_users = FirebaseAuth.getInstance();

    FirebaseFirestore db_recebido = FirebaseFirestore.getInstance();
    CollectionReference cl_recebido_parcial = db_recebido.collection("recebido_partcial")
            .document(db_users.getUid())
            .collection("recebido_parcial");

    public AdapterProdutosCliente(@NonNull FirestoreRecyclerOptions<Produto> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder_prod_cliente onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mostra_dados_db_produto_cliente, parent, false);

        return new Viewholder_prod_cliente(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final Viewholder_prod_cliente viewholder_prod_cliente, int i, @NonNull final Produto produto) {

        viewholder_prod_cliente.ch_recebido.setOnCheckedChangeListener(null);
        viewholder_prod_cliente.ch_devolvido.setOnCheckedChangeListener(null);

        viewholder_prod_cliente.nome_produto.setText(produto.getNomedoproduto());
        viewholder_prod_cliente.quantidade_produto.setText(produto.getQuantidade());
        viewholder_prod_cliente.valor_produto.setText(produto.getValor());
        viewholder_prod_cliente.total.setText(produto.getTotal());
        viewholder_prod_cliente.data.setText(produto.getData());
        viewholder_prod_cliente.ch_recebido.setChecked(produto.getRecebido());
        viewholder_prod_cliente.ch_devolvido.setChecked(produto.getDevolvido());

        /*viewholder_prod_cliente.recebido_parcial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recebido_parcialmente(viewholder_prod_cliente.getAdapterPosition());

            }
        });
*/
        viewholder_prod_cliente.ch_recebido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                estado_check_box(viewholder_prod_cliente.getAdapterPosition(), b);

            }
        });

        viewholder_prod_cliente.ch_devolvido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                estado_checkbox_devolvido(viewholder_prod_cliente.getAdapterPosition(), b);

            }
        });

        viewholder_prod_cliente.btn_excluir_prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                AlertDialog.Builder alert_excluir = new AlertDialog.Builder(context);
                alert_excluir.setMessage("Deseja realmente excluir o produto ?");

                alert_excluir.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        delete_categoria(viewholder_prod_cliente.getAdapterPosition(),view);

                    }
                }).setNegativeButton("Cancelar", null);

                alert_excluir.show();
            }
        });
    }

    /*public void recebido_parcialmente(int i) {

        final DocumentReference documentReference = getSnapshots().getSnapshot(i).getReference();

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                final Intent i_recebido_parcial = new Intent(context, RecebidoParcial.class);
                i_recebido_parcial.putExtra("id_recebido_parcial", documentReference.getId());
                context.startActivity(i_recebido_parcial);

            }
        });

    }*/

    public void delete_categoria(int i, final View view) {

        final DocumentReference documentReference = getSnapshots().getSnapshot(i).getReference();

        cl_recebido_parcial
                .whereEqualTo("id", documentReference.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        QuerySnapshot queryDocumentSnapshot = task.getResult();

                        if (!queryDocumentSnapshot.isEmpty()) {

                            Snackbar.make(view, "Há valores recebidos referente a esse produto", Snackbar.LENGTH_LONG)
                                    .show();

                        } else {

                            documentReference.delete();

                        }
                    }

                });

    }

    public void estado_check_box(int i, boolean b) {

        Map<String, Object> map = new HashMap<>();

        map.put("recebido", b);

        getSnapshots().getSnapshot(i).getReference().update(map);
    }

    public void estado_checkbox_devolvido(int i, boolean b) {

        Map<String, Object> map = new HashMap<>();

        map.put("devolvido", b);

        getSnapshots().getSnapshot(i).getReference().update(map);

    }

    public void estado_checkbox_recebido_parcial(int i, boolean b) {

        Map<String, Object> map = new HashMap<>();

        map.put("recebidoparcia", b);

        getSnapshots().getSnapshot(i).getReference().update(map);
    }

    public class Viewholder_prod_cliente extends RecyclerView.ViewHolder {

        TextView nome_produto, quantidade_produto, valor_produto, total, data;
        ImageButton btn_excluir_prod;;
        RadioButton ch_recebido, ch_devolvido;

        public Viewholder_prod_cliente(@NonNull View itemView) {
            super(itemView);

            nome_produto = itemView.findViewById(R.id.txt_nm_produto);
            quantidade_produto = itemView.findViewById(R.id.txt_qtd_produto);
            valor_produto = itemView.findViewById(R.id.vlr_produto);
            total = itemView.findViewById(R.id.txt_total);
            btn_excluir_prod = itemView.findViewById(R.id.btn_excluir_produto);
            data = itemView.findViewById(R.id.txt_data);
            ch_recebido = itemView.findViewById(R.id.ch_recebido);
            ch_devolvido = itemView.findViewById(R.id.ch_devolvido);

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

    public void setOnItemClicklistener(AdapterProdutosCliente.OnItemClickListener listener) {
        this.listener = listener;
    }

}




