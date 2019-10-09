package com.example.apprifa.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Cliente implements Parcelable {

    private String logradouro;
    private String nome;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String sexo;

    public Cliente( String logradouro, String nome, String numero, String bairro, String cidade, String estado, String cep, String sexo) {
        this.logradouro = logradouro;
        this.nome = nome;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.sexo = sexo;
    }

    public Cliente() {
    }

    protected Cliente(Parcel in) {
        logradouro = in.readString();
        nome = in.readString();
        numero = in.readString();
        bairro = in.readString();
        cidade = in.readString();
        estado = in.readString();
        cep = in.readString();
        sexo = in.readString();
    }

    public static final Creator<Cliente> CREATOR = new Creator<Cliente>() {
        @Override
        public Cliente createFromParcel(Parcel in) {
            return new Cliente(in);
        }

        @Override
        public Cliente[] newArray(int size) {
            return new Cliente[size];
        }
    };


    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(logradouro);
        parcel.writeString(nome);
        parcel.writeString(numero);
        parcel.writeString(bairro);
        parcel.writeString(cidade);
        parcel.writeString(estado);
        parcel.writeString(cep);
        parcel.writeString(sexo);
    }
}
