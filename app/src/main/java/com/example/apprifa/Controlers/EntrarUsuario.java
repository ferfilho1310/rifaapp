package com.example.apprifa.Controlers;

import android.content.Intent;
import android.os.Bundle;

import com.example.apprifa.Helpers.AccessFirebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.apprifa.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class EntrarUsuario extends AppCompatActivity {

    Button btn_entrar,btn_user_cadastrar;
    EditText ed_email, ed_senha;

    FirebaseAuth db_users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrar_usuario);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn_entrar = findViewById(R.id.btn_entrar);
        btn_user_cadastrar = findViewById(R.id.btn_entrar_cadastrar);
        ed_email = findViewById(R.id.ed_entrar_email);
        ed_senha = findViewById(R.id.ed_entrar_senha);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db_users = FirebaseAuth.getInstance();

        FirebaseApp.initializeApp(EntrarUsuario.this);

        new AccessFirebase().persistir_usuer(EntrarUsuario.this);

        btn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               new AccessFirebase().entrar_firebase(ed_email.getText().toString(),ed_senha.getText().toString(),EntrarUsuario.this);

            }
        });

        btn_user_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i_cadastrar = new Intent(getApplicationContext(),CadastroUser.class);
                startActivity(i_cadastrar);

            }
        });

    }

}
