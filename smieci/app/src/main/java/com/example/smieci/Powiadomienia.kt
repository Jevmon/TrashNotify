package com.example.smieci

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import java.util.Calendar

const val channelID = "Kosze01"
const val titleExtra = "powiadomienie"
const val messageExtra = "Opis"

class Powiadomienia : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent){
        var zapisaneDane = ObslugaPrzechowywaniaDanych(context)
        var notificationID = zapisaneDane.iloscPowiadomien()

        val powiadomienie : Notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .build()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID, powiadomienie)
    }
}

