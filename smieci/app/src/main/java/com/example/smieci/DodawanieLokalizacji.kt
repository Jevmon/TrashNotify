package com.example.smieci

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.smieci.databinding.DodawanieLokalizacjiBinding

class DodawanieLokalizacji : AppCompatActivity() {

    private lateinit var binding: DodawanieLokalizacjiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DodawanieLokalizacjiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViewById<Button>(R.id.buttonSubmit).setOnClickListener{
            val intent_main = Intent(this, MainActivity::class.java)
            startActivity(intent_main)
        }

    }
}