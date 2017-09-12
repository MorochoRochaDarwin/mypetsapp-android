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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.CameraUpdateFactory


class SitiosActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnInfoWindowClickListener {


    private fun midPoint() {
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


    override fun onInfoWindowClick(marker: Marker?) {
        if (marker != null) {

            try {
                val tmp = marker.snippet.split("-")
                val sitio = sitios[tmp[0].toInt()]

                val args = Bundle()
                args.putString(BottomSheetSitio.LAT, sitio.lat)
                args.putString(BottomSheetSitio.LNG, sitio.lng)
                args.putString(BottomSheetSitio.NOMBRE, sitio.nombre)
                args.putString(BottomSheetSitio.HTML, sitio.html)
                args.putInt(BottomSheetSitio.ICON, icon!!)
                val bss = BottomSheetSitio.newInstance(args);
                bss.show(supportFragmentManager, "Bss")
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
            googleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 15f))

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
        this.googleMap?.setMyLocationEnabled(true)
        this.googleMap?.setOnMyLocationButtonClickListener(this)
        this.googleMap?.setOnInfoWindowClickListener(this)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }


    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    companion object {
        const val CATEGORIA = "sitios.categoria"
        const val ICON = "sitios.icon"
    }


    private var categoria: String? = null
    private var icon: Int? = null
    private var provincias = ArrayList<Provincia>()
    private var ciudades = ArrayList<Ciudad>()
    private var sectores = ArrayList<Sector>()
    private var sitios = ArrayList<Sitio>()

    private var tmp_provincias = ArrayList<String>()
    private var tmp_ciudades = ArrayList<String>()
    private var tmp_sectores = ArrayList<String>()


    private var provincia = ""
    private var ciudad = ""
    private var sector = ""

    var posp = 0
    var posc = 0
    var poss = 0


    var adapterProvinciaa: ArrayAdapter<String>? = null
    var adapterCiudades: ArrayAdapter<String>? = null
    var adapterSectores: ArrayAdapter<String>? = null


    private var mGoogleApiClient: GoogleApiClient? = null//cliente del api de google
    private var googleMap: GoogleMap? = null
    private var mapFragment: MapFragment? = null
    private var marker: Marker? = null
    private var markers = ArrayList<Marker>()

    private var POS: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sitios)

        categoria = intent.getStringExtra(CATEGORIA)
        icon = intent.getIntExtra(ICON, R.drawable.ic_pizza)

        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build()
        }

        adapterProvinciaa = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tmp_provincias)
        adapterCiudades = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tmp_ciudades)
        adapterSectores = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tmp_sectores)
        as_spinner_provincias.adapter = adapterProvinciaa
        as_spinner_ciudades.adapter = adapterCiudades
        as_spinner_sectores.adapter = adapterSectores


        as_spinner_provincias.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                posp = p2

                updateCiudades(p2)

            }

        })

        as_spinner_ciudades.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                posc = p2
                updateSectores(p2)
            }

        })


        as_spinner_sectores.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                poss = p2 - 1
                sitios.clear()
                for (i in markers.indices) {
                    markers[i].remove()
                }
                markers.clear()


                val tmp = ciudades[posc].sitios
                if (p2 == 0) {
                    if (tmp != null) {
                        if (tmp.isEmpty()) {
                            Toast.makeText(this@SitiosActivity, "No se encontraron resultados", Toast.LENGTH_SHORT).show()
                        }
                        for (i in tmp.indices) {
                            val lat = tmp[i].lat!!.toDouble()
                            val lng = tmp[i].lng!!.toDouble()

                            sitios.add(tmp[i])

                            val marker = googleMap?.addMarker(MarkerOptions()
                                    .position(LatLng(lat, lng)).snippet("$i-${tmp[i].tipo}")
                                    .icon(BitmapDescriptorFactory.fromResource(icon!!)).title(sitios[i].nombre)
                            )
                            if (marker != null) {
                                markers.add(marker)
                            }
                        }
                    }


                } else {
                    if (tmp != null) {
                        for (i in tmp.indices) {

                            if (tmp[i].sector_id == sectores[poss].sector_id) {


                                sitios.add(tmp[i])

                                val lat = tmp[i].lat!!.toDouble()
                                val lng = tmp[i].lng!!.toDouble()


                                val marker = googleMap?.addMarker(MarkerOptions()
                                        .position(LatLng(lat, lng)).snippet("$i-${tmp[i].tipo}")
                                        .icon(BitmapDescriptorFactory.fromResource(icon!!)).title(tmp[i].nombre)
                                )
                                if (marker != null) {
                                    markers.add(marker)
                                    Log.i("latlng", "${marker.position.latitude} ${marker.position.longitude}")
                                }


                            }
                        }
                    }
                }



                midPoint()


            }

        })

        load()

        mapFragment = fragmentManager.findFragmentById(R.id.as_map) as MapFragment
        mapFragment?.getMapAsync(this)


    }


    private fun updateCiudades(pos: Int) {
        ciudades.clear()
        ciudades.addAll(provincias[pos].ciudades!!)
        tmp_ciudades.clear()
        for (i in ciudades.indices) {
            tmp_ciudades.add(ciudades[i].ciudad_nombre!!)
        }
        adapterCiudades!!.notifyDataSetChanged()
        updateSectores(0)
    }


    private fun updateSectores(pos: Int) {
        sectores.clear()
        sectores.addAll(ciudades[pos].sectores!!)
        tmp_sectores.clear()
        tmp_sectores.add("todos los sectores")
        for (i in sectores.indices) {
            tmp_sectores.add(sectores[i].sector_nombre!!)
        }
        adapterSectores!!.notifyDataSetChanged()
    }

    private fun load() {
        val ws = WebApiClient.client!!.create(WebService::class.java)
        Log.i("categoria", categoria)
        val mcall = ws.sitios(categoria)
        mcall.enqueue(object : Callback<List<Provincia>> {
            override fun onFailure(call: Call<List<Provincia>>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<List<Provincia>>?, response: Response<List<Provincia>>?) {
                val arr = response?.body()
                if (arr != null) {
                    if (arr.isNotEmpty()) {
                        provincias.addAll(arr)


                        tmp_provincias.clear()
                        tmp_ciudades.clear()
                        tmp_sectores.clear()

                        if (provincias.size > 0) {

                            for (i in provincias.indices) {
                                tmp_provincias.add(provincias[i].provincia_nombre!!)
                            }
                            adapterProvinciaa!!.notifyDataSetChanged()

                        }

                    }
                }
            }

        })
    }
}
