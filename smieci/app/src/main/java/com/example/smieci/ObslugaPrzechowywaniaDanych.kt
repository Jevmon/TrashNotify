package com.example.smieci

import android.content.Context

class ObslugaPrzechowywaniaDanych(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("SmieciDane", Context.MODE_PRIVATE)


    //Liczba powiadomień

    var iloscPowiadomien = iloscPowiadomien()

    fun dodajPowiadomienie(){
        iloscPowiadomien++
        val editor = sharedPreferences.edit()
        editor.putInt("IloscPowiadomien", iloscPowiadomien)
        editor.apply()
    }

    fun zresetujPowiadomienia(){
        val editor = sharedPreferences.edit()
        editor.putInt("IloscPowiadomien", 0)
        editor.apply()
    }

    fun iloscPowiadomien() : Int{
        return sharedPreferences.getInt("IloscPowiadomien", 0)
    }

    //Powiadomienia true - włączone, false - wyłączone
    fun zapiszPowiadomienia(ustawenie:Boolean){
        val editor = sharedPreferences.edit()
        editor.putBoolean("Powiadomienia", ustawenie)
        editor.apply()
    }
    fun ustawieniePowiadomienia(): Boolean{
        return sharedPreferences.getBoolean("Powiadomienia", true)
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
    //Email
    fun zapiszEmail(nazwa: String){
        val editor = sharedPreferences.edit()
        editor.putString("Email", nazwa)
        editor.apply()
    }
    fun email() : String?{
        return sharedPreferences.getString("Email", "jevmon@gmail.com")
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