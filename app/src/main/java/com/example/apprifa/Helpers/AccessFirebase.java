package com.example.apprifa.Helpers;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apprifa.Controlers.EntrarUsuario;
import com.example.apprifa.Controlers.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AccessFirebase extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    CollectionReference firebaseFirestore = FirebaseFirestore.getInstance().collection("cadastro de clientes");
    CollectionReference db_prod_cliente = FirebaseFirestore.getInstance().collection("produtos do cliente");
    CollectionReference db_users = FirebaseFirestore.getInstance().collection("Users");
    /*DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference db_users = FirebaseDatabase.getInstance().getReference().child("Users");
*/


    public AccessFirebase() {

    }

    public void salva_clientes(String nome, String endereco, String numero, String bairro, String cidade) {

        Map<String, String> map_categ_serv = new HashMap<>();

        map_categ_serv.put("nome", nome);
        map_categ_serv.put("endereco", endereco);
        map_categ_serv.put("numero", numero);
        map_categ_serv.put("bairro", bairro);
        map_categ_serv.put("cidade", cidade);

        /*databaseReference.child(firebaseAuth.getUid()).child("Serviços").push().setValue(map_categ_serv);*/

        firebaseFirestore.document(firebaseAuth.getUid()).collection("cliente").add(map_categ_serv);
    }

    public void salva_produtos(String nome_do_produto, String quantidade, String valor, String id) {

        Map<String, String> map_func = new HashMap<>();

        map_func.put("id", id);
        map_func.put("nome do produto", nome_do_produto);
        map_func.put("quantidade", quantidade);
        map_func.put("valor", valor);

        db_prod_cliente.document(firebaseAuth.getUid()).collection("produtos").add(map_func);

    }

    /*public void salva_especialidades(String espec, String valor) {
        Map<String, String> map = new HashMap<>();
        map.put("Serviços", espec);
        map.put("Valor do serviço", valor);
        databaseReference.child(firebaseAuth.getUid()).child("Categorias").child("Cabelos")
                .push().setValue(map);
    }*/

    public void cadastrar_user(final String nome, final String email, final String senha, final String senhaconfir, final String sexo, final Activity activity) {

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

        if (senha.length() < 6) {

            Toast.makeText(activity, "Senha inferior a 6 caracteres.", Toast.LENGTH_LONG).show();
            return;
        }

        if (senha.equals(senhaconfir)) {

            try {

                firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Map<String, String> map = new HashMap<>();

                            map.put("Nome:", nome);
                            map.put("E-mail:", email);
                            map.put("Senha:", senha);
                            map.put("Confirmar Senha:", senhaconfir);
                            map.put("Sexo:", sexo);

                            Intent intent = new Intent(activity, MainActivity.class);
                            activity.startActivity(intent);
                            activity.finish();

                            db_users.add(map);

                            Toast.makeText(activity, "Usuário cadastrado com sucesso.", Toast.LENGTH_LONG).show();

                        }
                    }
                });

            } catch (Exception e) {
                Toast.makeText(activity, "Ops! Ocorreu  um erro ao cadastrar o usuário", Toast.LENGTH_LONG).show();
                Log.d("Erro", "Erro Cadastro de usuário: " + e);
            }
        } else {

            Toast.makeText(activity, "As senhas estão diferentes.", Toast.LENGTH_LONG).show();
            return;

        }
    }

    public void persistir_usuer(Activity activity) {

        if (firebaseAuth.getCurrentUser() != null) {

            Intent intent = new Intent(activity, MainActivity.class);
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


        firebaseAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                try {
                    if (task.isSuccessful()) {

                        Intent i_entrar_prof = new Intent(activity, MainActivity.class);
                        activity.startActivity(i_entrar_prof);
                        activity.finish();

                        Toast.makeText(activity, "Login efetuado com sucesso", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(activity, "Erro ao efetuar o login. Verifique os dados digitados", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                    Toast.makeText(activity, "Ops! Ocorreu um erro inesperado.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}

