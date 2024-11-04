package com.example.smieci

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smieci.databinding.ActivityDodajDaneBinding

class DodajDane : AppCompatActivity() {

    private lateinit var binding: ActivityDodajDaneBinding
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDodajDaneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db= DatabaseHelper(this)

        var dana = Dane( "2024-11-08","Bio")
        db.wpiszDate(dana)
        dana = Dane( "2023-11-08","Papier")
        db.wpiszDate(dana)
        dana = Dane( "2024-11-12","Metal")
        db.wpiszDate(dana)
        //dana = Dane(1, "2024-11-08","Bio")
        //db.wpiszDate(dana)
        finish()


    }
}