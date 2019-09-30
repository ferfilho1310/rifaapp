package com.example.apprifa.Adapters;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprifa.Controlers.DatasVendasCobranca;
import com.example.apprifa.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class Adapter_Data_Cobranca extends FirestoreRecyclerAdapter<DatasVendasCobranca, Adapter_Data_Cobranca.ViewHolder_datas> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Adapter_Data_Cobranca(@NonNull FirestoreRecyclerOptions<DatasVendasCobranca> options) {
        super(options);
    }

    @NonNull
    @Override
    public ViewHolder_datas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mostra_dados_datas,parent,false);

        return new ViewHolder_datas(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder_datas viewHolder_datas, int i, @NonNull DatasVendasCobranca datasVendasCobranca) {



    }

    public class ViewHolder_datas extends RecyclerView.ViewHolder{

        public ViewHolder_datas(@NonNull View itemView) {
            super(itemView);
        }
    }
}
