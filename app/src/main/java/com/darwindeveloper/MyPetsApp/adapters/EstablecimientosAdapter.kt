package com.darwindeveloper.MyPetsApp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.darwindeveloper.MyPetsApp.R
import com.darwindeveloper.MyPetsApp.api.Constants
import com.darwindeveloper.MyPetsApp.api.modelos.Establecimiento
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_establecimiento.view.*

import java.util.ArrayList

/**
 * Created by DARWIN MOROCHO on 10/8/2017.
 */

class EstablecimientosAdapter(private val context: Context, private val establecimientos: ArrayList<Establecimiento>) : RecyclerView.Adapter<EstablecimientosAdapter.ESViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ESViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_establecimiento, parent, false)
        return ESViewHolder(view)
    }

    override fun onBindViewHolder(holder: ESViewHolder?, position: Int) {

        val est = establecimientos[position]

        holder!!.itemView.item_e_id.text = "ID: ${est.establecimiento_id}"
        holder.itemView.item_e_nombre.text = est.nombre_establecimiento

        Log.i("nombre", est.nombre_establecimiento)

        if (est.establecimiento_id == "-1") {
            holder.itemView.item_e_id.visibility=View.GONE
            holder.itemView.item_e_foto.setImageDrawable(context.resources.getDrawable(R.drawable.add))
        } else if (est.icono != null) {
            holder.itemView.item_e_id.visibility=View.VISIBLE
            Picasso.with(context)
                    .load(Constants.WEB_URL + est.icono)
                    .into(holder.itemView.item_e_foto)
        }



        holder.itemView.setOnClickListener {
            onClickEstListener?.onEstClick(est)
        }

    }

    override fun getItemCount(): Int {
        return establecimientos.size
    }


    inner class ESViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    var onClickEstListener: OnClickEstListener? = null

    public interface OnClickEstListener {
        /**
         * listener para cuando se da click en un establecimeinto
         */
        fun onEstClick(est: Establecimiento)
    }

}
