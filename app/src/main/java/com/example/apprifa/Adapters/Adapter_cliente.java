package com.example.apprifa.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprifa.Models.Cliente;
import com.example.apprifa.R;
import com.example.apprifa.Retrofit.RetrofitInit;
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


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_cliente extends FirestoreRecyclerAdapter<Cliente, Adapter_cliente.Viewholder_clientes> {

    OnItemClickListener listener;
    private Context context;
    private EditText ed_nome, ed_endereco, ed_numero, ed_bairro, ed_cidade, ed_estado, ed_cep;

    FirebaseAuth db_users = FirebaseAuth.getInstance();

    FirebaseFirestore db_datas = FirebaseFirestore.getInstance();
    CollectionReference cl_datas = db_datas.collection("datas_cobranca")
            .document(db_users.getUid())
            .collection("data_de_cobraca");


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
    protected void onBindViewHolder(@NonNull final Adapter_cliente.Viewholder_clientes viewholder_clientes, int i, @NonNull final Cliente cliente) {

        viewholder_clientes.nome.setText(cliente.getNome());
        viewholder_clientes.endereco_cli.setText(cliente.getLogradouro());
        viewholder_clientes.numero.setText(cliente.getNumero());
        viewholder_clientes.bairro.setText(cliente.getBairro());
        viewholder_clientes.cidade.setText(cliente.getCidade());
        viewholder_clientes.estado.setText(cliente.getEstado());

        viewholder_clientes.excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                AlertDialog.Builder alert_excluir = new AlertDialog.Builder(context);
                alert_excluir.setMessage("Deseja realmente excluir os dados do cliente ?");

                alert_excluir.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        delete_categoria(viewholder_clientes.getAdapterPosition(),view);


                    }
                }).setNegativeButton("Cancelar", null);

                alert_excluir.show();
            }
        });

        viewholder_clientes.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                atualiza_dados_cliente(viewholder_clientes, view, cliente);

            }
        });
    }

    public void atualiza_dados_cliente(final Adapter_cliente.Viewholder_clientes viewholder_clientes, View view, final Cliente cliente) {

        AlertDialog.Builder alrt_update_client = new AlertDialog.Builder(context);
        final View custom_layout = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_cad_clientes, null);
        alrt_update_client.setTitle("Informe os dados do cliente:");
        alrt_update_client.setView(custom_layout);

        ed_nome = custom_layout.findViewById(R.id.ed_nome);
        ed_endereco = custom_layout.findViewById(R.id.edend);
        ed_numero = custom_layout.findViewById(R.id.ed_numero);
        ed_bairro = custom_layout.findViewById(R.id.ed_bairro);
        ed_cidade = custom_layout.findViewById(R.id.ed_cidade);
        ed_estado = custom_layout.findViewById(R.id.ed_estado);
        ed_cep = custom_layout.findViewById(R.id.ed_cep);

        Button btn_cep = custom_layout.findViewById(R.id.btn_busca_cep);

        btn_cep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ed_cep.length() < 8 || ed_cep.length() > 8) {
                    Toast.makeText(context, "CEP inválido", Toast.LENGTH_LONG).show();
                    return;
                }

                Call<Cliente> call_cep = new RetrofitInit().getcep().cep(ed_cep.getText().toString());

                call_cep.enqueue(new Callback<Cliente>() {
                    @Override
                    public void onResponse(Call<Cliente> call, Response<Cliente> response) {

                        if (response.isSuccessful() && response != null) {

                            Cliente cliente_cep = response.body();

                            Log.d("Retrono WBC", response.toString());

                            String cl_endereco = cliente_cep.getLogradouro();
                            String cl_bairro = cliente_cep.getBairro();
                            String cl_cidade = cliente_cep.getCidade();
                            String cl_estado = cliente_cep.getEstado();

                            ed_endereco.setText(cl_endereco);
                            ed_bairro.setText(cl_bairro);
                            ed_cidade.setText(cl_cidade);
                            ed_estado.setText(cl_estado);

                        }
                    }

                    @Override
                    public void onFailure(Call<Cliente> call, Throwable t) {

                        Toast.makeText(context, "Erro ao consultar o CEP", Toast.LENGTH_LONG).show();
                        Log.e("Error", t.getMessage());
                    }
                });
            }
        });

        alrt_update_client.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                cliente.setNome(ed_nome.getText().toString());
                cliente.setLogradouro(ed_endereco.getText().toString());
                cliente.setNumero(ed_numero.getText().toString());
                cliente.setBairro(ed_bairro.getText().toString());
                cliente.setCidade(ed_cidade.getText().toString());
                cliente.setEstado(ed_estado.getText().toString());
                cliente.setCep(ed_cep.getText().toString());

                atualizada_dados_cliente_adapter(viewholder_clientes.getAdapterPosition(), cliente.getNome(), cliente.getLogradouro(), cliente.getNumero()
                        , cliente.getBairro(), cliente.getCidade(), cliente.getCep(), cliente.getEstado());

            }
        }).setNegativeButton("Cancelar", null);

        alrt_update_client.show();

    }

    public void delete_categoria(final int i, final View view) {

        final DocumentReference documentReference = getSnapshots().getSnapshot(i).getReference();

        cl_datas
                .whereEqualTo("id_data", documentReference.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        QuerySnapshot queryDocumentSnapshot = task.getResult();

                        if (!queryDocumentSnapshot.isEmpty()) {

                            Snackbar.make(view,"Há datas de vendas associadas a este cliente",Snackbar.LENGTH_LONG)
                                    .show();

                        } else {

                            documentReference.delete();
                        }
                    }

                });

         /* for (QueryDocumentSnapshot queryDocumentSnapshots : task.getResult()) {

                            Map<String, Object> map = new HashMap<>();

                            map.put("id_data", FieldValue.delete());
                            map.put("data_venda", FieldValue.delete());
                            map.put("data_cobranca", FieldValue.delete());

                            queryDocumentSnapshots.getReference().update(map);

                        }*/

        /*cl_clientes
                .whereEqualTo("id", documentReference1.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for (QueryDocumentSnapshot queryDocumentSnapshots : task.getResult()) {

                            Map<String, Object> map = new HashMap<>();

                            map.put("id", FieldValue.delete());
                            map.put("nomedoproduto", FieldValue.delete());
                            map.put("quantidade", FieldValue.delete());
                            map.put("valor", FieldValue.delete());
                            map.put("total", FieldValue.delete());
                            map.put("data", FieldValue.delete());
                            map.put("recebido", FieldValue.delete());
                            map.put("recebido_parcial", FieldValue.delete());
                            map.put("devolvido", FieldValue.delete());

                            queryDocumentSnapshots.getReference().update(map);

                        }
                    }
                });*/

    }

    public void atualizada_dados_cliente_adapter(int i, String nome, String enderecocliente, String numero, String bairro, String cidade, String cep, String estado) {

        Map<String, Object> map = new HashMap<>();

        map.put("nome", nome);
        map.put("logradouro", enderecocliente);
        map.put("numero", numero);
        map.put("bairro", bairro);
        map.put("cidade", cidade);
        map.put("cep", cep);
        map.put("estado", estado);

        getSnapshots().getSnapshot(i).getReference().update(map);
    }

    public class Viewholder_clientes extends RecyclerView.ViewHolder {

        TextView nome, endereco_cli, numero, bairro, cidade, estado;
        ImageButton excluir, editar;

        public Viewholder_clientes(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.txt_nome_cliente);
            endereco_cli = itemView.findViewById(R.id.txt_local);
            numero = itemView.findViewById(R.id.txt_numero);
            bairro = itemView.findViewById(R.id.txt_bairro);
            cidade = itemView.findViewById(R.id.txt_cidade);
            excluir = itemView.findViewById(R.id.btn_excluir_cliente);
            estado = itemView.findViewById(R.id.txt_estado);
            editar = itemView.findViewById(R.id.img_edit_cliente);

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
