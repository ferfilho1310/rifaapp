package br.com.medeve.Interfaces;

import br.com.medeve.Models.Cliente;
import br.com.medeve.Models.Usuario;

public interface IUsuarioControler {

    void entrar(Usuario usuario);

    void cadastrar(Usuario usuario);

    void recuperarSenha(Usuario usuario);
}
