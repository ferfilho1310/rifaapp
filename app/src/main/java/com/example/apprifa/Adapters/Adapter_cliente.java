package com.example.apprifa.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprifa.Models.Cliente;
import com.example.apprifa.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter_cliente extends FirestoreRecyclerAdapter<Cliente, Adapter_cliente.Viewholder_clientes> implements Filterable {

    private OnItemClickListener listener;
    List<Cliente> list_client;
    List<Cliente> list_client_filter;
    Context context;

    public Adapter_cliente(@NonNull FirestoreRecyclerOptions<Cliente> options, List<Cliente> list_client) {
        super(options);
        this.list_client = list_client;
        list_client_filter = new ArrayList<>();
    }

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Adapter_cliente(@NonNull FirestoreRecyclerOptions<Cliente> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter_cliente.Viewholder_clientes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mostra_dados_db_cliente, parent, false);

        return new Viewholder_clientes(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final Adapter_cliente.Viewholder_clientes viewholder_clientes, int i, @NonNull Cliente cliente) {

        viewholder_clientes.nome.setText(cliente.getNome());
        viewholder_clientes.endereco_cli.setText(cliente.getLocal());
        viewholder_clientes.numero.setText(cliente.getNumero());
        viewholder_clientes.bairro.setText(cliente.getBairro());
        viewholder_clientes.cidade.setText(cliente.getCidade());
        viewholder_clientes.estado.setText(cliente.getEstado());

        viewholder_clientes.excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert_excluir = new AlertDialog.Builder(context);
                alert_excluir.setMessage("Deseja realmente excluir o cliente ?");

                alert_excluir.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        delete_categoria(viewholder_clientes.getAdapterPosition());

                    }
                }).setNegativeButton("Cancelar", null);

                alert_excluir.show();
            }
        });

        viewholder_clientes.atualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = getSnapshots().getSnapshot(viewholder_clientes.getAdapterPosition()).getId();

            }
        });
    }

    public void delete_categoria(int i) {

        getSnapshots().getSnapshot(i).getReference().delete();
    }





    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                List<Cliente> ls_client = new ArrayList<>();

                if (charSequence == null || charSequence.length() == 0) {

                    ls_client.addAll(list_client);
                } else {
                    String filter = charSequence.toString().toLowerCase().trim();

                    for (Cliente cliente : list_client) {

                        if (cliente.getNome().toLowerCase().contains(filter)) {

                            ls_client.add(cliente);
                        }
                    }

                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = ls_client;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                list_client.clear();
                list_client.addAll((List) filterResults.values);
                notifyDataSetChanged();

            }
        };

    }

    public class Viewholder_clientes extends RecyclerView.ViewHolder {

        public TextView nome, endereco_cli, numero, bairro, cidade, estado;
        Button excluir, atualizar;

        public Viewholder_clientes(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.txt_nome_cliente);
            endereco_cli = itemView.findViewById(R.id.txt_local);
            numero = itemView.findViewById(R.id.txt_numero);
            bairro = itemView.findViewById(R.id.txt_bairro);
            cidade = itemView.findViewById(R.id.txt_cidade);
            excluir = itemView.findViewById(R.id.btn_excluir_cliente);
            atualizar = itemView.findViewById(R.id.btn_atualizar_cliente);
            estado = itemView.findViewById(R.id.txt_estado);

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
