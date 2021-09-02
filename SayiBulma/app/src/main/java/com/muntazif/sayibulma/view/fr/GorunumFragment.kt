package com.muntazif.sayibulma.view.fr

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.github.nkzawa.socketio.client.Socket
import com.muntazif.sayibulma.R
import com.muntazif.sayibulma.databinding.FragmentGorunumBinding
import com.muntazif.sayibulma.util.App
import com.muntazif.sayibulma.view.giris.GirisActivity

class GorunumFragment : Fragment() {

    lateinit var binding: FragmentGorunumBinding

    lateinit var socket: Socket

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gorunum, container, false)

        socket = App.socket

        context?.let {
            val pref = it.getSharedPreferences("App", Context.MODE_PRIVATE)
            val isim = pref.getString("isim", "")
            binding.isimGorunumFr.setText(isim)

        }

        binding.cikisYapGorunumFr.setOnClickListener {
            val intent = Intent(context, GirisActivity::class.java)
            startActivity(intent)
            killActivity()
        }

        binding.hakkimizdaGorunumFr.setOnClickListener {
            val action = GorunumFragmentDirections.actionGorunumToHakkimizdaFragment()
            Navigation.findNavController(it).navigate(action)
        }

        socket.on("deleteaccount") {
            activity?.runOnUiThread {
                run {
                    Toast.makeText(activity, it[0].toString(), Toast.LENGTH_SHORT).show()
                    val intent = Intent(activity, GirisActivity::class.java)
                    activity?.startActivity(intent)
                    killActivity()
                    socket.emit("deleteaccount").off()
                }
            }
        }

        return binding.root
    }

    private fun killActivity(){
        activity?.let {
            it.finish()
        }
    }

}