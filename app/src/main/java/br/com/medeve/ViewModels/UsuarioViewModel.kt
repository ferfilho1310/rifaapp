package br.com.medeve.ViewModels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.medeve.Repository.UsuarioRepository
import br.com.medeve.Models.Usuario
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UsuarioViewModel(
    val usuarioRepository: UsuarioRepository,
) : ViewModel() {

    init {
        runBlocking {
            getUserPersistence()
        }
    }

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

    fun getUserPersistence() : MutableLiveData<Boolean>{
        return usuarioRepository.getUserPersistence()
    }

    fun getUserSignOut() : MutableLiveData<Void> {
        return usuarioRepository.getUserSignOut()
    }
}