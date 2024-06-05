package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.dicoding.asclepius.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.
        val uriImg = intent.getStringExtra("EXTRA_IMAGE_URI")
        val result = intent.getStringExtra("EXTRA_RESULT")

        binding.resultText.setText(result)
        Glide.with(binding.root.context)
            .load(Uri.parse(uriImg))
            .into(binding.resultImage)

        Toast.makeText(this, "Gambar berhasil dianalisa", Toast.LENGTH_SHORT).show()
    }
}