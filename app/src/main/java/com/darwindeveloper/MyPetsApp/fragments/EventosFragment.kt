package com.darwindeveloper.MyPetsApp.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.darwindeveloper.MyPetsApp.R
import com.darwindeveloper.MyPetsApp.adapters.EventosAdapter
import com.darwindeveloper.MyPetsApp.api.WebApiClient
import com.darwindeveloper.MyPetsApp.api.WebService
import com.darwindeveloper.MyPetsApp.api.modelos.Cita
import kotlinx.android.synthetic.main.fragment_eventos.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by DARWIN MOROCHO on 24/8/2017.
 */
class EventosFragment : Fragment() {

    var user_id: String? = null
    var api_token: String? = null

    private var loadTask: LoadTask? = null

    private val eventos: ArrayList<Cita> = ArrayList()
    private var adapter: EventosAdapter? = null
    private var rootView: View? = null

    companion object {
        const val USER_ID = "EF.user_id";
        const val USER_API_TOKEN = "EF.user_api_token";

        fun newInstance(args: Bundle): EventosFragment {
            val fr = EventosFragment()
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
        rootView = inflater?.inflate(R.layout.fragment_eventos, container, false) as View

        adapter = EventosAdapter(context, eventos)
        rootView!!.fe_list.layoutManager = LinearLayoutManager(context)
        rootView!!.fe_list.adapter = adapter

        if (loadTask != null)
            loadTask = null
        loadTask = LoadTask()
        loadTask?.execute()

        return rootView
    }


    internal inner class LoadTask : AsyncTask<Void, Void, Void>(), Callback<MutableList<Cita>> {
        override fun onFailure(call: Call<MutableList<Cita>>?, t: Throwable?) {
            Log.i("eventos", "fallo")
        }

        override fun onResponse(call: Call<MutableList<Cita>>?, response: Response<MutableList<Cita>>?) {
            if (response?.body() != null) {

                if (response.body().size > 0) {
                    rootView!!.fe_list.visibility = View.VISIBLE
                    rootView!!.fe_cardView.visibility = View.GONE
                    eventos.addAll(response.body())
                    adapter?.notifyItemRangeInserted(0, eventos.size)
                    adapter?.notifyDataSetChanged()

                }

            }else
                Log.i("eventos", "null")
        }

        override fun onPreExecute() {
            Toast.makeText(context, "cargando eventos...", Toast.LENGTH_SHORT).show()
        }

        override fun doInBackground(vararg p0: Void?): Void? {

            val ws = WebApiClient.client!!.create(WebService::class.java)
            val call = ws.citas(user_id, api_token)
            call.enqueue(this)


            return null
        }

        override fun onCancelled() {
            loadTask = null
        }

    }


}