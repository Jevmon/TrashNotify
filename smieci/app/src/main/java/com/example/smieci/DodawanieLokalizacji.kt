package com.example.smieci

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.ScrollView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.smieci.databinding.DodawanieLokalizacjiBinding
import com.example.smieci.databinding.WyszukiwanieBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.internal.ViewUtils.hideKeyboard
import java.util.Locale

class DodawanieLokalizacji : AppCompatActivity() {

    private lateinit var binding: DodawanieLokalizacjiBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 100

    //lateinit var gminyLV: ListView

    //var lista = arrayOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DodawanieLokalizacjiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViewById<ConstraintLayout>(R.id.EkranLokalizacji).minHeight = wysokoscEkranu

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        //Obsługa cofnięcia
        findViewById<ImageView>(R.id.cofnij).setOnClickListener {
            var intent_dodawanieinf = Intent(this, DodawanieInformacji::class.java)
            startActivity(intent_dodawanieinf)
        }


        findViewById<Button>(R.id.buttonSubmit).setOnClickListener {
            val intentMain = Intent(this, MainActivity::class.java)
            startActivity(intentMain)
        }


        // Obsługa przycisku UŻYJ LOKALIZACJI
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        findViewById<Button>(R.id.buttonUzyjLokalizacji).setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            } else {
                getUserLocation()
            }
        }




        //scrollowanie listy gmina
        var listviewGminy = findViewById<ListView>(R.id.list_View_Gminy)
        var dScrollView = findViewById<ScrollView>(R.id.scrollViewD)

        listviewGminy.setOnTouchListener { _, event ->
            dScrollView.requestDisallowInterceptTouchEvent(true)
            when (event.actionMasked) {
                MotionEvent.ACTION_UP -> dScrollView.requestDisallowInterceptTouchEvent(false)
            }
            false
        }



        //lista gminy
        var gminy = arrayOf<String>("Gmina")

        val adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
            this@DodawanieLokalizacji,
            R.layout.text,
            gminy
        )
        listviewGminy.adapter = adapter
        adapter.notifyDataSetChanged()


        val textGmina = findViewById<EditText>(R.id.textGminaWybierz)
        val textGminaParent = findViewById<LinearLayout>(R.id.listViewParentG)
        val gminaLayout = findViewById<LinearLayout>(R.id.layoutGminaWybierz)

        // Obsługa kliknięcia na textGmina - pokazanie listy
        gminaLayout.setOnClickListener {
            textGminaParent.visibility = if (textGminaParent.visibility == View.VISIBLE) View.GONE else View.VISIBLE

        }
        textGmina.setOnFocusChangeListener{v, hasFocus ->
            if(hasFocus){
                textGminaParent.visibility = View.VISIBLE
            }
            else if(!hasFocus){
                textGminaParent.visibility = View.GONE

            }
        }


        //wybieranie elementu z listy
        listviewGminy.setOnItemClickListener { parent, view, position, id ->
            // Ustawienie klikniętego elementu w EditText
            val selectedItem = gminy[position]
            textGmina.setText(selectedItem)
            textGminaParent.visibility = View.GONE
            textGmina.clearFocus()
        }

        //Filtrowanie listy gmina
        textGmina.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s) // Filtrowanie wyników
            }
        })




        //scrollowanie listy miejcowosci
        var listviewMiejc = findViewById<ListView>(R.id.list_View_Miejsc)

        listviewMiejc.setOnTouchListener { _, event ->
            dScrollView.requestDisallowInterceptTouchEvent(true)
            when (event.actionMasked) {
                MotionEvent.ACTION_UP -> dScrollView.requestDisallowInterceptTouchEvent(false)
            }
            false
        }

        //lista miejscowosci
        var miejscowosci = arrayOf<String>("Miejscowosc")

        val adapterM: ArrayAdapter<String?> = ArrayAdapter<String?>(
            this@DodawanieLokalizacji,
            R.layout.text,
            miejscowosci
        )
        listviewMiejc.adapter = adapterM
        adapterM.notifyDataSetChanged()

        val textMiejsc = findViewById<EditText>(R.id.textMiejscowoscWybierz)
        val textMiejscParent = findViewById<LinearLayout>(R.id.listViewParentM)
        val textMiejscLayout= findViewById<LinearLayout>(R.id.layoutMiejscWybierz)

        // Obsługa kliknięcia na textGmina - pokazanie listy
        textMiejscLayout.setOnClickListener {
            textMiejscParent.visibility = if (textMiejscParent.visibility == View.VISIBLE) View.GONE else View.VISIBLE

        }
        textMiejsc.setOnFocusChangeListener{v, hasFocus ->
            if(hasFocus){
                textMiejscParent.visibility = View.VISIBLE
            }
            else{
                textMiejscParent.visibility = View.GONE
            }
        }

        //wybieranie elementu z listy miejscowosci
        listviewMiejc.setOnItemClickListener { parent, view, position, id ->
            // Ustawienie klikniętego elementu w EditText
            val selectedItem = miejscowosci[position]
            textMiejsc.setText(selectedItem)
            textMiejscParent.visibility = View.GONE
            textMiejsc.clearFocus()
        }

        //Filtrowanie listy miejscowosci
        textMiejsc.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapterM.filter.filter(s) // Filtrowanie wyników
            }
        })


        //scrollowanie listy ulic
        var listviewUlice = findViewById<ListView>(R.id.list_View_Ulica)

        listviewMiejc.setOnTouchListener { _, event ->
            dScrollView.requestDisallowInterceptTouchEvent(true)
            when (event.actionMasked) {
                MotionEvent.ACTION_UP -> dScrollView.requestDisallowInterceptTouchEvent(false)
            }
            false
        }

        //lista ulic
        var ulice = arrayOf<String>("Ulica")

        val adapterU: ArrayAdapter<String?> = ArrayAdapter<String?>(
            this@DodawanieLokalizacji,
            R.layout.text,
            ulice
        )
        listviewUlice.adapter = adapterU
        adapterU.notifyDataSetChanged()

        val textUlica = findViewById<EditText>(R.id.textUlicaWybierz)
        val textUlicaParent = findViewById<LinearLayout>(R.id.listViewParentU)
        val textUlicaLayout = findViewById<LinearLayout>(R.id.layoutUlicaWybierz)

        // Obsługa kliknięcia na textGmina - pokazanie listy
        textUlicaLayout.setOnClickListener {
            textUlicaParent.visibility = if (textUlicaParent.visibility == View.VISIBLE) View.GONE else View.VISIBLE

        }
        textUlica.setOnFocusChangeListener{v, hasFocus ->
            if(hasFocus){
                textUlicaParent.visibility = View.VISIBLE
            }
            else{
                textUlicaParent.visibility = View.GONE
            }
        }


        //wybieranie elementu z listy - ulice
        listviewUlice.setOnItemClickListener { parent, view, position, id ->
            // Ustawienie klikniętego elementu w EditText
            val selectedItem = ulice[position]
            textUlica.setText(selectedItem)
            textUlicaParent.visibility = View.GONE
            textUlica.clearFocus()
        }

        
        //Filtrowanie listy ulic
        textUlica.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapterU.filter.filter(s) // Filtrowanie wyników
            }
        })







        /*
        findViewById<LinearLayout>(R.id.layoutGminaWybierz).setOnClickListener{
            lista = arrayOf("A", "a", "aa")
            PopupWindow(it)
        }
        findViewById<EditText>(R.id.textGminaWybierz).setOnFocusChangeListener{v, hasFocus ->
            if(hasFocus){
                lista = arrayOf("A", "a", "aa")
                PopupWindow(v)
            }
        }
        findViewById<EditText>(R.id.textGminaWybierz).setOnClickListener(){
            lista = arrayOf("A", "a", "aa")
            PopupWindow(it)
        }

        findViewById<LinearLayout>(R.id.layoutMiejscWybierz).setOnClickListener{
            lista = arrayOf("3", "33", "aa3")
            PopupWindow(it)

        }

        findViewById<LinearLayout>(R.id.layoutUlicaWybierz).setOnClickListener{
            lista = arrayOf("44", "44", "444")
            PopupWindow(it)

        }

    private fun PopupWindow(anchorView: View) {
        //wyswietlanie okienka wyszukiwania
        val wyszukiwanie = layoutInflater.inflate(R.layout.wyszukiwanie, null)
        val szerokosc_wyszukiwania :Int = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true
        val popupWindow = PopupWindow(wyszukiwanie,  260.toPx(), szerokosc_wyszukiwania, focusable)
        popupWindow.showAsDropDown(anchorView, 0 , 0)

        //lista
        gminyLV = wyszukiwanie.findViewById(R.id.list_View)
        lista += "AAA"

        val adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
            this@DodawanieLokalizacji,
            R.layout.text,
            lista
        )

        gminyLV.adapter = adapter
        adapter.notifyDataSetChanged()

        //Wyszukiwanie z listy
        var searchbar = findViewById<EditText>(R.id.textGminaWybierz)
        //styl searchview



        searchbar.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchbar.clearFocus()
                if(lista.contains(query)){
                    adapter.filter.filter(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

        })
        }
        }
         */


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
    fun Int.toPx(): Int = (this * resources.displayMetrics.density).toInt()
}
