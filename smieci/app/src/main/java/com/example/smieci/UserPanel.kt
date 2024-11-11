package com.example.smieci

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
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

        //tworzenie obiektów potrzebnych do zmiany nazwy urzytkownika

        val textViewZmiana = findViewById<TextView>(R.id.NazwaUzytkownika)
        textViewZmiana.text = zapisaneDane.nazwaUzytkownika()
        val imageViewZmiana = findViewById<ImageView>(R.id.obrazZmianyNazwy)

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
                topMargin = 5.toPx()
                bottomMargin = 5.toPx()
            }
        }

        val zatwierdz = Button(this).apply {
            id = View.generateViewId()
            setText(getString(R.string.zatwierdz))
            setBackgroundResource(R.color.green_dark_button)
            setTextColor(ContextCompat.getColor(context, R.color.light_gray_text))
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                topMargin = 5.toPx()
                bottomMargin = 5.toPx()
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
                kliknieto = true
            }
        }

        anuluj.setOnClickListener {
            textViewZmiana.visibility = View.VISIBLE
            imageViewZmiana.visibility = View.VISIBLE
            layoutNazwy.removeView(editText)
            linearLayoutGuziki.removeView(anuluj)
            linearLayoutGuziki.removeView(zatwierdz)
            kliknieto = false
        }

        //Zatwierdzenie nowej nazwy
        zatwierdz.setOnClickListener {
            textViewZmiana.setText(editText.text)
            zapisaneDane.zapiszNazweUzytkownika(editText.text.toString())

            textViewZmiana.visibility = View.VISIBLE
            imageViewZmiana.visibility = View.VISIBLE
            layoutNazwy.removeView(editText)
            linearLayoutGuziki.removeView(anuluj)
            linearLayoutGuziki.removeView(zatwierdz)
            kliknieto = false
        }


        //Obsługa powiadomień

        val powiadomieniaSwitch = findViewById<Switch>(R.id.powiadomieniaSwitch)
        if(zapisaneDane.ustawieniePowiadomienia()){
            powiadomieniaSwitch.isChecked = true
        }

        powiadomieniaSwitch.setOnCheckedChangeListener{_, Zaznaczone ->
            if(Zaznaczone){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                    requestNotificationPermission(this)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                    zapisaneDane.zapiszPowiadomienia(false)
                    powiadomieniaSwitch.isChecked = false
                } else {
                    zapisaneDane.zapiszPowiadomienia(true)
                }
            } else {
                zapisaneDane.zapiszPowiadomienia(false)
            }
        }

        //Obsługa NightMode - DayMode
        val motywSwitch = findViewById<Switch>(R.id.motywSwitch)
        if(zapisaneDane.ustawienieMotyw()){
            motywSwitch.isChecked = true
        }
        motywSwitch.setOnCheckedChangeListener{_, Zaznaczone ->
            if(Zaznaczone){
                zapisaneDane.zapiszMotyw(true)
            } else {
                zapisaneDane.zapiszMotyw(false)
            }
        }

        //Obsługa przycisku edytuj adres zamieszkania
        //ZMIENIĆ DOCELOWĄ STRONĘ NA EDYTCJE LOKALIZACJI (USUNĄĆ PO ZROBIENIU)
        findViewById<LinearLayout>(R.id.EdytujAdresZamieszakniaLayout).setOnClickListener {
            var intent_lokalizacja = Intent(this, DodawanieLokalizacji::class.java)
            startActivity(intent_lokalizacja)
        }

        //Obsługa przycisku Zmień hasło
        //TO SAMO CO WYŻEJ TYLKO ZAMIAST LOKAZLIZACJA TO NA HASŁO (USUNĄĆ PO ZROBIENIU)
        findViewById<LinearLayout>(R.id.ZmienHasloLayout).setOnClickListener {
            //var intent_haslo = Intent(this, xxx::class.java)
            //startActivity(intent_haslo)
        }
    }

    fun Int.toPx(): Int = (this * resources.displayMetrics.density).toInt()
}