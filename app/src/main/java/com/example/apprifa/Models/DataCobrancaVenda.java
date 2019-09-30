package com.example.apprifa.Models;

public class DataCobrancaVenda {

    private String data_venda;
    private String data_cobranca;

    public DataCobrancaVenda() {
    }

    public DataCobrancaVenda(String data_venda, String data_cobranca) {
        this.data_venda = data_venda;
        this.data_cobranca = data_cobranca;
    }

    public String getData_venda() {
        return data_venda;
    }

    public void setData_venda(String data_venda) {
        this.data_venda = data_venda;
    }

    public String getData_cobranca() {
        return data_cobranca;
    }

    public void setData_cobranca(String data_cobranca) {
        this.data_cobranca = data_cobranca;
    }
}
