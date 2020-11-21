package br.com.medeve.Dao

import br.com.medeve.Interfaces.IUsuarioDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import br.com.medeve.Models.Usuario
import android.app.Activity
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import br.com.medeve.Helpers.IntentHelper
import br.com.medeve.Util.ResultadoCadastroUsuario
import java.lang.Exception
import java.util.HashMap

class UsuarioDao : IUsuarioDao {

    private val firebaseAuth = FirebaseAuth.getInstance()
    var collectionUser = FirebaseFirestore.getInstance().collection("Users")

    override fun cadastrarUsuairo(usuario: Usuario): Int {

        var resultado = ResultadoCadastroUsuario.Resultados.ERRO_DESCONHECIDO_CADASTRO_USUARIO

        firebaseAuth.createUserWithEmailAndPassword(usuario.email!!, usuario.senha!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val map: MutableMap<String, String?> = HashMap()
                map["Nome"] = usuario.nome
                map["E-mail"] = usuario.email
                map["Senha"] = usuario.senha
                map["Confirmar Senha"] = usuario.confirmaSenha
                map["Sexo"] = usuario.sexo
                collectionUser.add(map)
                resultado = ResultadoCadastroUsuario.Resultados.SUCESSO_CADASTRO_USUARIO
            } else if (!task.isSuccessful) {
                try {
                    throw task.exception!!
                } catch (e: FirebaseAuthWeakPasswordException) {

                } catch (e: FirebaseAuthInvalidCredentialsException) {

                } catch (e: FirebaseAuthUserCollisionException) {

                } catch (e: Exception) {

                }
            }
        }
        return resultado;
    }

    override fun entrarUsuario(usuario: Usuario) : Int{
        var resultado = 99
        firebaseAuth.signInWithEmailLink(usuario.email!!,usuario.senha!!).addOnCompleteListener { task ->
            if(task.isSuccessful){
                resultado = 0
            } else {
                resultado = 1
            }
        }
        return resultado
    }
    override fun recuperarSenhaUsuario(usuario: Usuario) {}
    override fun persistirUsuario(clazz: Class<*>?, activity: Activity) {
        if (firebaseAuth.currentUser != null) {
            IntentHelper.instance!!.intentWithFinish(activity, clazz)
        }
    }

    override fun sair(activity: Activity, clazz: Class<*>?) {
        IntentHelper.instance!!.intentWithFinish(activity, clazz)
    }

    companion object {
        private var usuarioDao: UsuarioDao? = null

        @get:Synchronized
        val instance: UsuarioDao?
            get() {
                if (usuarioDao == null) {
                    usuarioDao = UsuarioDao()
                }
                return usuarioDao
            }
    }
}