package com.example.smieci

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.smieci.databinding.DodawanieLokalizacjiBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.location.Location
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.search.SearchBar
import org.w3c.dom.Text
import java.util.Locale

class DodawanieLokalizacji : AppCompatActivity() {

    private lateinit var binding: DodawanieLokalizacjiBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 100

    lateinit var miejscowosc : String
    lateinit var ulica : String

    lateinit var nazwaUzytkownika : String
    lateinit var email : String
    lateinit var haslo : String
    lateinit var zamieszkanieTyp : String
    lateinit var rodzinaRodzaj : String
    lateinit var rodzajWP : String
    lateinit var rodzajSegregacji : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DodawanieLokalizacjiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViewById<ConstraintLayout>(R.id.EkranLokalizacji).minHeight= wysokoscEkranu

        val zapisaneDane = ObslugaPrzechowywaniaDanych(this)

        val bundle = intent.extras
        nazwaUzytkownika = bundle?.getString("nazwaUzytkownika")!!
        email = bundle.getString("email")!!
        haslo = bundle.getString("haslo")!!
        zamieszkanieTyp = bundle.getString("zamieszkanieTyp")!!
        rodzinaRodzaj = bundle.getString("rodzinaRodzaj")!!
        rodzajWP = bundle.getString("rodzajWP")!!
        rodzajSegregacji = bundle.getString("rodzajSegregacji")!!

        //Obsługa przycisku COFNIJ
        findViewById<ImageView>(R.id.cofnij).setOnClickListener {
            finish()
        }

