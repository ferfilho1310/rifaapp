package br.com.medeve.Interfaces;

import br.com.medeve.Models.Usuario;

public interface IUsuarioDao {

    void cadastrarUsuairo(Usuario usuario);

    void entrarUsuario(Usuario usuario);

    void recuperarSenhaUsuario(Usuario usuario);
}
