package com.darwindeveloper.MyPetsApp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.darwindeveloper.MyPetsApp.api.Constants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_event.*

class EventActivity : AppCompatActivity() {


    companion object {
        const val CITA_ID = "eventactivity.cita_id"
        const val TITULO = "eventactivity.titulo"
        const val FECHA = "eventactivity.fecha"
        const val CREADA = "eventactivity.creada"
        const val HORA = "eventactivity.HORA"
        const val DESCR = "eventactivity.descr"
        const val EST_ID = "eventactivity.est_id"
        const val EST = "eventactivity.est"
        const val MASCOTA_ID = "eventactivity.mascota_id"
        const val MASCOTA_NOMBRE = "eventactivity.mascota_nombre"
        const val MASCOTA_FOTO = "eventactivity.mascota_foro"
    }


    var cita_id: String? = null
    var titulo: String? = null
    var creada: String? = null
    var fecha: String? = null
    var hora: String? = null
    var descr: String? = null
    var est_id: String? = null
    var est: String? = null
    var mascota_id: String? = null
    var mascota_nombre: String? = null
    var mascota_foto: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)


        cita_id = intent.getStringExtra(CITA_ID)
        titulo = intent.getStringExtra(TITULO)
        fecha = intent.getStringExtra(FECHA)
        creada = intent.getStringExtra(CREADA)
        hora = intent.getStringExtra(HORA)
        descr = intent.getStringExtra(DESCR)
        est_id = intent.getStringExtra(EST_ID)
        est = intent.getStringExtra(EST)
        mascota_id = intent.getStringExtra(MASCOTA_ID)
        mascota_nombre = intent.getStringExtra(MASCOTA_NOMBRE)
        mascota_foto = intent.getStringExtra(MASCOTA_FOTO)


        aev_motivo.text = titulo
        aev_fecha.text = " $fecha $hora"
        aev_cita_id.text = "Cita # $cita_id"
        aev_creada.text = "Fecha de creación: $creada"
        aev_est.text = "Clinica/Establecimiento: $est"


        if (mascota_foto != null) {
            Picasso.with(this)
                    .load(Constants.WEB_URL + mascota_foto)
                    .into(aev_foto)
        }

        if (mascota_id != null) {
            aev_mascota_nombre.text = mascota_nombre
        } else {
            aev_mascota_nombre.visibility = View.GONE
        }

        if (descr != null) {
            aev_descr.text = descr
        } else {
            aev_descrt.visibility = View.GONE
            aev_descr  .visibility = View.GONE
        }


    }
}
