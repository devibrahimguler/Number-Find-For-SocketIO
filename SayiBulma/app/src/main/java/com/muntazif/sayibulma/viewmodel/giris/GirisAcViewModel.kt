package com.muntazif.sayibulma.viewmodel.giris

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.github.nkzawa.socketio.client.Socket
import com.muntazif.sayibulma.util.App
import com.muntazif.sayibulma.view.ana.AnaEkranActivity

class GirisAcViewModel : ViewModel() {


    lateinit var socket: Socket


    fun socketVeriAl(activity: Activity){

        socket = App.socket

        socket.on("user_connect"){
            activity.runOnUiThread {
                run{
                    val isim = it[0].toString()

                    val preference = activity.getSharedPreferences("App", Context.MODE_PRIVATE)
                    val editor = preference.edit()
                    editor.putString("isim",isim)
                    editor.apply()

                    val intent = Intent(activity, AnaEkranActivity::class.java)
                    activity.startActivity(intent)
                    activity.finish()
                }
            }
        }

        socket.on("wrong_password"){
            activity.runOnUiThread {
                run{
                    Toast.makeText(activity, it[0].toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}