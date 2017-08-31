package com.darwindeveloper.MyPetsApp.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.darwindeveloper.MyPetsApp.R
import com.darwindeveloper.MyPetsApp.adapters.MascotasAdapter
import com.darwindeveloper.MyPetsApp.api.WebApiClient
import com.darwindeveloper.MyPetsApp.api.WebService
import com.darwindeveloper.MyPetsApp.api.modelos.Mascota
import com.darwindeveloper.MyPetsApp.bottom_sheets.BSmascota
import kotlinx.android.synthetic.main.fragment_mascotas.*
import kotlinx.android.synthetic.main.fragment_mascotas.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by DARWIN MOROCHO on 23/8/2017.
 */
class MascotasFragment : Fragment(), MascotasAdapter.MascotasOnClickListener {


    override fun mascotaOnClick(mascota: Mascota) {

        val args = Bundle()
        args.putString(BSmascota.MASCOTA_ID, mascota.mascota_id)
        args.putString(BSmascota.USER_ID, user_id)
        val bs = BSmascota.newInstance(args)
        bs.show(activity.supportFragmentManager, "Bsmascota")


    }


    companion object {
        const val USER_ID: String = "mascotasFragment.user_id"

        fun newInstance(args: Bundle): MascotasFragment {
            val fragment = MascotasFragment()
            fragment.arguments = args
            return fragment
        }
    }

    var rootView: View? = null
    private var adapter: MascotasAdapter? = null
    private val mascotas: ArrayList<Mascota> = ArrayList()


    private var user_id: String? = null

    private var loadTask: LoadTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user_id = arguments[USER_ID] as String
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater?.inflate(R.layout.fragment_mascotas, container, false)

        rootView!!.list_mascotas.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = MascotasAdapter(context, mascotas)
        adapter?.mascotasOnClickListener = this
        rootView!!.list_mascotas.adapter = adapter




        if (user_id != null) {

            if (loadTask != null)
                loadTask = null

            loadTask = LoadTask()
            loadTask?.execute()

        }


        return rootView
    }


    inner class LoadTask : AsyncTask<Void, Void, Void>(), Callback<MutableList<Mascota>> {
        override fun onFailure(call: Call<MutableList<Mascota>>?, t: Throwable?) {
            Log.i("errorMascotas", t?.message)
            loadTask = null
        }

        override fun onResponse(call: Call<MutableList<Mascota>>?, response: Response<MutableList<Mascota>>?) {
            if (response?.body() != null) {
                val mascotas_response = response.body()
                for (masoota: Mascota in mascotas_response) {
                    mascotas.add(masoota)
                    adapter?.notifyItemInserted(mascotas_response.size)
                }
            } else
                Log.i("erroNull", "response null")
        }


        override fun doInBackground(vararg p0: Void?): Void? {

            val webService = WebApiClient.client!!.create(WebService::class.java)

            val call = webService.mascotas(user_id)
            call.enqueue(this)

            return null
        }

        override fun onCancelled() {
            loadTask = null
        }

    }


}