package com.example.apprifa.Controlers;

import android.content.Intent;
import android.os.Bundle;

import com.example.apprifa.Helpers.AccessFirebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.apprifa.R;

public class ResetSenha extends AppCompatActivity {

    EditText email_reset;
    Button reset_senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_senha);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        reset_senha = findViewById(R.id.btn_alterar_senha);
        email_reset = findViewById(R.id.ed_reset_email);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle("Trocar senha");

        reset_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AccessFirebase().reset_senha(email_reset.getText().toString(), ResetSenha.this);

            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(getApplicationContext(), EntrarUsuario.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finish();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:
                break;
        }
        return true;
    }

}
