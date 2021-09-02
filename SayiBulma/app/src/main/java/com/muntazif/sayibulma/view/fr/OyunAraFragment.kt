package com.muntazif.sayibulma.view.fr

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.nkzawa.socketio.client.Socket
import com.muntazif.sayibulma.R
import com.muntazif.sayibulma.adapter.OyunAraAdapter
import com.muntazif.sayibulma.databinding.FragmentOyunAraBinding
import com.muntazif.sayibulma.model.Kullanici
import com.muntazif.sayibulma.util.App
import com.muntazif.sayibulma.view.ana.OyunActivity
import com.muntazif.sayibulma.viewmodel.fr.OyunAraFrViewModel
import org.json.JSONArray
import org.json.JSONObject

class OyunAraFragment : Fragment() {

    lateinit var binding : FragmentOyunAraBinding

    lateinit var socket: Socket
    lateinit var viewModel : OyunAraFrViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_oyun_ara, container, false)

        viewModel = ViewModelProvider(this).get(OyunAraFrViewModel::class.java)

        socket = App.socket

        socket.emit("online_users")

        activity?.let {
            viewModel.socketVeriAl(it, binding.rcViewOyunAraFr)
        }

        return binding.root
    }
}