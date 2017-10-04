package com.darwindeveloper.MyPetsApp.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.darwindeveloper.MyPetsApp.R
import com.darwindeveloper.MyPetsApp.api.Constants
import com.darwindeveloper.MyPetsApp.api.WebApiClient
import com.darwindeveloper.MyPetsApp.api.WebService
import com.darwindeveloper.MyPetsApp.api.modelos.Provincia
import com.darwindeveloper.MyPetsApp.api.responses.ClientResponse
import kotlinx.android.synthetic.main.fragment_profile.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.darwindeveloper.MyPetsApp.MainActivity
import android.content.Intent
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.darwindeveloper.MyPetsApp.api.modelos.Ciudad
import com.darwindeveloper.MyPetsApp.api.modelos.Sector
import com.darwindeveloper.MyPetsApp.api.responses.DefaultResponse
import com.darwindeveloper.MyPetsApp.hover.MyHoverMenuService
import kotlinx.android.synthetic.main.fragment_profile.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by DARWIN MOROCHO on 23/8/2017.
 */
class ProfileFragment : Fragment() {

    private var rootView: View? = null

    private var loadData: LoadData? = null
    private var loadP: LoadP? = null

    private var user_id: String? = null
    private var api_token: String? = null


    private val provincias = ArrayList<Provincia>()
    private val ciudades = ArrayList<Ciudad>()
    private val sectores = ArrayList<Sector>()

    private var tmp_provincias = ArrayList<String>()
    private var tmp_ciudades = ArrayList<String>()
    private var tmp_sector = ArrayList<String>()


    private var provincia = ""
    private var ciudad = ""
    private var sector = ""

    var ip = 0
    var ic = 0
    var ise = 0

    private val sexos = arrayOf("Maculino", "Femenino")
    private val niveles = arrayOf("primaria", "secundaria", "tercer nivel", "cuarto nivel", "ninguno")
    private val paises = arrayOf("Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe")

    private var sexo = "M"
    private var nivel = "primaria"
    private var pais = "Ecuador"


    companion object {
        const val USER_ID = "ProfileFragment.user_ud"

        fun newInstance(args: Bundle): ProfileFragment {
            val fr = ProfileFragment()
            fr.arguments = args
            return fr
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user_id = arguments.getString(USER_ID, null)

        api_token = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.USER_API_TOKEN, null)
    }

    var adapterProvinciaa: ArrayAdapter<String>? = null
    var adapterCiudades: ArrayAdapter<String>? = null
    var adapterSectores: ArrayAdapter<String>? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater?.inflate(R.layout.fragment_profile, container, false) as View

        if (user_id != null) {

            if (loadP != null)
                loadP = null

            loadP = LoadP()
            loadP?.execute()

        }


