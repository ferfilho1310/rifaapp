package com.example.apprifa.Controlers;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.apprifa.Helpers.AccessFirebase;
import com.example.apprifa.Models.Usuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.apprifa.R;

public class CadastroUser extends AppCompatActivity {

    EditText ed_user_nome,ed_user_email,ed_user_senha,ed_user_confirmasenha;

    Button cadastro;

    RadioGroup rd_sexo;
    RadioButton rd_feminino,rd_masculino;

    String masculino,feminino;

    ProgressDialog pgd_cadastro_user;

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

        pgd_cadastro_user = new ProgressDialog(CadastroUser.this);

        rd_sexo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if(rd_feminino.isChecked()){

                    masculino = "Masculino";
                    new Usuario().setSexo(masculino);
                }

                if(rd_masculino.isChecked()){

                    feminino = "Feminino";
                    new Usuario().setSexo(feminino);

                }

            }
        });


        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pgd_cadastro_user.setMessage("Cadastrando");
                pgd_cadastro_user.show();

                new AccessFirebase().cadastrar_user(ed_user_nome.getText().toString(),ed_user_email.getText().toString()
                ,ed_user_senha.getText().toString(),ed_user_confirmasenha.getText().toString(),new Usuario().getSexo(),CadastroUser.this);

            }
        });

        pgd_cadastro_user.dismiss();
    }

}