        // Obsługa przycisku UŻYJ LOKALIZACJI
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        findViewById<Button>(R.id.buttonUzyjLokalizacji).setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            } else {
                getUserLocation()
                findViewById<EditText>(R.id.editTextMiejscowosc).setText(miejscowosc)
                findViewById<EditText>(R.id.editTextUlica).setText(ulica)
                val gmina = findViewById<EditText>(R.id.editText_Gmina)
                try{
                    val connectionHelper = ConnectionHelper()
                    val connect = connectionHelper.connectionClass()
                    if(connect!=null){
                        val query = "SELECT g.Nazwa_gminy FROM Miejscowosc m JOIN Gminy g ON m.Id_gminy = g.Id_gminy WHERE m.Nazwa_miejscowosci = '$miejscowosc';"
                        val statement = connect.createStatement()
                        val result = statement.executeQuery(query)
                        while (result.next()){
                            gmina.setText(result.getString("Nazwa_gminy"))
                        }
                        if(gmina.text==null || gmina.text.toString() == ""){
                            Toast.makeText(this, "Przepraszamy, nie posiadamy tej miejscowości w bazie danych", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch(ex:Exception){
                    Log.e("ErrorGmina", ex.message?: "Nieznany błąd")
                }
            }
        }

        // Obsługa przycisku ZATWIERDŹ
        findViewById<Button>(R.id.buttonSubmit).setOnClickListener {

            miejscowosc = findViewById<EditText>(R.id.editTextMiejscowosc).text.toString()
            ulica = findViewById<EditText>(R.id.editTextUlica).text.toString()

            val idTypuZamieszkania = walidujDaneZamieszkania()
            val idLokalizacji = walidujLokalizacje()
            if(idLokalizacji==null){
                Toast.makeText(this, "Błędna lokalizacja", Toast.LENGTH_LONG).show()
            } else {
                try{
                    val connectionHelper = ConnectionHelper()
                    val connect = connectionHelper.connectionClass()
                    if(connect!=null){
                        val query = "INSERT INTO Uzytkownik VALUES (NULL, '$nazwaUzytkownika', '$haslo', '$email', 0, 0, $idLokalizacji, $idTypuZamieszkania)"
                        connect.prepareStatement(query).executeUpdate()
                        zapisaneDane.zapiszEmail(email)
                        zapisaneDane.zapiszNazweUzytkownika(nazwaUzytkownika)
                        zapisaneDane.zalogowany(true)
                        val intentMain = Intent(this, MainActivity::class.java)
                        startActivity(intentMain)
                    }
                }catch (ex : Exception){
                    Log.e("ErrorDodawanieUzytkownika", ex.message?: "Nieznany błąd")
                }
            }
        }

    }

    private fun walidujDaneZamieszkania() : Int?{
        try {
            val connectionHelper = ConnectionHelper()
            val connect = connectionHelper.connectionClass()
            if(connect!=null){
                var query : String
                var wybor : Boolean?
                when{
                    rodzinaRodzaj == "null" -> {
                        query = "SELECT id_rodzaj_zamieszkania FROM Rodzaj_zamieszkania WHERE zamieszkanie = '$zamieszkanieTyp' AND typ_rodziny is null AND typ_wysoka is null AND segregacja = '$rodzajSegregacji'"
                        wybor = true
                    }
                    rodzajWP == "null" -> {
                        query = "SELECT id_rodzaj_zamieszkania FROM Rodzaj_zamieszkania WHERE zamieszkanie = '$zamieszkanieTyp' AND typ_rodziny = '$rodzinaRodzaj' AND typ_wysoka is null AND segregacja = '$rodzajSegregacji'"
                        wybor = false
                    }
                    else -> {
                        query = "SELECT id_rodzaj_zamieszkania FROM Rodzaj_zamieszkania WHERE zamieszkanie = '$zamieszkanieTyp' AND typ_rodziny = '$rodzinaRodzaj' AND typ_wysoka = '$rodzajWP' AND segregacja = '$rodzajSegregacji'"
                        wybor = null
                    }
                }
                val statement = connect.createStatement()
                var result = statement.executeQuery(query)
                if(!result.next()){
                    when(wybor){
                        true -> {
                            query = "INSERT INTO Rodzaj_zamieszkania VALUES (NULL, '$zamieszkanieTyp', NULL, NULL, '$rodzajSegregacji');"
                        }
                        false -> {
                            query = "INSERT INTO Rodzaj_zamieszkania VALUES (NULL, '$zamieszkanieTyp', '$rodzinaRodzaj', NULL, '$rodzajSegregacji');"
                        }
                        else -> {
                            query = "INSERT INTO Rodzaj_zamieszkania VALUES (NULL, '$zamieszkanieTyp', '$rodzinaRodzaj', '$rodzajWP','$rodzajSegregacji');"
                        }
                    }
                    connect.prepareStatement(query).executeUpdate()
                    query = "SELECT COUNT(*) AS liczba FROM Rodzaj_zamieszkania"
                    result = statement.executeQuery(query)
                    if(result.next()){
                        return result.getInt("liczba")
                    }
                    return null
                } else {
                    return result.getInt("id_rodzaj_zamieszkania")
                }
            } else {
                return null
            }
        }catch (ex:Exception){
            Log.e("ErrorWalidacjiZamieszkania", ex.message?: "Nieznany błąd")
            return null
        }
    }

    private fun walidujLokalizacje(): Int?{
        try {
            val connectionHelper = ConnectionHelper()
            val connect = connectionHelper.connectionClass()
            if(connect!=null){
                var query = "SELECT * FROM Miejscowosc WHERE nazwa_miejscowosci = '$miejscowosc'"
                val statement = connect.createStatement()
                var result = statement.executeQuery(query)
                if(!result.next()){
                    Toast.makeText(this, "Przepraszamy, nie posiadamy tej miejscowości w bazie danych", Toast.LENGTH_LONG).show()
                    return null
                } else {
                    val id_miejscowosci = result.getInt("Id_miejscowosci")
                    query = "SELECT * FROM Lokalizacja l JOIN Miejscowosc m ON l.Id_miejscowosci = m.Id_miejscowosci JOIN Ulice u ON l.id_ulicy = u.id_ulicy WHERE m.Nazwa_miejscowosci = '$miejscowosc'  AND u.Nazwa_ulicy = '$ulica' "
                    result = statement.executeQuery(query)
                    if(result.next()){
                        return result.getInt("Id_lokalizacji")
                    } else {
                        return null
                    }
                }
            } else {
                return null
            }
        } catch (ex : Exception){
            Log.e("ErrorLokalizacja", ex.message?: "Nieznany błąd")
            return null
        }
    }

    private fun getUserLocation() {
        // Obsługa uprawnień
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    convertLocationToAdress(location.latitude, location.longitude)
                } else {
                    Toast.makeText(this, "Nie udało się uzyskać lokalizacji", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            // Brak uprawnień
            Toast.makeText(this, "Brak uprawnień do lokalizacji", Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                if (!::fusedLocationClient.isInitialized) {
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                }
                getUserLocation()
            } else {
                // Obsłuż brak uprawnień
                Toast.makeText(this, "Uprawnienia do lokalizacji nie zostały przyznane", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun convertLocationToAdress(latitude : Double, longitude: Double){
        try{
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if(addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                val addressText = address.getAddressLine(0)
                val wojewodztwo = address.adminArea
                val numerDomu = address.featureName
                miejscowosc = address.locality?: ""
                val powiat = address.subAdminArea
                ulica = address.thoroughfare?: ""
                val kodPocztowy = address.postalCode
                val kraj = address.countryName
            } else {
                Toast.makeText(this, "Nie znaleziono adresu", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Błąd konwersji lokalizacji na adres: ${e.message}", Toast.LENGTH_SHORT).show()
        }

    }
}
