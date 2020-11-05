package br.com.medeve.Dao;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import br.com.medeve.Activitys.EntrarUsuarioActView;
import br.com.medeve.Helpers.IntentHelper;
import br.com.medeve.Helpers.ProgressBarHelper;
import br.com.medeve.Interfaces.IUsuarioDao;
import br.com.medeve.Models.Usuario;

public class UsuarioDao implements IUsuarioDao {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    CollectionReference collectionUser = FirebaseFirestore.getInstance().collection("Users");

    private static UsuarioDao usuarioDao;

    public static synchronized UsuarioDao getInstance(){
        if(usuarioDao == null){
            usuarioDao = new UsuarioDao();
        }
        return usuarioDao;
    }

    @Override
    public void cadastrarUsuairo(final Usuario usuario, final Activity activity) {

        final ProgressBarHelper progressBarHelper = new ProgressBarHelper(activity);
        firebaseAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Map<String, String> map = new HashMap<>();
                    map.put("Nome", usuario.getNome());
                    map.put("E-mail", usuario.getEmail());
                    map.put("Senha", usuario.getSenha());
                    map.put("Confirmar Senha", usuario.getConfirmaSenha());
                    map.put("Sexo", usuario.getSexo());

                    Intent intent = new Intent(activity, EntrarUsuarioActView.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(intent);

                    collectionUser.add(map);

                    Toast.makeText(activity, "Usuário cadastrado com sucesso.", Toast.LENGTH_LONG).show();
                } else if (!task.isSuccessful()) {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        Toast.makeText(activity, "Senha inferior a 6 caracteres", Toast.LENGTH_SHORT).show();
                        progressBarHelper.dismiss();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        Toast.makeText(activity, "E-mail inválido", Toast.LENGTH_SHORT).show();
                        progressBarHelper.dismiss();
                    } catch (FirebaseAuthUserCollisionException e) {
                        Toast.makeText(activity, "Usuário já cadastrado", Toast.LENGTH_SHORT).show();
                        progressBarHelper.dismiss();
                    } catch (Exception e) {
                        Toast.makeText(activity, "Ops!Erro a cadastrar o usuário", Toast.LENGTH_SHORT).show();
                        progressBarHelper.dismiss();
                    }
                }
            }
        });
    }

    @Override
    public void entrarUsuario(Usuario usuario) {

    }

    @Override
    public void recuperarSenhaUsuario(Usuario usuario) {

    }

    @Override
    public void persistirUsuario(Class clazz, Activity activity) {
        if(firebaseAuth.getCurrentUser() != null){
            IntentHelper.getInstance().intentWithFinish(activity,clazz);
        }
    }

    @Override
    public void sair(Activity activity, Class clazz) {
        IntentHelper.getInstance().intentWithFinish(activity,clazz);
    }
}
