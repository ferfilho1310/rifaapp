package br.com.medeve.Repository

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import br.com.medeve.Interfaces.IUsuarioDao
import br.com.medeve.Models.Usuario
import br.com.medeve.Util.Resultados
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class UsuarioRepository : IUsuarioDao {

    private val firebaseAuth = FirebaseAuth.getInstance()
    var collectionUser = FirebaseFirestore.getInstance().collection("Users")

    val responseCadastroUsuario = MutableLiveData<Int>()
    val cadstroUsuario = responseCadastroUsuario

    val responseEntrarUsuario = MutableLiveData<Int>()
    val entrarUsuario = responseEntrarUsuario

    override suspend fun cadastrarUsuairo(usuario: Usuario?): MutableLiveData<Int> {
        var resultado :Int

        firebaseAuth.createUserWithEmailAndPassword(usuario?.email!!, usuario.senha!!)
            .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val map: MutableMap<String, String?> = HashMap()
                map["Nome"] = usuario.nome
                map["E-mail"] = usuario.email
                map["Senha"] = usuario.senha
                map["Confirmar Senha"] = usuario.confirmaSenha
                map["Sexo"] = usuario.sexo

                collectionUser.add(map)
                resultado = Resultados.CadastroUsuario.SUCESSO_CADASTRO_USUARIO
                cadstroUsuario.postValue(resultado)

            } else if (!task.isSuccessful) {
                try {
                    throw task.exception!!
                } catch (e: FirebaseAuthWeakPasswordException) {
                    resultado = Resultados.CadastroUsuario.SENHA_COM_MENOS_DE_SEIS_CARACTERES
                    cadstroUsuario.postValue(resultado)
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    resultado = Resultados.CadastroUsuario.EMAIL_INVALIDO
                    cadstroUsuario.postValue(resultado)
                } catch (e: FirebaseAuthUserCollisionException) {
                    resultado = Resultados.CadastroUsuario.EMAIL_JA_CADASTRADO
                    cadstroUsuario.postValue(resultado)
                } catch (e: Exception) {
                    resultado = Resultados.CadastroUsuario.ERRO_DESCONHECIDO
                    cadstroUsuario.postValue(resultado)
                }
            }
        }
        return cadstroUsuario
    }

    override suspend fun entrarUsuario(usuario: Usuario?): MutableLiveData<Int> {

        var resultado : Int

        firebaseAuth.signInWithEmailAndPassword(usuario?.email!!, usuario.senha!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                resultado = Resultados.EntrarUsuario.LOGIN_REALIZADO_COM_SUCESSO
                entrarUsuario.postValue(resultado)
            } else if (!task.isSuccessful){
                try {
                    throw task.exception!!
                } catch (e: Exception) {
                    resultado = Resultados.EntrarUsuario.FALHA_NO_LOGIN
                    entrarUsuario.postValue(resultado)
                }
            }

        }
        return entrarUsuario
    }

    override fun recuperarSenhaUsuario(usuario: Usuario?) {
        TODO("Not yet implemented")
    }

    override fun persistirUsuario(clazz: Class<*>?, activity: Activity?) {
        TODO("Not yet implemented")
    }

    override fun sair(activity: Activity?, clazz: Class<*>?) {
        TODO("Not yet implemented")
    }

    /*override fun cadastrarUsuairo(usuario: Usuario): Int {

        var resultado = Resultados.CadastroUsuario.ERRO_DESCONHECIDO
        var recebeResultado = 0

        firebaseAuth.createUserWithEmailAndPassword(usuario.email!!, usuario.senha!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val map: MutableMap<String, String?> = HashMap()
                map["Nome"] = usuario.nome
                map["E-mail"] = usuario.email
                map["Senha"] = usuario.senha
                map["Confirmar Senha"] = usuario.confirmaSenha
                map["Sexo"] = usuario.sexo
                collectionUser.add(map)
                resultado = Resultados.CadastroUsuario.SUCESSO_CADASTRO_USUARIO

            } else if (!task.isSuccessful) {
                try {
                    throw task.exception!!
                } catch (e: FirebaseAuthWeakPasswordException) {
                    resultado = Resultados.CadastroUsuario.SENHA_COM_MENOS_DE_SEIS_CARACTERES
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    resultado = Resultados.CadastroUsuario.EMAIL_INVALIDO
                } catch (e: FirebaseAuthUserCollisionException) {
                    resultado = Resultados.CadastroUsuario.EMAIL_JA_CADASTRADO
                } catch (e: Exception) {
                    resultado = Resultados.CadastroUsuario.ERRO_DESCONHECIDO
                }
            }
        }
        return resultado
    }

    override fun entrarUsuario(usuario: Usuario): Int {
        var resultado = Resultados.CadastroUsuario.ERRO_DESCONHECIDO
        firebaseAuth.signInWithEmailLink(usuario.email!!, usuario.senha!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                resultado = Resultados.EntrarUsuario.LOGIN_REALIZADO_COM_SUCESSO
            } else {
                resultado = Resultados.EntrarUsuario.FALHA_NO_LOGIN
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
*/
}