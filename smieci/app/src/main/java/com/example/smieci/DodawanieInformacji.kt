package com.example.smieci

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.print.PrintAttributes.Margins
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.toColor
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginTop
import androidx.core.view.setMargins
import com.example.smieci.databinding.DodawanieInformacjiBinding
import com.example.smieci.databinding.DodawanieLokalizacjiBinding

class DodawanieInformacji : AppCompatActivity() {
    private lateinit var binding: DodawanieInformacjiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DodawanieInformacjiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var EkranInformacji = findViewById<ConstraintLayout>(R.id.EkranInformacji)
        EkranInformacji.minHeight= wysokoscEkranu

        //Tworzenie ToggleButton'ów Wysoka-Pozostałe

        var linearLayoutWP = LinearLayout(this).apply {
            id = View.generateViewId()
            setBackgroundResource(R.drawable.toggle_options_bg)
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(280.toPx(), 45.toPx())
        }
        val wysoka = ToggleButton(this).apply {
            id = View.generateViewId()
            text = "ToggleButton"
            textOn = getString(R.string.Wysoka)
            textOff = getString(R.string.Wysoka)
            layoutParams = LinearLayout.LayoutParams(110.toPx(), LinearLayout.LayoutParams.WRAP_CONTENT, 1f). apply {
                setMargins(3.toPx())
            }
            isChecked = false
            textSize = 12f
            stateListAnimator = null
            setTextColor(ResourcesCompat.getColorStateList(resources, R.color.toggle_button_text_color, null))
            setBackgroundResource(R.drawable.toggle_button_background)
            gravity = Gravity.CENTER
        }

        val liniaWP = View(this).apply {
            id= View.generateViewId()
            layoutParams = LinearLayout.LayoutParams(2.toPx(), LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                topMargin = 3.toPx()
                bottomMargin = 3.toPx()
            }
            setBackgroundResource(R.drawable.vertical_line)
        }

        val pozostale = ToggleButton(this).apply {
            id = View.generateViewId()
            text = "ToggleButton"
            textOn = getString(R.string.Pozostałe)
            textOff = getString(R.string.Pozostałe)
            layoutParams = LinearLayout.LayoutParams(110.toPx(), LinearLayout.LayoutParams.WRAP_CONTENT, 1f). apply {
                setMargins(3.toPx())
            }
            isChecked = false
            textSize = 12f
            stateListAnimator = null
            setTextColor(ResourcesCompat.getColorStateList(resources, R.color.toggle_button_text_color, null))
            setBackgroundResource(R.drawable.toggle_button_background)
            gravity = Gravity.CENTER
        }

        //tworzenie przycisków Wielorodzinna - Jednorodzinna

        val linearLayoutRodzina = LinearLayout(this).apply {
            id = View.generateViewId()
            setBackgroundResource(R.drawable.toggle_options_bg)
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(280.toPx(), 45.toPx())
        }

        val jednorodzinna = ToggleButton(this).apply {
            id = View.generateViewId()
            text="ToggleButton"
            textOn = getString(R.string.jednorodzinna)
            textOff = getString(R.string.jednorodzinna)
            textSize = 12f
            isChecked = false
            layoutParams = LinearLayout.LayoutParams(110.toPx(), LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                setMargins(3.toPx())
            }
            stateListAnimator = null
            gravity = Gravity.CENTER
            setTextColor(ResourcesCompat.getColorStateList(resources, R.color.toggle_button_text_color, null))
            setBackgroundResource(R.drawable.toggle_button_background)
        }

        val liniaRodzina = View(this).apply {
            id= View.generateViewId()
            layoutParams = LinearLayout.LayoutParams(2.toPx(), LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                topMargin = 3.toPx()
                bottomMargin = 3.toPx()
            }
            setBackgroundResource(R.drawable.vertical_line)
        }

        val wielorodzinna = ToggleButton(this).apply {
            id = View.generateViewId()
            text="ToggleButton"
            textOn = getString(R.string.wielorodzinna)
            textOff = getString(R.string.wielorodzinna)
            textSize = 12f
            isChecked = false
            layoutParams = LinearLayout.LayoutParams(110.toPx(), LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                setMargins(3.toPx())
            }
            stateListAnimator = null
            gravity = Gravity.CENTER
            setTextColor(ResourcesCompat.getColorStateList(resources, R.color.toggle_button_text_color, null))
            setBackgroundResource(R.drawable.toggle_button_background)
        }

        //Obsługa wyborów

        val zamieszkaly = findViewById<ToggleButton>(R.id.toggleOp1_1)
        val niezamieszkaly = findViewById<ToggleButton>(R.id.toggleOp1_2)

        val segregujacy = findViewById<ToggleButton>(R.id.toggleOp4_1)
        val niesegregujacy = findViewById<ToggleButton>(R.id.toggleOp4_2)

        var zamieszkanie: String = ""

        var rodzajRodzinny : String = ""

        var rodzajWP : String = ""
        val segregacja = findViewById<LinearLayout>(R.id.Segregacja)
        val segregacjaParams = segregacja.layoutParams as ConstraintLayout.LayoutParams

