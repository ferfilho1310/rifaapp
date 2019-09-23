package com.example.apprifa.Models;

public class Produto {

    private String nomedoproduto;
    private String quantidade;
    private String valor;
    private String total;
    private String data;

    public Produto(String nomedoproduto, String quantidade, String valor,String total, String data) {
        this.nomedoproduto = nomedoproduto;
        this.quantidade = quantidade;
        this.valor = valor;
        this.total = total;
        this.data = data;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
