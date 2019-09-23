package com.example.apprifa.Controlers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.apprifa.Helpers.AccessFirebase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.apprifa.R;

public class Reset_senha extends AppCompatActivity {

    EditText email_reset;
    Button reset_senha;

    ProgressDialog prg_reset_senha;

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

        prg_reset_senha = new ProgressDialog(Reset_senha.this);

        setTitle("Trocar senha");

        reset_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                prg_reset_senha.setMessage("Enviando...");
                prg_reset_senha.show();

                new AccessFirebase().reset_senha(email_reset.getText().toString(),Reset_senha.this);

                prg_reset_senha.dismiss();

                Intent i_reset = new Intent(getApplicationContext(),EntrarUsuario.class);
                startActivity(i_reset);
                finish();
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
