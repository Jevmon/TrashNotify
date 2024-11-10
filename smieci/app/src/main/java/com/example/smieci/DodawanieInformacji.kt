package com.example.smieci

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.print.PrintAttributes.Margins
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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginTop
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

        /*
        var linearLayout = LinearLayout(this).apply {
            id = View.generateViewId()
            setBackgroundResource(R.drawable.vertical_line)
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(280.toPx(), 50.toPx()).apply {
                topMargin = 330.toPx()
            }
        }

        EkranInformacji.addView(linearLayout)

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintSet)

        constraintSet.connect(linearLayout.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constraintSet.connect(linearLayout.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(linearLayout.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)

        constraintSet.applyTo(EkranInformacji)

        val wysoka = ToggleButton(this).apply {
            id = View.generateViewId()
            text = "Wysoka"
            textOn = "Wysoka"
            textOff = "Wysoka"
            layoutParams = LinearLayout.LayoutParams(110.toPx(), LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            isChecked = false
            textSize = 12f
        }

        val view = View(this).apply {
            id= View.generateViewId()
        }

        linearLayout.addView(wysoka)

        */

        //Obsługa wyborów

        val zamieszkaly = findViewById<ToggleButton>(R.id.toggleOp1_1)
        val niezamieszkaly = findViewById<ToggleButton>(R.id.toggleOp1_2)

        var zamieszkanie: String = ""

        zamieszkaly.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked){
                niezamieszkaly.isChecked = false
                zamieszkanie = (zamieszkaly.text).toString()
            }
        }
        niezamieszkaly.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked){
                zamieszkaly.isChecked = false
                zamieszkanie = (niezamieszkaly.text).toString()
            }
        }

        val jednorodzinna = findViewById<ToggleButton>(R.id.toggleOp2_1)
        val wielorodzinna = findViewById<ToggleButton>(R.id.toggleOp2_2)

        var rodzajRodzinny : String = ""

        jednorodzinna.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked){
                wielorodzinna.isChecked = false
                rodzajRodzinny = (jednorodzinna.text).toString()
            }
        }
        wielorodzinna.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked){
                jednorodzinna.isChecked = false
                rodzajRodzinny = (wielorodzinna.text).toString()
            }
        }

        val segregujacy = findViewById<ToggleButton>(R.id.toggleOp3_1)
        val niesegregujacy = findViewById<ToggleButton>(R.id.toggleOp3_2)

        var rodzajSegregacji : String = ""

        segregujacy.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked){
                niesegregujacy.isChecked = false
                rodzajSegregacji = (segregujacy.text).toString()
            }
        }

        niesegregujacy.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked){
                segregujacy.isChecked = false
                rodzajSegregacji = (niesegregujacy.text).toString()
            }
        }

        //Obsługa przycisku ZATWIERDŹ
        findViewById<Button>(R.id.buttonSubmit).setOnClickListener{
            //Toast.makeText(this, "${zamieszkanie} ${rodzajRodzinny} ${rodzajSegregacji}", Toast.LENGTH_LONG).show()
            val intent_lokalizacja = Intent(this, DodawanieLokalizacji::class.java)
            startActivity(intent_lokalizacja)
        }
    }
    fun Int.toPx(): Int = (this * resources.displayMetrics.density).toInt()
}