package br.com.medeve.Models;

public class Usuario {

    private String nome;
    private String email;
    private String senha;
    private String confirmaSenha;
    private String sexo;

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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
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

    public boolean confirmarSenhaVazia(){
        boolean confirmaSenhaVazia = false;
        if(getConfirmaSenha().isEmpty()){
            confirmaSenhaVazia = true;
        }
        return confirmaSenhaVazia;
    }
}
