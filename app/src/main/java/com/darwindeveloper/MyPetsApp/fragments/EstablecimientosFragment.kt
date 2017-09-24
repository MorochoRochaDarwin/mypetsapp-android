package com.darwindeveloper.MyPetsApp.fragments

import android.content.DialogInterface
import android.content.Intent
import android.support.v4.app.Fragment
import com.darwindeveloper.MyPetsApp.api.modelos.Establecimiento
import com.google.firebase.messaging.FirebaseMessaging
import com.darwindeveloper.MyPetsApp.api.WebApiClient
import android.text.method.TextKeyListener.clear
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.darwindeveloper.MyPetsApp.EstablecimientoActivity
import com.darwindeveloper.MyPetsApp.R
import com.darwindeveloper.MyPetsApp.adapters.EstablecimientosAdapter
import com.darwindeveloper.MyPetsApp.api.WebService
import com.darwindeveloper.MyPetsApp.sqlite.DBManager
import kotlinx.android.synthetic.main.fragment_est.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.LinearLayout
import com.darwindeveloper.MyPetsApp.MainActivity
import android.widget.EditText
import android.widget.Toast


/**
 * Created by DARWIN MOROCHO on 24/8/2017.
 */
class EstablecimientosFragment : Fragment(), EstablecimientosAdapter.OnClickEstListener {
    override fun onEstClick(est: Establecimiento) {

        if (est.establecimiento_id != "-1") {
            val mIntent = Intent(context, EstablecimientoActivity::class.java)
            mIntent.putExtra(EstablecimientoActivity.USER_ID, user_id)
            mIntent.putExtra(EstablecimientoActivity.API_TOKEN, api_token)
            mIntent.putExtra(EstablecimientoActivity.EST_ID, est.establecimiento_id)
            mIntent.putExtra(EstablecimientoActivity.EST_NOMBRE, est.nombre_establecimiento)
            mIntent.putExtra(EstablecimientoActivity.EST_ICONO, est.icono)

            startActivity(mIntent)
        } else {

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Agregar clinica o establecimiento")
            builder.setIcon(R.drawable.add)
            val input = EditText(context)
            input.hint = "ID clinica o establecimiento"
            input.inputType = InputType.TYPE_CLASS_NUMBER
            val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
            input.layoutParams = lp

            builder.setView(input)

            builder.setPositiveButton("CANCELAR", DialogInterface.OnClickListener { dialogInterface, i ->
                //Toast.makeText(context, input.text.toString(), Toast.LENGTH_SHORT).show()
                dialogInterface.dismiss()
            })

            builder.setNegativeButton("AGREGAR", DialogInterface.OnClickListener { dialogInterface, i ->
                Toast.makeText(context, "Por favor espere...", Toast.LENGTH_SHORT).show()

                val ws = WebApiClient.client!!.create(WebService::class.java)
                val mcall = ws.establecimiento(user_id, input.text.toString())

                mcall.enqueue(object : Callback<Establecimiento> {
                    override fun onFailure(call: Call<Establecimiento>?, t: Throwable?) {

                        Toast.makeText(context, "ERROR INTENTE NUEVAMENTE", Toast.LENGTH_SHORT).show()
                        dialogInterface.dismiss()


                    }

                    override fun onResponse(call: Call<Establecimiento>?, response: Response<Establecimiento>?) {

                        if (response != null) {
                            if (response.body() != null) {
                                val id = dbm!!.saveEst(response.body())
                                if (id != -1) {
                                    Toast.makeText(context, "AGREGADO CON EXITO", Toast.LENGTH_SHORT).show()

                                    establecimientos.clear()
                                    adapter!!.notifyDataSetChanged()
                                    if (mLoadDatatask != null)
                                        mLoadDatatask = null
                                    mLoadDatatask = LoadData()
                                    mLoadDatatask?.execute()

                                    dialogInterface.dismiss()

                                } else {
                                    Toast.makeText(context, "ERROR INTENTE NUEVAMENTE", Toast.LENGTH_SHORT).show()
                                    dialogInterface.dismiss()
                                }
                            } else {
                                Toast.makeText(context, "ERROR INTENTE NUEVAMENTE", Toast.LENGTH_SHORT).show()
                                dialogInterface.dismiss()
                            }
                        } else {
                            Toast.makeText(context, "ERROR INTENTE NUEVAMENTE", Toast.LENGTH_SHORT).show()
                            dialogInterface.dismiss()
                        }



                    }

                });


            })

            builder.create().show()


        }

    }

    private var rootView: View? = null
    private var user_id: String? = null
    private var api_token: String? = null

    private var mLoadDatatask: LoadData? = null


    private val establecimientos: ArrayList<Establecimiento> = ArrayList()
    private var adapter: EstablecimientosAdapter? = null
    private var dbm: DBManager? = null


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

        dbm = DBManager(context)


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


    private inner class LoadData : AsyncTask<Void, Void, Void>() {

        override fun onPreExecute() {

        }

        override fun doInBackground(vararg voids: Void): Void? {


            establecimientos.addAll(dbm!!.getEst())


            return null
        }

        override fun onPostExecute(result: Void?) {
            if (establecimientos.size > 0) {
                for (i in establecimientos.indices) {
                    FirebaseMessaging.getInstance().subscribeToTopic("platform-" + establecimientos[i].establecimiento_id!!)
                }
            }

            val tmp = Establecimiento()
            tmp.establecimiento_id = "-1"
            tmp.nombre_establecimiento = "Agregar clinica o Establecimiento"

            establecimientos.add(tmp)


            adapter!!.notifyItemRangeInserted(0, establecimientos.size)
            adapter!!.notifyDataSetChanged()


        }


        override fun onCancelled() {
            mLoadDatatask = null
        }


        /*
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
         */


    }

}