package br.com.medeve.Repository

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import br.com.medeve.Activitys.EntrarUsuarioActView
import br.com.medeve.Interfaces.IUsuarioDao
import br.com.medeve.Models.Usuario
import br.com.medeve.Util.Constantes
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class UsuarioRepository : IUsuarioDao {

    val entrarUsuarioMutableLiveData: MutableLiveData<Int> = MutableLiveData()
    val cadasUsuarioMutableLiveData: MutableLiveData<Int> = MutableLiveData()
    val recuperarSenhaMutableLiveData : MutableLiveData<Int> = MutableLiveData()

    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val collectionUser = FirebaseFirestore.getInstance().collection("Users")

    override fun cadastrarUsuairo(usuario: Usuario?) {
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
                    cadasUsuarioMutableLiveData.postValue(Constantes.CadastroUsuario.SUCESSO_CADASTRO_USUARIO)

                } else if (!task.isSuccessful) {
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        cadasUsuarioMutableLiveData.postValue(Constantes.CadastroUsuario.SENHA_COM_MENOS_DE_SEIS_CARACTERES)
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        cadasUsuarioMutableLiveData.postValue(Constantes.CadastroUsuario.EMAIL_INVALIDO)
                    } catch (e: FirebaseAuthUserCollisionException) {
                        cadasUsuarioMutableLiveData.postValue(Constantes.CadastroUsuario.EMAIL_JA_CADASTRADO)
                    } catch (e: Exception) {
                        cadasUsuarioMutableLiveData.postValue(Constantes.CadastroUsuario.ERRO_DESCONHECIDO)
                    }
                }
            }
    }

    override fun entrarUsuario(usuario: Usuario?) {
        firebaseAuth.signInWithEmailAndPassword(usuario?.email!!, usuario.senha!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    entrarUsuarioMutableLiveData.postValue(Constantes.EntrarUsuario.LOGIN_REALIZADO_COM_SUCESSO)
                } else if (!task.isSuccessful) {
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        entrarUsuarioMutableLiveData.postValue(Constantes.CadastroUsuario.EMAIL_INVALIDO)
                    } catch (e: FirebaseNetworkException) {
                        entrarUsuarioMutableLiveData.postValue(Constantes.CadastroUsuario.INTERNET_OFF)
                    } catch (e: Exception) {
                        entrarUsuarioMutableLiveData.postValue(Constantes.CadastroUsuario.ERRO_DESCONHECIDO)
                    }
                }
            }
    }

    override fun recuperarSenhaUsuario(usuario: Usuario?) {
        firebaseAuth.sendPasswordResetEmail(usuario?.email!!).addOnCompleteListener { task ->
            try {
                if (task.isSuccessful) {
                    recuperarSenhaMutableLiveData.postValue(Constantes.ResetSenha.RESET_SENHA_SUCESSO)
                } else {
                    recuperarSenhaMutableLiveData.postValue(Constantes.ResetSenha.EMAIL_INVALIDO)
                }
            } catch (e: Exception) {
                recuperarSenhaMutableLiveData.postValue(Constantes.ResetSenha.ERRO_ENVIO_EMAIL)
            }
        }
    }

    override fun persistirUsuario(clazz: Class<*>?, activity: Activity?) {
        TODO("Not yet implemented")
    }

    override fun sair(activity: Activity?, clazz: Class<*>?) {
        TODO("Not yet implemented")
    }

    fun getEntrarUsuarioMutable(): MutableLiveData<Int> {
        return entrarUsuarioMutableLiveData
    }

    fun getCadastrarUsuarioMutable(): MutableLiveData<Int> {
        return cadasUsuarioMutableLiveData
    }

    fun getRecuperarSenhaMutable(): MutableLiveData<Int> {
        return recuperarSenhaMutableLiveData
    }

    /*  override fun recuperarSenhaUsuario(usuario: Usuario) {}
      override fun persistirUsuario(clazz: Class<*>?, activity: Activity) {
          if (firebaseAuth.currentUser != null) {
              IntentHelper.instance!!.intentWithFinish(activity, clazz)
          }
      }

      override fun sair(activity: Activity, clazz: Class<*>?) {
          IntentHelper.instance!!.intentWithFinish(activity, clazz)
      }*/

}
