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
import kotlinx.android.synthetic.main.item_event.view.*

/**
 * Created by DARWIN MOROCHO on 24/8/2017.
 */
class EventosAdapter(val context: Context, val eventos: ArrayList<Cita>) : RecyclerView.Adapter<EventosAdapter.EViewHolder>() {
    override fun getItemCount(): Int {
        return eventos.size
    }

    override fun onBindViewHolder(holder: EViewHolder?, position: Int) {

        val cita = eventos[position]

        holder!!.itemView.ie_fecha.text = " ${cita.fecha} ${cita.hora}"
        holder.itemView.ie_motivo.text = "${cita.motivo}"

        if (cita.mascota_foto != null) {
            Picasso.with(context)
                    .load(Constants.WEB_URL + cita.mascota_foto)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.itemView.ie_foto);
        }

        holder.itemView.ie_container.setOnClickListener {
            onEventsClickListener?.onEventClick(cita)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): EViewHolder {
        return EViewHolder(LayoutInflater.from(context).inflate(R.layout.item_event, parent, false))
    }


    class EViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    var onEventsClickListener: OnEventsClickListener? = null

    public interface OnEventsClickListener {
        /**
         * listsner para cunado se de click en un evento o citaX
         */
        fun onEventClick(cita: Cita)
    }


}