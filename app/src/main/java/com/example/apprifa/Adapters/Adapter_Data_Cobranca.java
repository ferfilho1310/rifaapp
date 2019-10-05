package com.example.apprifa.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
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
    Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Adapter_Data_Cobranca(@NonNull FirestoreRecyclerOptions<DataCobrancaVenda> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder_datas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mostra_dados_datas, parent, false);

        return new ViewHolder_datas(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder_datas viewHolder_datas, int i, @NonNull DataCobrancaVenda datasVendasCobranca) {

        viewHolder_datas.datavenda.setText(datasVendasCobranca.getData_venda());
        viewHolder_datas.datacobranca.setText(datasVendasCobranca.getData_cobranca());

        viewHolder_datas.excluir_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert_datas = new AlertDialog.Builder(context);
                alert_datas.setMessage("Deseja realmente exlcuir a data ?");

                alert_datas.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        excluir_data(viewHolder_datas.getAdapterPosition());

                    }
                }).setNegativeButton("Cancelar", null);

                alert_datas.show();
            }
        });
    }

    public void excluir_data(int i) {

        getSnapshots().getSnapshot(i).getReference().delete();

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
