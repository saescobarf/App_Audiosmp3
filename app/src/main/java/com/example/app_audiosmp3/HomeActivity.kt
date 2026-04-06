package com.example.app_audiosmp3

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import java.io.File
import java.io.FileOutputStream

class HomeActivity : AppCompatActivity() {

    // Variable global para controlar el audio
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Canción 1
        findViewById<ImageButton>(R.id.btn_play_1).setOnClickListener { reproducirMusica(R.raw.cancion_1, "Reproduciendo Lo Logré")
        }
        // Canción 2
        findViewById<ImageButton>(R.id.btn_play_2).setOnClickListener { reproducirMusica(R.raw.cancion_2, "Reproduciendo Shiny")
        }
        // Canción 3
        findViewById<ImageButton>(R.id.btn_play_3).setOnClickListener { reproducirMusica(R.raw.cancion_3, "Reproduciendo No hay novedad")
        }
        // canciones 4, 5, 6 y 7
        findViewById<ImageButton>(R.id.btn_play_4).setOnClickListener { reproducirMusica(R.raw.cancion_4, "Reproduciendo Canción 4") }
        findViewById<ImageButton>(R.id.btn_play_5).setOnClickListener { reproducirMusica(R.raw.cancion_5, "Reproduciendo Canción 5") }
        findViewById<ImageButton>(R.id.btn_play_6).setOnClickListener { reproducirMusica(R.raw.cancion_6, "Reproduciendo Canción 6") }
        findViewById<ImageButton>(R.id.btn_play_7).setOnClickListener { reproducirMusica(R.raw.cancion_7, "Reproduciendo Canción 7") }


        // Configurar TODOS los botones de compartir
        val botonesCompartir = listOf(
            R.id.btn_share_1, R.id.btn_share_2, R.id.btn_share_3,
            R.id.btn_share_4, R.id.btn_share_5, R.id.btn_share_6, R.id.btn_share_7
        )

        botonesCompartir.forEach { id ->
            findViewById<ImageButton>(id).setOnClickListener {
                val intent = Intent(this, ShareActivity::class.java)
                startActivity(intent)
            }
        }
        // Aquí se elige qué canción enviar
        findViewById<ImageButton>(R.id.btn_blue_1).setOnClickListener {

            compartirAudioPorBluetooth(R.raw.cancion_1)
        }
        findViewById<ImageButton>(R.id.btn_blue_2).setOnClickListener {

            compartirAudioPorBluetooth(R.raw.cancion_2)
        }
        findViewById<ImageButton>(R.id.btn_blue_3).setOnClickListener {

            compartirAudioPorBluetooth(R.raw.cancion_3)
        }
        findViewById<ImageButton>(R.id.btn_blue_4).setOnClickListener {

            compartirAudioPorBluetooth(R.raw.cancion_4)
        }
        findViewById<ImageButton>(R.id.btn_blue_5).setOnClickListener {

            compartirAudioPorBluetooth(R.raw.cancion_5)
        }
        findViewById<ImageButton>(R.id.btn_blue_6).setOnClickListener {

            compartirAudioPorBluetooth(R.raw.cancion_6)
        }
        findViewById<ImageButton>(R.id.btn_blue_7).setOnClickListener {

            compartirAudioPorBluetooth(R.raw.cancion_7)
        }
    }

    private var currentSongId: Int = -1 // Esta variable guarda el ID de la canción actual

    private fun reproducirMusica(archivoId: Int, mensaje: String) {

        // primer caso: el usuario tocó la MISMA canción que ya estaba cargada
        if (currentSongId == archivoId && mediaPlayer != null) {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer?.pause() // Si estaba sonando, la pausamos
                Toast.makeText(this, "Pausado", Toast.LENGTH_SHORT).show()
            } else {
                mediaPlayer?.start() // Si estaba en pausa, la reanudamos
                Toast.makeText(this, "Reanudando...", Toast.LENGTH_SHORT).show()
            }
            return // Salimos de la función aquí para no reiniciar la canción
        }

        // Segundo caso: es una canción NUEVA o es la primera vez que se da Play
        // 1. Detenemos lo que sea que esté sonando
        mediaPlayer?.stop()
        mediaPlayer?.release()

        // 2. Creamos la nueva instancia con la canción elegida
        mediaPlayer = MediaPlayer.create(this, archivoId)
        currentSongId = archivoId // Guardamos el ID de esta nueva canción

        // 3. Iniciamos
        mediaPlayer?.start()

        // 4. Aviso en pantalla
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()

        // 5. Si la canción termina sola, limpiamos el ID
        mediaPlayer?.setOnCompletionListener {
            currentSongId = -1
        }
    }

    private fun compartirAudioPorBluetooth(resId: Int) {
        try {
            // Preparamos el archivo temporal
            val inputStream = resources.openRawResource(resId)
            val tempFile = File(cacheDir, "compartir_audio.mp3")
            val outputStream = FileOutputStream(tempFile)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()

            // Obtenemos la URI segura
            val contentUri = androidx.core.content.FileProvider.getUriForFile(
                this, "${packageName}.fileprovider", tempFile
            )

            // Creamos el Intent de envío
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "audio/mp3"
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            // Abrimos el menú de compartir (Bluetooth, etc.)
            startActivity(Intent.createChooser(shareIntent, "Compartir audio por..."))

            // Mostramos la alerta de los puntos
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("¡FELICIDADES!")
            builder.setMessage("Haz ganado un punto, Gracias por compartir el audio.")
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }

            // Usamos un pequeño retraso o lo mostramos directamente
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                builder.show()
            }, 2000)

        } catch (e: Exception) {
            Toast.makeText(this, "Error al preparar el archivo", Toast.LENGTH_SHORT).show()
        }
    }
}