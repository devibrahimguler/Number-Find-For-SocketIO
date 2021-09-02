package com.muntazif.sayibulma.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.muntazif.sayibulma.R
import com.muntazif.sayibulma.databinding.SiralamaRcRowBinding
import com.muntazif.sayibulma.model.Siralama

class SiralamaAdapter(private val siralamaList : MutableList<Siralama>) : RecyclerView.Adapter<SiralamaAdapter.SiralamaViewHolder>() {
    class SiralamaViewHolder(val view : SiralamaRcRowBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiralamaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<SiralamaRcRowBinding>(inflater, R.layout.siralama_rc_row, parent, false)
        return SiralamaViewHolder(view)
    }

    override fun onBindViewHolder(holder: SiralamaViewHolder, position: Int) {
        holder.view.siralama = siralamaList[position]
    }

    override fun getItemCount(): Int {
        return siralamaList.size
    }
}