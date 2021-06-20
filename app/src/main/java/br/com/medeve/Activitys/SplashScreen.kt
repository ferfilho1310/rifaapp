package br.com.medeve.Activitys

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import br.com.medeve.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val h_splash = Handler()

        h_splash.postDelayed({
            val i_splash = Intent(this@SplashScreen, EntrarUsuarioActView::class.java)
            startActivity(i_splash)
            finish()
        }, 2000)

        try {
            val version = this.packageManager.getPackageInfo("br.com.medeve", 0).versionName
            versao_app.text = version
        } catch (e: Exception) {
            Log.d("Erro", "Versão não encontrada", e)
        }

    }
}