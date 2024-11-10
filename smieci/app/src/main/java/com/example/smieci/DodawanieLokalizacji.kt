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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.search.SearchBar
import java.util.Locale

class DodawanieLokalizacji : AppCompatActivity() {

    private lateinit var binding: DodawanieLokalizacjiBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DodawanieLokalizacjiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViewById<ConstraintLayout>(R.id.EkranLokalizacji).minHeight= wysokoscEkranu

        // Obsługa przycisku ZATWIERDŹ
        findViewById<Button>(R.id.buttonSubmit).setOnClickListener {
            val intentMain = Intent(this, MainActivity::class.java)
            startActivity(intentMain)
        }

        // Obsługa przycisku UŻYJ LOKALIZACJI
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        findViewById<Button>(R.id.buttonUzyjLokalizacji).setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            } else {
                getUserLocation()
            }
        }

        //findViewById<TextView>(R.id.textGminaWybierz).setOnClickListener{
        //
        //}
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
                val miasto = address.locality
                val powiat = address.subAdminArea
                val nwm = address.subLocality //Null gdy w Szczercowie
                val ulica = address.thoroughfare
                val kodPocztowy = address.postalCode
                val kraj = address.countryName
                Toast.makeText(this, "Lokalizacja: ${addressText}", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Nie znaleziono adresu dla podanych współrzędnych", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Błąd konwersji lokalizacji na adres: ${e.message}", Toast.LENGTH_SHORT).show()
        }

    }
}
