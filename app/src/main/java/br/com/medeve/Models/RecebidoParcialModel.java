package br.com.medeve.Models;

public class RecebidoParcialModel {

    private String data;
    private String valor_recebido;

    public RecebidoParcialModel(String data, String valor_recebido) {
        this.data = data;
        this.valor_recebido = valor_recebido;
    }

    public RecebidoParcialModel() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getValor_recebido() {
        return valor_recebido;
    }

    public void setValor_recebido(String valor_recebido) {
        this.valor_recebido = valor_recebido;
    }
}
