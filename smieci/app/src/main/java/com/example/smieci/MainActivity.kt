package com.example.smieci

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
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
import androidx.appcompat.app.AppCompatActivity
import android.os.Build
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat


class MainActivity : ComponentActivity() {

    private lateinit var binding: MainBinding
    private lateinit var db: DatabaseHelper

    private val channelId = "Kosze01"
    private val channelName = "Przypomnienie_o_koszach"
    private val notificationId = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Funkcja tworząca globalną zmienną
        val displayMetrics  = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        pobierzWysokosc(screenHeight)

        //Tworzenie kanału do powiadomień (żeby nie było kolizji pomiędzy powiadomieniami)
        //createNotificationChannel()

        //Wysyłanie powiadomienia
        //sendNotification()


        val intent_logowanie = Intent(this, UserPanel::class.java)
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

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "Kanał powiadomień dla aplikacji przykład"
            }

            val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Jeśli brak uprawnień, poproś o ich nadanie
            requestNotificationPermission(this)
            return  // Przerwij, jeśli nie ma uprawnień
        }
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent : PendingIntent = PendingIntent.getActivity(this, 0 ,intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("Powiadomenie o koszach")
            .setContentText("Jutro wywozy papierów \nNie zapomnij wystawić kosza! ;)")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId, builder.build())
        }
    }

}

public fun requestNotificationPermission(context : Context){
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.POST_NOTIFICATIONS),0)
        }
    }
}

//Globalna zmienna przechowująca wysokość
public var wysokoscEkranu : Int = 0

fun pobierzWysokosc(wysokosc : Int){
    wysokoscEkranu = (wysokosc - wysokosc*0.1).toInt()
}