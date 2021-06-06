package br.com.medeve

import br.com.medeve.Models.Usuario
import br.com.medeve.Util.Resultados
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Assert
import org.junit.Test
import java.util.*

class UsuarioRepositoryTest {

    private val firebaseAuth = FirebaseAuth.getInstance()
    var collectionUser = FirebaseFirestore.getInstance().collection("Users")

    @Test
    fun cadastro_usuario_sucesso(){

        var resultado: Int? = null
        val usuario = Usuario()

        usuario.email = "re1310@gmail.com"
        usuario.senha = "123456"

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

                } catch (e: FirebaseAuthInvalidCredentialsException) {

                } catch (e: FirebaseAuthUserCollisionException) {

                } catch (e: Exception) {

                }
            }
            Assert.assertEquals(0, resultado)
        }
    }

    @Test
    fun entrar_usuario_sucesso(){

        val usuario = Usuario()
        var resultado: Int

        usuario.email = "as1310@gmail.com"
        usuario.senha = "123456"

        firebaseAuth.signInWithEmailLink(usuario.email!!,usuario.senha!!).addOnCompleteListener { task ->
            if(task.isSuccessful){
                resultado = Resultados.CadastroUsuario.SUCESSO_CADASTRO_USUARIO
            } else {
                resultado = 1
            }
            Assert.assertEquals(0, resultado)
        }
    }
}