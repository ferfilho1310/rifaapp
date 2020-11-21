package br.com.medeve.Activitys

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import br.com.medeve.Controlers.UsuarioControler
import br.com.medeve.Helpers.IntentHelper
import br.com.medeve.Models.Usuario
import br.com.medeve.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class EntrarUsuarioActView : AppCompatActivity(), View.OnClickListener {
    var btnEntrar: Button? = null
    var btnCadastrarUsuario: Button? = null
    var edtEmail: EditText? = null
    var edtSenha: EditText? = null
    var txtResetSenha: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrar_usuario)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        btnEntrar = findViewById(R.id.btn_entrar)
        btnCadastrarUsuario = findViewById(R.id.btn_cadastrar_usuario)
        edtEmail = findViewById(R.id.ed_entrar_email)
        edtSenha = findViewById(R.id.ed_entrar_senha)
        txtResetSenha = findViewById(R.id.txt_reset_senha)

        supportActionBar!!.hide()
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        FirebaseApp.initializeApp(this@EntrarUsuarioActView)
        UsuarioControler.instance!!.persistirUsuario(this@EntrarUsuarioActView, CadastroClienteActView::class.java)

        btnEntrar!!.setOnClickListener(this)
        btnCadastrarUsuario!!.setOnClickListener(this)
        txtResetSenha!!.setOnClickListener(this)
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_entrar -> {
                val usuario = Usuario()
                usuario.email = edtEmail!!.text.toString()
                usuario.senha = edtSenha!!.text.toString()
                validacaoCamposEmailSenha(usuario)
            }
            R.id.btn_cadastrar_usuario -> IntentHelper.instance!!.intentWithFlags(this@EntrarUsuarioActView, CadastrarUsuarioActView::class.java)
            R.id.txt_reset_senha -> IntentHelper.instance!!.intentWithOutFinish(this@EntrarUsuarioActView, ResetSenha::class.java)
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
            UsuarioControler.instance!!.entrar(usuario)
        }
    }
}