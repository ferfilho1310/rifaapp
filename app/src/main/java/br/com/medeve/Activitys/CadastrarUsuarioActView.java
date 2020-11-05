package br.com.medeve.Activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import br.com.medeve.Controlers.UsuarioControler;
import br.com.medeve.Models.Usuario;
import br.com.medeve.R;
import br.com.medeve.Helpers.AccessFirebase;
import br.com.medeve.Models.Cliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class CadastrarUsuarioActView extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    EditText edtNomeUsuario;
    EditText edtEmailUsuario;
    EditText edtSenhaUsuario;
    EditText edtConfirmaSenhaUsuario;

    Button btnCadastrarUsuario;

    RadioGroup rd_sexo;
    RadioButton rd_feminino, rd_masculino;

    String sexo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtNomeUsuario = findViewById(R.id.ed_cad_user_nome);
        edtEmailUsuario = findViewById(R.id.ed_cad_user_email);
        edtSenhaUsuario = findViewById(R.id.ed_cad_user_senha);
        edtConfirmaSenhaUsuario = findViewById(R.id.ed_cad_user_confirmasenha);
        rd_sexo = findViewById(R.id.rd_sexos);
        btnCadastrarUsuario = findViewById(R.id.btn_cadastrar);
        rd_feminino = findViewById(R.id.femi);
        rd_masculino = findViewById(R.id.masc);

        setTitle("Cadastre-se");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        btnCadastrarUsuario.setOnClickListener(this);
        rd_sexo.setOnCheckedChangeListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.rd_sexos:
                if (rd_feminino.isChecked()) {
                    sexo = "Feminino";
                } else if (rd_masculino.isChecked()) {
                    sexo = "Masculino";
                }
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cadastrar:
                Usuario usuario = new Usuario();
                usuario.setSenha(edtSenhaUsuario.getText().toString());
                usuario.setConfirmaSenha(edtConfirmaSenhaUsuario.getText().toString());
                usuario.setEmail(edtEmailUsuario.getText().toString());
                usuario.setNome(edtNomeUsuario.getText().toString());
                usuario.setSexo(sexo);

                validarCadastroUsuario(usuario);
                break;
        }
    }


    private void validarCadastroUsuario(Usuario usuario) {
        if (usuario.nomeVazio()) {
            Toast.makeText(this, "Digite o seu nome", Toast.LENGTH_SHORT).show();
        } else if (usuario.senhaVazia()) {
            Toast.makeText(this, "Digite sua senha", Toast.LENGTH_SHORT).show();
        } else if (usuario.confirmarSenhaVazia()) {
            Toast.makeText(this, "Confirme sua senha", Toast.LENGTH_SHORT).show();
        } else if (usuario.nomeVazio()) {
            Toast.makeText(this, "Digite seu nome", Toast.LENGTH_SHORT).show();
        } else if (!usuario.getSenha().equals(usuario.getConfirmaSenha())) {
            Toast.makeText(this, "As senhas estão diferentes", Toast.LENGTH_SHORT).show();
        } else if (usuario.getSexo() == null){
            Toast.makeText(this, "Informe seu sexo", Toast.LENGTH_SHORT).show();
        } else {
            UsuarioControler.getInstance().cadastrar(usuario, CadastrarUsuarioActView.this);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i_cad_user = new Intent(getApplicationContext(), EntrarUsuarioActView.class);
                startActivity(i_cad_user);
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
