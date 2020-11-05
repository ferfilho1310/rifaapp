package br.com.medeve.Interfaces;

import android.app.Activity;

import br.com.medeve.Models.Cliente;
import br.com.medeve.Models.Usuario;

public interface IUsuarioControler {

    void entrar(Usuario usuario);

    void cadastrar(Usuario usuario, Activity activity);

    void recuperarSenha(Usuario usuario);

    void persistirUsuario(Activity activity, Class clazz);

    void sair(Activity activity, Class clazz);
}
