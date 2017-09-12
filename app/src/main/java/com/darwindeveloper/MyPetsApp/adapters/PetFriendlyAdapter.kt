package com.darwindeveloper.MyPetsApp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.darwindeveloper.MyPetsApp.R
import com.darwindeveloper.MyPetsApp.api.modelos.ItemPetFriendly
import kotlinx.android.synthetic.main.item_pet_friendly.view.*

/**
 * Created by DARWIN MOROCHO on 9/9/2017.
 */
class PetFriendlyAdapter(val context: Context, val categorias: Array<ItemPetFriendly>) : RecyclerView.Adapter<PetFriendlyAdapter.PFViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PFViewHolder {
        return PFViewHolder(LayoutInflater.from(context).inflate(R.layout.item_pet_friendly, parent, false))
    }

    override fun onBindViewHolder(holder: PFViewHolder?, position: Int) {
        val cat = categorias[position]
        holder!!.itemView.ipf_icono.setImageDrawable(context.resources.getDrawable(cat.image))
        holder.itemView.ipf_text.text = cat.text
        holder.itemView.ipf_layout.setOnClickListener {
            onItemListener?.onClickPetFriendly(cat)
        }
    }

    override fun getItemCount(): Int {
        return categorias.size
    }


    inner class PFViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    var onItemListener: OnItemPetFriendlyListener? = null

    public interface OnItemPetFriendlyListener {
        fun onClickPetFriendly(item: ItemPetFriendly)
    }
}