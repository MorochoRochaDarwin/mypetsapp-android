package com.darwindeveloper.MyPetsApp.bottom_sheets

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.View
import android.widget.Toast
import com.darwindeveloper.MyPetsApp.R
import com.darwindeveloper.MyPetsApp.api.WebApiClient
import com.darwindeveloper.MyPetsApp.api.WebService
import com.darwindeveloper.MyPetsApp.api.modelos.Mascota
import kotlinx.android.synthetic.main.bottom_sheet_mascota.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by DARWIN MOROCHO on 24/8/2017.
 */
class BSmascota : BottomSheetDialogFragment() {

    private var mascota_id: String? = null
    private var rootView: View? = null

    private var dataPetTask: DataPet? = null

    companion object {
        const val MASCOTA_ID = "BSmascota.mascota_id"

        fun newInstance(args: Bundle): BSmascota {
            val bsm = BSmascota()
            bsm.arguments = args
            return bsm
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mascota_id = arguments[MASCOTA_ID] as String
    }


    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)
        rootView = View.inflate(context, R.layout.bottom_sheet_mascota, null)
        dialog?.setContentView(rootView)

        if (dataPetTask != null)
            dataPetTask = null
        dataPetTask = DataPet()
        dataPetTask?.execute()


    }

    internal inner class DataPet : AsyncTask<Void, Void, Void>(), Callback<Mascota> {
        override fun onResponse(call: Call<Mascota>?, response: Response<Mascota>?) {

            val pet = response?.body()
            if (pet != null) {
                rootView!!.bsm_id.text = "MASCOTA ID: " + pet.mascota_id
                rootView!!.bsm_nombre.text = "Nombre: " + pet.nombre
                rootView!!.bsm_especie.text = "Especie: " + pet.especie
                rootView!!.bsm_raza.text = "Raza: " + pet.raza
                rootView!!.bsm_color.text = "Color: " + pet.color
                rootView!!.bsm_sexo.text = "Sexo: " + pet.sexo
                rootView!!.bsm_tamanio.text = "Tamaño: " + pet.tamanio
                rootView!!.bsm_nacimiento.text = "Fecha nacimiento: " + pet.fecha_nacimiento
                rootView!!.bsm_estado.text = "Estado: " + pet.estado
                rootView!!.bsm_muerte.visibility = View.GONE

                rootView!!.bsm_esterilizado.text = "Esterilizado: " + pet.esterilizado
                rootView!!.bsm_alimentacion.text = "Esterilizado: " + pet.alimentacion
                rootView!!.bsm_obs.text = pet.observaciones
            }
        }

        override fun onFailure(call: Call<Mascota>?, t: Throwable?) {
            Toast.makeText(context, "Error: no se pudo cargar los datos de la mascota", Toast.LENGTH_SHORT).show()
        }

        override fun doInBackground(vararg p0: Void?): Void? {

            val ws = WebApiClient.client!!.create(WebService::class.java)
            val call = ws.mascota(mascota_id)
            call.enqueue(this)

            return null
        }


        override fun onCancelled() {
            dataPetTask = null
        }

    }


}