package com.darwindeveloper.MyPetsApp

import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.darwindeveloper.MyPetsApp.adapters.EventosAdapter
import com.darwindeveloper.MyPetsApp.adapters.MascotasAdapter
import com.darwindeveloper.MyPetsApp.api.Constants
import com.darwindeveloper.MyPetsApp.api.WebApiClient
import com.darwindeveloper.MyPetsApp.api.WebService
import com.darwindeveloper.MyPetsApp.api.modelos.Cita
import com.darwindeveloper.MyPetsApp.api.modelos.Mascota
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_establecimiento.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EstablecimientoActivity : AppCompatActivity(), MascotasAdapter.MascotasOnClickListener, EventosAdapter.OnEventsClickListener {
    override fun onEventClick(cita: Cita) {
        val i = Intent(this, EventActivity::class.java)
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

    override fun mascotaOnClick(mascota: Mascota) {

    }

    companion object {
        const val USER_ID = "EstablecimientoActivity.USER_ID"
        const val API_TOKEN = "EstablecimientoActivity.api_token"
        const val EST_ID = "EstablecimientoActivity.est_ID"
        const val EST_ICONO = "EstablecimientoActivity.est_icono"
        const val EST_NOMBRE = "EstablecimientoActivity.est_nombre"
    }


    private var user_id: String? = null
    private var api_token: String? = null
    private var est_id: String? = null
    private var est_icono: String? = null
    private var est_nombre: String? = null

    private val eventos: ArrayList<Cita> = ArrayList()
    private var adapterE: EventosAdapter? = null
    private var adapter: MascotasAdapter? = null
    private val mascotas: ArrayList<Mascota> = ArrayList()
    private var loadTask: LoadTask? = null
    private var loadTaskE: LoadTaskE? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_establecimiento)



        user_id = intent.getStringExtra(USER_ID)
        api_token = intent.getStringExtra(API_TOKEN)
        est_id = intent.getStringExtra(EST_ID)
        est_icono = intent.getStringExtra(EST_ICONO)
        est_nombre = intent.getStringExtra(EST_NOMBRE)






        ae_id.text = "ID: $est_id"
        ae_nombre.text = "$est_nombre"




        Picasso.with(this)
                .load(Constants.WEB_URL + est_icono)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(ae_icono)


        adapter = MascotasAdapter(context = this, mascotas = mascotas)
        adapterE = EventosAdapter(context = this, eventos = eventos)
        adapterE?.onEventsClickListener=this
        ae_mascotas.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        ae_eventos.layoutManager = LinearLayoutManager(this)
        adapter?.mascotasOnClickListener = this
        ae_mascotas.adapter = adapter
        ae_eventos.adapter = adapterE


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


        ae_back.setOnClickListener {
            finish()
        }

        ae_agendar_cita.setOnClickListener {

            val tmp = arrayOfNulls<String>(mascotas.size + 1);

            for (i in mascotas.indices) {
                tmp[i] = mascotas[i].nombre
            }
            tmp[mascotas.size] = "ninguno"


            val builder = AlertDialog.Builder(this)
            builder.setTitle("seleccione una de sus mascotas")
            builder.setItems(tmp, DialogInterface.OnClickListener { dialogInterface, op ->


                val mIntent = Intent(this, AgendarCitaActivity::class.java)
                mIntent.putExtra(AgendarCitaActivity.USER_ID, user_id)
                mIntent.putExtra(AgendarCitaActivity.API_TOKEN, api_token)
                mIntent.putExtra(AgendarCitaActivity.EST_NAME, est_nombre)
                mIntent.putExtra(AgendarCitaActivity.EST_ID, est_id)



            }).create().show()

        }

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
                    ae_eventos.visibility = View.VISIBLE
                    ae_cardView.visibility = View.GONE
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
