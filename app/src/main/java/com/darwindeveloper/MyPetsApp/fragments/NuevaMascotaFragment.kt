package com.darwindeveloper.MyPetsApp.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.darwindeveloper.MyPetsApp.R
import com.darwindeveloper.MyPetsApp.api.WebApiClient
import com.darwindeveloper.MyPetsApp.api.WebService
import com.darwindeveloper.MyPetsApp.api.modelos.Especies
import com.darwindeveloper.MyPetsApp.api.responses.DefaultResponse
import kotlinx.android.synthetic.main.fragment_nueva_mascota.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by DARWIN MOROCHO on 7/10/2017.
 */
class NuevaMascotaFragment : Fragment() {


    private var user_id: String? = null
    private var est_id: String? = null
    private var api_token: String? = null

    companion object {
        const val USER_ID = "frnp.userid"
        const val EST_ID = "frnp.est_id"
        const val API_TOKEN = "frnp.apitoken"

        fun newInstance(args: Bundle): NuevaMascotaFragment {
            val fr = NuevaMascotaFragment()
            fr.arguments = args
            return fr
        }

    }

    private var rootView: View? = null

    private val tamanios = arrayOf("grande", "mediano", "pequeño", "miniatura")
    private val sexos = arrayOf("macho", "hembra")
    private val esterilizados = arrayOf("NO", "SI")
    private val microchips = arrayOf("NO", "SI")
    private val perros = ArrayList<String>()
    private val gatos = ArrayList<String>()
    private val especies = arrayOf("canino", "felino", "conejo", "canario", "hamster", "otros")
    private val razas = ArrayList<String>()

    private var tamanio = "grande"
    private var sexo = "macho"
    private var esterilizado = "NO"
    private var microchip = "NO"
    private var especie = "canino"
    private var raza = "Mestizo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user_id = arguments.getString(USER_ID)
        est_id = arguments.getString(EST_ID)
        api_token = arguments.getString(API_TOKEN)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater!!.inflate(R.layout.fragment_nueva_mascota, container, false)
        rootView!!.fnm_datePicker.maxDate = Date().time

        val adapterSpinnertamanios = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, tamanios)
        adapterSpinnertamanios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rootView!!.fnm_tamanio.adapter = adapterSpinnertamanios

        val adapterSpinnerSexos = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, sexos)
        adapterSpinnerSexos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rootView!!.fnm_sexo.adapter = adapterSpinnerSexos


        val adapterSpinnerMicrochip = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, microchips)
        adapterSpinnerMicrochip.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rootView!!.fnm_microchip.adapter = adapterSpinnerMicrochip


        val adapterSpinnerEsterilizado = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, esterilizados)
        adapterSpinnerEsterilizado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rootView!!.fnm_esterilizado.adapter = adapterSpinnerEsterilizado


        val adapterEspecies = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, especies)
        adapterEspecies.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rootView!!.fnm_especie.adapter = adapterEspecies


        val adapterRazas = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, razas)
        adapterEspecies.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rootView!!.fnm_raza.adapter = adapterRazas



        rootView!!.fnm_tamanio.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                tamanio = tamanios[position]
            }

        })


        rootView!!.fnm_sexo.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                sexo = sexos[position]
            }

        })

        rootView!!.fnm_esterilizado.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                esterilizado = esterilizados[position]
            }

        })


        rootView!!.fnm_microchip.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                microchip = microchips[position]
            }

        })

        rootView!!.fnm_raza.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                raza = razas[position]
            }

        })


        rootView!!.fnm_especie.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                especie = especies[position]

                if (position == 0) {
                    rootView!!.fnm_eraza.visibility = View.GONE
                    rootView!!.fnm_raza.visibility = View.VISIBLE
                    razas.clear()
                    razas.addAll(perros)
                    adapterRazas.notifyDataSetChanged()
                } else if (position == 1) {
                    rootView!!.fnm_eraza.visibility = View.GONE
                    rootView!!.fnm_raza.visibility = View.VISIBLE
                    razas.clear()
                    razas.addAll(gatos)
                    adapterRazas.notifyDataSetChanged()
                } else {
                    rootView!!.fnm_eraza.visibility = View.VISIBLE
                    rootView!!.fnm_raza.visibility = View.GONE
                }


            }

        })


        val ws = WebApiClient.client!!.create(WebService::class.java)
        val mcall = ws.especies()
        mcall.enqueue(object : Callback<Especies> {
            override fun onFailure(call: Call<Especies>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<Especies>?, response: Response<Especies>?) {
                val res = response?.body()
                if (res != null) {
                    perros.addAll(res.canino!!)
                    gatos.addAll(res.felino!!)

                    razas.addAll(perros)
                    adapterRazas.notifyDataSetChanged()

                }
            }

        })



        rootView!!.fnm_save.setOnClickListener {
            save()
        }


        return rootView
    }

    var pd: ProgressDialog? = null
    fun save() {


        val nombre = rootView!!.fnm_nombre.text.toString()
        val color = rootView!!.fnm_color.text.toString()
        val alimentacion = rootView!!.fnm_alimentacion.text.toString()
        val obs = rootView!!.fnm_obs.text.toString()


        val nday = rootView!!.fnm_datePicker.dayOfMonth
        val nmonth = rootView!!.fnm_datePicker.month
        val nyear = rootView!!.fnm_datePicker.year


        val nacimiento = "$nyear-${monthToString(nmonth)}-$nday"

        if (nombre.isEmpty()) {
            rootView!!.fnm_nombre.setError("Campo obligatorio")
            rootView!!.fnm_nombre.requestFocus()
            return
        }
        if (color.isEmpty()) {
            rootView!!.fnm_color.setError("Campo obligatorio")
            rootView!!.fnm_color.requestFocus()
            return
        }

        if(especie !="canino" && especie!="felino"){
            raza=rootView!!.fnm_eraza.text.toString()
            if (raza.isEmpty()) {
                rootView!!.fnm_eraza.setError("Campo obligatorio")
                rootView!!.fnm_eraza.requestFocus()
                return
            }
        }


        pd = ProgressDialog.show(context, "Procesando petición", "por favor espere", false)
        pd!!.setCancelable(true)
        pd!!.show()

        val ws = WebApiClient.client!!.create(WebService::class.java)
        val mcall = ws.nueva_mascota(user_id, est_id, nombre, raza, sexo, nacimiento, color, especie, microchip, esterilizado, tamanio, alimentacion, obs)

        mcall.enqueue(object : Callback<DefaultResponse> {
            override fun onFailure(call: Call<DefaultResponse>?, t: Throwable?) {
                pd!!.dismiss()
            }

            override fun onResponse(call: Call<DefaultResponse>?, response: Response<DefaultResponse>?) {
                pd!!.dismiss()

                val res = response?.body()
                if (res != null) {
                    if (res.status == 200) {
                        rootView!!.fnm_text_ok.visibility = View.VISIBLE
                        rootView!!.fnma_form.visibility = View.GONE
                    }
                }
            }

        })


    }

    fun monthToString(month: Int): String {
        if (month >= 0 && month <= 8) {
            return "0${month + 1}";
        } else {
            return "${month + 1}";
        }
    }

}