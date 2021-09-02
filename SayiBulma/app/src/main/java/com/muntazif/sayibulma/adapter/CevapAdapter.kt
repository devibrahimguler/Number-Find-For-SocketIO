package com.muntazif.sayibulma.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.muntazif.sayibulma.R
import com.muntazif.sayibulma.databinding.SayiCevapRcRowBinding
import com.muntazif.sayibulma.model.Cevap

class CevapAdapter(private val cevapList : MutableList<Cevap>) : RecyclerView.Adapter<CevapAdapter.CevapViewHolder>() {
    class CevapViewHolder(val view : SayiCevapRcRowBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CevapViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<SayiCevapRcRowBinding>(inflater, R.layout.sayi_cevap_rc_row, parent, false)
        return CevapViewHolder(view)
    }

    override fun onBindViewHolder(holder: CevapViewHolder, position: Int) {
        holder.view.cevaplar = cevapList[position]
    }

    override fun getItemCount(): Int {
        return cevapList.size
    }

}