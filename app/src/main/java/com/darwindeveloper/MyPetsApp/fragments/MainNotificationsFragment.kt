package com.darwindeveloper.MyPetsApp.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        return rootView

    }


    private inner class LoadNums : AsyncTask<Void, Void, Void>() {
        var arr: Array<Int>? = null
        override fun doInBackground(vararg p0: Void?): Void? {
            arr = dbm!!.getNums()
            return null
        }

        override fun onPostExecute(result: Void?) {
            rootView!!.fmn_text_num_dato.text = "${arr!![3]}"
            Toast.makeText(context, "${arr!![3]}", Toast.LENGTH_SHORT).show()
        }


        override fun onCancelled() {
            load = null
        }

    }

}