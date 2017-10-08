package com.darwindeveloper.MyPetsApp.api.responses

import com.darwindeveloper.MyPetsApp.api.modelos.Establecimiento
import com.darwindeveloper.MyPetsApp.api.modelos.User

/**
 * Created by DARWIN MOROCHO on 10/8/2017.
 */

class CheckTokenResponse {
    val status: Int = 0
    val msg: String? = null
    val est: Establecimiento? = null
    val user: User? = null
}
