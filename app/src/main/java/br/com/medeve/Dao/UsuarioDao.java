package br.com.medeve.Dao;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;

import br.com.medeve.Helpers.IntentHelper;
import br.com.medeve.Interfaces.IUsuarioDao;
import br.com.medeve.Models.Usuario;

public class UsuarioDao implements IUsuarioDao {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private static UsuarioDao usuarioDao;

    public static synchronized UsuarioDao getInstance(){
        if(usuarioDao == null){
            usuarioDao = new UsuarioDao();
        }
        return usuarioDao;
    }

    @Override
    public void cadastrarUsuairo(Usuario usuario) {

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
