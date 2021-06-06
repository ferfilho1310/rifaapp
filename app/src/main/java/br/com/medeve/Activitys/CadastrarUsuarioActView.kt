package br.com.medeve.Activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import br.com.medeve.Helpers.ProgressBarHelper
import br.com.medeve.Models.Usuario
import br.com.medeve.R
import br.com.medeve.Util.Resultados
import br.com.medeve.ViewModels.UsuarioViewModel
import kotlinx.android.synthetic.main.activity_cadastro_user.*
import kotlinx.android.synthetic.main.activity_entrar_usuario.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CadastrarUsuarioActView : AppCompatActivity(), View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    var sexo : String? = null

    val viewModelCadastroUsuario : UsuarioViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_user)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        title = "Cadastre-se"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        btn_cadastrar!!.setOnClickListener(this)
        rd_sexos!!.setOnCheckedChangeListener(this)

        setObservers()
    }

    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
        when (group.id) {
            R.id.rd_sexos -> if (femi!!.isChecked) {
                sexo = "Feminino"
            } else if (masc!!.isChecked) {
                sexo = "Masculino"
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_cadastrar -> {
                val usuario = Usuario()
                usuario.senha = ed_cad_user_senha!!.text.toString()
                usuario.confirmaSenha = ed_cad_user_confirmasenha!!.text.toString()
                usuario.email = ed_cad_user_email!!.text.toString()
                usuario.nome = ed_cad_user_nome!!.text.toString()
                usuario.sexo = sexo
                validarCadastroUsuario(usuario)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val i_cad_user = Intent(applicationContext, EntrarUsuarioActView::class.java)
                startActivity(i_cad_user)
                finish()
            }
            else -> {
            }
        }
        return true
    }

    private fun validarCadastroUsuario(usuario: Usuario) {
        if (usuario.nomeVazio()) {
            Toast.makeText(this, "Digite o seu nome", Toast.LENGTH_SHORT).show()
        } else if (usuario.senhaVazia()) {
            Toast.makeText(this, "Digite sua senha", Toast.LENGTH_SHORT).show()
        } else if (usuario.confirmarSenhaVazia()) {
            Toast.makeText(this, "Confirme sua senha", Toast.LENGTH_SHORT).show()
        } else if (usuario.nomeVazio()) {
            Toast.makeText(this, "Digite seu nome", Toast.LENGTH_SHORT).show()
        } else if (usuario.senha != usuario.confirmaSenha) {
            Toast.makeText(this, "As senhas estão diferentes", Toast.LENGTH_SHORT).show()
        } else if (usuario.sexo == null) {
            Toast.makeText(this, "Informe seu sexo", Toast.LENGTH_SHORT).show()
        } else {
            viewModelCadastroUsuario.cadastrarUsuario(usuario)
        }
    }

    private fun setObservers(){

        viewModelCadastroUsuario.resultadoCadastroUsuario.observe(this, {

            val progressBarHelper = ProgressBarHelper(this)
            when (it) {
                Resultados.CadastroUsuario.SUCESSO_CADASTRO_USUARIO -> {
                    Toast.makeText(this, "Usuário cadastrado com sucesso.", Toast.LENGTH_LONG).show()
                    //IntentHelper.instance!!.intentWithFinish(this,CadastroClienteActView::class.java)
                }
                Resultados.CadastroUsuario.EMAIL_JA_CADASTRADO -> {
                    Toast.makeText(this, "Email já cadastrado", Toast.LENGTH_LONG).show()
                    progressBarHelper.dismiss()
                }
                Resultados.CadastroUsuario.EMAIL_INVALIDO -> {
                    Toast.makeText(this, "Email inválido", Toast.LENGTH_LONG).show()
                    progressBarHelper.dismiss()
                }
                Resultados.CadastroUsuario.SENHA_COM_MENOS_DE_SEIS_CARACTERES -> {
                    Toast.makeText(this, "Senha inferior a 6 caracteres", Toast.LENGTH_LONG).show()
                    progressBarHelper.dismiss()
                }
                Resultados.CadastroUsuario.ERRO_DESCONHECIDO -> {
                    Toast.makeText(this, "Erro Desconhecido. Falha. Ao cadastar usuário", Toast.LENGTH_LONG).show()
                    progressBarHelper.dismiss()
                }
            }
        })
    }
}