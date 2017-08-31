package com.darwindeveloper.MyPetsApp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.darwindeveloper.MyPetsApp.R
import com.darwindeveloper.MyPetsApp.api.Constants
import com.darwindeveloper.MyPetsApp.api.modelos.Mascota
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_mascota.view.*

/**
 * Created by DARWIN MOROCHO on 23/8/2017.
 */
class MascotasAdapter(private val context: Context, private val mascotas: ArrayList<Mascota>) : RecyclerView.Adapter<MascotasAdapter.MViewHolder>() {


    override fun onBindViewHolder(holder: MViewHolder?, position: Int) {
        val mascota = mascotas[position]
        holder!!.name.text = mascota.nombre
        holder.id.text = "ID: ${mascota.mascota_id}"

        if (mascota.foto != null) {
            Picasso.with(context)
                    .load(Constants.WEB_URL+mascota.foto)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.image);
        }


        holder.itemView.setOnClickListener {
            mascotasOnClickListener?.mascotaOnClick(mascota)
        }

    }

    override fun getItemCount(): Int {
        return mascotas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MViewHolder {
        return MViewHolder(LayoutInflater.from(context).inflate(R.layout.item_mascota, parent, false))
    }


    inner class MViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView
        var name: TextView
        var id: TextView

        init {

            image = itemView.item_mascota_foto
            name = itemView.item_mascota_nombre
            id = itemView.item_mascota_id

        }
    }


    var mascotasOnClickListener: MascotasOnClickListener? = null

    public interface MascotasOnClickListener {
        fun mascotaOnClick(mascota: Mascota)
    }
}