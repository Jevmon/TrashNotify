package com.example.smieci

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.ContactsContract.CommonDataKinds.Note
import com.example.smieci.databinding.MainBinding
import java.time.LocalDate
import java.util.Calendar

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "wywozy.db"
        private const val DATABASE_VERSION = 5
        private const val TABLE_NAME = "tablicaWywozy"
        private const val COLUMN_ID = "id"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_TYPE = "type"
    }

    override fun onCreate(db: SQLiteDatabase?){
        val createTableQuery = "CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_DATE DATE, $COLUMN_TYPE TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int){
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun wpiszDate(note: Dane){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DATE, note.date) //YYYY-MM-DD
            put(COLUMN_TYPE, note.type)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun wypiszWszystkieDate(aktualne : Boolean, dataDziejsza : String) : List<Dane>{
        val db = readableDatabase
        var cursor = db.rawQuery("SELECT $COLUMN_DATE, $COLUMN_TYPE FROM $TABLE_NAME", null)
        val dataList = mutableListOf<Dane>()

        if(cursor.moveToFirst()){
            do {
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                val type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE))
                if(aktualne){
                    if(LocalDate.parse(date) >= LocalDate.parse(dataDziejsza)){
                        dataList.add(Dane(date, type))
                    }
                } else {
                    dataList.add(Dane(date, type))
                }
            } while(cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return dataList
    }
}