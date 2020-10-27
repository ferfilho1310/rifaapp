package br.com.medeve.Dao;

import br.com.medeve.Interfaces.IUsuarioDao;
import br.com.medeve.Models.Usuario;

public class UsuarioDao implements IUsuarioDao {

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
}