        adapterProvinciaa = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, tmp_provincias)
        rootView!!.fp_spinner_provincia.adapter = adapterProvinciaa

        adapterCiudades = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, tmp_ciudades)
        rootView!!.fp_spinner_ciudad.adapter = adapterCiudades

        adapterSectores = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, tmp_sector)
        rootView!!.fp_spinner_sector.adapter = adapterSectores


        val adapterGenero = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, sexos)
        rootView!!.fp_spinner_genero.adapter = adapterGenero
        rootView!!.fp_spinner_genero.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 == 0) {
                    sexo = "M"
                } else {
                    sexo = "F"
                }
            }

        })


        val adapterPaises = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, paises)
        rootView!!.fp_spinner_pais.adapter = adapterPaises
        rootView!!.fp_spinner_pais.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                pais = paises[p2]
            }

        })


        val adapterNiveles = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, niveles)
        rootView!!.fp_spinner_nivel.adapter = adapterNiveles
        rootView!!.fp_spinner_nivel.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                nivel = niveles[p2]
            }

        })




        rootView!!.fp_fab.setOnClickListener {
            rootView!!.fp_fab.visibility = View.GONE
            rootView!!.webview_profile.visibility = View.GONE
            rootView!!.fp_layout_edit.visibility = View.VISIBLE

        }


        rootView!!.fp_btn_save.setOnClickListener {
            saveEdit()

        }

        rootView!!.fp_btn_cancel.setOnClickListener {
            rootView!!.fp_fab.visibility = View.VISIBLE
            rootView!!.webview_profile.visibility = View.VISIBLE
            rootView!!.fp_layout_edit.visibility = View.GONE

        }


        return rootView

    }


    private fun saveEdit() {

        val nombres = rootView!!.fp_nombres.text.toString()
        if (nombres.trim().isEmpty()) {

            rootView!!.fp_nombres.setError("ingrese un nombre valido")
            rootView!!.fp_nombres.requestFocus()
            return
        }


        val apellidos = rootView!!.fp_apellidos.text.toString()
        if (apellidos.trim().isEmpty()) {

            rootView!!.fp_apellidos.setError("ingrese un apellido valido")
            rootView!!.fp_apellidos.requestFocus()
            return
        }


        val email = rootView!!.fp_email.text.toString()
        if (email.trim().isEmpty()) {

            rootView!!.fp_email.setError("ingrese un email valido")
            rootView!!.fp_email.requestFocus()
            return
        }


        val cedula = rootView!!.fp_cedula.text.toString()
        if (cedula.trim().isEmpty()) {

            rootView!!.fp_cedula.setError("ingrese una cedula o pasaporte valido")
            rootView!!.fp_cedula.requestFocus()
            return
        }


        val celular = rootView!!.fp_celular.text.toString()
        if (celular.trim().isEmpty()) {

            rootView!!.fp_celular.setError("ingrese un telefono movil valido")
            rootView!!.fp_celular.requestFocus()
            return
        }


        val telefono = rootView!!.fp_telefono.text.toString()
        if (telefono.trim().isEmpty()) {
            rootView!!.fp_telefono.setError("ingrese un telefono fijo valido")
            rootView!!.fp_telefono.requestFocus()
            return
        }


        val direccion = rootView!!.fp_direccion.text.toString()
        if (direccion.trim().isEmpty()) {

            rootView!!.fp_direccion.setError("ingrese una dirección valida")
            rootView!!.fp_direccion.requestFocus()
            return
        }


        val sdf = SimpleDateFormat("yyyy-MM-dd")


        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val date = sdf.parse("$currentYear-$currentMonth-$currentDay")

        val nday = rootView!!.fp_datePicker.dayOfMonth
        val nmonth = rootView!!.fp_datePicker.month
        val nyear = rootView!!.fp_datePicker.year

        val ndate = sdf.parse("$nyear-$nmonth-$nday")


        val nacimiento = "$nyear-${monthToString(nmonth)}-$nday"


        if (!date.after(ndate) && date != ndate) {
            Toast.makeText(context, "La fecha de nacimiento no es valida ", Toast.LENGTH_LONG).show()
            return
        }


        val tmpp = provincia.split("-")
        val tmpc = ciudad.split("-")
        val tmps = sector.split("-")

        Toast.makeText(context, "Actualizando...", Toast.LENGTH_SHORT).show()

        val ws = WebApiClient.client!!.create(WebService::class.java)
        val mcall = ws.actualizar_cliente(api_token,
                user_id,
                nombres,
                apellidos,
                cedula, email,
                sexo,
                nacimiento,
                pais, celular,
                telefono, nivel, direccion, tmpp[0], tmpc[0], tmps[0])


        mcall.enqueue(object : Callback<DefaultResponse> {
            override fun onFailure(call: Call<DefaultResponse>?, t: Throwable?) {
                Toast.makeText(context, "Error intente intente nuevamente", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<DefaultResponse>?, response: Response<DefaultResponse>?) {
                val res = response?.body()
                if (res != null) {
                    Toast.makeText(context, res.msg, Toast.LENGTH_SHORT).show()
                    if (res.status == 200) {
                        if (loadData != null)
                            loadData = null
                        loadData = LoadData()
                        loadData?.execute()


                        rootView!!.fp_fab.visibility = View.VISIBLE
                        rootView!!.webview_profile.visibility = View.VISIBLE
                        rootView!!.fp_layout_edit.visibility = View.GONE
                    }
                }
            }

        })

    }


    fun monthToString(month: Int): String {
        if (month >= 0 && month <= 8) {
            return "0${month + 1}";
        } else {
            return "${month + 1}";
        }
    }


    private fun buscar_nivel(nivel: String): Int {

        for (i in niveles.indices) {
            val mnivel: String = niveles[i] as String
            if (nivel == mnivel) {
                return i
            }
        }
        return 0
    }


    private fun buscar_pais(pais: String): Int {

        for (i in paises.indices) {
            if (pais == paises[i]) {
                return i
            }
        }
        return 0
    }


    private inner class LoadData : AsyncTask<Void, Void, Void>(), Callback<ClientResponse> {
        override fun onResponse(call: Call<ClientResponse>?, response: Response<ClientResponse>?) {
            if (response?.body() != null) {
                val mResponse = response.body();

                if (mResponse.status == 200) {


                    val mime = "text/html"
                    val encoding = "utf-8"

                    webview_profile.getSettings().setJavaScriptEnabled(true)
                    webview_profile.loadDataWithBaseURL(null, mResponse.html, mime, encoding, null)

                    val cliente = mResponse.client

                    if (cliente != null) {

                        rootView!!.fp_nombres.setText(cliente.nombres)
                        rootView!!.fp_apellidos.setText(cliente.apellidos)
                        rootView!!.fp_cedula.setText(cliente.cedula)
                        rootView!!.fp_email.setText(cliente.email)
                        rootView!!.fp_celular.setText(cliente.celular)
                        rootView!!.fp_telefono.setText(cliente.telefono)
                        rootView!!.fp_direccion.setText(cliente.direccion)

                        val arr1 = cliente.nacimiento?.split("-")

                        rootView!!.fp_datePicker.updateDate(Integer.parseInt(arr1!![0]), Integer.parseInt(arr1[1]) - 1, Integer.parseInt(arr1[2]))



                        if (cliente.sexo.equals("M")) {
                            rootView!!.fp_spinner_genero.setSelection(0)
                        } else {
                            rootView!!.fp_spinner_genero.setSelection(1)
                        }


                        val inivel = buscar_nivel(cliente.estudio!!)
                        val ipais = buscar_pais(cliente.nacionalidad!!)

                        rootView!!.fp_spinner_nivel.setSelection(inivel)
                        rootView!!.fp_spinner_pais.setSelection(ipais)



                        tmp_provincias.clear()
                        tmp_ciudades.clear()
                        tmp_sector.clear()

                        if (provincias.size > 0) {

                            for (i in provincias.indices) {
                                if (cliente.provincia.equals(provincias[i].provincia_id))
                                    ip = i
                                tmp_provincias.add(provincias[i].provincia_id + "-" + provincias[i].provincia_nombre!!)
                            }

                        }

                        adapterProvinciaa!!.notifyDataSetChanged()



                        ciudades.clear()
                        ciudades.addAll(provincias[ip].ciudades!!)

                        if (tmp_provincias.size > 0) {

                            for (i in ciudades.indices) {
                                if (cliente.ciudad.equals(ciudades[i].ciudad_id))
                                    ic = i
                                tmp_ciudades.add(ciudades[i].ciudad_id!! + "-" + ciudades[i].ciudad_nombre!!)
                            }

                        }
                        adapterCiudades!!.notifyDataSetChanged()

                        sectores.clear()
                        sectores.addAll(ciudades[ic].sectores!!)

                        if (tmp_ciudades.size > 0) {

                            for (i in sectores.indices) {
                                if (cliente.sector.equals(sectores[i].sector_id))
                                    ise = i
                                tmp_sector.add(sectores[i].sector_id!! + "-" + sectores[i].sector_nombre!!)
                            }

                        }

                        adapterSectores!!.notifyDataSetChanged()



                        rootView!!.fp_spinner_provincia.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(p0: AdapterView<*>?) {

                            }

                            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                                ciudades.clear()
                                ciudades.addAll(provincias[p2].ciudades!!)
                                tmp_ciudades.clear()
                                for (i in ciudades.indices) {
                                    tmp_ciudades.add(ciudades[i].ciudad_id!! + "-" + ciudades[i].ciudad_nombre!!)
                                }



                                adapterCiudades!!.notifyDataSetChanged()

                                rootView!!.fp_spinner_ciudad.setSelection(0)



                                sectores.clear()
                                sectores.addAll(ciudades[0].sectores!!)
                                tmp_sector.clear()
                                for (i in sectores.indices) {
                                    tmp_sector.add(sectores[i].sector_id!! + "-" + sectores[i].sector_nombre!!)
                                }

                                adapterSectores!!.notifyDataSetChanged()
                                rootView!!.fp_spinner_sector.setSelection(0)



                                provincia = provincias[p2].provincia_id + "-" + provincias[p2].provincia_nombre
                                ciudad = ciudades[0].ciudad_id + "-" + ciudades[0].ciudad_nombre
                                sector = sectores[0].sector_id + "-" + sectores[0].sector_nombre


                            }

                        })



                        rootView!!.fp_spinner_ciudad.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(p0: AdapterView<*>?) {

                            }

                            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                                sectores.clear()
                                sectores.addAll(ciudades[p2].sectores!!)
                                tmp_sector.clear()
                                for (i in sectores.indices) {
                                    tmp_sector.add(sectores[i].sector_id!! + "-" + sectores[i].sector_nombre!!)
                                }

                                adapterSectores!!.notifyDataSetChanged()


                                ciudad = ciudades[p2].ciudad_id + "-" + ciudades[p2].ciudad_nombre
                                sector = sectores[0].sector_id + "-" + sectores[0].sector_nombre


                            }

                        })




                        rootView!!.fp_spinner_sector.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(p0: AdapterView<*>?) {

                            }

                            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                                sector = sectores[p2].sector_id + "-" + sectores[p2].sector_nombre
                            }

                        })



                        rootView!!.fp_spinner_provincia.setSelection(ip)
                        //rootView!!.fp_spinner_ciudad.setSelection(ic)
                        //rootView!!.fp_spinner_sector.setSelection(ise)


                    }


                } else {
                    Toast.makeText(context, mResponse.msg, Toast.LENGTH_LONG).show()
                }

            } else {
                Toast.makeText(context, "null", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<ClientResponse>?, t: Throwable?) {
            Log.i("errorresponse", t?.message)
            loadData = null

        }

        override fun onPreExecute() {

        }

        override fun doInBackground(vararg p0: Void?): Void? {

            if (user_id != null && api_token != null) {
                val webService = WebApiClient.client!!.create(WebService::class.java)
                val call = webService.cliente(user_id, api_token)
                call.enqueue(this)
            } else {
                Log.i("errorresponse", "datos nulos")
            }

            return null
        }

        override fun onCancelled() {
            loadData = null
        }

    }


    private inner class LoadP : AsyncTask<Void, Void, Void>(), Callback<MutableList<Provincia>> {
        override fun onFailure(call: Call<MutableList<Provincia>>?, t: Throwable?) {
            Toast.makeText(context, t?.message, Toast.LENGTH_SHORT).show()
            loadP = null
        }

        override fun onResponse(call: Call<MutableList<Provincia>>?, response: Response<MutableList<Provincia>>?) {
            val tmpA = response?.body()

            if (tmpA != null) {
                provincias.addAll(tmpA)

                if (loadData != null)
                    loadData = null
                loadData = LoadData()
                loadData?.execute()
            } else {
                Toast.makeText(context, "null p", Toast.LENGTH_SHORT).show()
            }

        }


        override fun onPreExecute() {
            Toast.makeText(context, "Obteniendo datos...", Toast.LENGTH_SHORT).show()
        }

        override fun doInBackground(vararg p0: Void?): Void? {

            val ws = WebApiClient.client!!.create(WebService::class.java)
            val call = ws.provincias()

            call.enqueue(this)

            return null
        }

        override fun onCancelled() {
            loadP = null
        }

    }


    override fun onStop() {

        if (loadP != null)
            loadP = null



        if (loadData != null)
            loadData = null

        super.onStop()
    }
}