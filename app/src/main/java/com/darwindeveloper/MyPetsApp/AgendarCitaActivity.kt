package com.darwindeveloper.MyPetsApp

import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.TimePicker
import android.widget.Toast
import com.darwindeveloper.MyPetsApp.api.WebApiClient
import com.darwindeveloper.MyPetsApp.api.WebService
import com.darwindeveloper.MyPetsApp.api.responses.DefaultResponse

import kotlinx.android.synthetic.main.activity_agendar_cita.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AgendarCitaActivity : AppCompatActivity() {


    companion object {
        const val USER_ID = "agendarcita.user_id"
        const val API_TOKEN = "agendarcita.user_api_token"
        const val EST_ID = "agendarcita.est_id"
        const val PET_ID = "agendarcita.pet_id"
        const val EST_NAME = "agendarcita.est_name"
    }

    private var user_id: String? = null
    private var api_token: String? = null
    private var est_id: String? = null
    private var est_name: String? = null
    private var pet_id: String? = null
    private var hour: Int? = null
    private var minute: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendar_cita)
        setSupportActionBar(aec_toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        user_id = intent.getStringExtra(USER_ID)
        api_token = intent.getStringExtra(API_TOKEN)
        est_id = intent.getStringExtra(EST_ID)
        est_name = intent.getStringExtra(EST_NAME)
        pet_id = intent.getStringExtra(PET_ID)


        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        supportActionBar?.setTitle("Nueva cita - $est_name")

        aec_datePicker.minDate = Date().time
        aec_time.setIs24HourView(true)
        aec_time.setOnTimeChangedListener(object : TimePicker.OnTimeChangedListener {
            override fun onTimeChanged(p0: TimePicker?, mhour: Int, mminute: Int) {
                hour = mhour
                minute = mminute
            }

        });




        aec_send.setOnClickListener {

            val motivo = aec_motivo.text.toString()

            if (motivo.isEmpty()) {
                aec_motivo.setError("este campo es obligatorio")
                aec_motivo.requestFocus()
            } else {
                val obs = aec_obs.text.toString()
                val nday = aec_datePicker.dayOfMonth
                val nmonth = aec_datePicker.month
                val nyear = aec_datePicker.year


                val fecha = "$nyear-${monthToString(nmonth)}-$nday"

                var hora = ""
                if (hour!! < 10) {
                    hora = "0${hour}:"
                } else {
                    hora = "${hour}:"
                }

                if (minute!! < 10) {
                    hora += "0${minute}"
                } else {
                    hora += "${minute}"
                }




                Toast.makeText(this@AgendarCitaActivity, "Enviando ....", Toast.LENGTH_SHORT).show()

                val ws = WebApiClient.client!!.create(WebService::class.java)
                val mcall = ws.agendar_cita(user_id, api_token, pet_id, est_id, motivo, hora, fecha, obs)
                mcall.enqueue(object : Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>?, t: Throwable?) {
                        Toast.makeText(this@AgendarCitaActivity, "Error intente mas tarde", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<DefaultResponse>?, response: Response<DefaultResponse>?) {

                        val res = response?.body()
                        if (res != null) {
                            if (res.status == 200) {
                                val builder = AlertDialog.Builder(this@AgendarCitaActivity)
                                builder.setCancelable(false)
                                builder.setTitle("EXITO")
                                builder.setMessage("Se ha enviado una solicitud de cita a ${est_name}, cuando haya sido aprobada recibiras una notificación o simplemente la podras observarla en tu lista de eventos.")
                                builder.setPositiveButton("ENTENDIDO", DialogInterface.OnClickListener { dialogInterface, i ->
                                    finish()
                                })
                                builder.create().show()

                            } else {
                                Toast.makeText(this@AgendarCitaActivity, res.msg, Toast.LENGTH_SHORT).show()
                            }
                        }

                    }

                });

            }

        }
    }


    fun monthToString(month: Int): String {
        if (month >= 0 && month <= 8) {
            return "0${month + 1}";
        } else {
            return "${month + 1}";
        }
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

}
