package com.example.smieci

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.AppCompatImageView
import com.example.smieci.databinding.MainBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Calendar

class MainActivity : ComponentActivity() {

    private lateinit var binding: MainBinding
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val intent_logowanie = Intent(this, Logowanie::class.java)
        startActivity(intent_logowanie)


        //Ogólne funkcje i dane

        fun DateHandle(datePart : String) : String{
            if(datePart.toInt() < 10){
                return ("0" + datePart)
            } else {
                return datePart
            }
        }

        val formatDaty = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        //Dzisiejsza Data

        val calendar = Calendar.getInstance()
        var dzien = calendar.get(Calendar.DAY_OF_MONTH).toString()
        dzien = DateHandle(dzien)
        var miesiac = (calendar.get(Calendar.MONTH)+1).toString()
        miesiac = DateHandle(miesiac)
        val rok = calendar.get(Calendar.YEAR)
        var dzisiejszaData = "$rok-$miesiac-$dzien"


        //wpisywanie do bazy danych

        val intent = Intent(this, DodajDane::class.java)
        startActivity(intent)


        //wypisywanie z bazy danych

        db=DatabaseHelper(this)
        val wszystkie_daty = db.wypiszWszystkieDate(true, dzisiejszaData)

        val najblizsza = wszystkie_daty.minOfOrNull { it.date }?.let { LocalDate.parse(it).format(formatDaty)}?: "Pusta baza"


        // Termin Najbliższego wywozu i jego typ

        val TerminWywozu = findViewById<TextView>(R.id.termin_najblizszego_wywozu)
        TerminWywozu.setText(najblizsza.toString())

        val TloWywozu = findViewById<AppCompatImageView>(R.id.Tlo_kosz)
        TloWywozu.setImageResource(R.drawable.wywoz_bio)

        val GradientWywoz = findViewById<View>(R.id.gradient_kosz)
        GradientWywoz.setBackgroundResource(R.drawable.gradient_bio)


        //Obsługa wywozów w wybranym dniu w kalendarzu

        val kalendarz = findViewById<CalendarView>(R.id.calendar)
        val tekstWybranaData = findViewById<TextView>(R.id.WybranaData)

            //Wypisywanie dzisiejszej daty

            val datka = "$dzien.$miesiac.$rok"
            tekstWybranaData.setText(datka)

        kalendarz.setOnDateChangeListener { kalendarz, year, month, day ->
            val WybranaData = "$day.${month+1}.$year"
            tekstWybranaData.setText(WybranaData)

        }



    }


}