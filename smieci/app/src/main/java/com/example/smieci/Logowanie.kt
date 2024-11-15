package com.example.smieci

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smieci.databinding.LogowanieBinding

class Logowanie : AppCompatActivity() {
    private lateinit var binding: LogowanieBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LogowanieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViewById<ConstraintLayout>(R.id.EkranLogowania).minHeight= wysokoscEkranu

        val zapisaneDane = ObslugaPrzechowywaniaDanych(this)

        //Obsługa przycisku ZATWIERDŹ
        findViewById<Button>(R.id.button).setOnClickListener {
            val email = findViewById<EditText>(R.id.editTextEmail).text
            val password = findViewById<EditText>(R.id.editTextHaslo).text
            try{
                val connectionHelper = ConnectionHelper()
                val connect = connectionHelper.connectionClass()
                if(connect!=null){
                    var query = "SELECT * FROM Uzytkownik WHERE Email = '$email' AND Haslo = '$password'"
                    val statement = connect.createStatement()
                    val result = statement.executeQuery(query)
                    if(result.next()){
                        zapisaneDane.zapiszNazweUzytkownika(result.getString("Nazwa_uzytkownika"))
                        zapisaneDane.zapiszEmail(result.getString("Email"))
                        zapisaneDane.zalogowany(true)
                        val intent_main = Intent(this, MainActivity::class.java)
                        startActivity(intent_main)
                    } else {
                        Toast.makeText(this, "Błędne dane", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (ex: Exception){
                Log.e("ErrorLogowanie", ex.message?: "Nieznany błąd")
            }
        }

        //Obsługa przycisku Zarejestruj się
        findViewById<TextView>(R.id.textOdnosnikRejestracja).setOnClickListener{
            val intent_rejestracja = Intent(this, Rejestracja::class.java)
            startActivity(intent_rejestracja)
        }
    }

}