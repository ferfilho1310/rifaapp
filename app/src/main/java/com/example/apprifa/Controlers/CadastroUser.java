package com.example.apprifa.Controlers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.apprifa.Helpers.AccessFirebase;
import com.example.apprifa.Models.Cliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.apprifa.R;

public class CadastroUser extends AppCompatActivity {

    EditText ed_user_nome, ed_user_email, ed_user_senha, ed_user_confirmasenha;

    Button cadastro;

    RadioGroup rd_sexo;
    RadioButton rd_feminino, rd_masculino;

    String masculino, feminino;

    Cliente usuario = new Cliente();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ed_user_nome = findViewById(R.id.ed_cad_user_nome);
        ed_user_email = findViewById(R.id.ed_cad_user_email);
        ed_user_senha = findViewById(R.id.ed_cad_user_senha);
        ed_user_confirmasenha = findViewById(R.id.ed_cad_user_confirmasenha);
        rd_sexo = findViewById(R.id.rd_sexos);
        cadastro = findViewById(R.id.btn_cadastrar);
        rd_feminino = findViewById(R.id.femi);
        rd_masculino = findViewById(R.id.masc);

        setTitle("Cadastro de usuário");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        rd_sexo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (rd_feminino == null) {

                    Toast.makeText(getApplicationContext(), "Informe o sexo", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (rd_masculino == null) {

                    Toast.makeText(getApplicationContext(), "Informe o sexo", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (rd_feminino.isChecked()) {

                    feminino = "Feminino";
                    usuario.setSexo(feminino);
                }

                if (rd_masculino.isChecked()) {

                    masculino = "Masculino";
                    usuario.setSexo(masculino);

                }
            }
        });

        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AccessFirebase().cadastrar_user(ed_user_nome.getText().toString(), ed_user_email.getText().toString()
                        , ed_user_senha.getText().toString(), ed_user_confirmasenha.getText().toString(), usuario.getSexo(), CadastroUser.this);

            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i_cad_user = new Intent(getApplicationContext(), EntrarUsuario.class);//ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(i_cad_user);//O efeito ao ser pressionado do botão (no caso abre a activity)
                finish();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:
                break;
        }
        return true;
    }

}
