package com.darwindeveloper.MyPetsApp.fragments

import android.content.Intent
import android.support.v4.app.Fragment
import com.darwindeveloper.MyPetsApp.api.modelos.Establecimiento
import com.google.firebase.messaging.FirebaseMessaging
import com.darwindeveloper.MyPetsApp.api.WebApiClient
import android.text.method.TextKeyListener.clear
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.darwindeveloper.MyPetsApp.EstablecimientoActivity
import com.darwindeveloper.MyPetsApp.R
import com.darwindeveloper.MyPetsApp.adapters.EstablecimientosAdapter
import com.darwindeveloper.MyPetsApp.api.WebService
import kotlinx.android.synthetic.main.fragment_est.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by DARWIN MOROCHO on 24/8/2017.
 */
class EstablecimientosFragment : Fragment(), EstablecimientosAdapter.OnClickEstListener {
    override fun onEstClick(est: Establecimiento) {

        val mIntent = Intent(context, EstablecimientoActivity::class.java)
        mIntent.putExtra(EstablecimientoActivity.USER_ID, user_id)
        mIntent.putExtra(EstablecimientoActivity.API_TOKEN, api_token)
        mIntent.putExtra(EstablecimientoActivity.EST_ID, est.establecimiento_id)
        mIntent.putExtra(EstablecimientoActivity.EST_NOMBRE, est.nombre_establecimiento)
        mIntent.putExtra(EstablecimientoActivity.EST_ICONO, est.icono)

        startActivity(mIntent)

    }

    private var rootView: View? = null
    private var user_id: String? = null
    private var api_token: String? = null

    private var mLoadDatatask: LoadData? = null


    private val establecimientos: ArrayList<Establecimiento> = ArrayList()
    private var adapter: EstablecimientosAdapter? = null

    companion object {
        const val USER_ID = "estFr.user_id"
        const val API_TOKEN = "estFr.api_token"

        fun newInstance(args: Bundle): EstablecimientosFragment {
            val fr = EstablecimientosFragment()
            fr.arguments = args
            return fr
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user_id = arguments[USER_ID] as String
        api_token = arguments[API_TOKEN] as String
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater?.inflate(R.layout.fragment_est, container, false)


        adapter = EstablecimientosAdapter(context, establecimientos)
        adapter?.onClickEstListener = this
        rootView!!.fes_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rootView!!.fes_list.adapter = adapter


        if (mLoadDatatask != null)
            mLoadDatatask = null
        mLoadDatatask = LoadData()
        mLoadDatatask?.execute()

        return rootView

    }


    private inner class LoadData : AsyncTask<Void, Void, Void>(), Callback<List<Establecimiento>> {

        override fun onPreExecute() {

        }

        override fun doInBackground(vararg voids: Void): Void? {

            val webService = WebApiClient.client!!.create(WebService::class.java)
            val call = webService.establecimientos(user_id)
            call.enqueue(this)

            return null
        }

        override fun onResponse(call: Call<List<Establecimiento>>, response: Response<List<Establecimiento>>) {


            val tmp = response.body()

            if (tmp != null) {

                for (i in tmp.indices) {
                    establecimientos.add(tmp.get(i))
                    adapter?.notifyItemInserted(establecimientos.size)
                    FirebaseMessaging.getInstance().subscribeToTopic("platform-" + tmp.get(i).establecimiento_id!!)
                }

            }
        }

        override fun onFailure(call: Call<List<Establecimiento>>, t: Throwable) {
            mLoadDatatask = null

        }

        override fun onCancelled() {
            mLoadDatatask = null
        }
    }

}