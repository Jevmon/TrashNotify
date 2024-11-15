package com.example.smieci

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smieci.databinding.EdytowanieInformacjiBinding

class EdytowanieInformacji : AppCompatActivity() {

    lateinit var binding: EdytowanieInformacjiBinding

    var email : String = ""

    lateinit var zamieszkaly: ToggleButton
    lateinit var niezamieszkaly: ToggleButton
    lateinit var jednorodzinna: ToggleButton
    lateinit var wielorodzinna: ToggleButton
    lateinit var wysoka: ToggleButton
    lateinit var pozostale: ToggleButton
    lateinit var segregujacy: ToggleButton
    lateinit var niesegregujacy: ToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EdytowanieInformacjiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicjalizacja przycisków po załadowaniu widoku
        zamieszkaly = findViewById(R.id.zamieszkaly)
        niezamieszkaly = findViewById(R.id.niezamieszkaly)
        jednorodzinna = findViewById(R.id.jednorodzinna)
        wielorodzinna = findViewById(R.id.wielorodzinna)
        wysoka = findViewById(R.id.wysoka)
        pozostale = findViewById(R.id.pozostale)
        segregujacy = findViewById(R.id.segregujacy)
        niesegregujacy = findViewById(R.id.niesegregujacy)

        var edytowanieInformacji = findViewById<ConstraintLayout>(R.id.ekranEdycjaInformacji)
        edytowanieInformacji.minHeight = wysokoscEkranu

        val zapisaneDane = ObslugaPrzechowywaniaDanych(this)

        email = zapisaneDane.email().toString()

        ustaw()

        findViewById<ImageView>(R.id.cofnij).setOnClickListener {
            var intent_userPanel = Intent(this, UserPanel::class.java)
            startActivity(intent_userPanel)
        }

        var rodzajZamieszkania : String = ""
        var rodzinaRodzaj : String? = null
        var rodzajWP : String? = null
        var rodzajSegregacji : String = ""

