package com.example.smieci

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smieci.databinding.RejestracjaBinding

class Rejestracja : AppCompatActivity() {
    private lateinit var binding: RejestracjaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RejestracjaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var ekranRejestracji = findViewById<ConstraintLayout>(R.id.EkranRejestracji)
        ekranRejestracji.minHeight= wysokoscEkranu

        //Obsługa przycisku POMIŃ
        findViewById<LinearLayout>(R.id.linearLayoutPomin).setOnClickListener{
            val intent_informacje = Intent(this, DodawanieInformacji::class.java)
            startActivity(intent_informacje)
        }

        //obsluga przycisku Zaloguj sie
        findViewById<TextView>(R.id.textOdnosnikLogowanie).setOnClickListener{
            val intent_logowanie = Intent(this, Logowanie::class.java)
            startActivity(intent_logowanie)
        }

    }
}