package com.muntazif.sayibulma.viewmodel.fr

import android.app.Activity
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.nkzawa.socketio.client.Socket
import com.muntazif.sayibulma.adapter.OyunAraAdapter
import com.muntazif.sayibulma.model.Kullanici
import com.muntazif.sayibulma.util.App
import com.muntazif.sayibulma.view.ana.AnaEkranActivity
import com.muntazif.sayibulma.view.ana.OyunActivity
import org.json.JSONArray
import org.json.JSONObject

class OyunAraFrViewModel : ViewModel() {

    lateinit var socket: Socket
    lateinit var dialog : AlertDialog

    fun socketVeriAl(activity: Activity, rcViewOyunAraFr : RecyclerView){

        socket = App.socket

        socket.on("online_users"){
            activity.runOnUiThread {
                run {
                    val onlineKullanicilar = mutableListOf<Kullanici>()
                    val kullanicilar = JSONArray(it[0].toString())
                    for (i in 0 until kullanicilar.length()){
                        val item = kullanicilar.getJSONObject(i)
                        if (item.getString("id") != socket.id()){
                            onlineKullanicilar.add(Kullanici(item.getString("id"),item.getString("isim"),item.getBoolean("durum")))
                        }
                    }
                    rcViewOyunAraFr.layoutManager = LinearLayoutManager(activity)
                    rcViewOyunAraFr.adapter = OyunAraAdapter(onlineKullanicilar)
                }
            }
        }
            .on("request"){
                activity.runOnUiThread {
                    run {
                        val kullanici = JSONObject(it[0].toString())
                        val isim = kullanici.getString("isim")
                        val id = kullanici.getString("id")
                        val builder = AlertDialog.Builder(activity)
                        builder.setTitle("Oyun İsteği")
                        builder.setMessage("$isim seninle oynamak istiyor.")
                        builder.setPositiveButton("Kabul Et") { dialog, which ->
                            socket.emit("accept", id)
                            Toast.makeText(activity, "$isim kişisinden gelen oyun isteği kabul edildi.", Toast.LENGTH_SHORT).show()
                        }
                        builder.setNegativeButton("Reddet") { dialog, which ->
                            socket.emit("reject", id)
                            Toast.makeText(activity, "$isim kişisinden gelen oyun isteği reddedildi.", Toast.LENGTH_SHORT).show()
                        }
                        dialog = builder.create()
                        if (!activity.isFinishing){
                            dialog.show()
                        }

                    }
                }
            }
            .on("accept") {
                activity.runOnUiThread {
                    run {
                        val kullanici = JSONObject(it[0].toString())
                        val home = kullanici.getString("home")
                        val away = kullanici.getString("away")
                        val intent = Intent(activity, OyunActivity::class.java)
                        intent.putExtra("home", home)
                        intent.putExtra("away", away)
                        activity.startActivity(intent)
                        activity.finish()
                    }
                }
            }
            .on("reject"){
                activity.runOnUiThread {
                    run {
                        val isim = it[0].toString()
                        val builder = AlertDialog.Builder(activity)
                        builder.setTitle("Oyun İsteği Cevabı")
                        builder.setMessage("$isim kişisine yaptığınız oyun isteği reddedildi.")
                        builder.setNegativeButton("Tamam") { _,_ -> }
                        val dialog : AlertDialog = builder.create()
                        if (!activity.isFinishing){
                            dialog.show()
                        }
                    }
                }
            }
    }

}