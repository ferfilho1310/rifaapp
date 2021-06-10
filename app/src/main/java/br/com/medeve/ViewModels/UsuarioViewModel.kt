package br.com.medeve.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.medeve.Repository.UsuarioRepository
import br.com.medeve.Models.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsuarioViewModel(
    val usuarioRepository: UsuarioRepository,
) : ViewModel() {

    fun entrarUsuario(usuario: Usuario) {
        usuarioRepository.entrarUsuario(usuario)
    }

    fun getUsuarioEntrarMutableLiveData(): MutableLiveData<Int> {
        return usuarioRepository.getEntrarUsuarioMutable()
    }

    fun cadastrarUsuario(usuario: Usuario) {
        usuarioRepository.cadastrarUsuairo(usuario)
    }

    fun getUsuarioCadastrarMutableLiveData(): MutableLiveData<Int> {
        return usuarioRepository.getCadastrarUsuarioMutable()
    }

    fun resetSenhaUsuario(usuario: Usuario){
        usuarioRepository.recuperarSenhaUsuario(usuario)
    }

    fun getResetSenhaUsuarioMutableLiveData() : MutableLiveData<Int> {
        return usuarioRepository.getRecuperarSenhaMutable()
    }

  /*  override fun recuperarSenha(usuario: Usuario?) {
        iUsuarioDao!!.recuperarSenhaUsuario(usuario)
    }

    override fun persistirUsuario(activity: Activity?, clazz: Class<*>?) {
        iUsuarioDao!!.persistirUsuario(clazz, activity)
    }

    override fun sair(activity: Activity?, clazz: Class<*>?) {
        iUsuarioDao!!.sair(activity, clazz)
    }*/
}