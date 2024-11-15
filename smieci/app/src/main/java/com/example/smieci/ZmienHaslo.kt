package com.example.smieci

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smieci.databinding.ZmienHasloBinding
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class ZmienHaslo : AppCompatActivity() {

    private lateinit var binding : ZmienHasloBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ZmienHasloBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var ekranZmianyHasla = findViewById<ConstraintLayout>(R.id.EkranZmienHaslo)
        ekranZmianyHasla.minHeight= wysokoscEkranu

        var zapisaneDane = ObslugaPrzechowywaniaDanych(this)

        //Obsługa cofnięcia
        findViewById<ImageView>(R.id.cofnij).setOnClickListener {
            var intent_userPanel = Intent(this, UserPanel::class.java)
            startActivity(intent_userPanel)
        }

        var haslo = findViewById<EditText>(R.id.editTextAktualneHaslo)
        var noweHaslo = findViewById<EditText>(R.id.editTextNoweHaslo)
        var powtorzNoweHaslo = findViewById<EditText>(R.id.editTextNoweHasloPow)

        findViewById<Button>(R.id.zatwierdz).setOnClickListener {
            //Sprawdzenie poprawności hasła
            try{
                val connectionHelper = ConnectionHelper()
                val connect = connectionHelper.connectionClass()
                if(connect!=null){
                    var query = "SELECT Haslo FROM Uzytkownik WHERE Email='${zapisaneDane.email()}'"
                    val statement = connect.createStatement()
                    var result = statement.executeQuery(query)
                    while(result.next()){
                        if(haslo.text.toString() == result.getString("Haslo")){
                            if(noweHaslo.text.toString() == powtorzNoweHaslo.text.toString()){
                                query = "UPDATE Uzytkownik SET Haslo = '${noweHaslo.text}' WHERE Email='${zapisaneDane.email()}'"
                                connect.prepareStatement(query).executeUpdate()
                                Toast.makeText(this,"Hasło zostało zmienione", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Hasła muszę być takie same", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, "Niepoprawne hasło", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (ex : Exception){
                Log.e("ErrorZamianaHasła", ex.message?: "Nieznany błąd")
            }
        }

    }
}