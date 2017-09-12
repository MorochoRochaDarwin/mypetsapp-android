package com.darwindeveloper.MyPetsApp.api.modelos

import com.google.gson.annotations.SerializedName

/**
 * Created by DARWIN MOROCHO on 24/8/2017.
 */
class Cita {


    val cita_id: String? = null
    val motivo: String? = null
    val descripcion: String? = null
    val fecha_creada: String? = null
    val fecha: String? = null
    val hora: String? = null
    val mascota_id: String? = null
    val establecimiento_id: String? = null

    @SerializedName("nombre")
    val mascota_nombre: String? = null
    @SerializedName("foto")
    val mascota_foto: String? = null

    val nombre_establecimiento: String? = null

}