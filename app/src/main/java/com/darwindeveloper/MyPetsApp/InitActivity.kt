package com.darwindeveloper.MyPetsApp

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import com.darwindeveloper.MyPetsApp.api.Constants
import com.darwindeveloper.MyPetsApp.api.WebApiClient
import com.darwindeveloper.MyPetsApp.api.WebService
import com.darwindeveloper.MyPetsApp.api.responses.CheckTokenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InitActivity : Activity() {

    private var preferences: SharedPreferences? = null

    private var initTask: InitTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)

        preferences = PreferenceManager.getDefaultSharedPreferences(this@InitActivity)

        val user_id: String? = preferences?.getString(Constants.USER_ID, null)

        if (user_id != null) {

            val api_token: String = preferences?.getString(Constants.USER_API_TOKEN, null) as String

            initTask = InitTask(user_id, api_token)
            initTask?.execute()


        } else {
            lauchMainActivity()
        }

    }


    private fun lauchMainActivity() {
        startActivity(Intent(this@InitActivity, MainActivity::class.java))
        finish()
    }


    private inner class InitTask(userId: String, apiToken: String) : AsyncTask<Void, Void, Void>(), Callback<CheckTokenResponse> {


        final val mUserId = userId
        final val mApiToken = apiToken


        override fun doInBackground(vararg p0: Void?): Void? {

            val webService = WebApiClient.client!!.create(WebService::class.java)
            val call = webService.check_token(mUserId, mApiToken)
            call.enqueue(this)


            return null
        }


        override fun onCancelled() {
            initTask = null
        }


        override fun onFailure(call: Call<CheckTokenResponse>?, t: Throwable?) {
            initTask = null
            lauchMainActivity()
        }

        override fun onResponse(call: Call<CheckTokenResponse>?, response: Response<CheckTokenResponse>?) {

            val checkTokenResponse = response?.body()
            if (checkTokenResponse != null) {
                if (checkTokenResponse.status == 200) {

                    val est_id = preferences?.getString(Constants.ESTABLECIMIENTO_ID, null)
                    if (est_id != null) {

                    } else {
                        val intent = Intent(this@InitActivity, DashboardActivity::class.java)
                        intent.putExtra(DashboardActivity.USER_ID, mUserId)
                        startActivity(intent)
                        finish()
                    }

                } else {
                    Toast.makeText(this@InitActivity, "Su sesión expiro", Toast.LENGTH_SHORT).show()
                    lauchMainActivity()
                }
            } else {
                lauchMainActivity()
            }

        }

    }


}
