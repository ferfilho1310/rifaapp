package br.com.medeve.Activitys;

import android.annotation.SuppressLint;
import android.os.Bundle;

import br.com.medeve.Controlers.UsuarioControler;
import br.com.medeve.Helpers.IntentHelper;
import br.com.medeve.Models.Usuario;
import br.com.medeve.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import static java.lang.Thread.sleep;

public class EntrarUsuarioActView extends AppCompatActivity implements View.OnClickListener {

    Button btnEntrar;
    Button btnCadastrarUsuario;
    EditText edtEmail;
    EditText edtSenha;
    TextView txtResetSenha;

    FirebaseAuth db_users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrar_usuario);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnEntrar = findViewById(R.id.btn_entrar);
        btnCadastrarUsuario = findViewById(R.id.btn_cadastrar_usuario);
        edtEmail = findViewById(R.id.ed_entrar_email);
        edtSenha = findViewById(R.id.ed_entrar_senha);
        txtResetSenha = findViewById(R.id.txt_reset_senha);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db_users = FirebaseAuth.getInstance();

        FirebaseApp.initializeApp(EntrarUsuarioActView.this);

        UsuarioControler.getInstance().persistirUsuario(EntrarUsuarioActView.this, CadastroClienteActView.class);

        btnEntrar.setOnClickListener(this);
        btnCadastrarUsuario.setOnClickListener(this);
        txtResetSenha.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_entrar:
                Usuario usuario = new Usuario();
                usuario.setEmail(edtEmail.getText().toString());
                usuario.setSenha(edtSenha.getText().toString());
                validacaoCamposEmailSenha(usuario);
                break;
            case R.id.btn_cadastrar_usuario:
                IntentHelper.getInstance().intentWithFlags(EntrarUsuarioActView.this, CadastrarUsuarioActView.class);
                break;
            case R.id.txt_reset_senha:
                IntentHelper.getInstance().intentWithOutFinish(EntrarUsuarioActView.this, ResetSenha.class);
                break;
            default:
                break;
        }
    }

    private void validacaoCamposEmailSenha(Usuario usuario) {
        if (usuario.emailVazio()) {
            Toast.makeText(this, "Digite seu e-mail", Toast.LENGTH_SHORT).show();
        } else if (usuario.senhaVazia()) {
            Toast.makeText(this, "Digite sua senha", Toast.LENGTH_SHORT).show();
        } else {
            UsuarioControler.getInstance().entrar(usuario);
        }
    }
}
