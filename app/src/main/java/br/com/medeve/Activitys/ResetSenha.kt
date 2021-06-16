package br.com.medeve.Activitys

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import br.com.medeve.Models.Usuario
import br.com.medeve.R
import br.com.medeve.Util.Constantes
import br.com.medeve.ViewModels.UsuarioViewModel
import kotlinx.android.synthetic.main.activity_reset_senha.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResetSenha : AppCompatActivity(), View.OnClickListener {

    val usuarioViewModel: UsuarioViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_senha)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        title = "Trocar senha"

        btn_alterar_senha.setOnClickListener(this)

        setObservers()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_alterar_senha -> {
                val usuario = Usuario()
                usuario.email = ed_reset_email.text.toString()
                if (usuario.email.isNullOrEmpty()) {
                    Toast.makeText(this, "Informe o seu e-mail.", Toast.LENGTH_LONG).show()
                } else {
                    usuarioViewModel.resetSenhaUsuario(usuario)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(
                    Intent(
                        applicationContext,
                        EntrarUsuarioActView::class.java
                    )
                )
                finish()
            }
            else -> {
            }
        }
        return true
    }

    fun setObservers() {
        usuarioViewModel.getResetSenhaUsuarioMutableLiveData().observe(this, {
            when (it) {
                Constantes.ResetSenha.RESET_SENHA_SUCESSO -> {
                    Toast.makeText(
                        this,
                        "E-mail de reset de senha enviado para ${ed_reset_email.text.toString()}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
                Constantes.ResetSenha.EMAIL_INVALIDO -> {
                    Toast.makeText(this, "E-mail Inválido", Toast.LENGTH_LONG)
                        .show()
                }
                Constantes.ResetSenha.ERRO_ENVIO_EMAIL -> {
                    Toast.makeText(this, "Erro ao enviar o e-mail.", Toast.LENGTH_LONG)
                        .show()
                }
            }
        })
    }


}

