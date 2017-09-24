package com.darwindeveloper.MyPetsApp.fragments

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.darwindeveloper.MyPetsApp.NotificacionesActivity
import com.darwindeveloper.MyPetsApp.R
import com.darwindeveloper.MyPetsApp.sqlite.DBManager
import kotlinx.android.synthetic.main.fragment_main_notifications.view.*

/**
 * Created by DARWIN MOROCHO on 21/9/2017.
 */
class MainNotificationsFragment : Fragment() {

    private var rootView: View? = null
    private var dbm: DBManager? = null
    private var load: LoadNums? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dbm = DBManager(context)
        rootView = inflater!!.inflate(R.layout.fragment_main_notifications, container, false)


        if (load != null)
            load = null

        load = LoadNums()
        load!!.execute()



        rootView!!.fmn_medico.setOnClickListener {
            runAN("Veterinario")
        }

        rootView!!.fmn_promociones.setOnClickListener {
            runAN("Descuentos")
        }

        rootView!!.fmn_dato.setOnClickListener {
            runAN("Ultimas noticias")
        }

        rootView!!.fmn_publicidad.setOnClickListener {
            runAN("Publicidad")
        }


        rootView!!.fmn_event.setOnClickListener {
            runAN("none")
        }

        return rootView

    }


    private fun runAN(tipo: String) {
        val i = Intent(context, NotificacionesActivity::class.java)
        i.putExtra(NotificacionesActivity.TIPO, tipo)
        startActivity(i)
    }


    private inner class LoadNums : AsyncTask<Void, Void, Void>() {
        var arr: Array<Int>? = null
        override fun doInBackground(vararg p0: Void?): Void? {
            arr = dbm!!.getNums()
            return null
        }

        override fun onPostExecute(result: Void?) {
            rootView!!.fmn_text_num_dato.text = "${arr!![3]}"
            rootView!!.fmn_text_num_medico.text = "${arr!![0]}"
            rootView!!.fmn_text_num_promociones.text = "${arr!![1]}"
            rootView!!.fmn_text_num_publicidad.text = "${arr!![2]}"
            rootView!!.fmn_text_num_event.text = "${arr!![4]}"
            // Toast.makeText(context, "${arr!![3]}", Toast.LENGTH_SHORT).show()
        }


        override fun onCancelled() {
            load = null
        }

    }

}