package br.com.medeve.Models;

public class Usuario {

    private String nome;
    private String email;
    private String senha;
    private String confirmaSenha;

    public Usuario() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }

    public boolean nomeVazio(){
        boolean nomeVazio = false;
        if(getNome().isEmpty()){
            nomeVazio = true;
        }
        return nomeVazio;
    }

    public boolean emailVazio(){
        boolean emailVazio = false;
        if(getEmail().isEmpty()){
            emailVazio = true;
        }
        return emailVazio;
    }

    public boolean senhaVazia(){
        boolean senhaVazia = false;
        if(getSenha().isEmpty()){
            senhaVazia = true;
        }
        return senhaVazia;
    }
}
