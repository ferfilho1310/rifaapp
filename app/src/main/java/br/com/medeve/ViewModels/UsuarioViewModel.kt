package br.com.medeve.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.medeve.Repository.UsuarioRepository
import br.com.medeve.Models.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsuarioViewModel(val usuarioRepository: UsuarioRepository) : ViewModel() {

    private val entrarUsuario = MutableLiveData<Int>()
    val resultadoEntrarUsuario = entrarUsuario

    private val cadastrarUsuario = MutableLiveData<Int>()
    val resultadoCadastroUsuario = cadastrarUsuario

    fun entrarUsuario(usuario: Usuario) {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                usuarioRepository.entrarUsuario(usuario)
            }.onSuccess {
                resultadoEntrarUsuario.postValue(it.value)
            }.onFailure {

            }
        }
    }

    fun cadastrarUsuario(usuario: Usuario){
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                usuarioRepository.cadastrarUsuairo(usuario)
            }.onSuccess {
                resultadoCadastroUsuario.postValue(it.value)
            }.onFailure {

            }
        }
    }

    /*
    override fun entrar(usuario: Usuario?) {
        iUsuarioDao!!.entrarUsuario(usuario)
    }

    override fun cadastrar(usuario: Usuario?, activity: Activity?) {

        val progressBarHelper = ProgressBarHelper(activity)
        val resultado = iUsuarioDao!!.cadastrarUsuairo(usuario)

        when (resultado) {
            Resultados.CadastroUsuario.SUCESSO_CADASTRO_USUARIO -> {
                Toast.makeText(activity, "Usuário cadastrado com sucesso.", Toast.LENGTH_LONG).show()
                IntentHelper.instance!!.intentWithFinish(activity,CadastroClienteActView::class.java)
            }
            Resultados.CadastroUsuario.EMAIL_JA_CADASTRADO -> {
                Toast.makeText(activity, "Email já cadastrado", Toast.LENGTH_LONG).show()
                progressBarHelper.dismiss()
            }
            Resultados.CadastroUsuario.EMAIL_INVALIDO -> {
                Toast.makeText(activity, "Email inválido", Toast.LENGTH_LONG).show()
                progressBarHelper.dismiss()
            }
            Resultados.CadastroUsuario.SENHA_COM_MENOS_DE_SEIS_CARACTERES -> {
                Toast.makeText(activity, "Senha inferior a 6 caracteres", Toast.LENGTH_LONG).show()
                progressBarHelper.dismiss()
            }
            Resultados.CadastroUsuario.ERRO_DESCONHECIDO -> {
                Toast.makeText(activity, "Erro Desconhecido. Falha. Ao cadastar usuário", Toast.LENGTH_LONG).show()
                progressBarHelper.dismiss()
            }
        }
    }

    override fun recuperarSenha(usuario: Usuario?) {
        iUsuarioDao!!.recuperarSenhaUsuario(usuario)
    }

    override fun persistirUsuario(activity: Activity?, clazz: Class<*>?) {
        iUsuarioDao!!.persistirUsuario(clazz, activity)
    }

    override fun sair(activity: Activity?, clazz: Class<*>?) {
        iUsuarioDao!!.sair(activity, clazz)
    }*/
}