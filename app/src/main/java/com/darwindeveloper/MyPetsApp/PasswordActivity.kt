package com.darwindeveloper.MyPetsApp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.darwindeveloper.MyPetsApp.api.WebApiClient
import com.darwindeveloper.MyPetsApp.api.WebService
import com.darwindeveloper.MyPetsApp.api.responses.DefaultResponse
import kotlinx.android.synthetic.main.activity_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasswordActivity : AppCompatActivity() {

    companion object {
        const val USER_ID = "pass.user_id"
        const val API_TOKEN = "pass.user_api_token"
    }

    var user_id: String? = null
    var api_token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        user_id = intent.getStringExtra(USER_ID)
        api_token = intent.getStringExtra(API_TOKEN)

        btnpass.setOnClickListener {
            cambiar()
        }
    }


    fun cambiar() {
        val password = pass.text.toString()

        if (password.isEmpty()) {
            pass.setError("Campo obligatorio")
            pass.requestFocus()
            return
        }


        val npassword = npass.text.toString()

        if (npassword.isEmpty()) {
            npass.setError("Campo obligatorio")
            npass.requestFocus()
            return
        }


        val vnpassword = vnpass.text.toString()

        if (vnpassword.isEmpty()) {
            vnpass.setError("Campo obligatorio")
            vnpass.requestFocus()
            return
        }


        if (npassword != vnpassword) {
            vnpass.setError("Las contraseñas no coinciden")
            vnpass.requestFocus()
            return
        }


        if (api_token != null && user_id != null) {
            val mc = WebApiClient.client!!.create(WebService::class.java)
            val mcall = mc.change_password(user_id, api_token, password, npassword)
            mcall.enqueue(object : Callback<DefaultResponse> {
                override fun onFailure(call: Call<DefaultResponse>?, t: Throwable?) {
                    Toast.makeText(this@PasswordActivity, "Error intente mas tarde", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<DefaultResponse>?, response: Response<DefaultResponse>?) {
                    val res = response!!.body()
                    if (res != null) {
                        if (res.status == 200) {
                            Toast.makeText(this@PasswordActivity, res.msg, Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@PasswordActivity, res.msg, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            });

        }


    }
}