        //kiedy kliknięty Zamieszkały
        zamieszkaly.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked){
                niezamieszkaly.isChecked = false
                zamieszkanie = (zamieszkaly.text).toString()

                linearLayoutRodzina.addView(jednorodzinna)
                linearLayoutRodzina.addView(liniaRodzina)
                linearLayoutRodzina.addView(wielorodzinna)
                linearLayoutRodzina.invalidate()
                EkranInformacji.addView(linearLayoutRodzina)

                val constraintSet = ConstraintSet()
                constraintSet.clone(EkranInformacji)

                constraintSet.connect(linearLayoutRodzina.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END,0)
                constraintSet.connect(linearLayoutRodzina.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,400.toPx())
                constraintSet.connect(linearLayoutRodzina.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START,0)

                constraintSet.applyTo(EkranInformacji)

                //kiedy kliknięta Jednorodzinna
                jednorodzinna.setOnCheckedChangeListener{ _, isChecked ->
                    if(isChecked){
                        wielorodzinna.isChecked = false
                        rodzajRodzinny = (jednorodzinna.text).toString()

                        linearLayoutWP.removeAllViews()
                        EkranInformacji.removeView(linearLayoutWP)

                        segregacjaParams.topMargin = 470.toPx()
                        segregacja.layoutParams = segregacjaParams

                        wysoka.isChecked = false
                        pozostale.isChecked = false
                    }
                }

                //kiedy kliknięta Wielorodzinna
                wielorodzinna.setOnCheckedChangeListener{_, Dziala ->
                    if(Dziala){
                        jednorodzinna.isChecked = false
                        rodzajRodzinny = (wielorodzinna.text).toString()

                        linearLayoutWP.addView(wysoka)
                        linearLayoutWP.addView(liniaWP)
                        linearLayoutWP.addView(pozostale)
                        linearLayoutWP.invalidate()
                        EkranInformacji.addView(linearLayoutWP)


                        val constraintSet = ConstraintSet()
                        constraintSet.clone(EkranInformacji)

                        constraintSet.connect(linearLayoutWP.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END,0)
                        constraintSet.connect(linearLayoutWP.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,470.toPx())
                        constraintSet.connect(linearLayoutWP.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START,0)

                        constraintSet.applyTo(EkranInformacji)

                        //kiedy kliknięta Wysoka
                        wysoka.setOnCheckedChangeListener{_, isChecked ->
                            if(isChecked){
                                pozostale.isChecked = false
                                rodzajWP = (wysoka.text).toString()
                            }
                        }

                        //kiedy kliknięte Pozostałe
                        pozostale.setOnCheckedChangeListener{_, isChecked ->
                            if(isChecked){
                                wysoka.isChecked = false
                                rodzajWP = (pozostale.text).toString()
                            }
                        }

                        segregacjaParams.topMargin = 540.toPx()
                        segregacja.layoutParams = segregacjaParams
                    }
                    //kiedy odkliknięte Wielorodzina
                    else {
                        linearLayoutWP.removeAllViews()
                        EkranInformacji.removeView(linearLayoutWP)
                        segregacjaParams.topMargin = 470.toPx()
                        segregacja.layoutParams = segregacjaParams

                        wysoka.isChecked = false
                        pozostale.isChecked = false
                    }
                }

                segregacjaParams.topMargin = 470.toPx()
                segregacja.layoutParams = segregacjaParams


            }
            //kiedy odkliknięty Zamieszkały
            else {
                linearLayoutRodzina.removeAllViews()
                EkranInformacji.removeView(linearLayoutRodzina)

                linearLayoutWP.removeAllViews()
                EkranInformacji.removeView(linearLayoutWP)

                segregacjaParams.topMargin = 400.toPx()
                segregacja.layoutParams = segregacjaParams

                jednorodzinna.isChecked = false
                wielorodzinna.isChecked = false
            }
        }

        //Kiedy kliknięty Niezamieszkały
        niezamieszkaly.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked){
                zamieszkaly.isChecked = false
                zamieszkanie = (niezamieszkaly.text).toString()

                linearLayoutRodzina.removeAllViews()
                EkranInformacji.removeView(linearLayoutRodzina)

                linearLayoutWP.removeAllViews()
                EkranInformacji.removeView(linearLayoutWP)

                segregacjaParams.topMargin = 400.toPx()
                segregacja.layoutParams = segregacjaParams

                jednorodzinna.isChecked = false
                wielorodzinna.isChecked = false
            }
        }

        var rodzajSegregacji : String =""

        segregujacy.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked){
                niesegregujacy.isChecked = false
                rodzajSegregacji = (segregujacy.text).toString()
            }
        }

        niesegregujacy.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                segregujacy.isChecked = false
                rodzajSegregacji = (segregujacy.text).toString()
            }
        }

        //Obsługa przycisku ZATWIERDŹ
        findViewById<Button>(R.id.buttonSubmit).setOnClickListener{
            //Toast.makeText(this, "${zamieszkanie} ${rodzajRodzinny} ${rodzajWP} ${rodzajSegregacji}", Toast.LENGTH_LONG).show()
            val intent_lokalizacja = Intent(this, DodawanieLokalizacji::class.java)
            startActivity(intent_lokalizacja)
        }
    }
    fun Int.toPx(): Int = (this * resources.displayMetrics.density).toInt()
}