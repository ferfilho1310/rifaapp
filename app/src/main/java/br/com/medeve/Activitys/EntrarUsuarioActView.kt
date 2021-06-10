package br.com.medeve.Activitys

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import br.com.medeve.Helpers.IntentHelper
import br.com.medeve.Models.Usuario
import br.com.medeve.R
import br.com.medeve.Util.Constantes
import br.com.medeve.ViewModels.UsuarioViewModel
import com.google.firebase.FirebaseApp
import kotlinx.android.synthetic.main.activity_entrar_usuario.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class EntrarUsuarioActView : AppCompatActivity(), View.OnClickListener {

    val viewModelEntrarUsuario: UsuarioViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrar_usuario)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar!!.hide()
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        FirebaseApp.initializeApp(this@EntrarUsuarioActView)
        /*   viewModelEntrarUsuario.persistirUsuario(
               this@EntrarUsuarioActView,
               CadastroClienteActView::class.java
           )*/

        btn_entrar!!.setOnClickListener(this)
        btn_cadastrar_usuario!!.setOnClickListener(this)
        txt_reset_senha!!.setOnClickListener(this)

        setObservers()
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_entrar -> {
                val usuario = Usuario()
                usuario.email = ed_entrar_email!!.text.toString()
                usuario.senha = ed_entrar_senha!!.text.toString()
                validacaoCamposEmailSenha(usuario)
            }
            R.id.btn_cadastrar_usuario -> IntentHelper.intentWithFlags(
                this@EntrarUsuarioActView,
                CadastrarUsuarioActView::class.java
            )
            R.id.txt_reset_senha -> IntentHelper.intentWithOutFinish(
                this@EntrarUsuarioActView,
                ResetSenha::class.java
            )
            else -> {
            }
        }
    }


    private fun validacaoCamposEmailSenha(usuario: Usuario) {
        if (usuario.emailVazio()) {
            Toast.makeText(this, "Digite seu e-mail", Toast.LENGTH_SHORT).show()
        } else if (usuario.senhaVazia()) {
            Toast.makeText(this, "Digite sua senha", Toast.LENGTH_SHORT).show()
        } else {
            viewModelEntrarUsuario.entrarUsuario(usuario)
        }
    }

    fun setObservers() {
        viewModelEntrarUsuario.getUsuarioEntrarMutableLiveData().observe(this, {
            //val progressBarHelper = ProgressBarHelper(this)
            when (it) {
                Constantes.EntrarUsuario.LOGIN_REALIZADO_COM_SUCESSO -> {
                    Toast.makeText(this, "Login realizado com sucesso.", Toast.LENGTH_LONG)
                        .show()
                }
                Constantes.CadastroUsuario.EMAIL_INVALIDO -> {
                    Toast.makeText(this, "Email e/ou senha inválido", Toast.LENGTH_LONG).show()
                    //    progressBarHelper.dismiss()
                }

                Constantes.CadastroUsuario.INTERNET_OFF -> {
                    Toast.makeText(this, "Verifique a conexão de internet", Toast.LENGTH_LONG)
                        .show()
                }
                Constantes.CadastroUsuario.ERRO_DESCONHECIDO -> {
                    Toast.makeText(
                        this,
                        "Erro Desconhecido.",
                        Toast.LENGTH_LONG
                    ).show()
                    //    progressBarHelper.dismiss()
                }
            }
        })
    }

}