package br.com.medeve.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import br.com.medeve.Models.Cliente;
import br.com.medeve.R;
import br.com.medeve.ViewModels.CepViewModel;
import br.com.medeve.ViewModels.ClienteViewModel;

public class AdapterCliente extends FirestoreRecyclerAdapter<Cliente, AdapterCliente.ViewHolderCliente> {

    OnItemClickListener listener;
    private Context context;
    private CepViewModel cepViewModel;
    private ClienteViewModel clienteViewModel;
    private EditText ed_nome, ed_endereco, ed_numero, ed_bairro, ed_cidade, ed_estado, ed_cep, ed_telefone;

    public AdapterCliente(@NonNull FirestoreRecyclerOptions<Cliente> options,
                          Context context,
                          CepViewModel cepViewModel,
                          ClienteViewModel clienteViewModel) {
        super(options);
        this.context = context;
        this.cepViewModel = cepViewModel;
        this.clienteViewModel = clienteViewModel;
    }

    @NonNull
    @Override
    public ViewHolderCliente onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view_normal = LayoutInflater.from(parent.getContext()).inflate(R.layout.dados_cliente_item, parent, false);
        return new ViewHolderCliente(view_normal);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolderCliente viewHolderCliente, int i, @NonNull final Cliente cliente) {

        viewHolderCliente.nome.setText(cliente.getNome());
        viewHolderCliente.endereco_cli.setText(cliente.getLogradouro());
        viewHolderCliente.numero.setText(cliente.getNumero());
        viewHolderCliente.bairro.setText(cliente.getBairro());
        viewHolderCliente.cidade.setText(cliente.getCidade());
        viewHolderCliente.estado.setText(cliente.getEstado());
        viewHolderCliente.telefone.setText(cliente.getTelefone());

        viewHolderCliente.excluir.setOnClickListener(view -> {

            AlertDialog.Builder alertDialogExcluir = new AlertDialog.Builder(context);
            alertDialogExcluir.setMessage("Deseja realmente excluir os dados do cliente ?");

            alertDialogExcluir.setPositiveButton("Ok", (dialogInterface, i1) ->
                    deleteCliente(viewHolderCliente.getAdapterPosition()))
                    .setNegativeButton("Cancelar", null);

            alertDialogExcluir.show();
        });

        viewHolderCliente.editar.setOnClickListener(view -> atualizaDadosCliente(viewHolderCliente, view, cliente));
    }


    public void atualizaDadosCliente(final ViewHolderCliente viewHolderCliente, View view, final Cliente cliente) {

        AlertDialog.Builder atualizarDadosClienteDialog = new AlertDialog.Builder(context);
        final View inflateLayoutDialogAtualizaDadosCliente = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_cad_clientes, null);
        atualizarDadosClienteDialog.setTitle("Quais dados do(a) " + cliente.getNome() + " serão atualizados ? ");
        atualizarDadosClienteDialog.setView(inflateLayoutDialogAtualizaDadosCliente);

        ed_nome = inflateLayoutDialogAtualizaDadosCliente.findViewById(R.id.edNomeClienteDialogAtualizar);
        ed_endereco = inflateLayoutDialogAtualizaDadosCliente.findViewById(R.id.edEnderecoDialogAtualizar);
        ed_numero = inflateLayoutDialogAtualizaDadosCliente.findViewById(R.id.ed_numeroDialogAtualizar);
        ed_bairro = inflateLayoutDialogAtualizaDadosCliente.findViewById(R.id.edBairroDialogAtualizar);
        ed_cidade = inflateLayoutDialogAtualizaDadosCliente.findViewById(R.id.edCidadeDialogAtualizar);
        ed_estado = inflateLayoutDialogAtualizaDadosCliente.findViewById(R.id.edEstadoDialogAtualizar);
        ed_cep = inflateLayoutDialogAtualizaDadosCliente.findViewById(R.id.ed_cepDialogAtualizar);
        ed_telefone = inflateLayoutDialogAtualizaDadosCliente.findViewById(R.id.ed_telefoneDialogAtualizar);

        //preenchendo dialog com dados antigos do cliente
        ed_nome.setText(cliente.getNome());
        ed_endereco.setText(cliente.getLogradouro());
        ed_numero.setText(cliente.getNumero());
        ed_bairro.setText(cliente.getBairro());
        ed_cidade.setText(cliente.getCidade());
        ed_estado.setText(cliente.getEstado());
        ed_cep.setText(cliente.getCep());
        ed_telefone.setText(cliente.getTelefone());

        Button btnCep = inflateLayoutDialogAtualizaDadosCliente.findViewById(R.id.btnBuscarCepDialogAtualizar);

        btnCep.setOnClickListener(v ->
                cepViewModel.getCep(ed_cep.getText().toString()));

        cepViewModel.getCelClienteObserver().observe((LifecycleOwner) context, cep -> {

            ed_bairro.setText(cep.getBairro());
            ed_cidade.setText(cep.getBairro());
            ed_endereco.setText(cep.getLogradouro());
            ed_estado.setText(cep.getUf());

        });

        atualizarDadosClienteDialog.setPositiveButton("OK", null)
                .setNegativeButton("Cancelar", null);

        final AlertDialog btnOkDialog = atualizarDadosClienteDialog.create();
        btnOkDialog.setOnShowListener(dialogInterface -> {

            Button btnOkDadosClienteDialog = btnOkDialog.getButton(AlertDialog.BUTTON_POSITIVE);

            btnOkDadosClienteDialog.setOnClickListener(view1 -> {

                cliente.setNome(ed_nome.getText().toString());
                cliente.setLogradouro(ed_endereco.getText().toString());
                cliente.setNumero(ed_numero.getText().toString());
                cliente.setBairro(ed_bairro.getText().toString());
                cliente.setCidade(ed_cidade.getText().toString());
                cliente.setEstado(ed_estado.getText().toString());
                cliente.setCep(ed_cep.getText().toString());
                cliente.setTelefone(ed_telefone.getText().toString());

                if (ed_nome.getText().length() == 0) {
                    ed_nome.setError("Informe o novo nome");
                } else if (ed_endereco.getText().length() == 0) {
                    ed_endereco.setError("Informe o novo endereço");
                } else if (ed_numero.getText().length() == 0) {
                    ed_numero.setError("Informe o novo número");
                } else if (ed_bairro.getText().length() == 0) {
                    ed_bairro.setError("Informe o novo bairro");
                } else if (ed_cidade.getText().length() == 0) {
                    ed_cidade.setError("Informe a nova cidade");
                } else if (ed_estado.getText().length() == 0) {
                    ed_estado.setError("Informe o novo estado");
                } else if (ed_telefone.getText().length() == 0) {
                    ed_telefone.setError("Informe o novo telefone");
                } else {
                    atualizaDadosClienteAdapter(viewHolderCliente.getAdapterPosition(), cliente);
                    dialogInterface.dismiss();
                }
            });
        });

        btnOkDialog.show();
    }

    public void deleteCliente(final int i) {

        final DocumentReference documentReference = getSnapshots().getSnapshot(i).getReference();

        clienteViewModel.deleteUsuario(documentReference);
        clienteViewModel.deleteClienteMutableLiveData().observe((LifecycleOwner) context, aBoolean -> {
            if (aBoolean){
                Toast.makeText(context,"Cliente deletado",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context,"Cliente possui contas em aberto",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void atualizaDadosClienteAdapter(int i, Cliente cliente) {

        Map<String, Object> map = new HashMap<>();

        map.put("nome", cliente.getNome());
        map.put("nome_maiusculo", cliente.getBairro().toUpperCase());
        map.put("logradouro", cliente.getLogradouro());
        map.put("numero", cliente.getNumero());
        map.put("bairro", cliente.getBairro());
        map.put("cidade", cliente.getCidade());
        map.put("cep", cliente.getCep());
        map.put("estado", cliente.getEstado());
        map.put("telefone", cliente.getTelefone());

        getSnapshots().getSnapshot(i).getReference().set(map, SetOptions.merge());
    }


    public class ViewHolderCliente extends RecyclerView.ViewHolder {

        TextView nome, endereco_cli, numero, bairro, cidade, estado, telefone;
        ImageButton excluir, editar;

        public ViewHolderCliente(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.txt_nome_cliente);
            endereco_cli = itemView.findViewById(R.id.txt_local);
            numero = itemView.findViewById(R.id.txt_numero);
            bairro = itemView.findViewById(R.id.txt_bairro);
            cidade = itemView.findViewById(R.id.txt_cidade);
            excluir = itemView.findViewById(R.id.btn_excluir_cliente);
            estado = itemView.findViewById(R.id.txt_estado);
            editar = itemView.findViewById(R.id.img_edit_cliente);
            telefone = itemView.findViewById(R.id.txt_telefone);

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
