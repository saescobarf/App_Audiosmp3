package com.example.app_audiosmp3

import android.content.Intent // ESTA IMPORTACIÓN ES NECESARIA
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val logo = findViewById<ImageView>(R.id.mainImage)
        val texto = findViewById<TextView>(R.id.mainText)


        val animAparecer = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba)
        logo.startAnimation(animAparecer)
        texto.startAnimation(animAparecer)


        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            finish()
        }, 3000)
    }
}