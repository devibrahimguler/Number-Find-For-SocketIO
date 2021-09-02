package com.muntazif.sayibulma.view.giris

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.github.nkzawa.socketio.client.Socket
import com.muntazif.sayibulma.R
import com.muntazif.sayibulma.databinding.ActivityGirisBinding
import com.muntazif.sayibulma.util.App
import com.muntazif.sayibulma.view.ana.AnaEkranActivity
import com.muntazif.sayibulma.viewmodel.giris.GirisAcViewModel
import org.json.JSONObject

class GirisActivity : AppCompatActivity() {

    lateinit var binding: ActivityGirisBinding

    lateinit var socket: Socket
    lateinit var viewModel : GirisAcViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGirisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(GirisAcViewModel::class.java)

        socket = App.socket

        binding.girisYapGirisAc.setOnClickListener {
            val isim = binding.isimGirisAc.text.toString()
            val sifre = binding.sifreGirisAc.text.toString()

            val kullanici = JSONObject()
            kullanici.put("isim", isim)
            kullanici.put("sifre", sifre)
            socket.emit("user_connect", kullanici)
        }

        viewModel.socketVeriAl(this)

    }
}