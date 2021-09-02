package com.muntazif.sayibulma.view.fr

import android.app.AlertDialog
import android.content.Context
import android.database.DatabaseUtils
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.nkzawa.socketio.client.Socket
import com.muntazif.sayibulma.R
import com.muntazif.sayibulma.adapter.SiralamaAdapter
import com.muntazif.sayibulma.databinding.ActivityAnaEkranBinding.inflate
import com.muntazif.sayibulma.databinding.FragmentSiralamaBinding
import com.muntazif.sayibulma.model.Siralama
import com.muntazif.sayibulma.util.App
import org.json.JSONArray
import org.json.JSONObject

class SiralamaFragment : Fragment() {

    lateinit var binding: FragmentSiralamaBinding

    lateinit var socket: Socket

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_siralama, container, false)

        socket = App.socket

        context?.let {
            val pref = it.getSharedPreferences("App", Context.MODE_PRIVATE)
            val isim = pref.getString("isim", "")
            binding.isimSiralamaFr.setText(isim)

        }

        socket.emit("leaderboard")

        socket.on("leaders") {
            activity?.let { ac ->
                ac.runOnUiThread {
                    run {
                        val siralama = mutableListOf<Siralama>()
                        val siralar = JSONArray(it[0].toString())
                        for (i in 0 until siralar.length()){
                            val item = siralar.getJSONObject(i)
                            siralama.add(Siralama(item.getInt("sira"),item.getString("isim"),item.getInt("puan")))
                        }
                        binding.recyclerViewSiralamaFr.layoutManager = LinearLayoutManager(ac)
                        binding.recyclerViewSiralamaFr.adapter = SiralamaAdapter(siralama)
                    }
                }
            }
        }

        return binding.root
    }
}