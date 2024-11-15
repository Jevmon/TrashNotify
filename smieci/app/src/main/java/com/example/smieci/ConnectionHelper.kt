package com.example.smieci

import android.os.StrictMode
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager

class ConnectionHelper {
    var con: Connection? = null
    var uname: String = ""
    var pass: String = ""
    var ip: String = ""
    var port: String = ""
    var database: String = ""

    fun connectionClass(): Connection? {
        ip = "65.21.206.53"
        database = "db_c336"
        uname = "c336"
        pass = "49D8_b33d64"
        port = "3306"

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        var connection : Connection? = null
        var connectionUrl : String = ""
        try {
            Class.forName("com.mysql.jdbc.Driver")
            connectionUrl = "jdbc:mysql://$ip:$port/$database?user=$uname&password=$pass"
            connection = DriverManager.getConnection(connectionUrl)
        } catch (ex : Exception){
            Log.e("ErrorPort", ex.message?: "Nieznany błąd")
        }
        return connection

    }

}