package com.darwindeveloper.MyPetsApp.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.darwindeveloper.MyPetsApp.R
import android.support.v4.widget.NestedScrollView
import com.darwindeveloper.MyPetsApp.sqlite.DBManager
import kotlinx.android.synthetic.main.fragment_main.view.*


/**
 * Created by DARWIN MOROCHO on 24/8/2017.
 */
class MainFragment : Fragment() {


    private var user_id: String? = null
    private var api_token: String? = null


    companion object {
        const val USER_ID = "frmain.user_id"
        const val USER_API_TOKEN = "frmain.user_api_token"


        fun newInstance(args: Bundle): MainFragment {
            val fr = MainFragment()
            fr.arguments = args
            return fr
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user_id = arguments[USER_ID] as String
        api_token = arguments[USER_API_TOKEN] as String

    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_main, container, false)

        val args = Bundle()
        args.putString(MascotasFragment.USER_ID, user_id)
        args.putString(MascotasFragment.API_TOKEN, api_token)

        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_mascotas, MascotasFragment.newInstance(args))
        transaction.commit()


        val args2 = Bundle()
        args2.putString(EstablecimientosFragment.USER_ID, user_id)
        args2.putString(EstablecimientosFragment.API_TOKEN, api_token)


        val transaction2 = activity.supportFragmentManager.beginTransaction()
        transaction2.replace(R.id.fragment_container_establecimientos, EstablecimientosFragment.newInstance(args2))
        transaction2.commit()


        val args3 = Bundle()
        args3.putString(EventosFragment.USER_ID, user_id)
        args3.putString(EventosFragment.USER_API_TOKEN, api_token)


        val transaction3 = activity.supportFragmentManager.beginTransaction()
        transaction3.replace(R.id.fragment_container_eventos, EventosFragment.newInstance(args3))
        transaction3.commit()



        rootView!!.frm_scrollview.isFillViewport = true
        return rootView
    }

}