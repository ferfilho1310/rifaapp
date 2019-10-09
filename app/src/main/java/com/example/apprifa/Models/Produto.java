package com.example.apprifa.Models;

public class Produto {

    private String nomedoproduto;
    private String quantidade;
    private String valor;
    private String total;
    private String data;
    private Boolean recebido;
    private Boolean recebido_parcial;
    private Boolean devolvido;

    public Produto(String nomedoproduto, String quantidade, String valor,String total, String data, Boolean recebido,Boolean recebido_parcial, Boolean devolvido) {
        this.nomedoproduto = nomedoproduto;
        this.quantidade = quantidade;
        this.valor = valor;
        this.total = total;
        this.data = data;
        this.recebido = recebido;
        this.recebido_parcial = recebido_parcial;
        this.devolvido = devolvido;

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

    public Boolean getRecebido() {
        return recebido;
    }

    public void setRecebido(Boolean recebido) {
        this.recebido = recebido;
    }

    public Boolean getRecebido_parcial() {
        return recebido_parcial;
    }

    public void setRecebido_parcial(Boolean recebido_parcial) {
        this.recebido_parcial = recebido_parcial;
    }

    public Boolean getDevolvido() {
        return devolvido;
    }

    public void setDevolvido(Boolean devolvido) {
        this.devolvido = devolvido;
    }
}
