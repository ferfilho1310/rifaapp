package br.com.medeve.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import br.com.medeve.R;
import br.com.medeve.Models.DataCobrancaVenda;
import br.com.medeve.ViewModels.DataVendasClienteViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class AdapterDataCobranca extends FirestoreRecyclerAdapter<DataCobrancaVenda, AdapterDataCobranca.ViewHolder_datas> {

    OnItemClickListener listener;
    Context context;

    DataVendasClienteViewModel dataVendasClienteViewModel;

    public AdapterDataCobranca(@NonNull FirestoreRecyclerOptions<DataCobrancaVenda> options, Context context,
                               DataVendasClienteViewModel dataVendasClienteViewModel) {
        super(options);
        this.context = context;
        this.dataVendasClienteViewModel = dataVendasClienteViewModel;
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

        viewHolder_datas.excluir_data.setOnClickListener(view -> {

            AlertDialog.Builder alert_datas = new AlertDialog.Builder(context);
            alert_datas.setMessage("Deseja realmente exlcuir a data ?");

            alert_datas.setPositiveButton("Ok", (dialogInterface, i1) ->
                    excluirDataVendaCliente(viewHolder_datas.getAdapterPosition())).setNegativeButton("Cancelar", null);

            alert_datas.show();
        });
    }

    public void excluirDataVendaCliente(int i) {
        final DocumentReference documentReference = getSnapshots().getSnapshot(i).getReference();
        dataVendasClienteViewModel.deleteDataVendaCliente(documentReference);

        dataVendasClienteViewModel.deleteDataVendaClienteMutableLiveData().observe((LifecycleOwner) context, aBoolean -> {
            if (aBoolean){
                Toast.makeText(context,"Data de venda e cobrança deletado",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context,"Você possui produtos vendidos nessa data",Toast.LENGTH_SHORT).show();
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

            itemView.setOnClickListener(view -> {

                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {

                    listener.onItemClick(getSnapshots().getSnapshot(position), position);
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
