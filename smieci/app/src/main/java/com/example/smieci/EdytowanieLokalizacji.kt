package com.example.smieci

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.smieci.databinding.EdytowanieLokalizacjiBinding

class EdytowanieLokalizacji : AppCompatActivity() {
    private lateinit var binding: EdytowanieLokalizacjiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EdytowanieLokalizacjiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var edytowanieLokalizacji = findViewById<ConstraintLayout>(R.id.EkranEdycjaLokalizacji)
        edytowanieLokalizacji.minHeight= wysokoscEkranu

        val zapisaneDane = ObslugaPrzechowywaniaDanych(this)

        findViewById<ImageView>(R.id.cofnij).setOnClickListener {
            var intent_userPanel = Intent(this, UserPanel::class.java)
            startActivity(intent_userPanel)
        }

        findViewById<ToggleButton>(R.id.toggleLokalizacja).setOnClickListener {
            var intent_edytowanieInformacji = Intent(this, EdytowanieInformacji::class.java)
            startActivity(intent_edytowanieInformacji)
        }

    }
}