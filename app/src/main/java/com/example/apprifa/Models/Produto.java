package com.example.apprifa.Models;

public class Produto {

    private String nomeproduto;
    private String quantidade;
    private String valor;

    public Produto(String nomeproduto, String quantidade, String valor) {
        this.nomeproduto = nomeproduto;
        this.quantidade = quantidade;
        this.valor = valor;
    }

    public Produto() {
    }

    public String getNomeproduto() {
        return nomeproduto;
    }

    public void setNomeproduto(String nomeproduto) {
        this.nomeproduto = nomeproduto;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}
