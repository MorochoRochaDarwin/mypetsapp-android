package com.darwindeveloper.MyPetsApp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.darwindeveloper.MyPetsApp.R
import com.darwindeveloper.MyPetsApp.api.modelos.Sitio
import kotlinx.android.synthetic.main.item_sitio.view.*

/**
 * Created by DARWIN MOROCHO on 23/9/2017.
 */
class SitioAdapter(val context: Context, val sitios: ArrayList<Sitio>) : RecyclerView.Adapter<SitioAdapter.SViewHolder>() {
    override fun onBindViewHolder(holder: SViewHolder?, position: Int) {
        val sitio = sitios[position]
        holder!!.itemView.item_sitio_nombre.text = sitio.nombre
        holder.itemView.item_sitio_tipo.text = sitio.tipo
        holder.itemView.item_sitio_layout.setOnClickListener {
            onSitioListener!!.onClickSitio(sitio)
        }

    }

    var onSitioListener: OnSitioListener? = null

    public interface OnSitioListener {
        fun onClickSitio(sitio: Sitio)
    }

    override fun getItemCount(): Int {
        return sitios.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SViewHolder {
        return SViewHolder(LayoutInflater.from(context).inflate(R.layout.item_sitio, parent, false))
    }


    class SViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
}