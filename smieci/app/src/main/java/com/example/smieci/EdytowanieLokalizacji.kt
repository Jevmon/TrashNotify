package com.example.smieci

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.ScrollView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.smieci.databinding.EdytowanieLokalizacjiBinding
import com.example.smieci.databinding.WyszukiwanieBinding

class EdytowanieLokalizacji : AppCompatActivity() {

    private lateinit var binding: EdytowanieLokalizacjiBinding

    var gminy : MutableSet<String> = mutableSetOf()
    var miejscowosci : MutableSet<String> = mutableSetOf()
    var ulice : MutableSet<String> = mutableSetOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EdytowanieLokalizacjiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var edytowanieLokalizacji = findViewById<ConstraintLayout>(R.id.EkranEdycjaLokalizacji)
        edytowanieLokalizacji.minHeight= wysokoscEkranu

        val zapisaneDane = ObslugaPrzechowywaniaDanych(this)

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
        wypelnianieGmin()
        val gminy = gminy.toTypedArray()


        val adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
            this@EdytowanieLokalizacji,
            R.layout.text,
            gminy
        )
        listviewGminy.adapter = adapter
        adapter.notifyDataSetChanged()


        val textGmina = findViewById<EditText>(R.id.editText_Gmina)
        val textGminaParent = findViewById<LinearLayout>(R.id.listViewParentG)

        // Obsługa kliknięcia na textGmina - pokazanie listy
        textGmina.setOnClickListener {
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
        wypelnienieMiejscowosci()
        val miejscowosci = miejscowosci.toTypedArray()

        val adapterM: ArrayAdapter<String?> = ArrayAdapter<String?>(
            this@EdytowanieLokalizacji,
            R.layout.text,
            miejscowosci
        )
        listviewMiejc.adapter = adapterM
        adapterM.notifyDataSetChanged()

        val textMiejsc = findViewById<EditText>(R.id.editTextMiejscowosc)
        val textMiejscParent = findViewById<LinearLayout>(R.id.listViewParentM)
        //val textMiejscLayout= findViewById<LinearLayout>(R.id.layoutMiejscWybierz)

        // Obsługa kliknięcia na textGmina - pokazanie listy
        textMiejsc.setOnClickListener {
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
        wypelnianieUlic()
        var ulice = ulice.toTypedArray()

        val adapterU: ArrayAdapter<String?> = ArrayAdapter<String?>(
            this@EdytowanieLokalizacji,
            R.layout.text,
            ulice
        )
        listviewUlice.adapter = adapterU
        adapterU.notifyDataSetChanged()

        val textUlica = findViewById<EditText>(R.id.editTextUlica)
        val textUlicaParent = findViewById<LinearLayout>(R.id.listViewParentU)
        //val textUlicaLayout = findViewById<LinearLayout>(R.id.layoutUlicaWybierz)

        // Obsługa kliknięcia na textGmina - pokazanie listy
        textUlica.setOnClickListener {
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

        //Zatwierdź
        findViewById<Button>(R.id.buttonSubmit).setOnClickListener {
            akutalizacjaLokalizacji(textUlica.text.toString(), textGmina.text.toString(), textMiejsc.text.toString(), zapisaneDane)
        }

        //Przenień do Informacji
        findViewById<ToggleButton>(R.id.toggleInformacje).setOnClickListener {
            val intent_informacje = Intent(this, EdytowanieInformacji::class.java)
            startActivity(intent_informacje)
        }
    }

    private fun akutalizacjaLokalizacji(ulica : String, gmina : String, miejscowosc : String, dane : ObslugaPrzechowywaniaDanych){
        try {
            val connectionHelper = ConnectionHelper()
            val connect = connectionHelper.connectionClass()
            if(connect!=null){
                var query = "SELECT l.id_lokalizacji FROM Lokalizacja l JOIN Ulice u ON l.id_ulicy = u.id_ulicy JOIN Miejscowosc m ON l.id_miejscowosci = m.id_miejscowosci JOIN Gminy g ON m.id_gminy = g.id_gminy WHERE u.Nazwa_ulicy = '$ulica' AND m.Nazwa_miejscowosci = '$miejscowosc' AND g.Nazwa_gminy = '$gmina';"
                val statement = connect.createStatement()
                var result = statement.executeQuery(query)
                if(result.next()){
                    query = "UPDATE Uzytkownik SET Id_lokalizacji = ${result.getInt("id_lokalizacji")} WHERE Email = '${dane.email()}'"
                    connect.prepareStatement(query).executeUpdate()
                    Toast.makeText(this, "Zaaktualizowano lokalizację", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Niestety nie posiadamy takiej lokalizacji w naszej bazie danych", Toast.LENGTH_LONG).show()
                }
            }
        }catch (ex: Exception){
            Log.e("ErrorAktualizacjiLokalizacji", ex.message?: "Nieznany błąd")
        }
    }

    private fun wypelnianieGmin(){
        try {
            val connectionHelper = ConnectionHelper()
            val connect = connectionHelper.connectionClass()
            if(connect!=null){
                val query = "SELECT Nazwa_gminy FROM Gminy ORDER BY Nazwa_gminy ASC"
                val statement = connect.createStatement()
                val result = statement.executeQuery(query)
                while(result.next()){
                    gminy.add(result.getString("Nazwa_gminy"))
                }
            }
        }catch (ex: Exception){
            Log.e("ErrorGminyArray", ex.message?: "Nieznany błąd")
        }
    }

    private fun wypelnienieMiejscowosci(){
        try {
            val connectionHelper = ConnectionHelper()
            val connet = connectionHelper.connectionClass()
            if(connet!=null){
                val query = "SELECT Nazwa_miejscowosci FROM Miejscowosc ORDER BY Nazwa_miejscowosci ASC"
                val statement = connet.createStatement()
                val result = statement.executeQuery(query)
                while(result.next()){
                    miejscowosci.add(result.getString("Nazwa_miejscowosci"))
                }
            }
        }catch (ex: Exception){
            Log.e("ErrorMiejscowosciArray", ex.message?: "Nieznany błąd")
        }
    }

    private fun wypelnianieUlic(){
        try {
            val connectionHelper = ConnectionHelper()
            val connect = connectionHelper.connectionClass()
            if(connect!=null){
                var query = "SELECT Nazwa_ulicy FROM Ulice ORDER BY Nazwa_ulicy ASC"
                val statement = connect.createStatement()
                val result = statement.executeQuery(query)
                while(result.next()){
                    ulice.add(result.getString("Nazwa_ulicy"))
                }
            }
        }catch (ex:Exception){
            Log.e("ErrorUliceArray", ex.message?: "Nieznany błąd")
        }
    }
}