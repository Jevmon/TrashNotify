package com.example.smieci

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smieci.databinding.LogowanieBinding

class Logowanie : AppCompatActivity() {
    private lateinit var binding: LogowanieBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LogowanieBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}