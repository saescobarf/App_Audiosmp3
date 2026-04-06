package com.example.app_audiosmp3

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ShareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_share)

        // Ajustamos los márgenes de barras del sistema
        val rootLayout = findViewById<LinearLayout>(R.id.share_main)
        if (rootLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        // 1. Referenciamos los botones del XML
        val btnFacebook = findViewById<LinearLayout>(R.id.option_facebook)
        val btnWhatsApp = findViewById<LinearLayout>(R.id.option_whatsapp)
        val btnX = findViewById<LinearLayout>(R.id.option_x)
        val btnInstagram = findViewById<LinearLayout>(R.id.option_instagram)
        val btnBack = findViewById<ImageButton>(R.id.btn_back_share)

        val mensaje = "¡Encuentra la mejor variedad de sonidos en Audio Coppel!"

        // 2. Eventos Clic
        btnFacebook?.setOnClickListener { compartirEnApp("com.facebook.katana", mensaje) }
        btnWhatsApp?.setOnClickListener { compartirEnApp("com.whatsapp", mensaje) }
        btnX?.setOnClickListener { compartirEnApp("com.twitter.android", mensaje) }
        btnInstagram?.setOnClickListener { compartirEnApp("com.instagram.android", mensaje) }

        // Botón de flecha azul para regresar
        btnBack?.setOnClickListener {
            finish()
        }
    }

    private fun compartirEnApp(packageName: String, mensaje: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, mensaje)
        intent.setPackage(packageName)

        try {
            startActivity(intent)
        } catch (e: Exception) {
            // Si la app no está instalada, mostramos este aviso
            Toast.makeText(this, "La aplicación no está instalada", Toast.LENGTH_SHORT).show()

            // Si no está la app específica, abrir el menú general de compartir
            val intentGenerico = Intent(Intent.ACTION_SEND)
            intentGenerico.type = "text/plain"
            intentGenerico.putExtra(Intent.EXTRA_TEXT, mensaje)
            startActivity(Intent.createChooser(intentGenerico, "Compartir con:"))
        }
    }
}