package br.com.medeve.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class DataCobrancaVenda implements Parcelable {

    private String data_venda;
    private String data_cobranca;
    private String id;

    public DataCobrancaVenda() {
    }

    public DataCobrancaVenda(String data_venda, String data_cobranca, String id) {
        this.data_venda = data_venda;
        this.data_cobranca = data_cobranca;
        this.id = id;
    }

    protected DataCobrancaVenda(Parcel in) {
        data_venda = in.readString();
        data_cobranca = in.readString();
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(data_venda);
        dest.writeString(data_cobranca);
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DataCobrancaVenda> CREATOR = new Creator<DataCobrancaVenda>() {
        @Override
        public DataCobrancaVenda createFromParcel(Parcel in) {
            return new DataCobrancaVenda(in);
        }

        @Override
        public DataCobrancaVenda[] newArray(int size) {
            return new DataCobrancaVenda[size];
        }
    };

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
