package com.darwindeveloper.MyPetsApp.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.darwindeveloper.MyPetsApp.DashboardActivity
import com.darwindeveloper.MyPetsApp.DataUserActivity
import com.darwindeveloper.MyPetsApp.R
import com.darwindeveloper.MyPetsApp.api.Constants
import com.darwindeveloper.MyPetsApp.api.WebApiClient
import com.darwindeveloper.MyPetsApp.api.WebService
import com.darwindeveloper.MyPetsApp.api.responses.UserResponse
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by DARWIN MOROCHO on 22/8/2017.
 */
class LoginFragment : Fragment() {

    var mAuthTask: UserLoginTask? = null;
    var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater?.inflate(R.layout.fragment_login, container, false) as View


        rootView!!.password.setOnEditorActionListener(TextView.OnEditorActionListener { textView, id, keyEvent ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })


        rootView!!.email_sign_in_button.setOnClickListener { attemptLogin() }

        return rootView
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        if (mAuthTask != null) {
            return
        }

        // Reset errors.
        rootView!!.email.setError(null)
        rootView!!.password.setError(null)

        // obtenemos el email y la contraseña
        val email = rootView!!.email.getText().toString()
        val password = rootView!!.password.getText().toString()
        val est_id = rootView!!.estid.getText().toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            rootView!!.password.setError("Contraseña invalida")
            focusView = rootView!!.password
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            rootView!!.email.setError("Complete este campo")
            focusView = rootView!!.email
            cancel = true
        } else if (!isEmailValid(email)) {
            rootView!!.email.setError("Email invalido")
            focusView = rootView!!.email
            cancel = true
        } else if (TextUtils.isEmpty(password)) {
            rootView!!.password.setError("ingrese una contraseña valida")
            focusView = rootView!!.password
            cancel = true
        } else if (TextUtils.isEmpty(est_id)) {
            rootView!!.estid.setError("ingrese un código de establecimiento valido")
            focusView = rootView!!.estid
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView!!.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            Toast.makeText(context, "Verificando información", Toast.LENGTH_SHORT).show()
            mAuthTask = UserLoginTask(email, password, est_id)
            mAuthTask?.execute(null as Void?)
        }
    }


    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    inner class UserLoginTask internal constructor(private val mEmail: String, private val mPassword: String, private val mID: String) : AsyncTask<Void, Void, Void>(), Callback<UserResponse> {
        internal var preferences: SharedPreferences

        init {
            preferences = PreferenceManager.getDefaultSharedPreferences(context)
        }


        override fun doInBackground(vararg voids: Void): Void? {

            val firebase_token = preferences.getString(Constants.FIREBASE_TOKEN, null)


            val webService = WebApiClient.client!!.create(WebService::class.java)
            val call = webService.api_login(mEmail, mPassword, mID, firebase_token)
            call.enqueue(this)

            /*

             */
            return null
        }

        override fun onCancelled() {
            mAuthTask = null
        }

        override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
            mAuthTask = null


            val userResponse = response.body()
            if (userResponse != null) {
                if (userResponse.status == 200) {
                    val user = userResponse.user

                    val editor = preferences.edit()
                    editor.putString(Constants.USER_ID, user!!.user_id)
                    editor.putString(Constants.USER_NAME, user.name)
                    editor.putString(Constants.USER_LAST_NAME, user.last_name)
                    editor.putString(Constants.USER_EMAIL, user.email)
                    editor.putString(Constants.USER_NUMBRE_IDENTIFICATION, user.cedula)
                    editor.putString(Constants.USER_PHOTO, user.foto)
                    editor.putString(Constants.USER_API_TOKEN, user.api_token)
                    editor.putString(Constants.ESTABLECIMIENTO_ID, mID)
                    editor.apply()

                    val intent = Intent(context, DataUserActivity::class.java)
                    intent.putExtra(DataUserActivity.USER_ID, user.user_id)
                    intent.putExtra(DataUserActivity.API_TOKEN, user.api_token)
                    startActivity(intent)
                    activity.finish()

                } else {
                    Toast.makeText(context, userResponse.msg, Toast.LENGTH_SHORT).show()
                }
            }

            /*
             mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
             */
        }

        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
            Log.i("errorl", t.message)
            Toast.makeText(context, "Error intente nuevamente", Toast.LENGTH_SHORT).show()
            mAuthTask = null
        }
    }

}