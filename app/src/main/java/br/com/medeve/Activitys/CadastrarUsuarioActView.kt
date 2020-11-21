package br.com.medeve.Activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import br.com.medeve.Controlers.UsuarioControler.Companion.instance
import br.com.medeve.Models.Usuario
import br.com.medeve.R

class CadastrarUsuarioActView : AppCompatActivity(), View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    var edtNomeUsuario: EditText? = null
    var edtEmailUsuario: EditText? = null
    var edtSenhaUsuario: EditText? = null
    var edtConfirmaSenhaUsuario: EditText? = null
    var btnCadastrarUsuario: Button? = null
    var rd_sexo: RadioGroup? = null
    var rd_feminino: RadioButton? = null
    var rd_masculino: RadioButton? = null
    var sexo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_user)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        edtNomeUsuario = findViewById(R.id.ed_cad_user_nome)
        edtEmailUsuario = findViewById(R.id.ed_cad_user_email)
        edtSenhaUsuario = findViewById(R.id.ed_cad_user_senha)
        edtConfirmaSenhaUsuario = findViewById(R.id.ed_cad_user_confirmasenha)
        rd_sexo = findViewById(R.id.rd_sexos)
        btnCadastrarUsuario = findViewById(R.id.btn_cadastrar)
        rd_feminino = findViewById(R.id.femi)
        rd_masculino = findViewById(R.id.masc)

        title = "Cadastre-se"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        
        btnCadastrarUsuario!!.setOnClickListener(this)
        rd_sexo!!.setOnCheckedChangeListener(this)
    }

    @SuppressLint("NonConstantResourceId")
    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
        when (group.id) {
            R.id.rd_sexos -> if (rd_feminino!!.isChecked) {
                sexo = "Feminino"
            } else if (rd_masculino!!.isChecked) {
                sexo = "Masculino"
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_cadastrar -> {
                val usuario = Usuario()
                usuario.senha = edtSenhaUsuario!!.text.toString()
                usuario.confirmaSenha = edtConfirmaSenhaUsuario!!.text.toString()
                usuario.email = edtEmailUsuario!!.text.toString()
                usuario.nome = edtNomeUsuario!!.text.toString()
                usuario.sexo = sexo
                validarCadastroUsuario(usuario)
            }
        }
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
            instance!!.cadastrar(usuario, this@CadastrarUsuarioActView)
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
}