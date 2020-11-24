package br.com.medeve.Interfaces

import android.app.Activity
import br.com.medeve.Models.Usuario

interface IUsuarioControler {
    fun entrar(usuario: Usuario?)
    fun cadastrar(usuario: Usuario?, activity: Activity?)
    fun recuperarSenha(usuario: Usuario?)
    fun persistirUsuario(activity: Activity?, clazz: Class<*>?)
    fun sair(activity: Activity?, clazz: Class<*>?)
}