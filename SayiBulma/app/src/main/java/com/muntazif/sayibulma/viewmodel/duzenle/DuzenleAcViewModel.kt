package com.muntazif.sayibulma.viewmodel.duzenle

import android.app.Activity
import android.graphics.YuvImage
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.nkzawa.socketio.client.Socket
import com.muntazif.sayibulma.util.App

class DuzenleAcViewModel : ViewModel() {

    val yukleniyor = MutableLiveData<Boolean>()
    lateinit var socket: Socket

    fun socketVeriAl(activity : Activity){

        socket = App.socket

        socket.on("updateaccount") {
            activity.runOnUiThread {
                run {
                    yukleniyor.value = false
                    activity.finish()
                }
            }
        }
    }
}