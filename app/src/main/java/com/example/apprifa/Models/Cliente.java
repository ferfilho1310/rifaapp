package com.example.apprifa.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Cliente implements Parcelable {

    private String id;
    private String local;
    private String Nome;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;


    public Cliente() {
    }

    protected Cliente(Parcel in) {
        Nome = in.readString();
        numero = in.readString();
        bairro = in.readString();
        cidade = in.readString();
        local = in.readString();
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

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
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

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Nome);
        parcel.writeString(local);
        parcel.writeString(numero);
        parcel.writeString(bairro);
        parcel.writeString(cidade);
    }

}