        zamieszkaly.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                niezamieszkaly.isChecked = false
                waliduj()
                rodzajZamieszkania = "zamieszkała"

            } else {
                rodzinaRodzaj = null
                rodzajWP = null
            }
        }
        niezamieszkaly.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                zamieszkaly.isChecked = false
                waliduj()
                rodzajZamieszkania = "niezamieszkała"
                rodzinaRodzaj = null
                rodzajWP = null
            }
        }
        jednorodzinna.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                wielorodzinna.isChecked = false
                waliduj()
                rodzinaRodzaj = jednorodzinna.text.toString()
                rodzajWP = null
            }
        }
        wielorodzinna.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                jednorodzinna.isChecked = false
                waliduj()
                rodzinaRodzaj = wielorodzinna.text.toString()
            }else{
                rodzajWP = null
            }
        }
        wysoka.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                pozostale.isChecked = false
                waliduj()
                rodzajWP = wysoka.text.toString()
            }
        }
        pozostale.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                wysoka.isChecked = false
                waliduj()
                rodzajWP = pozostale.text.toString()
            }
        }
        segregujacy.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                niesegregujacy.isChecked = false
                rodzajSegregacji = segregujacy.text.toString()
            }
        }
        niesegregujacy.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                segregujacy.isChecked = false
                rodzajSegregacji = niesegregujacy.text.toString()
            }
        }

        findViewById<ToggleButton>(R.id.toggleLokalizacja).setOnClickListener {
            var intent_edytowanieLokalizacji = Intent(this, EdytowanieLokalizacji::class.java)
            startActivity(intent_edytowanieLokalizacji)
        }

        findViewById<Button>(R.id.buttonSubmit).setOnClickListener {
            if(walidacja()){
                try {
                    val connectionHelper = ConnectionHelper()
                    val connect = connectionHelper.connectionClass()
                    if (connect != null) {
                        var query: String
                        when {
                            rodzinaRodzaj == null -> {
                                query = "SELECT id_rodzaj_zamieszkania FROM Rodzaj_zamieszkania WHERE zamieszkanie = '$rodzajZamieszkania' AND typ_rodziny is null AND typ_wysoka is null AND segregacja = '$rodzajSegregacji'"
                            }
                            rodzajWP == null -> {
                                query = "SELECT id_rodzaj_zamieszkania FROM Rodzaj_zamieszkania WHERE zamieszkanie = '$rodzajZamieszkania' AND typ_rodziny = '$rodzinaRodzaj' AND typ_wysoka is null AND segregacja = '$rodzajSegregacji'"
                            }
                            else -> {
                                query = "SELECT id_rodzaj_zamieszkania FROM Rodzaj_zamieszkania WHERE zamieszkanie = '$rodzajZamieszkania' AND typ_rodziny = '$rodzinaRodzaj' AND typ_wysoka = '$rodzajWP' AND segregacja = '$rodzajSegregacji'"
                            }
                        }
                        val statement = connect.createStatement()
                        var result = statement.executeQuery(query)
                        if(result.next()){
                            query = "UPDATE Uzytkownik SET id_rodzaj_zamieszkania = ${result.getInt("id_rodzaj_zamieszkania")} WHERE email = '$email'"
                            connect.prepareStatement(query).executeUpdate()
                            Toast.makeText(this, "Zmieniono informacje", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (ex:Exception){
                    Log.e("ErrorZapiszEdycjeInformacji", ex.message?: "Nieznany błąd")
                }
            } else {
                Toast.makeText(this, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun ustaw(){

        try{
            val connectionHelper = ConnectionHelper()
            val connect = connectionHelper.connectionClass()
            if(connect!=null){
                val query = "SELECT rz.zamieszkanie, rz.typ_rodziny, rz.typ_wysoka, rz.segregacja FROM Uzytkownik AS u JOIN Rodzaj_zamieszkania as rz ON u.id_rodzaj_zamieszkania = rz.id_rodzaj_zamieszkania WHERE u.Email = '$email';"
                val statement = connect.createStatement()
                val result = statement.executeQuery(query)
                while(result.next()){
                    if(result.getString("zamieszkanie") == "zamieszkała"){
                        zamieszkaly.isChecked = true
                        niezamieszkaly.isChecked = false
                    } else {
                        zamieszkaly.isChecked = false
                        niezamieszkaly.isChecked = true
                    }
                    if(result.getString("typ_rodziny") == "jednorodzinna"){
                        jednorodzinna.isChecked = true
                        wielorodzinna.isChecked = false
                    } else {
                        jednorodzinna.isChecked = false
                        wielorodzinna.isChecked = true
                    }
                    if(result.getString("typ_wysoka") == "wysoka"){
                        wysoka.isChecked = true
                        pozostale.isChecked = false
                    } else{
                        wysoka.isChecked = false
                        pozostale.isChecked = true
                    }
                    if(result.getString("segregacja") == "segregacja"){
                        segregujacy.isChecked = true
                        niesegregujacy.isChecked = false
                    } else {
                        segregujacy.isChecked = false
                        niesegregujacy.isChecked = true
                    }
                }
                waliduj()

            }
        }catch (ex: Exception){
            Log.e("Error", ex.message?: "Nieznany błąd")
        }
    }

    private fun waliduj(){
        if(zamieszkaly.isChecked){
            findViewById<LinearLayout>(R.id.rodzina).visibility = LinearLayout.VISIBLE
            if(wielorodzinna.isChecked){
                findViewById<LinearLayout>(R.id.wp).visibility = LinearLayout.VISIBLE
            } else {
                findViewById<LinearLayout>(R.id.wp).visibility = LinearLayout.GONE
                wysoka.isChecked = false
                pozostale.isChecked = false
            }
        } else {
            findViewById<LinearLayout>(R.id.rodzina).visibility = LinearLayout.GONE
            jednorodzinna.isChecked = false
            findViewById<LinearLayout>(R.id.wp).visibility = LinearLayout.GONE
            wielorodzinna.isChecked = false
        }
    }

    private fun walidacja() : Boolean{
        if(segregujacy.isChecked || niesegregujacy.isChecked){
            if(zamieszkaly.isChecked){
                if(wielorodzinna.isChecked){
                    if(wysoka.isChecked || pozostale.isChecked){
                        return true
                    } else {
                        return false
                    }
                } else if(jednorodzinna.isChecked) {
                    return true
                } else {
                    return false
                }
            } else if(niezamieszkaly.isChecked){
                return true
            } else {
                return false
            }
        } else {
            return false
        }
    }
}