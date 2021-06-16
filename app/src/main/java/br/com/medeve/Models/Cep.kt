package br.com.medeve.Models

import com.google.gson.annotations.SerializedName

data class Cep(
    @SerializedName("cep") var cep : String,
    @SerializedName("logradouro") var logradouro : String,
    @SerializedName("complemento") var complemento : String,
    @SerializedName("bairro") var bairro : String,
    @SerializedName("localidade") var localidade : String,
    @SerializedName("uf") var uf : String,
    @SerializedName("ibge") var ibge : String,
    @SerializedName("gia") var gia : String,
    @SerializedName("ddd") var ddd : String,
    @SerializedName("siafi") var siafi : String

)
