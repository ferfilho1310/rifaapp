package com.example.apprifa.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprifa.Models.Produto;
import com.example.apprifa.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class Adapter_produtos_cliente extends FirestoreRecyclerAdapter<Produto, Adapter_produtos_cliente.Viewholder_prod_cliente> {


    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Adapter_produtos_cliente(@NonNull FirestoreRecyclerOptions<Produto> options) {
        super(options);
    }

    @NonNull
    @Override
    public Viewholder_prod_cliente onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mostra_dados_db_produto_cliente, parent, false);

        return new Viewholder_prod_cliente(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull Viewholder_prod_cliente viewholder_prod_cliente, int i, @NonNull Produto produto) {

        viewholder_prod_cliente.quantidade_produto.setText(produto.getQuantidade());
        viewholder_prod_cliente.valor_produto.setText(produto.getValor());
        viewholder_prod_cliente.nome_produto.setText(produto.getNomeproduto());

    }

    public void delete_categoria(int i) {

        getSnapshots().getSnapshot(i).getReference().delete();
    }


    class Viewholder_prod_cliente extends RecyclerView.ViewHolder {

        TextView nome_produto, quantidade_produto, valor_produto;

        public Viewholder_prod_cliente(@NonNull View itemView) {
            super(itemView);

            nome_produto = itemView.findViewById(R.id.txt_nm_produto);
            quantidade_produto = itemView.findViewById(R.id.txt_qtd_produto);
            valor_produto = itemView.findViewById(R.id.vlr_produto);

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




