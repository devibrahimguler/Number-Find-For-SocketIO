package com.muntazif.sayibulma.viewmodel.ana

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.nkzawa.socketio.client.Socket
import com.muntazif.sayibulma.adapter.CevapAdapter
import com.muntazif.sayibulma.model.Cevap
import com.muntazif.sayibulma.util.App
import com.muntazif.sayibulma.view.ana.AnaEkranActivity
import com.muntazif.sayibulma.view.sonuc.SonucActivity
import org.json.JSONArray
import org.json.JSONObject

class OyunAcViewModel : ViewModel() {

    lateinit var socket: Socket

    fun socketVeriAl(
            activity: Activity,
            zamanOyunAc: TextView,
            gonderOyunAc: Button,
            textSayi1OyunAc: TextView,
            textSayi2OyunAc: TextView,
            textSayi3OyunAc: TextView,
            textSayi4OyunAc: TextView,
            linearLayout3OyunAc: LinearLayout,
            uyariOyunAc: TextView,
            recyclerViewOyunAc: RecyclerView) {

        socket = App.socket

        socket.on("game") {
            activity.runOnUiThread {
                run {
                    val numbers = JSONObject(it[0].toString())
                    zamanOyunAc.text = numbers.getInt("time").toString()

                }
            }
        }
                .on("time") {
                    activity.runOnUiThread {
                        run {
                            val time = it[0].toString()
                            zamanOyunAc.text = time
                        }
                    }
                }
                .on("sendhomegame") {
                    activity.runOnUiThread {
                        run {
                            val sends = JSONObject(it[0].toString())
                            zamanOyunAc.text = sends.getInt("sendtimehome").toString()
                        }
                    }
                }
                .on("sendtimehome") {
                    activity.runOnUiThread {
                        run {
                            val sendTimeHome = it[0].toString()
                            zamanOyunAc.text = sendTimeHome
                        }
                    }
                }
                .on("enableclickhome") {
                    activity.runOnUiThread {
                        run {
                            gonderOyunAc.isEnabled = true
                            gonderOyunAc.isClickable = true
                        }
                    }
                }
                .on("notenableclickaway") {
                    activity.runOnUiThread {
                        run {
                            gonderOyunAc.isEnabled = false
                            gonderOyunAc.isClickable = false
                        }
                    }
                }
                .on("sendawaygame") {
                    activity.runOnUiThread {
                        run {
                            val sends = JSONObject(it[0].toString())
                            zamanOyunAc.text = sends.getInt("sendtimeaway").toString()
                        }
                    }
                }
                .on("sendtimeaway") {
                    activity.runOnUiThread {
                        run {
                            val sendTimeAway = it[0].toString()
                            zamanOyunAc.text = sendTimeAway
                        }
                    }
                }
                .on("enableclickaway") {
                    activity.runOnUiThread {
                        run {
                            gonderOyunAc.isEnabled = true
                            gonderOyunAc.isClickable = true
                        }
                    }
                }
                .on("notenableclickhome") {
                    activity.runOnUiThread {
                        run {
                            gonderOyunAc.isEnabled = false
                            gonderOyunAc.isClickable = false
                        }
                    }
                }
                .on("selected") {
                    activity.runOnUiThread {
                        run {
                            val myNumbers = JSONArray(it[0].toString())
                            textSayi1OyunAc.text = myNumbers[0].toString()
                            textSayi2OyunAc.text = myNumbers[1].toString()
                            textSayi3OyunAc.text = myNumbers[2].toString()
                            textSayi4OyunAc.text = myNumbers[3].toString()

                            linearLayout3OyunAc.visibility = View.VISIBLE
                            uyariOyunAc.visibility = View.GONE
                            gonderOyunAc.visibility = View.VISIBLE
                        }
                    }
                }
                .on("not_selected") {
                    activity.runOnUiThread {
                        run {
                            val intent = Intent(activity, AnaEkranActivity::class.java)
                            activity.startActivity(intent)
                            activity.finish()
                        }
                    }
                }
                .on("totalanswer") {
                    activity.runOnUiThread {
                        run {
                            val totalAnswers = mutableListOf<Cevap>()
                            val totalAnswer = JSONArray(it[0].toString())
                            for (i in 0 until totalAnswer.length()) {
                                val item = totalAnswer.getJSONObject(i)
                                totalAnswers.add(Cevap(item.getString("cevap1"), item.getString("cevap2"), item.getString("cevap3"), item.getString("cevap4"), item.getString("allcevap")))
                            }
                            val linearLayoutManager = LinearLayoutManager(activity)
                            linearLayoutManager.reverseLayout = true
                            linearLayoutManager.stackFromEnd = true
                            recyclerViewOyunAc.layoutManager = linearLayoutManager
                            recyclerViewOyunAc.adapter = CevapAdapter(totalAnswers)
                        }
                    }
                }
                .on("finish") {
                    activity.runOnUiThread {
                        run {
                            val data = JSONObject(it[0].toString())
                            val intent = Intent(activity, SonucActivity::class.java)
                            intent.putExtra("homePlayer", data.getString("homePlayer"))
                            intent.putExtra("awayPlayer", data.getString("awayPlayer"))
                            intent.putExtra("homeAnswer", data.getString("homeAnswer"))
                            intent.putExtra("homeAnswer1", data.getString("homeAnswer1"))
                            intent.putExtra("homeAnswer2", data.getString("homeAnswer2"))
                            intent.putExtra("homeAnswer3", data.getString("homeAnswer3"))
                            intent.putExtra("awayAnswer", data.getString("awayAnswer"))
                            intent.putExtra("awayAnswer1", data.getString("awayAnswer1"))
                            intent.putExtra("awayAnswer2", data.getString("awayAnswer2"))
                            intent.putExtra("awayAnswer3", data.getString("awayAnswer3"))
                            intent.putExtra("result", data.getString("result"))
                            activity.startActivity(intent)
                            activity.finish()
                        }
                    }
                }
    }
}