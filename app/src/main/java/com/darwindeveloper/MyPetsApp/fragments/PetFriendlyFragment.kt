package com.darwindeveloper.MyPetsApp.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.darwindeveloper.MyPetsApp.R
import com.darwindeveloper.MyPetsApp.SitiosActivity
import com.darwindeveloper.MyPetsApp.adapters.PetFriendlyAdapter
import com.darwindeveloper.MyPetsApp.api.modelos.ItemPetFriendly
import kotlinx.android.synthetic.main.fragment_pet_friendly.view.*

/**
 * Created by DARWIN MOROCHO on 9/9/2017.
 */
class PetFriendlyFragment : Fragment(), PetFriendlyAdapter.OnItemPetFriendlyListener {
    override fun onClickPetFriendly(item: ItemPetFriendly) {


        val intent = Intent(context, SitiosActivity::class.java)
        intent.putExtra(SitiosActivity.CATEGORIA, item.text)
        intent.putExtra(SitiosActivity.ICON, item.icon)
        startActivity(intent)
    }


    var rootView: View? = null
    private var gridsP = 1
    private var gridsL = 2
    private var glm: GridLayoutManager? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater?.inflate(R.layout.fragment_pet_friendly, container, false)


        val categorias = arrayOf(ItemPetFriendly(R.drawable.coffe,R.drawable.ic_coffe, "Cafeteria"),
                ItemPetFriendly(R.drawable.mall,R.drawable.ic_mall, "Centros Comerciales"),
                ItemPetFriendly(R.drawable.vet,R.drawable.ic_vet, "Establecimiento Veterinario Afiliado"),
                ItemPetFriendly(R.drawable.icecream, R.drawable.ic_icecream, "Heladería"),
                ItemPetFriendly(R.drawable.hostel, R.drawable.ic_hostel, "Hosterias"),
                ItemPetFriendly(R.drawable.hotel, R.drawable.ic_hotel, "Hoteles"),
                ItemPetFriendly(R.drawable.church,R.drawable.ic_church, "Iglesias"),
                ItemPetFriendly(R.drawable.cake, R.drawable.ic_cake, "Pasteleria"),
                ItemPetFriendly(R.drawable.bread, R.drawable.ic_bread, "Panaderia"),
                ItemPetFriendly(R.drawable.pizza,R.drawable.ic_pizza, "Pizzeria"),
                ItemPetFriendly(R.drawable.park,R.drawable.ic_park, "Parques"),
                ItemPetFriendly(R.drawable.restaurant, R.drawable.ic_restaurant, "Restaurantes"),
                ItemPetFriendly(R.drawable.bell_boy,R.drawable.ic_bell_boy, "Servicios Varios"))


        val adapter = PetFriendlyAdapter(context, categorias)

        rootView!!.fpf_list.adapter = adapter
        adapter.onItemListener = this

        val screenSize = resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK


        when (screenSize) {
            Configuration.SCREENLAYOUT_SIZE_LARGE -> {
                gridsL = 5
                gridsP = 4
            }
            Configuration.SCREENLAYOUT_SIZE_NORMAL -> {
                gridsL = 4
                gridsP = 3
            }
            Configuration.SCREENLAYOUT_SIZE_SMALL -> {
                gridsL = 3
                gridsP = 2
            }
            else -> {
                gridsL = 2
                gridsP = 1
            }
        }


        if (activity.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            glm = GridLayoutManager(context, gridsP)
        } else {
            // Landscape Mode
            glm = GridLayoutManager(context, gridsL)

        }
        rootView!!.fpf_list.layoutManager = glm

        return rootView
    }

}