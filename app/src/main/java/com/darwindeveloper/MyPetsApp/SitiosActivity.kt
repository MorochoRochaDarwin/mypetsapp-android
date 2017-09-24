package com.darwindeveloper.MyPetsApp

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.darwindeveloper.MyPetsApp.api.WebApiClient
import com.darwindeveloper.MyPetsApp.api.WebService
import com.darwindeveloper.MyPetsApp.api.modelos.Ciudad
import com.darwindeveloper.MyPetsApp.api.modelos.Provincia
import com.darwindeveloper.MyPetsApp.api.modelos.Sector
import com.darwindeveloper.MyPetsApp.api.modelos.Sitio
import com.darwindeveloper.MyPetsApp.bottom_sheets.BottomSheetSitio
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_sitios.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.location.LocationManager
import android.content.Context.LOCATION_SERVICE
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.content.Intent
import android.graphics.Color
import android.provider.Settings
import android.graphics.Color.parseColor
import android.support.design.widget.BottomSheetBehavior
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.CameraUpdateFactory


class SitiosActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnInfoWindowClickListener {


    /* private fun midPoint() {
         if (markers.size > 0) {
             val builder = LatLngBounds.builder();
             for (i in markers.indices) {
                 builder.include(markers[i].position)
             }
             val bounds = builder.build()
             val cu = CameraUpdateFactory.newLatLngBounds(bounds, 0)
             googleMap?.animateCamera(cu)
         }
     }
 */

    override fun onInfoWindowClick(marker: Marker?) {
        if (marker != null) {

            try {

            } catch (e: Exception) {

            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMyLocationButtonClick(): Boolean {

        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // si el gps no esta encendido
            turnGPSOn()
        } else {
            val location = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            // setMarketPosition(location.latitude, location.longitude)
        }
        return false
    }


    private fun setMarketPosition(lat: Double, lng: Double) {
        if (googleMap != null) {
            googleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 19f))

            if (marker != null) {
                marker?.remove()
            }

            marker = googleMap?.addMarker(MarkerOptions()
                    .position(LatLng(lat, lng))
                    .draggable(true))

        }
    }


    private val REQUEST_TURNON_GPS = 1921

    /**
     * enciende el gps
     */
    private fun turnGPSOn() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivityForResult(intent, REQUEST_TURNON_GPS)
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0
        //this.googleMap?.setMyLocationEnabled(true)
        //this.googleMap?.setOnMyLocationButtonClickListener(this)
        this.googleMap?.setOnInfoWindowClickListener(this)


        setMarketPosition(lat!!.toDouble(), lng!!.toDouble())


    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }


    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    companion object {
        const val NOMBRE = "sitios.nombre"
        const val TIPO = "sitios.tipo"
        const val HTML = "sitios.html"
        const val LAT = "sitios.lat"
        const val LNG = "sitios.lng"
        const val ICON = "sitios.icon"
    }


    private var googleMap: GoogleMap? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mapFragment: MapFragment? = null

    private var icon: Int? = null
    private var nombre: String? = null
    private var tipo: String? = null
    private var html: String? = null
    private var lat: String? = null
    private var lng: String? = null


    private var marker: Marker? = null


    private var POS: LatLng? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sitios)

        nombre = intent.getStringExtra(NOMBRE)
        tipo = intent.getStringExtra(TIPO)
        lat = intent.getStringExtra(LAT)
        lng = intent.getStringExtra(LNG)
        html = intent.getStringExtra(HTML)
        icon = intent.getIntExtra(ICON, R.drawable.ic_pizza)

        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build()
        }


        mapFragment = fragmentManager.findFragmentById(R.id.as_map) as MapFragment
        mapFragment?.getMapAsync(this)


        val bottomSheetBehavior = BottomSheetBehavior.from(linear_layout_bottom_sheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.isHideable=false

        as_nombre.text = nombre
        as_tipo.text = tipo
        as_icono.setImageDrawable(resources.getDrawable(icon!!))
        val mime = "text/html"
        val encoding = "utf-8"
        as_webview.loadDataWithBaseURL(null, html, mime, encoding, null)


    }


}
