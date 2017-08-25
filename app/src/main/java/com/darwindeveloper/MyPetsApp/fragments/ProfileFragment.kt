package com.darwindeveloper.MyPetsApp.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.darwindeveloper.MyPetsApp.R
import com.darwindeveloper.MyPetsApp.api.Constants
import com.darwindeveloper.MyPetsApp.api.WebApiClient
import com.darwindeveloper.MyPetsApp.api.WebService
import com.darwindeveloper.MyPetsApp.api.modelos.Provincia
import com.darwindeveloper.MyPetsApp.api.responses.ClientResponse
import kotlinx.android.synthetic.main.fragment_profile.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.darwindeveloper.MyPetsApp.MainActivity
import android.content.Intent
import com.darwindeveloper.MyPetsApp.hover.MyHoverMenuService


/**
 * Created by DARWIN MOROCHO on 23/8/2017.
 */
class ProfileFragment : Fragment() {

    private var rootView: View? = null

    private var loadData: LoadData? = null
    private var loadP: LoadP? = null

    private var user_id: String? = null
    private var api_token: String? = null


    private val provincias = ArrayList<Provincia>()


    companion object {
        const val USER_ID = "ProfileFragment.user_ud"

        fun newInstance(args: Bundle): ProfileFragment {
            val fr = ProfileFragment()
            fr.arguments = args
            return fr
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user_id = arguments.getString(USER_ID, null)

        api_token = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.USER_API_TOKEN, null)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater?.inflate(R.layout.fragment_profile, container, false) as View

        if (user_id != null) {

            if (loadP != null)
                loadP = null

            loadP = LoadP()
            loadP?.execute()

        }



        rootView!!.fp_fab.setOnClickListener {
            val startHoverIntent = Intent(context, MyHoverMenuService::class.java)
            activity.startService(startHoverIntent)
        }
        return rootView

    }


    private inner class LoadData : AsyncTask<Void, Void, Void>(), Callback<ClientResponse> {
        override fun onResponse(call: Call<ClientResponse>?, response: Response<ClientResponse>?) {
            if (response?.body() != null) {
                val mResponse = response.body();

                if (mResponse.status == 200) {
                    rootView!!.fp_nombres.text = "Nombres: ${mResponse.client!!.nombres}"
                    rootView!!.fp_apellidos.text = "Apellidos: ${mResponse.client.apellidos}"
                    rootView!!.fp_email.text = "Email: ${mResponse.client.email}"
                    rootView!!.fp_cedula.text = "Cedula/Pasaporte: ${mResponse.client.cedula}"
                    rootView!!.fp_nacimiento.text = "Fecha nacimiento: ${mResponse.client.nacimiento}"
                    rootView!!.fp_pasis_nacimiento.text = "Pais nacimiento: ${mResponse.client.nacionalidad}"
                    rootView!!.fp_celular.text = "Telefono movil: ${mResponse.client.celular}"
                    rootView!!.fp_telefono.text = "Telefono fijo: ${mResponse.client.telefono}"
                    rootView!!.fp_sexo.text = "Sexo: ${mResponse.client.sexo}"
                    rootView!!.fp_estudio.text = "Nivel estudio: ${mResponse.client.estudio}"
                    rootView!!.fp_ocupacion.text = "Profesión: ${mResponse.client.profesion}"
                    rootView!!.fp_direccion.text = "Dirección: ${mResponse.client.direccion}"


                    for (prov in provincias) {
                        if (mResponse.client.provincia == prov.provincia_id) {
                            rootView!!.fp_provincia.text = "Provincia: ${prov.provincia_nombre}"

                            for (ciudad in prov.ciudades!!) {
                                if (mResponse.client.ciudad == ciudad.ciudad_id) {
                                    rootView!!.fp_ciudad.text = "Ciudad: ${ciudad.ciudad_nombre}"

                                    for (sector in ciudad.sectores!!) {
                                        if (mResponse.client.sector == sector.sector_id) {
                                            rootView!!.fp_sector.text = "Sector: ${sector.sector_nombre}"
                                            break
                                        }
                                    }
                                    break
                                }
                            }
                            break
                        }
                    }


                } else {
                    Toast.makeText(context, mResponse.msg, Toast.LENGTH_LONG).show()
                }

            } else {
                Toast.makeText(context, "null", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<ClientResponse>?, t: Throwable?) {
            Log.i("errorresponse", t?.message)
            loadData = null

        }

        override fun onPreExecute() {

        }

        override fun doInBackground(vararg p0: Void?): Void? {

            if (user_id != null && api_token != null) {
                val webService = WebApiClient.client!!.create(WebService::class.java)
                val call = webService.cliente(user_id, api_token)
                call.enqueue(this)
            } else {
                Log.i("errorresponse", "datos nulos")
            }

            return null
        }

        override fun onCancelled() {
            loadData = null
        }

    }


    private inner class LoadP : AsyncTask<Void, Void, Void>(), Callback<MutableList<Provincia>> {
        override fun onFailure(call: Call<MutableList<Provincia>>?, t: Throwable?) {
            Toast.makeText(context, t?.message, Toast.LENGTH_SHORT).show()
            loadP = null
        }

        override fun onResponse(call: Call<MutableList<Provincia>>?, response: Response<MutableList<Provincia>>?) {
            val tmpA = response?.body()

            if (tmpA != null) {
                provincias.addAll(tmpA)

                if (loadData != null)
                    loadData = null
                loadData = LoadData()
                loadData?.execute()
            } else {
                Toast.makeText(context, "null p", Toast.LENGTH_SHORT).show()
            }

        }


        override fun onPreExecute() {
            Toast.makeText(context, "Obteniendo datos...", Toast.LENGTH_SHORT).show()
        }

        override fun doInBackground(vararg p0: Void?): Void? {

            val ws = WebApiClient.client!!.create(WebService::class.java)
            val call = ws.provincias()

            call.enqueue(this)

            return null
        }

        override fun onCancelled() {
            loadP = null
        }

    }
}