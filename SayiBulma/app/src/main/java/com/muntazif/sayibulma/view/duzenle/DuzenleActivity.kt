package com.muntazif.sayibulma.view.duzenle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.nkzawa.socketio.client.Socket
import com.muntazif.sayibulma.databinding.ActivityDuzenleBinding
import com.muntazif.sayibulma.util.App
import com.muntazif.sayibulma.view.ana.AnaEkranActivity
import com.muntazif.sayibulma.viewmodel.duzenle.DuzenleAcViewModel
import org.json.JSONArray
import org.json.JSONObject

class DuzenleActivity : AppCompatActivity() {

    lateinit var binding : ActivityDuzenleBinding

    lateinit var socket: Socket
    lateinit var viewModel : DuzenleAcViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDuzenleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(DuzenleAcViewModel::class.java)

        socket = App.socket

        binding.geriDuzenleAc.setOnClickListener {
            finish()
        }

        binding.kaydetDuzenleAc.setOnClickListener {

            viewModel.yukleniyor.value = true

            val isim = binding.isimDuzenleAc.text.toString()
            val sifre = binding.sifreDuzenleAc.text.toString()

            val yeniKullanici = JSONObject()
            yeniKullanici.put("isim", isim)
            yeniKullanici.put("sifre", sifre)
            socket.emit("updateaccount", yeniKullanici)

        }

        viewModel.socketVeriAl(this)

        obsorveLiveData()

    }

    private fun obsorveLiveData(){
        viewModel.yukleniyor.observe(this, Observer { yukleniyor ->
            if (yukleniyor){
                binding.relative2DuzenleAc.visibility = View.GONE
                binding.yukleniyorDuzenleAc.visibility = View.VISIBLE
            }else{
                binding.relative2DuzenleAc.visibility = View.VISIBLE
                binding.yukleniyorDuzenleAc.visibility = View.GONE
            }
        })
    }
}