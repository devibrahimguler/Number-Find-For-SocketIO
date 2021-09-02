package com.muntazif.sayibulma.view.ana

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import com.github.nkzawa.socketio.client.Socket
import com.muntazif.sayibulma.adapter.CevapAdapter
import com.muntazif.sayibulma.databinding.ActivityOyunBinding
import com.muntazif.sayibulma.util.App
import com.muntazif.sayibulma.view.sonuc.SonucActivity
import com.muntazif.sayibulma.viewmodel.ana.OyunAcViewModel
import org.json.JSONArray
import org.json.JSONObject

class OyunActivity : AppCompatActivity() {

    lateinit var binding: ActivityOyunBinding

    lateinit var socket: Socket
    lateinit var viewModel : OyunAcViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOyunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(OyunAcViewModel::class.java)

        socket = App.socket

        val home = intent.extras?.getString("home", "")
        val away = intent.extras?.getString("away", "")

        val players = JSONObject()
        players.put("home", home)
        players.put("away", away)
        socket.emit("game", players)

        binding.sayiSecOyunAc.setOnClickListener {
            val sayi1 = binding.editSayi1OyunAc.text.toString()
            val sayi2 = binding.editSayi2OyunAc.text.toString()
            val sayi3 = binding.editSayi3OyunAc.text.toString()
            val sayi4 = binding.editSayi4OyunAc.text.toString()

            if (sayi1 != "" && sayi2 != "" && sayi3 != "" && sayi4 != "") {
                if (sayi1 != sayi2 && sayi1 != sayi3 && sayi1 != sayi4 && sayi2 != sayi3 && sayi2 != sayi4 && sayi3 != sayi4) {

                    socket.emit("my_number_1", sayi1)
                    socket.emit("my_number_2", sayi2)
                    socket.emit("my_number_3", sayi3)
                    socket.emit("my_number_4", sayi4)
                    socket.emit("selectedhome", "selectedhome")
                    socket.emit("selectedaway", "selectedaway")
                    
                    binding.editSayi1OyunAc.setText("")
                    binding.editSayi2OyunAc.setText("")
                    binding.editSayi3OyunAc.setText("")
                    binding.editSayi4OyunAc.setText("")

                    binding.sayiSecOyunAc.visibility = View.GONE
                }
            }
        }

        binding.gonderOyunAc.setOnClickListener {

            val sayi1 = binding.editSayi1OyunAc.text.toString()
            val sayi2 = binding.editSayi2OyunAc.text.toString()
            val sayi3 = binding.editSayi3OyunAc.text.toString()
            val sayi4 = binding.editSayi4OyunAc.text.toString()
            if (sayi1 != "" && sayi2 != "" && sayi3 != "" && sayi4 != "") {
                if (sayi1 != sayi2 && sayi1 != sayi3 && sayi1 != sayi4 && sayi2 != sayi3 && sayi2 != sayi4 && sayi3 != sayi4) {

                    socket.emit("answer_1", sayi1)
                    socket.emit("answer_2", sayi2)
                    socket.emit("answer_3", sayi3)
                    socket.emit("answer_4", sayi4)
                    socket.emit("clickhome", "clickhome")
                    socket.emit("clickaway", "clickaway")

                    binding.editSayi1OyunAc.setText("")
                    binding.editSayi2OyunAc.setText("")
                    binding.editSayi3OyunAc.setText("")
                    binding.editSayi4OyunAc.setText("")

                }
            }

        }

        viewModel.socketVeriAl(
                this,
                binding.zamanOyunAc,
                binding.gonderOyunAc,
                binding.textSayi1OyunAc,
                binding.textSayi2OyunAc,
                binding.textSayi3OyunAc,
                binding.textSayi4OyunAc,
                binding.linearLayout3OyunAc,
                binding.uyariOyunAc,
                binding.recyclerViewOyunAc)


    }

}