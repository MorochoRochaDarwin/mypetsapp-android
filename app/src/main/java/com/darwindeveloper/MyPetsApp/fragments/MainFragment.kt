package com.darwindeveloper.MyPetsApp.fragments

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.darwindeveloper.MyPetsApp.R
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.darwindeveloper.MyPetsApp.EventActivity
import com.darwindeveloper.MyPetsApp.MascotaActivity
import com.darwindeveloper.MyPetsApp.adapters.EventosAdapter
import com.darwindeveloper.MyPetsApp.adapters.MascotasAdapter
import com.darwindeveloper.MyPetsApp.api.WebApiClient
import com.darwindeveloper.MyPetsApp.api.WebService
import com.darwindeveloper.MyPetsApp.api.modelos.Cita
import com.darwindeveloper.MyPetsApp.api.modelos.Mascota
import com.darwindeveloper.MyPetsApp.sqlite.DBManager
import kotlinx.android.synthetic.main.fragment_main.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by DARWIN MOROCHO on 24/8/2017.
 */
class MainFragment : Fragment(), EventosAdapter.OnEventsClickListener, MascotasAdapter.MascotasOnClickListener {
    override fun mascotaOnClick(mascota: Mascota) {
        val i = Intent(context, MascotaActivity::class.java)
        i.putExtra(MascotaActivity.USER_ID, user_id)
        i.putExtra(MascotaActivity.API_TOKEN, api_token)
        i.putExtra(MascotaActivity.MASCOTA_ID, mascota.mascota_id)
        i.putExtra(MascotaActivity.EST_ID, est_id)

        startActivity(i)
    }

    override fun onEventClick(cita: Cita) {
        val i = Intent(context, EventActivity::class.java)
        i.putExtra(EventActivity.CITA_ID, cita.cita_id)
        i.putExtra(EventActivity.TITULO, cita.motivo)
        i.putExtra(EventActivity.CREADA, cita.fecha_creada)
        i.putExtra(EventActivity.FECHA, cita.fecha)
        i.putExtra(EventActivity.HORA, cita.hora)
        i.putExtra(EventActivity.DESCR, cita.descripcion)
        i.putExtra(EventActivity.EST_ID, cita.establecimiento_id)
        i.putExtra(EventActivity.EST, cita.nombre_establecimiento)
        i.putExtra(EventActivity.MASCOTA_ID, cita.mascota_id)
        i.putExtra(EventActivity.MASCOTA_NOMBRE, cita.mascota_nombre)
        i.putExtra(EventActivity.MASCOTA_FOTO, cita.mascota_foto)

        startActivity(i)
    }


    private var user_id: String? = null
    private var api_token: String? = null
    private var est_id: String? = null

    private val eventos: ArrayList<Cita> = ArrayList()
    private var adapterE: EventosAdapter? = null
    private var adapter: MascotasAdapter? = null
    private val mascotas: ArrayList<Mascota> = ArrayList()
    private var loadTask: LoadTask? = null
    private var loadTaskE: LoadTaskE? = null
    private var rootView: View? = null

    companion object {
        const val USER_ID = "frmain.user_id"
        const val USER_API_TOKEN = "frmain.user_api_token"
        const val EST_ID = "frmain.est_ID"


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
        est_id = arguments[EST_ID] as String

    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater?.inflate(R.layout.fragment_main, container, false)

        adapter = MascotasAdapter(context = context, mascotas = mascotas)
        adapterE = EventosAdapter(context = context, eventos = eventos)
        adapterE?.onEventsClickListener = this
        rootView!!.ae_mascotas.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rootView!!.ae_eventos.layoutManager = LinearLayoutManager(context)
        adapter?.mascotasOnClickListener = this
        rootView!!.ae_mascotas.adapter = adapter
        rootView!!.ae_eventos.adapter = adapterE


        if (user_id != null) {

            if (loadTask != null)
                loadTask = null

            loadTask = LoadTask()
            loadTask?.execute()


            if (loadTaskE != null)
                loadTaskE = null

            loadTaskE = LoadTaskE()
            loadTaskE?.execute()

        }


        //rootView!!.frm_scrollview.isFillViewport = true
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

            val call = webService.mascotas_establecimiento(user_id, est_id)
            call.enqueue(this)

            return null
        }

        override fun onCancelled() {
            loadTask = null
        }

    }


    internal inner class LoadTaskE : AsyncTask<Void, Void, Void>(), Callback<MutableList<Cita>> {
        override fun onFailure(call: Call<MutableList<Cita>>?, t: Throwable?) {
            Log.i("eventos", "fallo")
            loadTaskE = null
        }

        override fun onResponse(call: Call<MutableList<Cita>>?, response: Response<MutableList<Cita>>?) {
            if (response?.body() != null) {

                if (response.body().size > 0) {
                    rootView!!.ae_eventos.visibility = View.VISIBLE
                    rootView!!.ae_cardView.visibility = View.GONE
                    eventos.addAll(response.body())
                    adapter?.notifyItemRangeInserted(0, eventos.size)
                    adapter?.notifyDataSetChanged()

                }

            } else
                Log.i("eventos", "null")
        }

        override fun onPreExecute() {
            //Toast.makeText(this@EstablecimientoActivity, "cargando eventos...", Toast.LENGTH_SHORT).show()
        }

        override fun doInBackground(vararg p0: Void?): Void? {

            val ws = WebApiClient.client!!.create(WebService::class.java)
            val call = ws.citas_est(user_id, est_id)
            call.enqueue(this)


            return null
        }

        override fun onCancelled() {
            loadTaskE = null
        }

    }

}