package com.example.apprifa.Models;

public class Usuario {

    private String nome;
    private String email;
    private String senha;
    private String confirmaseha;
    private String sexo;

    public Usuario(String nome, String email, String senha, String confirmaseha, String sexo) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.confirmaseha = confirmaseha;
        this.sexo = sexo;
    }

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

    public String getConfirmaseha() {
        return confirmaseha;
    }

    public void setConfirmaseha(String confirmaseha) {
        this.confirmaseha = confirmaseha;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
}
