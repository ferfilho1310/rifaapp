package com.example.apprifa.Models;

public class Produto {

    private String nomedoproduto;
    private String quantidade;
    private String valor;

    public Produto(String nomedoproduto, String quantidade, String valor) {
        this.nomedoproduto = nomedoproduto;
        this.quantidade = quantidade;
        this.valor = valor;
    }

    public Produto() {
    }

    public String getNomedoproduto() {
        return nomedoproduto;
    }

    public void setNomedoproduto(String nomedoproduto) {
        this.nomedoproduto = nomedoproduto;
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
