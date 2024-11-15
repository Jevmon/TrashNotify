package com.example.smieci

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginTop
import androidx.core.view.setMargins
import com.example.smieci.databinding.UserPanelBinding

class UserPanel : AppCompatActivity() {
    private lateinit var binding: UserPanelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var userPanel = findViewById<ConstraintLayout>(R.id.userPanel)
        userPanel.minHeight= wysokoscEkranu

        val zapisaneDane = ObslugaPrzechowywaniaDanych(this)

        //Obsługa cofnięcia
        findViewById<ImageView>(R.id.cofnij).setOnClickListener {
            var intent_main = Intent(this, MainActivity::class.java)
            startActivity(intent_main)
        }

        //tworzenie obiektów potrzebnych do zmiany nazwy urzytkownika

        val textViewZmiana = findViewById<TextView>(R.id.NazwaUzytkownika)
        textViewZmiana.text = zapisaneDane.nazwaUzytkownika()
        val imageViewZmiana = findViewById<ImageView>(R.id.obrazZmianyNazwy)
        val ustawieniaLayout = findViewById<LinearLayout>(R.id.ustawienia)
        val ustawieniaParams = ustawieniaLayout.layoutParams as ConstraintLayout.LayoutParams

        val text = textViewZmiana.text
        val layoutNazwy = findViewById<LinearLayout>(R.id.ZmianaNazwy)

        val editText = EditText(this).apply {
            id = View.generateViewId()
            gravity = Gravity.CENTER
            textSize = 20f
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            setText(text)
            setTextColor(ContextCompat.getColor(context, R.color.gray_text))
            setBackgroundResource(R.color.gray)
        }

        var constraintSet = ConstraintSet()

        val linearLayoutGuziki = LinearLayout(this).apply {
            id = View.generateViewId()
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        }

        userPanel.addView(linearLayoutGuziki)
        constraintSet.clone(userPanel)

        constraintSet.connect(linearLayoutGuziki.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END,0)
        constraintSet.connect(linearLayoutGuziki.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,250.toPx())
        constraintSet.connect(linearLayoutGuziki.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START,0)

        constraintSet.applyTo(userPanel)

        val anuluj = Button(this).apply {
            id = View.generateViewId()
            setText(getString(R.string.anuluj))
            setBackgroundResource(R.color.gray)
            setTextColor(ContextCompat.getColor(context, R.color.light_gray_text))
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                topMargin = 10.toPx()
                bottomMargin = 10.toPx()
            }
        }

        val zatwierdz = Button(this).apply {
            id = View.generateViewId()
            setText(getString(R.string.zatwierdz))
            setBackgroundResource(R.color.green_dark_button)
            setTextColor(ContextCompat.getColor(context, R.color.light_gray_text))
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                topMargin = 10.toPx()
                bottomMargin = 10.toPx()
            }
        }




        //Obsługa zmiany nazwy

        var kliknieto : Boolean = false
        findViewById<LinearLayout>(R.id.ZmianaNazwy).setOnClickListener {
            if(!kliknieto) {
                textViewZmiana.visibility = View.GONE
                imageViewZmiana.visibility = View.GONE
                layoutNazwy.addView(editText)
                linearLayoutGuziki.addView(anuluj)
                linearLayoutGuziki.addView(zatwierdz)
                ustawieniaParams.topMargin = 310.toPx()
                ustawieniaLayout.layoutParams = ustawieniaParams
                kliknieto = true
            }
        }

        anuluj.setOnClickListener {
            textViewZmiana.visibility = View.VISIBLE
            imageViewZmiana.visibility = View.VISIBLE
            layoutNazwy.removeView(editText)
            linearLayoutGuziki.removeView(anuluj)
            linearLayoutGuziki.removeView(zatwierdz)
            ustawieniaParams.topMargin = 300.toPx()
            ustawieniaLayout.layoutParams = ustawieniaParams
            kliknieto = false
        }

        //Zatwierdzenie nowej nazwy
        zatwierdz.setOnClickListener {
            try{
                val connectionHelper = ConnectionHelper()
                val connect = connectionHelper.connectionClass()
                if(connect!=null){
                    val query="UPDATE Uzytkownik SET Nazwa_uzytkownika = '${editText.text}' WHERE email='${zapisaneDane.email()}'"
                    connect.prepareStatement(query).executeUpdate()
                }
            }catch (ex:Exception){
                Log.e("ErrorZmianaNazwyUżytkownika", ex.message?: "Nieznany błąd")
            }
            zapisaneDane.zapiszNazweUzytkownika(editText.text.toString())
            textViewZmiana.setText(editText.text)
            textViewZmiana.visibility = View.VISIBLE
            imageViewZmiana.visibility = View.VISIBLE
            layoutNazwy.removeView(editText)
            linearLayoutGuziki.removeView(anuluj)
            linearLayoutGuziki.removeView(zatwierdz)
            ustawieniaParams.topMargin = 300.toPx()
            ustawieniaLayout.layoutParams = ustawieniaParams
            kliknieto = false
        }


        //Obsługa powiadomień

        val powiadomieniaSwitch = findViewById<Switch>(R.id.powiadomieniaSwitch)
        if(zapisaneDane.ustawieniePowiadomienia()){
            powiadomieniaSwitch.isChecked = true
        }

        powiadomieniaSwitch.setOnCheckedChangeListener{_, Zaznaczone ->
            try{
                val connectionHelper = ConnectionHelper()
                val connect = connectionHelper.connectionClass()
                if(connect!=null){
                    var query : String
                    if(Zaznaczone){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                            requestNotificationPermission(this)
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                            zapisaneDane.zapiszPowiadomienia(false)
                            query = "UPDATE Uzytkownik SET Ustawienia_powiadomien = 0"
                            connect.prepareStatement(query).executeUpdate()
                            powiadomieniaSwitch.isChecked = false
                        } else {
                            query = "UPDATE Uzytkownik SET Ustawienia_powiadomien = 1"
                            connect.prepareStatement(query).executeUpdate()
                            zapisaneDane.zapiszPowiadomienia(true)
                        }
                    } else {
                        query = "UPDATE Uzytkownik SET Ustawienia_powiadomien = 0"
                        connect.prepareStatement(query).executeUpdate()
                        zapisaneDane.zapiszPowiadomienia(false)
                    }
                }
            } catch (ex:Exception){
                Log.e("ErrorPowiadomieniaSwitch", ex.message?: "Nieznany błąd")
            }
        }

        //Obsługa NightMode - DayMode
        /*val motywSwitch = findViewById<Switch>(R.id.motywSwitch)
        if(zapisaneDane.ustawienieMotyw()){
            motywSwitch.isChecked = true
        }
        motywSwitch.setOnCheckedChangeListener{_, Zaznaczone ->
            if(Zaznaczone){
                zapisaneDane.zapiszMotyw(true)
            } else {
                zapisaneDane.zapiszMotyw(false)
            }
        }*/

        //Obsługa przycisku edytuj adres zamieszkania
        findViewById<LinearLayout>(R.id.EdytujAdresZamieszakniaLayout).setOnClickListener {
            var intent_edytowanie_lokalizacji = Intent(this, EdytowanieLokalizacji::class.java)
            startActivity(intent_edytowanie_lokalizacji)
        }

        //Obsługa przycisku Zmień hasło
        findViewById<LinearLayout>(R.id.ZmienHasloLayout).setOnClickListener {
            var intent_haslo = Intent(this, ZmienHaslo::class.java)
            startActivity(intent_haslo)
        }
    }

    fun Int.toPx(): Int = (this * resources.displayMetrics.density).toInt()
}