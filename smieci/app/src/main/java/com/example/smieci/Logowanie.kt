package com.example.smieci

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
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

        findViewById<LinearLayout>(R.id.linearLayoutPomin).setOnClickListener{
            val intent_informacje = Intent(this, DodawanieInformacji::class.java)
            startActivity(intent_informacje)
        }
    }

}