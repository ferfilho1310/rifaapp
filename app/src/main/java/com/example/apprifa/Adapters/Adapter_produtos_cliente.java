package com.example.apprifa.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprifa.Controlers.ProdutosCliente;
import com.example.apprifa.Models.Produto;
import com.example.apprifa.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.*;

public class Adapter_produtos_cliente extends FirestoreRecyclerAdapter<Produto, Adapter_produtos_cliente.Viewholder_prod_cliente> {


    private OnItemClickListener listener;
    Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public Adapter_produtos_cliente(@NonNull FirestoreRecyclerOptions<Produto> options, Context context) {
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
        viewholder_prod_cliente.ch_receb_parcial.setOnCheckedChangeListener(null);
        viewholder_prod_cliente.ch_devolvido.setOnCheckedChangeListener(null);

        viewholder_prod_cliente.nome_produto.setText(produto.getNomedoproduto());
        viewholder_prod_cliente.quantidade_produto.setText(produto.getQuantidade());
        viewholder_prod_cliente.valor_produto.setText(produto.getValor());
        viewholder_prod_cliente.total.setText(produto.getTotal());
        viewholder_prod_cliente.data.setText(produto.getData());
        viewholder_prod_cliente.ch_recebido.setChecked(produto.getRecebido());
        viewholder_prod_cliente.ch_receb_parcial.setChecked(produto.getRecebido_parcial());
        viewholder_prod_cliente.ch_devolvido.setChecked(produto.getDevolvido());

        viewholder_prod_cliente.ch_recebido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                estado_check_box(viewholder_prod_cliente.getAdapterPosition(), b);

            }
        });

        viewholder_prod_cliente.ch_receb_parcial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                estado_checkbox_receb_parcial(viewholder_prod_cliente.getAdapterPosition(), b);

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
            public void onClick(View view) {

                AlertDialog.Builder alert_excluir = new AlertDialog.Builder(context);
                alert_excluir.setMessage("Deseja realmente excluir o produto ?");

                alert_excluir.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        delete_categoria(viewholder_prod_cliente.getAdapterPosition());

                    }
                }).setNegativeButton("Cancelar", null);

                alert_excluir.show();
            }
        });
    }

    public void delete_categoria(int i) {

        getSnapshots().getSnapshot(i).getReference().delete();

    }

    public void estado_check_box(int i, boolean b) {

        Map<String, Object> map = new HashMap<>();

        map.put("recebido", b);

        getSnapshots().getSnapshot(i).getReference().update(map);
    }

    public void estado_checkbox_receb_parcial(int i, boolean b) {

        Map<String, Object> map = new HashMap<>();

        map.put("recebido_parcial", b);

        getSnapshots().getSnapshot(i).getReference().update(map);

    }

    public void estado_checkbox_devolvido(int i, boolean b) {

        Map<String, Object> map = new HashMap<>();

        map.put("devolvido", b);

        getSnapshots().getSnapshot(i).getReference().update(map);

    }


    public class Viewholder_prod_cliente extends RecyclerView.ViewHolder {

        TextView nome_produto, quantidade_produto, valor_produto, total, data;
        ImageButton btn_excluir_prod;
        RadioButton ch_recebido, ch_receb_parcial, ch_devolvido;

        public Viewholder_prod_cliente(@NonNull View itemView) {
            super(itemView);

            nome_produto = itemView.findViewById(R.id.txt_nm_produto);
            quantidade_produto = itemView.findViewById(R.id.txt_qtd_produto);
            valor_produto = itemView.findViewById(R.id.vlr_produto);
            total = itemView.findViewById(R.id.txt_total);
            btn_excluir_prod = itemView.findViewById(R.id.btn_excluir_produto);
            data = itemView.findViewById(R.id.txt_data);
            ch_recebido = itemView.findViewById(R.id.ch_recebido);
            ch_receb_parcial = itemView.findViewById(R.id.ch_receb_parcial);
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

    public void setOnItemClicklistener(OnItemClickListener listener) {
        this.listener = listener;
    }
}




