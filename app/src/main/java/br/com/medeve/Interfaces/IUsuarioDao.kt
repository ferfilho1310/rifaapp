package br.com.medeve.Interfaces

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import br.com.medeve.Models.Usuario

interface IUsuarioDao {
    suspend fun cadastrarUsuairo(usuario: Usuario?): MutableLiveData<Int>
    suspend fun entrarUsuario(usuario: Usuario?): MutableLiveData<Int>
    fun recuperarSenhaUsuario(usuario: Usuario?)
    fun persistirUsuario(clazz: Class<*>?, activity: Activity?)
    fun sair(activity: Activity?, clazz: Class<*>?)
}