package com.example.smieci

import android.app.LauncherActivity.ListItem
import android.content.ClipData.Item
import android.media.RouteListingPreference
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.smieci.databinding.WyszukiwanieBinding


class Wyszukiwanie : AppCompatActivity() {
    private lateinit var binding: WyszukiwanieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WyszukiwanieBinding.inflate(layoutInflater)
        setContentView(binding.root)






    }
}