package com.muntazif.sayibulma.util

import android.app.Application
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket

class App : Application() {
    companion object{
        lateinit var socket: Socket
    }
    private val url = "http://10.0.2.2:1304"
    override fun onCreate() {
        super.onCreate()
        socket = IO.socket(url)
        socket.connect()
    }
}