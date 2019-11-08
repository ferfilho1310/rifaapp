package br.com.medeve.Controlers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import br.com.medeve.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler h_splash = new Handler();

        h_splash.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i_splash = new Intent(SplashScreen.this, EntrarUsuario.class);
                startActivity(i_splash);
                finish();

            }
        }, 2000);

    }
}
