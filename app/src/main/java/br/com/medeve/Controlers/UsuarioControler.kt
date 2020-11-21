package br.com.medeve.Controlers

import android.app.Activity
import android.widget.Toast
import br.com.medeve.Dao.UsuarioDao
import br.com.medeve.Helpers.ProgressBarHelper
import br.com.medeve.Interfaces.IUsuarioControler
import br.com.medeve.Interfaces.IUsuarioDao
import br.com.medeve.Models.Usuario
import br.com.medeve.Util.ResultadoCadastroUsuario

class UsuarioControler : IUsuarioControler {

    companion object {
        private var usuarioControler: UsuarioControler? = null
        private var iUsuarioDao: IUsuarioDao? = null

        @JvmStatic
        @get:Synchronized
        val instance: UsuarioControler?
            get() {
                if (usuarioControler == null) {
                    usuarioControler = UsuarioControler()
                }
                iUsuarioDao = UsuarioDao()
                return usuarioControler
            }
    }

    override fun entrar(usuario: Usuario) {
        iUsuarioDao!!.entrarUsuario(usuario)
    }

    override fun cadastrar(usuario: Usuario, activity: Activity) {

        val progressBarHelper = ProgressBarHelper(activity)
        val resultado = iUsuarioDao!!.cadastrarUsuairo(usuario)

        when (resultado) {
            ResultadoCadastroUsuario.Resultados.SUCESSO_CADASTRO_USUARIO -> {
                Toast.makeText(activity, "Usuário cadastrado com sucesso.", Toast.LENGTH_LONG).show()
            }
            ResultadoCadastroUsuario.Resultados.ERRO_DESCONHECIDO_CADASTRO_USUARIO  -> {
                Toast.makeText(activity, "Erro Desconhecido. Falha. Ao cadastar usuário", Toast.LENGTH_LONG).show()
                progressBarHelper.dismiss()
            }
        }
    }

    override fun recuperarSenha(usuario: Usuario) {
        iUsuarioDao!!.recuperarSenhaUsuario(usuario)
    }

    override fun persistirUsuario(activity: Activity, clazz: Class<*>?) {
        iUsuarioDao!!.persistirUsuario(clazz, activity)
    }

    override fun sair(activity: Activity, clazz: Class<*>?) {
        iUsuarioDao!!.sair(activity, clazz)
    }
}