package com.darwindeveloper.MyPetsApp.fragments

import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.darwindeveloper.MyPetsApp.R
import com.darwindeveloper.MyPetsApp.api.WebApiClient
import com.darwindeveloper.MyPetsApp.api.WebService
import com.darwindeveloper.MyPetsApp.api.responses.PasswordResetResponse
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_password_reset.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by DARWIN MOROCHO on 22/8/2017.
 */
class PasswordResetFragment : Fragment() {


    private var mSendtask: SendTask? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater?.inflate(R.layout.fragment_password_reset, container, false)

        rootView!!.send.setOnClickListener(View.OnClickListener {
            val email = rootView.remail.getText().toString()

            if (email.contains("@")) {
                if (mSendtask != null) {
                    mSendtask = null
                }
                mSendtask = SendTask(email)
                mSendtask?.execute()
            } else {
                Toast.makeText(context, "Ingrese un email valido", Toast.LENGTH_SHORT).show()
            }
        })



        return rootView

    }


    private inner class SendTask(private val email: String) : AsyncTask<Void, Void, Void>(), Callback<PasswordResetResponse> {
        private var progressDialog: ProgressDialog? = null

        override fun onPreExecute() {
            progressDialog = ProgressDialog.show(context, "Enviando petición", "Por favor, espere ...", true)
            progressDialog!!.show()
        }

        override fun doInBackground(vararg voids: Void): Void? {

            val webService = WebApiClient.client!!.create(WebService::class.java)
            val call = webService.password_reset(email)
            call.enqueue(this)

            return null
        }

        override fun onCancelled() {
            progressDialog!!.hide()
            mSendtask = null
        }

        override fun onResponse(call: Call<PasswordResetResponse>, response: Response<PasswordResetResponse>) {
            progressDialog!!.hide()
            if (response.body() != null) {
                val builder = AlertDialog.Builder(context)
                builder.setTitle(response.body().status.toString() + "")
                builder.setMessage(response.body().msg)
                builder.create().show()
            } else {
                Toast.makeText(context, "Error: Intente mas tarde", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<PasswordResetResponse>, t: Throwable) {
            progressDialog!!.hide()
            mSendtask = null
        }
    }

}