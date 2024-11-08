package com.example.smieci

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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

        //Obsługa przycisku POMIŃ
        findViewById<LinearLayout>(R.id.linearLayoutPomin).setOnClickListener{
            val intent_informacje = Intent(this, DodawanieInformacji::class.java)
            startActivity(intent_informacje)
        }

        //Obsługa przycisku ZATWIERDŹ
        findViewById<Button>(R.id.button).setOnClickListener {
            val email = findViewById<EditText>(R.id.editTextEmail).text
            val password = findViewById<EditText>(R.id.editTextHaslo).text
            //Toast.makeText(this, "${email} ${password}", Toast.LENGTH_LONG).show()
        }
    }

}