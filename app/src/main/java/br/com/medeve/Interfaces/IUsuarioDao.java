package br.com.medeve.Interfaces;

import android.app.Activity;

import br.com.medeve.Models.Usuario;

public interface IUsuarioDao {

    int cadastrarUsuairo(Usuario usuario);

    int entrarUsuario(Usuario usuario);

    void recuperarSenhaUsuario(Usuario usuario);

    void persistirUsuario(Class clazz, Activity activity);

    void sair(Activity activity, Class clazz);
}