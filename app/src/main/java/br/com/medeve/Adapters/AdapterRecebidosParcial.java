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
import br.com.medeve.Models.RecebidoParcial;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdapterRecebidosParcial extends FirestoreRecyclerAdapter<RecebidoParcial, AdapterRecebidosParcial.View_Holder_Recebido_parcial> {

    Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdapterRecebidosParcial(@NonNull FirestoreRecyclerOptions<RecebidoParcial> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public View_Holder_Recebido_parcial onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mostra_dados_db_recebido_parcial, parent, false);

        return new View_Holder_Recebido_parcial(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final View_Holder_Recebido_parcial holder, int position, @NonNull RecebidoParcial model) {

        holder.txt_recebido_parcial.setText(model.getValor_recebido());
        holder.txt_data_recebido.setText(model.getData());

        holder.btn_img_recebido_parcial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                AlertDialog.Builder alrt_excluir_recebido = new AlertDialog.Builder(context);
                alrt_excluir_recebido.setTitle("Deseja realmente excluir o valor recebido ?");

                alrt_excluir_recebido.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        delete_recebido(holder.getAdapterPosition());

                    }
                }).setNegativeButton("Cancelar", null);

                alrt_excluir_recebido.show();
            }
        });

    }

    public void delete_recebido(int i) {

        getSnapshots().getSnapshot(i).getReference().delete();

    }

    public class View_Holder_Recebido_parcial extends RecyclerView.ViewHolder {

        TextView txt_recebido_parcial, txt_data_recebido;
        ImageButton btn_img_recebido_parcial;

        public View_Holder_Recebido_parcial(@NonNull View itemView) {
            super(itemView);

            txt_data_recebido = itemView.findViewById(R.id.txt_data_recebimento);
            txt_recebido_parcial = itemView.findViewById(R.id.txt_valor_recebido);
            btn_img_recebido_parcial = itemView.findViewById(R.id.btn_img_delete_recebido);



        }


    }

}
