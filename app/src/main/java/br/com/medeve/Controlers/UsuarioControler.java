package br.com.medeve.Controlers;

import android.app.Activity;

import br.com.medeve.Dao.UsuarioDao;
import br.com.medeve.Interfaces.IUsuarioControler;
import br.com.medeve.Interfaces.IUsuarioDao;
import br.com.medeve.Models.Usuario;

public class UsuarioControler implements IUsuarioControler {

    private static UsuarioControler usuarioControler;
    private static IUsuarioDao iUsuarioDao;

    public static synchronized UsuarioControler getInstance() {
        if (usuarioControler == null) {
            usuarioControler = new UsuarioControler();
        }
        iUsuarioDao = new UsuarioDao();
        return usuarioControler;
    }

    @Override
    public void entrar(Usuario usuario) {
        iUsuarioDao.entrarUsuario(usuario);
    }

    @Override
    public void cadastrar(Usuario usuario) {
        iUsuarioDao.cadastrarUsuairo(usuario);
    }

    @Override
    public void recuperarSenha(Usuario usuario) {
        iUsuarioDao.recuperarSenhaUsuario(usuario);
    }

    @Override
    public void persistirUsuario(Activity activity, Class clazz) {
        iUsuarioDao.persistirUsuario(clazz, activity);
    }

    @Override
    public void sair(Activity activity, Class clazz) {
        iUsuarioDao.sair(activity, clazz);
    }
}
