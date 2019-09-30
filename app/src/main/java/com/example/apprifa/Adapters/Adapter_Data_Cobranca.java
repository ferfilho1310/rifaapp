package com.example.apprifa.Adapters;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprifa.Controlers.DatasVendasCobranca;
import com.example.apprifa.Models.DataCobrancaVenda;
import com.example.apprifa.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class Adapter_Data_Cobranca extends FirestoreRecyclerAdapter<DataCobrancaVenda, Adapter_Data_Cobranca.ViewHolder_datas> {

    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Adapter_Data_Cobranca(@NonNull FirestoreRecyclerOptions<DataCobrancaVenda> options) {
        super(options);
    }

    @NonNull
    @Override
    public ViewHolder_datas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mostra_dados_datas, parent, false);

        return new ViewHolder_datas(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder_datas viewHolder_datas, int i, @NonNull DataCobrancaVenda datasVendasCobranca) {

        viewHolder_datas.datavenda.setText(datasVendasCobranca.getData_venda());
        viewHolder_datas.datacobranca.setText(datasVendasCobranca.getData_cobranca());

    }

    public class ViewHolder_datas extends RecyclerView.ViewHolder {

        TextView datacobranca, datavenda;

        public ViewHolder_datas(@NonNull View itemView) {
            super(itemView);

            datacobranca = itemView.findViewById(R.id.txt_data_cobranca);
            datavenda = itemView.findViewById(R.id.txt_data_venda);

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
