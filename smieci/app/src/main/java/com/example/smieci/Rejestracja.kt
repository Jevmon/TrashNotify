package com.example.smieci

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.smieci.databinding.RejestracjaBinding

class Rejestracja : AppCompatActivity() {
    private lateinit var binding: RejestracjaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RejestracjaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var ekranRejestracji = findViewById<ConstraintLayout>(R.id.EkranRejestracji)
        ekranRejestracji.minHeight= wysokoscEkranu

        val zapisaneDane = ObslugaPrzechowywaniaDanych(this)


        val nazwaUzytkownika = findViewById<EditText>(R.id.editNazwaUzytkownika)
        val email = findViewById<EditText>(R.id.editTextEmail)
        val haslo = findViewById<EditText>(R.id.editTextHaslo)
        val hasloPowt = findViewById<EditText>(R.id.editTextHasloPow)

        findViewById<Button>(R.id.zatwierdz).setOnClickListener{
            if(nazwaUzytkownika.text!=null){
                if(walidacjaEmail(email.text.toString())){
                    if(haslo.text!=null && hasloPowt.text!=null){
                        if(haslo.text.toString() == hasloPowt.text.toString()){

                            zapisaneDane.zapiszNazweUzytkownika(nazwaUzytkownika.text.toString())
                            zapisaneDane.zapiszEmail(email.text.toString())

                            val intent_informacje = Intent(this, DodawanieInformacji::class.java)
                            intent_informacje.putExtra("nazwaUzytkownika", nazwaUzytkownika.text.toString())
                            intent_informacje.putExtra("email", email.text.toString())
                            intent_informacje.putExtra("haslo", haslo.text.toString())
                            startActivity(intent_informacje)

                        } else {
                            Toast.makeText(this,"Hasła nie są takie same", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this,"Podaj hasło", Toast.LENGTH_LONG).show()
                    }
                }else if(uniqueEmail(email.text.toString())){
                    Toast.makeText(this,"Ten email jest już używany", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this,"Błędny email", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this,"Podaj nazwę użytkownika", Toast.LENGTH_LONG).show()
            }
        }

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

    private fun walidacjaEmail(email:String) : Boolean{
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$".toRegex()
        return emailRegex.matches(email)
    }

    private fun uniqueEmail(email: String) : Boolean{
        try{
            val connectionHelper = ConnectionHelper()
            val connect = connectionHelper.connectionClass()
            if(connect!=null){
                val query = "SELECT email FROM Uzytkownik"
                val statement = connect.createStatement()
                val result = statement.executeQuery(query)
                while (result.next()){
                    if(result.getString("email") == email){
                        return true
                    }
                }
                return false
            }
        }catch (ex:Exception){
            Log.e("ErrorEmaile",ex.message?: "Nieznany błąd")
        }
        return true
    }
}