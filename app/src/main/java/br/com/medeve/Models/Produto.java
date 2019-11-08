package br.com.medeve.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Produto implements Parcelable {

    private String nomedoproduto;
    private String quantidade;
    private String valor;
    private String total;
    private String data;
    private Boolean recebido;
    private Boolean devolvido;

    public Produto(String nomedoproduto, String quantidade, String valor,String total, String data, Boolean recebido, Boolean devolvido) {
        this.nomedoproduto = nomedoproduto;
        this.quantidade = quantidade;
        this.valor = valor;
        this.total = total;
        this.data = data;
        this.recebido = recebido;
        this.devolvido = devolvido;

    }

    public Produto() {
    }

    protected Produto(Parcel in) {
        nomedoproduto = in.readString();
        quantidade = in.readString();
        valor = in.readString();
        total = in.readString();
        data = in.readString();
        byte tmpRecebido = in.readByte();
        recebido = tmpRecebido == 0 ? null : tmpRecebido == 1;
        byte tmpDevolvido = in.readByte();
        devolvido = tmpDevolvido == 0 ? null : tmpDevolvido == 1;
    }

    public static final Creator<Produto> CREATOR = new Creator<Produto>() {
        @Override
        public Produto createFromParcel(Parcel in) {
            return new Produto(in);
        }

        @Override
        public Produto[] newArray(int size) {
            return new Produto[size];
        }
    };

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

    public Boolean getDevolvido() {
        return devolvido;
    }

    public void setDevolvido(Boolean devolvido) {
        this.devolvido = devolvido;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nomedoproduto);
        parcel.writeString(quantidade);
        parcel.writeString(valor);
        parcel.writeString(total);
        parcel.writeString(data);
        parcel.writeByte((byte) (recebido == null ? 0 : recebido ? 1 : 2));
        parcel.writeByte((byte) (devolvido == null ? 0 : devolvido ? 1 : 2));
    }
}
