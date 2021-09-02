package com.muntazif.sayibulma.view.ana

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.github.nkzawa.socketio.client.Socket
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.muntazif.sayibulma.R
import com.muntazif.sayibulma.databinding.ActivityAnaEkranBinding
import org.json.JSONObject

class AnaEkranActivity : AppCompatActivity() {

    lateinit var binding : ActivityAnaEkranBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnaEkranBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView : BottomNavigationView = findViewById(R.id.altMenuAnaEkranAc)
        val navController = findNavController(R.id.fragmentAnaEkranAc)
        navView.setupWithNavController(navController)

    }
}