package com.example.smieci

import android.content.Context

class ObslugaPrzechowywaniaDanych(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("SmieciDane", Context.MODE_PRIVATE)


    //Powiadomienia
    fun zapiszPowiadomienia(ustawenie:Boolean){
        val editor = sharedPreferences.edit()
        editor.putBoolean("Powiadomienia", ustawenie)
        editor.apply()
    }
    fun ustawieniePowiadomienia(): Boolean{
        return sharedPreferences.getBoolean("Powiadomienia", false)
    }

    //Motyw
    fun zapiszMotyw(ustawenie: Boolean){
        val editor = sharedPreferences.edit()
        editor.putBoolean("Motyw", ustawenie)
        editor.apply()
    }
    fun ustawienieMotyw(): Boolean{
        return sharedPreferences.getBoolean("Motyw", false)
    }

    //Nazwa użytkownika
    fun zapiszNazweUzytkownika(nazwa:String){
        val editor = sharedPreferences.edit()
        editor.putString("NazwaUzytkownika", nazwa)
        editor.apply()
    }
    fun nazwaUzytkownika() : String?{
        return sharedPreferences.getString("NazwaUzytkownika", "Uzytkownik")
    }

    //Haslo - USUNĄĆ PRZY ZMIANIE NA BAZE DANYCH
    fun zapiszHaslo(haslo: String){
        val editor = sharedPreferences.edit()
        editor.putString("Haslo", haslo)
        editor.apply()
    }

    fun Haslo(): String?{
        return sharedPreferences.getString("Haslo", "x")
    }
}