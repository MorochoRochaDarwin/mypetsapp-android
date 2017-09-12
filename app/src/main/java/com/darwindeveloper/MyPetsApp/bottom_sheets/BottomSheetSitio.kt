package com.darwindeveloper.MyPetsApp.bottom_sheets

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.darwindeveloper.MyPetsApp.R
import kotlinx.android.synthetic.main.bottom_sheet_sitio.view.*
import retrofit2.http.HTTP

/**
 * Created by DARWIN MOROCHO on 12/9/2017.
 */
class BottomSheetSitio : BottomSheetDialogFragment() {


    var rootView: View? = null

    companion object {
        val LAT: String = "bss.lat"
        val LNG: String = "bss.lng"
        val NOMBRE: String = "bss.nombre"
        val HTML: String = "bss.html"
        val ICON: String = "bss.icon"


        fun newInstance(args: Bundle): BottomSheetSitio {
            val b = BottomSheetSitio()
            b.arguments = args
            return b
        }


    }

    var lat: String? = null
    var lng: String? = null
    var nombre: String? = null
    var html: String? = null
    var icon: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lat = arguments.getString(LAT, null)
        lng = arguments.getString(LNG, null)
        nombre = arguments.getString(NOMBRE, null)
        html = arguments.getString(HTML, null)
        icon = arguments.getInt(ICON, R.mipmap.ic_launcher)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater!!.inflate(R.layout.bottom_sheet_sitio, container, false)


        rootView!!.bss_nombre.text = nombre
        rootView!!.bss_icono.setImageDrawable(resources.getDrawable(icon!!))

        val mime = "text/html"
        val encoding = "utf-8"
        rootView!!.bss_webview.loadDataWithBaseURL(null, html, mime, encoding, null)

        return rootView
    }
}