package br.com.medeve.Interfaces

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import br.com.medeve.Models.Usuario

interface IUsuarioRepository {
    fun cadastrarUsuairo(usuario: Usuario?)
    fun entrarUsuario(usuario: Usuario?)
    fun recuperarSenhaUsuario(usuario: Usuario?)
    fun persistirUsuario()
    fun sair()
}