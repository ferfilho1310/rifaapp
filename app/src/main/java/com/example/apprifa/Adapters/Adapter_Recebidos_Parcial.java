package com.example.apprifa.Adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprifa.Models.RecebidoParcial;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class Adapter_Recebidos_Parcial extends FirestoreRecyclerAdapter<RecebidoParcial, Adapter_Recebidos_Parcial.View_Holder_Recebido_parcial> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Adapter_Recebidos_Parcial(@NonNull FirestoreRecyclerOptions<RecebidoParcial> options) {
        super(options);
    }

    @NonNull
    @Override
    public View_Holder_Recebido_parcial onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }


    @Override
    protected void onBindViewHolder(@NonNull View_Holder_Recebido_parcial holder, int position, @NonNull RecebidoParcial model) {

    }


    public class View_Holder_Recebido_parcial extends RecyclerView.ViewHolder{


        public View_Holder_Recebido_parcial(@NonNull View itemView) {
            super(itemView);
        }
    }

}
