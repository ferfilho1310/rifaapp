package br.com.medeve.Interfaces

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import br.com.medeve.Models.Usuario

interface IUsuarioDao {
    fun cadastrarUsuairo(usuario: Usuario?)
    fun entrarUsuario(usuario: Usuario?)
    fun recuperarSenhaUsuario(usuario: Usuario?)
    fun persistirUsuario(clazz: Class<*>?, activity: Activity?)
    fun sair(activity: Activity?, clazz: Class<*>?)
}