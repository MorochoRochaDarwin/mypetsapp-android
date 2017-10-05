package com.darwindeveloper.MyPetsApp

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.darwindeveloper.MyPetsApp.adapters.EstablecimientosAdapter
import com.darwindeveloper.MyPetsApp.adapters.EventosMascotaAdapter
import com.darwindeveloper.MyPetsApp.api.Constants
import com.darwindeveloper.MyPetsApp.api.WebApiClient
import com.darwindeveloper.MyPetsApp.api.WebService
import com.darwindeveloper.MyPetsApp.api.modelos.Cita
import com.darwindeveloper.MyPetsApp.api.modelos.Establecimiento
import com.darwindeveloper.MyPetsApp.api.modelos.Mascota
import com.darwindeveloper.MyPetsApp.api.responses.DefaultResponse
import com.darwindeveloper.MyPetsApp.api.responses.UploadResponse
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_mascota.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MascotaActivity : AppCompatActivity(), EstablecimientosAdapter.OnClickEstListener, EventosMascotaAdapter.OnEventsPetClickListener {
    override fun onEventPetClick(cita: Cita) {
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

    override fun onEstClick(est: Establecimiento) {

        val mIntent = Intent(this, EstablecimientoActivity::class.java)
        mIntent.putExtra(EstablecimientoActivity.USER_ID, user_id)
        mIntent.putExtra(EstablecimientoActivity.API_TOKEN, api_token)
        mIntent.putExtra(EstablecimientoActivity.EST_ID, est.establecimiento_id)
        mIntent.putExtra(EstablecimientoActivity.EST_NOMBRE, est.nombre_establecimiento)
        mIntent.putExtra(EstablecimientoActivity.EST_ICONO, est.icono)

        startActivity(mIntent)
    }


    private var mascota_id: String? = null
    private var user_id: String? = null
    private var est_id: String? = null
    private var api_token: String? = null


    private var vivo = true

    private var dataPetTask: DataPet? = null
    private var mLoadDatatask: LoadData? = null
    private var mLoadcDatatask: LoadcTask? = null

    private val establecimientos: ArrayList<Establecimiento> = ArrayList()
    private val eventos: ArrayList<Cita> = ArrayList()
    private var adapterEstablecimientos: EstablecimientosAdapter? = null
    private var adapterEvents: EventosMascotaAdapter? = null


    private val tamanios = arrayOf("grande", "mediano", "pequeño", "miniatura")
    private val sexos = arrayOf("macho", "hembra")
    private val estados = arrayOf("vivo", "muerto")
    private val esterilizados = arrayOf("NO", "SI")
    private val microchips = arrayOf("NO", "SI")

    private var tamanio = "grande"
    private var sexo = "macho"
    private var esterilizado = "NO"
    private var microchip = "NO"

    companion object {
        const val MASCOTA_ID = "Amascota.mascota_id"
        const val USER_ID = "Amascota.user_id"
        const val API_TOKEN = "Amascota.api_token"
        const val EST_ID = "Amascota.estid"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mascota)

        mascota_id = intent.getStringExtra(MASCOTA_ID)
        user_id = intent.getStringExtra(USER_ID)
        api_token = intent.getStringExtra(API_TOKEN)
        est_id = intent.getStringExtra(EST_ID)



       /* adapterEstablecimientos = EstablecimientosAdapter(this@MascotaActivity, establecimientos)
        adapterEstablecimientos?.onClickEstListener = this*/
        adapterEvents = EventosMascotaAdapter(this@MascotaActivity, eventos)
        adapterEvents?.onEventsPetClickListener=this
       // bsm_establecimientos.layoutManager = LinearLayoutManager(this@MascotaActivity, LinearLayoutManager.HORIZONTAL, false)
        bsm_list_eventos.layoutManager = LinearLayoutManager(this@MascotaActivity)
       // bsm_establecimientos.adapter = adapterEstablecimientos
        bsm_list_eventos.adapter = adapterEvents


        val adapterSpinnertamanios = ArrayAdapter<String>(this@MascotaActivity, android.R.layout.simple_spinner_dropdown_item, tamanios)
        adapterSpinnertamanios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bsm_spinner_tamanio.adapter = adapterSpinnertamanios

        val adapterSpinnerSexos = ArrayAdapter<String>(this@MascotaActivity, android.R.layout.simple_spinner_dropdown_item, sexos)
        adapterSpinnerSexos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bsm_spinner_sexo.adapter = adapterSpinnerSexos

        val adapterSpinnerEstado = ArrayAdapter<String>(this@MascotaActivity, android.R.layout.simple_spinner_dropdown_item, estados)
        adapterSpinnerEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bsm_spinner_estado.adapter = adapterSpinnerEstado


        val adapterSpinnerMicrochip = ArrayAdapter<String>(this@MascotaActivity, android.R.layout.simple_spinner_dropdown_item, microchips)
        adapterSpinnerMicrochip.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bsm_spinner_microchip.adapter = adapterSpinnerMicrochip


        val adapterSpinnerEsterilizado = ArrayAdapter<String>(this@MascotaActivity, android.R.layout.simple_spinner_dropdown_item, esterilizados)
        adapterSpinnerEsterilizado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bsm_spinner_esterilizado.adapter = adapterSpinnerEsterilizado


        bsm_spinner_tamanio.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                tamanio = tamanios[position]
            }

        })

        bsm_spinner_estado.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (position == 0) {
                    vivo = true;
                } else {
                    vivo = false;
                }

                if (!vivo) {
                    bsm_input_muerte.visibility = View.VISIBLE
                } else {
                    bsm_input_muerte.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        })


        bsm_spinner_sexo.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                sexo = sexos[p2]
            }

        });

        bsm_spinner_esterilizado.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                esterilizado = esterilizados[p2]
            }

        });

        bsm_spinner_microchip.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                microchip = microchips[p2]
            }

        });

        bsm_fab_edit.setOnClickListener() {
            showEdit()
        }

        bsm_btn_cancel.setOnClickListener() {
            hideEdit()
        }

        bsm_btn_save.setOnClickListener() {
            edit()
        }

        bsm_foto.setOnClickListener {
            CropImage.activity().setAspectRatio(200, 200)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }


        if (dataPetTask != null)
            dataPetTask = null
        dataPetTask = DataPet()
        dataPetTask?.execute()


        /*if (mLoadDatatask != null)
            mLoadDatatask = null
        mLoadDatatask = LoadData()
        mLoadDatatask?.execute()*/


        if (mLoadcDatatask != null)
            mLoadcDatatask = null
        mLoadcDatatask = LoadcTask()
        mLoadcDatatask?.execute()



        am_btn_carnet.setOnClickListener {
            val i=Intent(this,CarnetActivity::class.java)
            i.putExtra(CarnetActivity.USER_ID,user_id)
            i.putExtra(CarnetActivity.MASCOTA_ID,mascota_id)
            i.putExtra(CarnetActivity.API_TOKEN,api_token)
            startActivity(i)
        }

    }


    private fun showEdit() {
        bsm_fab_edit.visibility = View.GONE
        bsm_btns.visibility = View.VISIBLE

        bsm_input_nombre.visibility = View.VISIBLE
        bsm_nombre.visibility = View.GONE
        bsm_input_especie.visibility = View.VISIBLE
        bsm_especie.visibility = View.GONE
        bsm_input_tamanio.visibility = View.VISIBLE
        bsm_tamanio.visibility = View.GONE
        bsm_input_raza.visibility = View.VISIBLE
        bsm_raza.visibility = View.GONE
        bsm_input_nacimiento.visibility = View.VISIBLE
        bsm_nacimiento.visibility = View.GONE

        if (!vivo) {
            bsm_muerte.visibility = View.GONE
            bsm_input_muerte.visibility = View.VISIBLE
        }

        bsm_input_sexo.visibility = View.VISIBLE
        bsm_sexo.visibility = View.GONE
        bsm_input_color.visibility = View.VISIBLE
        bsm_color.visibility = View.GONE
        bsm_input_estado.visibility = View.VISIBLE
        bsm_estado.visibility = View.GONE
        bsm_input_alimentacion.visibility = View.VISIBLE
        bsm_alimentacion.visibility = View.GONE
        bsm_input_microchip.visibility = View.VISIBLE
        bsm_microchip.visibility = View.GONE
        bsm_input_esterilizado.visibility = View.VISIBLE
        bsm_esterilizado.visibility = View.GONE
        bsm_edit_obs.visibility = View.VISIBLE
        bsm_obs.visibility = View.GONE
    }


    private fun hideEdit() {
        bsm_fab_edit.visibility = View.VISIBLE
        bsm_btns.visibility = View.GONE

        bsm_input_nombre.visibility = View.GONE
        bsm_nombre.visibility = View.VISIBLE
        bsm_input_especie.visibility = View.GONE
        bsm_especie.visibility = View.VISIBLE
        bsm_input_tamanio.visibility = View.GONE
        bsm_tamanio.visibility = View.VISIBLE
        bsm_input_raza.visibility = View.GONE
        bsm_raza.visibility = View.VISIBLE
        bsm_input_nacimiento.visibility = View.GONE
        bsm_nacimiento.visibility = View.VISIBLE

        if (!vivo) {
            bsm_muerte.visibility = View.VISIBLE
            bsm_input_muerte.visibility = View.GONE
        }

        bsm_input_sexo.visibility = View.GONE
        bsm_sexo.visibility = View.VISIBLE
        bsm_input_color.visibility = View.GONE
        bsm_color.visibility = View.VISIBLE
        bsm_input_estado.visibility = View.GONE
        bsm_estado.visibility = View.VISIBLE
        bsm_input_alimentacion.visibility = View.GONE
        bsm_alimentacion.visibility = View.VISIBLE
        bsm_input_microchip.visibility = View.GONE
        bsm_microchip.visibility = View.VISIBLE
        bsm_input_esterilizado.visibility = View.GONE
        bsm_esterilizado.visibility = View.VISIBLE
        bsm_edit_obs.visibility = View.GONE
        bsm_obs.visibility = View.VISIBLE
    }


    private fun edit() {


        val nombre = bsm_edit_nombre.text.toString()
        val especie = bsm_edit_especie.text.toString()
        val raza = bsm_edit_raza.text.toString()
        val color = bsm_edit_color.text.toString()
        val alimentacion = bsm_edit_alimentacion.text.toString()
        val obs = bsm_edit_obs.text.toString()


        val sdf = SimpleDateFormat("yyyy-MM-dd")


        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val date = sdf.parse("$currentYear-$currentMonth-$currentDay")

        val nday = bsm_datePicker1.dayOfMonth
        val nmonth = bsm_datePicker1.month
        val nyear = bsm_datePicker1.year

        val ndate = sdf.parse("$nyear-$nmonth-$nday")


        val nacimiento = "$nyear-${monthToString(nmonth)}-$nday"
        var muerte: String? = null

        if (!date.after(ndate) && date != ndate) {
            Toast.makeText(this@MascotaActivity, "La fecha de nacimiento no es valida ", Toast.LENGTH_LONG).show()
            return
        }


        if (!vivo) {
            val mday = bsm_datePicker2.dayOfMonth
            val mmonth = bsm_datePicker2.month
            val myear = bsm_datePicker2.year
            val mdate = sdf.parse("$myear-$mmonth-$mday")
            if (!date.after(mdate) && date != mdate) {
                Toast.makeText(this@MascotaActivity, "La fecha de muerte no es valida", Toast.LENGTH_LONG).show()
                return
            }

            if (!mdate.after(ndate)) {
                Toast.makeText(this@MascotaActivity, "La fecha de nacimiento no puede ser mayor a la fecha de muerte", Toast.LENGTH_LONG).show()
                return
            }

            muerte = "$myear-${monthToString(mmonth)}-$mday" as String
        }

        Toast.makeText(this@MascotaActivity, "Actualizando...", Toast.LENGTH_SHORT).show()

        val estado = if (vivo) "vivo" else "muerto"

        val ws = WebApiClient.client!!.create(WebService::class.java)
        val mcall = ws.actualizar_mascota(mascota_id, user_id, nombre, raza, sexo, nacimiento, muerte, color, especie, microchip, estado, esterilizado, tamanio, alimentacion, obs)

        mcall.enqueue(object : Callback<DefaultResponse> {
            override fun onFailure(call: Call<DefaultResponse>?, t: Throwable?) {
                Toast.makeText(this@MascotaActivity, t?.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<DefaultResponse>?, response: Response<DefaultResponse>?) {
                Toast.makeText(this@MascotaActivity, "Datos actualizados", Toast.LENGTH_SHORT).show()

                bsm_fab_edit.visibility = View.VISIBLE
                bsm_nombre.visibility = View.VISIBLE
                bsm_especie.visibility = View.VISIBLE
                bsm_tamanio.visibility = View.VISIBLE
                bsm_raza.visibility = View.VISIBLE
                bsm_nacimiento.visibility = View.VISIBLE
                bsm_sexo.visibility = View.VISIBLE
                bsm_color.visibility = View.VISIBLE
                bsm_estado.visibility = View.VISIBLE
                bsm_alimentacion.visibility = View.VISIBLE
                bsm_microchip.visibility = View.VISIBLE
                bsm_esterilizado.visibility = View.VISIBLE
                bsm_obs.visibility = View.VISIBLE



                bsm_btns.visibility = View.GONE
                bsm_input_nombre.visibility = View.GONE
                bsm_input_especie.visibility = View.GONE
                bsm_input_tamanio.visibility = View.GONE
                bsm_input_raza.visibility = View.GONE
                bsm_input_nacimiento.visibility = View.GONE
                bsm_input_muerte.visibility = View.GONE
                bsm_input_sexo.visibility = View.GONE
                bsm_input_color.visibility = View.GONE
                bsm_input_estado.visibility = View.GONE
                bsm_input_alimentacion.visibility = View.GONE
                bsm_input_microchip.visibility = View.GONE
                bsm_input_esterilizado.visibility = View.GONE
                bsm_edit_obs.visibility = View.GONE


                if (dataPetTask != null)
                    dataPetTask = null
                dataPetTask = DataPet()
                dataPetTask?.execute()

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


    internal inner class DataPet : AsyncTask<Void, Void, Void>(), Callback<Mascota> {
        override fun onResponse(call: Call<Mascota>?, response: Response<Mascota>?) {

            val pet = response?.body()
            if (pet != null) {
                bsm_id.text = "MASCOTA ID: " + pet.mascota_id
                bsm_nombre.text = "Nombre: " + pet.nombre
                bsm_edit_nombre.setText(pet.nombre)
                bsm_especie.text = "Especie: " + pet.especie
                bsm_edit_especie.setText(pet.especie)
                bsm_raza.text = "Raza: " + pet.raza
                bsm_edit_raza.setText(pet.raza)
                bsm_color.text = "Color: " + pet.color
                bsm_edit_color.setText(pet.color)
                bsm_sexo.text = "Sexo: " + pet.sexo
                bsm_tamanio.text = "Tamaño: " + pet.tamanio
                bsm_nacimiento.text = "Fecha nacimiento: " + pet.fecha_nacimiento

                val arr1 = pet.fecha_nacimiento?.split("-")

                bsm_datePicker1.updateDate(Integer.parseInt(arr1!![0]), Integer.parseInt(arr1[1]) - 1, Integer.parseInt(arr1[2]))


                bsm_estado.text = "Estado: " + pet.estado
                if (pet.fecha_muerte != null) {
                    vivo = false
                    bsm_spinner_estado.setSelection(1)
                    bsm_muerte.text = "Fecha muerte: " + pet.fecha_muerte
                    val arr2 = pet.fecha_muerte.split("-")
                    bsm_datePicker2.updateDate(Integer.parseInt(arr2[0]), Integer.parseInt(arr2[1]) - 1, Integer.parseInt(arr2[2]))

                } else {
                    bsm_muerte?.visibility = View.GONE
                }

                bsm_esterilizado.text = "Esterilizado: " + pet.esterilizado
                bsm_alimentacion.text = "Tipo alimentación: " + pet.alimentacion
                bsm_edit_alimentacion.setText(pet.alimentacion)
                bsm_microchip.text = "Microchip: " + pet.microchip
                bsm_obs.text = pet.observaciones
                bsm_edit_obs.setText(pet.observaciones)

                if (pet.foto != null) {
                    Picasso.with(this@MascotaActivity)
                            .load(Constants.WEB_URL + pet.foto)
                            .into(bsm_foto)
                }
            }
        }

        override fun onFailure(call: Call<Mascota>?, t: Throwable?) {
            Toast.makeText(this@MascotaActivity, "Error: no se pudo cargar los datos de la mascota", Toast.LENGTH_SHORT).show()
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        //cuando se regresa de la actividad cropper
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {// si dodo es ok

                Toast.makeText(this@MascotaActivity, "subiendo imagen...", Toast.LENGTH_SHORT).show()

                val uri = result.uri//obtenemos la uri de la imagen recortada
                val file = File(uri.path)


                val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
                // MultipartBody.Part is used to send also the actual file name
                val body =
                        MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                // add another part within the multipart request
                val mmascota_id = RequestBody.create(
                        MediaType.parse("ext/plain"), mascota_id)


                val muser_id = RequestBody.create(
                        MediaType.parse("ext/plain"), user_id)


                // val user_id = RequestBody.create(MediaType.parse("text/plain"), user_id)


                val ws = WebApiClient.client!!.create(WebService::class.java)
                val mcall = ws.actualizar_foto_mascota(muser_id, mmascota_id, body)

                mcall.enqueue(object : Callback<UploadResponse> {
                    override fun onResponse(call: Call<UploadResponse>?, response: Response<UploadResponse>?) {

                        val res = response?.body();
                        if (res != null) {
                            Toast.makeText(this@MascotaActivity, res.msg, Toast.LENGTH_SHORT).show()

                            if (res.status == 200) {
                                Picasso.with(this@MascotaActivity)
                                        .load(Constants.WEB_URL + res.url)
                                        .into(bsm_foto)
                            }
                        }

                    }

                    override fun onFailure(call: Call<UploadResponse>?, t: Throwable?) {
                        Toast.makeText(this@MascotaActivity, t?.message, Toast.LENGTH_SHORT).show()
                    }

                })


            }
        }

    }


    private inner class LoadData : AsyncTask<Void, Void, Void>(), Callback<List<Establecimiento>> {

        override fun onPreExecute() {

        }

        override fun doInBackground(vararg voids: Void): Void? {

            val webService = WebApiClient.client!!.create(WebService::class.java)
            val call = webService.establecimientos_mascota(mascota_id)
            call.enqueue(this)

            return null
        }

        override fun onResponse(call: Call<List<Establecimiento>>, response: Response<List<Establecimiento>>) {


            val tmp = response.body()

            if (tmp != null) {


                for (i in tmp.indices) {
                    establecimientos.add(tmp.get(i))
                    adapterEstablecimientos?.notifyItemInserted(establecimientos.size)
                    // FirebaseMessaging.getInstance().subscribeToTopic("platform-" + tmp.get(i).establecimiento_id!!)
                }

            } else {
                Toast.makeText(this@MascotaActivity, "null", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<List<Establecimiento>>, t: Throwable) {
            mLoadDatatask = null
            Toast.makeText(this@MascotaActivity, t.message, Toast.LENGTH_SHORT).show()
        }

        override fun onCancelled() {
            mLoadDatatask = null
        }
    }


    internal inner class LoadcTask : AsyncTask<Void, Void, Void>(), Callback<MutableList<Cita>> {
        override fun onFailure(call: Call<MutableList<Cita>>?, t: Throwable?) {
            Log.i("eventos", "fallo")
        }

        override fun onResponse(call: Call<MutableList<Cita>>?, response: Response<MutableList<Cita>>?) {
            if (response?.body() != null) {

                if (response.body().size > 0) {
                    bsm_list_eventos.visibility = View.VISIBLE
                    bsm_cardView.visibility = View.GONE
                    eventos.addAll(response.body())
                    adapterEvents?.notifyItemRangeInserted(0, eventos.size)
                    adapterEvents?.notifyDataSetChanged()

                }

            } else
                Log.i("eventos", "null")
        }

        override fun onPreExecute() {
            // Toast.makeText(this@MascotaActivity, "cargando eventos...", Toast.LENGTH_SHORT).show()
        }

        override fun doInBackground(vararg p0: Void?): Void? {

            val ws = WebApiClient.client!!.create(WebService::class.java)
            val call = ws.citas_mascota(mascota_id,est_id)
            call.enqueue(this)


            return null
        }

        override fun onCancelled() {
            mLoadcDatatask = null
        }

    }

}
