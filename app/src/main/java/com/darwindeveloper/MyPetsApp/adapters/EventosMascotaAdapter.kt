package com.darwindeveloper.MyPetsApp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.darwindeveloper.MyPetsApp.R
import com.darwindeveloper.MyPetsApp.api.Constants
import com.darwindeveloper.MyPetsApp.api.modelos.Cita
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_event_p.view.*

/**
 * Created by DARWIN MOROCHO on 24/8/2017.
 */
class EventosMascotaAdapter(val context: Context, val eventos: ArrayList<Cita>) : RecyclerView.Adapter<EventosMascotaAdapter.EViewHolder>() {
    override fun getItemCount(): Int {
        return eventos.size
    }

    override fun onBindViewHolder(holder: EViewHolder?, position: Int) {

        val cita = eventos[position]

        holder!!.itemView.iem_fecha.text = " ${cita.fecha} ${cita.hora}"
        holder.itemView.iem_motivo.text = "${cita.motivo}"

        holder.itemView.iem_container.setOnClickListener {
            onEventsPetClickListener?.onEventPetClick(cita)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): EViewHolder {
        return EViewHolder(LayoutInflater.from(context).inflate(R.layout.item_event_p, parent, false))
    }


    class EViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var onEventsPetClickListener: OnEventsPetClickListener? = null

    public interface OnEventsPetClickListener {
        /**
         * listsner para cunado se de click en un evento o citaX
         */
        fun onEventPetClick(cita: Cita)
    }


}