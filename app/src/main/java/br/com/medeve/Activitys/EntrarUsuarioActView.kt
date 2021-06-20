package br.com.medeve.Activitys

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import br.com.medeve.Helpers.IntentHelper
import br.com.medeve.Helpers.ProgressBarHelper
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

        FirebaseApp.initializeApp(this@EntrarUsuarioActView)
    
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
            prbEntrar.visibility = View.VISIBLE
            btn_entrar.visibility = View.GONE
            btn_cadastrar_usuario.isEnabled = false
            txt_reset_senha.isEnabled = false
            hideKeyboard(this)
        }
    }

    fun setObservers() {
        viewModelEntrarUsuario.getUsuarioEntrarMutableLiveData().observe(this, {
            when (it) {
                Constantes.EntrarUsuario.LOGIN_REALIZADO_COM_SUCESSO -> {
                    IntentHelper.intentWithFinish(this, CadastroClienteActView::class.java)
                    Toast.makeText(
                        applicationContext,
                        "Login realizado com sucesso.",
                        Toast.LENGTH_SHORT
                    ).show()
                    prbEntrar.visibility = View.GONE
                    btn_entrar.visibility = View.VISIBLE
                    btn_cadastrar_usuario.isEnabled = true
                    txt_reset_senha.isEnabled = true
                    viewModelEntrarUsuario.getUsuarioEntrarMutableLiveData().value = null
                }
                Constantes.CadastroUsuario.EMAIL_INVALIDO -> {
                    Toast.makeText(
                        applicationContext,
                        "Email e/ou senha inválido",
                        Toast.LENGTH_LONG
                    ).show()
                    prbEntrar.visibility = View.GONE
                    btn_entrar.visibility = View.VISIBLE
                    btn_cadastrar_usuario.isEnabled = true
                    txt_reset_senha.isEnabled = true
                    viewModelEntrarUsuario.getUsuarioEntrarMutableLiveData().value = null
                }

                Constantes.CadastroUsuario.INTERNET_OFF -> {
                    Toast.makeText(
                        applicationContext,
                        "Verifique a conexão de internet",
                        Toast.LENGTH_LONG
                    ).show()
                    prbEntrar.visibility = View.GONE
                    btn_entrar.visibility = View.VISIBLE
                    btn_cadastrar_usuario.isEnabled = true
                    txt_reset_senha.isEnabled = true
                    viewModelEntrarUsuario.getUsuarioEntrarMutableLiveData().value = null
                }
                Constantes.CadastroUsuario.ERRO_DESCONHECIDO -> {
                    Toast.makeText(
                        applicationContext,
                        "Erro Desconhecido.",
                        Toast.LENGTH_LONG
                    ).show()
                    prbEntrar.visibility = View.GONE
                    btn_entrar.visibility = View.VISIBLE
                    btn_cadastrar_usuario.isEnabled = true
                    txt_reset_senha.isEnabled = true
                    viewModelEntrarUsuario.getUsuarioEntrarMutableLiveData().value = null
                }
            }
        })

    }

    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onStart() {
        super.onStart()
        viewModelEntrarUsuario.getUserPersistence().observe(this, {
            if (it) {
                IntentHelper.intentWithFinish(this, CadastroClienteActView::class.java)
            }
        })
    }

}
