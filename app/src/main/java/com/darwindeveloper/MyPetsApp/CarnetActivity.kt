package com.darwindeveloper.MyPetsApp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.darwindeveloper.MyPetsApp.api.Constants
import com.darwindeveloper.MyPetsApp.api.WebApiClient
import com.darwindeveloper.MyPetsApp.api.WebService
import com.darwindeveloper.MyPetsApp.api.modelos.Mascota
import com.darwindeveloper.MyPetsApp.api.responses.ClientResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_carnet.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class CarnetActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_carnet)

        mascota_id = intent.getStringExtra(MASCOTA_ID)
        user_id = intent.getStringExtra(USER_ID)
        api_token = intent.getStringExtra(API_TOKEN)

        getData()

    }


    companion object {
        const val MASCOTA_ID = "CAmascota.mascota_id"
        const val USER_ID = "CAmascota.user_id"
        const val API_TOKEN = "CAmascota.api_token"
    }

    private var mascota_id: String? = null
    private var user_id: String? = null
    private var api_token: String? = null


    private fun getData() {
        val ws = WebApiClient.client!!.create(WebService::class.java)
        val mcall = ws.mascota(mascota_id)
        mcall.enqueue(object : Callback<Mascota> {
            override fun onFailure(call: Call<Mascota>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<Mascota>?, response: Response<Mascota>?) {
                if (response?.body() != null) {
                    val pet = response.body()

                    if (pet.foto != null) {
                        Picasso.with(this@CarnetActivity)
                                .load(Constants.WEB_URL + pet.foto)
                                .into(ac_foto)
                    }

                    ac_nombre.text = pet.nombre
                    ac_id.text = "ID: ${pet.mascota_id}"
                    ac_especie.text = "Especie: ${pet.especie}"
                    ac_raza.text = "Raza: ${pet.raza}"
                    ac_sexo.text = "Sexo: ${pet.sexo}"
                    ac_color.text = "Color: ${pet.color}"
                    ac_esterilizado.text = "Esterilizad@: ${pet.esterilizado}"
                    ac_microchip.text = "Microchip: ${pet.microchip}"
                    ac_nacimiento.text = "Fecha de nacimiento: ${pet.fecha_nacimiento}"

                }
            }

        })


        val mcall2 = ws.datoscliente(user_id, api_token)
        mcall2.enqueue(object : Callback<ClientResponse> {
            override fun onResponse(call: Call<ClientResponse>?, response: Response<ClientResponse>?) {
                val cliente = response?.body()
                if (cliente != null) {
                    ac_user_id.text = "ID: ${user_id}"
                    ac_user_cedula.text = "C.I./PAS.: ${cliente.client!!.cedula}"
                    ac_user_email.text = "E-mail: ${cliente.client.email}"
                    ac_user_name.text = "Nombres: ${cliente.client.nombres}"
                    ac_user_last_name.text = "Apellidos: ${cliente.client.apellidos}"
                    ac_user_celular.text = "Celular: ${cliente.client.celular}"
                    ac_user_last_telefono.text = "Teléfono: ${cliente.client.telefono}"
                }
            }

            override fun onFailure(call: Call<ClientResponse>?, t: Throwable?) {

            }

        })
    }


}
