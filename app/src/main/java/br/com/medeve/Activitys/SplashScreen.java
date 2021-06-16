package br.com.medeve.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import br.com.medeve.R;

public class SplashScreen extends AppCompatActivity {

    TextView txt_versao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        txt_versao = findViewById(R.id.versao_app);

        Handler h_splash = new Handler();

        h_splash.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i_splash = new Intent(SplashScreen.this, EntrarUsuarioActView.class);
                startActivity(i_splash);
                finish();

            }
        }, 2000);

        try {
            String version = this.getPackageManager().getPackageInfo("br.com.medeve", 0).versionName;
            txt_versao.setText(version);
        } catch (Exception e) {

            Log.d("Erro", "Versão não encontrada", e);
        }
    }
}
