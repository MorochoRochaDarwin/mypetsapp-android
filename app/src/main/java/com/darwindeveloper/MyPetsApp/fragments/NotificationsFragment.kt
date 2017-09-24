package com.darwindeveloper.MyPetsApp.fragments

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.darwindeveloper.MyPetsApp.NotificacionActivity
import com.darwindeveloper.MyPetsApp.R
import com.darwindeveloper.MyPetsApp.adapters.NotificacionesAdapter
import com.darwindeveloper.MyPetsApp.sqlite.DBManager
import com.darwindeveloper.MyPetsApp.sqlite.Notification
import kotlinx.android.synthetic.main.fragment_notifications.view.*

/**
 * Created by DARWIN MOROCHO on 4/9/2017.
 */
class NotificationsFragment : Fragment(), NotificacionesAdapter.OnNotificacionesListener {


    companion object {
        const val TIPO = "nf.tipo"

        fun newInstance(args: Bundle): NotificationsFragment {
            val f = NotificationsFragment()
            f.arguments = args
            return f
        }
    }

    var tipo: String? = null

    override fun verNotificacion(notificacion: Notification) {

        dbm!!.visto(notificacion.id)

        val mIntent = Intent(context, NotificacionActivity::class.java)
        mIntent.putExtra("guardado", true)
        mIntent.putExtra("establecimiento_id", notificacion.remitente_id)
        mIntent.putExtra("nombre_establecimiento", notificacion.remitente)
        mIntent.putExtra("publicado", notificacion.created_at)
        mIntent.putExtra("html", notificacion.html)
        mIntent.putExtra("titulo", notificacion.titulo)

        startActivity(mIntent)

    }

    override fun eliminarNotificacion(pos: Int, notificacion: Notification) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirmación requerida")
        builder.setMessage("Eliminar esta notificación permanentemente?")
        builder.setPositiveButton("SI", DialogInterface.OnClickListener { dialogInterface, i ->
            val num = dbm!!.delete("${notificacion.id}")
            if (num > 0) {
                notificaciones.removeAt(pos)
                adapter?.notifyItemRemoved(pos)
                Toast.makeText(context, "Eliminado con exito", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error intent nuevamente", Toast.LENGTH_SHORT).show()
            }
        })

        builder.setNegativeButton("NO", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        })
        builder.create().show()


    }


    private var rootView: View? = null
    private var adapter: NotificacionesAdapter? = null
    private val notificaciones = ArrayList<Notification>()

    private var loadtask: LoadData? = null
    private var dbm: DBManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tipo = arguments.getString(TIPO, "medico")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater?.inflate(R.layout.fragment_notifications, container, false)
        dbm = DBManager(context)

        adapter = NotificacionesAdapter(context, notificaciones)
        adapter?.onNotificacionesListener = this
        rootView!!.fn_list.layoutManager = LinearLayoutManager(context)
        rootView!!.fn_list.adapter = adapter


        if (loadtask != null)
            loadtask = null
        loadtask = LoadData()
        loadtask?.execute()


        return rootView
    }


    private inner class LoadData : AsyncTask<Void, Void, Void>() {

        var progressDialog: ProgressDialog? = null

        override fun onPreExecute() {
            progressDialog = ProgressDialog.show(context, "Cargando", "por favor espere")
            progressDialog?.setCancelable(true)
            progressDialog?.show()
        }

        override fun doInBackground(vararg p0: Void?): Void? {


            if (tipo != "none") {
                val n = dbm!!.getQhere("tipo='${tipo}'")
                notificaciones.addAll(n)
            } else {
                val n = dbm!!.getQhere("tipo!='Veterinario' and tipo!='Publicidad' and tipo!='Descuentos' and tipo!='Ultimas noticias'")
                notificaciones.addAll(n)
            }






            return null
        }


        override fun onCancelled() {
            progressDialog?.dismiss()
            loadtask = null
        }

        override fun onPostExecute(result: Void?) {
            progressDialog?.dismiss()

            if (notificaciones.size > 0) {
                rootView!!.fn_cardView.visibility = View.GONE
                rootView!!.fn_list.visibility = View.VISIBLE
                adapter?.notifyItemRangeInserted(0, notificaciones.size)
                adapter?.notifyDataSetChanged()
            }
        }

    }


}