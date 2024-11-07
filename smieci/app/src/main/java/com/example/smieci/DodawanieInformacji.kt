package com.example.smieci

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smieci.databinding.DodawanieInformacjiBinding
import com.example.smieci.databinding.DodawanieLokalizacjiBinding

class DodawanieInformacji : AppCompatActivity() {
    private lateinit var binding: DodawanieInformacjiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DodawanieInformacjiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViewById<Button>(R.id.buttonSubmit).setOnClickListener{
            val intent_lokalizacja = Intent(this, DodawanieLokalizacji::class.java)
            startActivity(intent_lokalizacja)
        }
    }
}