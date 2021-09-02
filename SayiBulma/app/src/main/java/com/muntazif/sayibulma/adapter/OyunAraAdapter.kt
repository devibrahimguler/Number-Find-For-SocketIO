package com.muntazif.sayibulma.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import android.app.AlertDialog
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.muntazif.sayibulma.R
import com.muntazif.sayibulma.databinding.OnlineOyuncuRcRowBinding
import com.muntazif.sayibulma.model.Kullanici
import com.muntazif.sayibulma.util.App
import com.muntazif.sayibulma.view.giris.GirisActivity

class OyunAraAdapter(private val kullanicilar : MutableList<Kullanici>) : RecyclerView.Adapter<OyunAraAdapter.OyunViewHolder>() {
    class OyunViewHolder(val view : OnlineOyuncuRcRowBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OyunViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<OnlineOyuncuRcRowBinding>(inflater, R.layout.online_oyuncu_rc_row, parent, false)
        return OyunViewHolder(view)
    }

    override fun onBindViewHolder(holder: OyunViewHolder, position: Int) {
        val kullanici = kullanicilar[position]

        holder.view.kullanici = kullanicilar[position]

        if (kullanici.durum) {
            holder.view.durumOnlineOyunRc.setImageResource(R.drawable.acik)
            holder.view.isimOnlineOyuncuRc.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.available))
        }else{
            holder.view.durumOnlineOyunRc.setImageResource(R.drawable.kapali)
            holder.view.isimOnlineOyuncuRc.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.busy))
        }

        holder.itemView.setOnClickListener {
            if (kullanici.durum){
                val builder = AlertDialog.Builder(holder.itemView.context)
                builder.setTitle("Oyun İsteği")
                builder.setMessage(kullanici.isim + " kişisine oyun isteği yapmak istediğinize emin misiniz ?")
                builder.setPositiveButton("İstek Yap") { dialog, which ->
                    App.socket.emit("request", kullanici.id)
                    Toast.makeText(holder.itemView.context, kullanici.isim + " kişisine oyun isteği yapıldı.", Toast.LENGTH_SHORT).show()
                }
                builder.setNegativeButton("Vazgeç"){ dialog, which -> }
                val dialog : AlertDialog = builder.create()
                dialog.show()
            }else{
                val builder = AlertDialog.Builder(holder.itemView.context)
                builder.setTitle("Oyun İsteği")
                builder.setMessage(kullanici.isim + " kişisi meşgul olduğu için ona oyun isteği gönderemezsiniz.")
                builder.setNegativeButton("Tamam") { dialog, which ->  }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }

    }

    override fun getItemCount(): Int {
        return kullanicilar.size
    }
}