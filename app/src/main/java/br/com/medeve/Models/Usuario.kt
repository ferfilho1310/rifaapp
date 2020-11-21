package br.com.medeve.Models

class Usuario {
    var nome: String? = null
    var email: String? = null
    var senha: String? = null
    var confirmaSenha: String? = null
    var sexo: String? = null

    fun nomeVazio(): Boolean {
        var nomeVazio = false
        if (nome!!.isEmpty()) {
            nomeVazio = true
        }
        return nomeVazio
    }

    fun emailVazio(): Boolean {
        var emailVazio = false
        if (email!!.isEmpty()) {
            emailVazio = true
        }
        return emailVazio
    }

    fun senhaVazia(): Boolean {
        var senhaVazia = false
        if (senha!!.isEmpty()) {
            senhaVazia = true
        }
        return senhaVazia
    }

    fun confirmarSenhaVazia(): Boolean {
        var confirmaSenhaVazia = false
        if (confirmaSenha!!.isEmpty()) {
            confirmaSenhaVazia = true
        }
        return confirmaSenhaVazia
    }
}