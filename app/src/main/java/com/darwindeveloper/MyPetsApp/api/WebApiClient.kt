package com.darwindeveloper.MyPetsApp.api


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Darwin Morocho on 18/06/2017.
 */

object WebApiClient {
    private var retrofit: Retrofit? = null

    val client: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                        .baseUrl(Constants.WEB_URL + "/api/v1/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }
            return retrofit
        }
}
