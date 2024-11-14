package com.example.smieci

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.Notification
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
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.ActionBarDrawerToggle
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Message
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import android.view.MenuItem


class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainBinding
    private lateinit var db: DatabaseHelper

    private val channelId = "Kosze01"

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    lateinit var toggle: ActionBarDrawerToggle

    private var iloscPowi : Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Funkcja tworząca globalną zmienną
        val displayMetrics  = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        pobierzWysokosc(screenHeight)

        val zapisaneDane = ObslugaPrzechowywaniaDanych(this)
        iloscPowi = zapisaneDane.iloscPowiadomien()


        //Obsługa powiadomień

        if(zapisaneDane.ustawieniePowiadomienia()){

            createNotificationChannel()

            requestNotificationPermission(this)

            powiadomienie()
            zapisaneDane.dodajPowiadomienie()

        } else {
            val notificationManager = NotificationManagerCompat.from(this)
            notificationManager.cancelAll()
            zapisaneDane.zresetujPowiadomienia()
        }
        val intent_userpanel = Intent(this, UserPanel::class.java)

        //tworzenie powiazania z menu
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(binding.toolbar)

        toggle = ActionBarDrawerToggle(
            this, // activity
            drawerLayout,
            toolbar,
            R.string.open,
            R.string.close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

       // toolbar.setNavigationIcon(R.drawable.user_icon_vector)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> drawerLayout.closeDrawers()
                R.id.nav_settings -> startActivity(intent_userpanel)
                R.id.nav_about -> Toast.makeText(applicationContext, "ab", Toast.LENGTH_SHORT).show()
                //wylogowywanie
                R.id.nav_logout -> Toast.makeText(applicationContext, "logout", Toast.LENGTH_SHORT).show()
                }
            true
        }


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
        //POPRAWIC TO TRZEBA
        /*
        val TerminWywozu = findViewById<TextView>(R.id.termin_najblizszego_wywozu)
        //TerminWywozu.setText(zapisaneDane.iloscPowiadomien().toString())
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

         */

    }


    //wybieranie opcji - nawigacja
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return false
    }

    private fun powiadomienie() {
        checkExactAlarmPermission()
        val intent = Intent(applicationContext, Powiadomienia::class.java)
        // tytuł i tekst powiadomienia
        val title = "E żydzie!"
        val message = "Czas na piec ;D"
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            iloscPowi,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }


    private fun getTime(): Long{
        //ustawienie kiedy powiadomienie
        val minute = 0
        val hour = 9
        val day = 13
        val month = 11
        val year = 2024

        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day, hour, minute)
        return calendar.timeInMillis
    }

    private fun createNotificationChannel() {
        val name = "KanlendarzKosze"
        val desc = "Powiadomienia o wystawianiu koszy"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }



    private fun checkExactAlarmPermission(){
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()){
            Toast.makeText(this, "Proszę włączyć dokładne alarmy dla tej apliakcji", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, Uri.parse("package:$packageName"))
            startActivity(intent)
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

