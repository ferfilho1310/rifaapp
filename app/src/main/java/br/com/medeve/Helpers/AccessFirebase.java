package br.com.medeve.Helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.com.medeve.R;
import br.com.medeve.Controlers.EntrarUsuario;
import br.com.medeve.Controlers.CadastroCliente;
import br.com.medeve.Models.Cliente;
import br.com.medeve.Retrofit.RetrofitInit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccessFirebase extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    CollectionReference firebaseFirestore = FirebaseFirestore.getInstance().collection("cadastro_clientes");
    CollectionReference db_prod_cliente = FirebaseFirestore.getInstance().collection("produtos_cliente");
    CollectionReference db_datas_cobranca = FirebaseFirestore.getInstance().collection("datas_cobranca");
    CollectionReference db_receb_partcial = FirebaseFirestore.getInstance().collection("recebido_partcial");
    CollectionReference db_users = FirebaseFirestore.getInstance().collection("Users");

    ProgressDialog progressDialog;

    EditText ed_nome, ed_endereco, ed_numero, ed_bairro, ed_cidade, ed_estado, ed_cep;

    public AccessFirebase() {

    }

    public void cadastro_cliente(View view, final Cliente cliente, final Activity context) {

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

                        if (response.isSuccessful()) {

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

                salva_clientes(cliente.getNome(), cliente.getLogradouro(), cliente.getNumero()
                        , cliente.getBairro(), cliente.getCidade(), cliente.getCep(), cliente.getEstado(),cliente.getNome().toUpperCase());


            }
        }).setNegativeButton("Cancelar", null);

        alrt_update_client.show();

    }

    public void  salva_recebido_parcial(String receb_parcial, String id_valor_recebido,String data_recebimento){

        Map<String,Object> map = new HashMap<>();

        map.put("id",id_valor_recebido);
        map.put("valor_recebido",receb_parcial);
        map.put("data",data_recebimento);

        db_receb_partcial.document(firebaseAuth.getUid()).collection("recebido_parcial").add(map);

    }

    public void salva_clientes(String nome, String enderecocliente, String numero, String bairro, String cidade, String cep, String estado,String nome_maiusculo) {

        Map<String, Object> map = new HashMap<>();

        map.put("nome", nome);
        map.put("nome_maiusculo", nome_maiusculo);
        map.put("logradouro", enderecocliente);
        map.put("numero", numero);
        map.put("bairro", bairro);
        map.put("cidade", cidade);
        map.put("cep", cep);
        map.put("estado", estado);

        firebaseFirestore.document(firebaseAuth.getUid()).collection("cliente").add(map);
        SetOptions.merge();
    }

    public void salva_produtos(String dia, String nomedoproduto, String quantidade, String valor, String total, String id, boolean recebido, boolean devolvido) {

        Map<String, Object> map = new HashMap<>();

        map.put("id", id);
        map.put("nomedoproduto", nomedoproduto);
        map.put("quantidade", quantidade);
        map.put("valor", valor);
        map.put("total", total);
        map.put("data", dia);
        map.put("recebido", recebido);
        map.put("devolvido", devolvido);

        db_prod_cliente.document(firebaseAuth.getUid()).collection("produtos").add(map);

    }

    public void data_cobranca(String id, String data_venda, String data_cobranca) {

        Map<String, Object> map = new HashMap<>();

        map.put("id_data", id);
        map.put("data_venda", data_venda);
        map.put("data_cobranca", data_cobranca);

        db_datas_cobranca.document(firebaseAuth.getUid()).collection("data_de_cobraca").add(map);

    }

    public void cadastrar_user(final String nome, final String email, final String senha,
                               final String senhaconfir, final String sexo, final Activity activity) {

        if (TextUtils.isEmpty(nome)) {
            Toast.makeText(activity, "Digite seu nome", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(activity, "Informe um e-mail.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(senha)) {
            Toast.makeText(activity, "Informe uma senha.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(senhaconfir)) {

            Toast.makeText(activity, "Confirme a senha", Toast.LENGTH_LONG).show();
            return;
        }

        if (senha.equals(senhaconfir)) {

            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Cadastrando...");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        Map<String, String> map = new HashMap<>();

                        map.put("Nome", nome);
                        map.put("E-mail", email);
                        map.put("Senha", senha);
                        map.put("Confirmar Senha", senhaconfir);
                        map.put("Sexo", sexo);

                        Intent intent = new Intent(activity, EntrarUsuario.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent);

                        db_users.add(map);

                        Toast.makeText(activity, "Usuário cadastrado com sucesso.", Toast.LENGTH_LONG).show();

                    } else if (!task.isSuccessful()) {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {

                            Toast.makeText(activity, "Senha inferior a 6 caracteres", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        } catch (FirebaseAuthInvalidCredentialsException e) {

                            Toast.makeText(activity, "E-mail inválido", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        } catch (FirebaseAuthUserCollisionException e) {

                            Toast.makeText(activity, "Usuário já cadastrado", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        } catch (Exception e) {

                            Toast.makeText(activity, "Ops!Erro a cadastrar o usuário", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                }

            });
        } else {

            Toast.makeText(activity, "As senhas estão diferentes.", Toast.LENGTH_LONG).show();
            return;

        }
    }

    public void persistir_usuer(Activity activity) {

        if (firebaseAuth.getCurrentUser() != null) {

            Intent intent = new Intent(activity, CadastroCliente.class);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    public void sign_out_firebase(Activity activity) {

        Intent intent = new Intent(activity, EntrarUsuario.class);
        activity.startActivity(intent);
        activity.finish();

        firebaseAuth.signOut();
    }

    public void entrar_firebase(final String email, String senha, final Activity activity) {

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(activity, "Digite seu e-mail", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(senha)) {
            Toast.makeText(activity, "Informe uma senha.", Toast.LENGTH_LONG).show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(activity);

        progressDialog.setMessage("Entrando...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                try {
                    if (task.isSuccessful()) {

                        Intent i_entrar_prof = new Intent(activity, CadastroCliente.class);
                        i_entrar_prof.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(i_entrar_prof);
                        activity.finish();

                        Toast.makeText(activity, "Login efetuado com sucesso", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(activity, "Erro ao efetuar o login. Verifique os dados digitados", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                    Toast.makeText(activity, "Ops! Ocorreu um erro inesperado.", Toast.LENGTH_LONG).show();
                }

                progressDialog.dismiss();
            }
        });
    }

    public void reset_senha(final String email, final Activity context) {

        if (TextUtils.isEmpty(email)) {

            Toast.makeText(context, "Informe um e-mail.", Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                try {
                    if (task.isSuccessful()) {

                        Toast.makeText(context, "Enviado e-mail para reset de senha para " + email, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(context, EntrarUsuario.class);
                        context.startActivity(intent);
                        context.finish();

                    } else {

                        Toast.makeText(context, "E-mail inválido", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                    Toast.makeText(context, "Erro ao enviar e-mail de recuperação:" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}

