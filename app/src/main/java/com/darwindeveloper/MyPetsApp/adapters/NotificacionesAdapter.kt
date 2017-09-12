package com.darwindeveloper.MyPetsApp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.darwindeveloper.MyPetsApp.R
import com.darwindeveloper.MyPetsApp.sqlite.Notification
import kotlinx.android.synthetic.main.item_notification.view.*

/**
 * Created by DARWIN MOROCHO on 4/9/2017.
 */
class NotificacionesAdapter(private val context: Context, private val notifivations: ArrayList<Notification>) : RecyclerView.Adapter<NotificacionesAdapter.NViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NViewHolder {
        return NViewHolder(LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false))
    }

    override fun getItemCount(): Int {
        return notifivations.size
    }

    override fun onBindViewHolder(holder: NViewHolder?, position: Int) {
        val notifi = notifivations[position]


        holder!!.itemView.itemn_title.text = notifi.titulo
        holder.itemView.itemn_created.text = notifi.created_at
        holder.itemView.itemn_delete.setOnClickListener {
            onNotificacionesListener!!.eliminarNotificacion(position, notifi)
        }

        holder.itemView.itemn_see.setOnClickListener {
            onNotificacionesListener!!.verNotificacion(notifi)
        }

    }


    inner class NViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    var onNotificacionesListener: OnNotificacionesListener? = null

    public interface OnNotificacionesListener {
        fun verNotificacion(notificacion: Notification)
        fun eliminarNotificacion(pos: Int, notificacion: Notification)
    }

}