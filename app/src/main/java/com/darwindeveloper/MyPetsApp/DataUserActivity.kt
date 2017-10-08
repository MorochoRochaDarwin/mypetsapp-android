package com.darwindeveloper.MyPetsApp

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.darwindeveloper.MyPetsApp.api.Constants
import com.darwindeveloper.MyPetsApp.api.WebApiClient
import com.darwindeveloper.MyPetsApp.api.WebService
import com.darwindeveloper.MyPetsApp.api.modelos.Ciudad
import com.darwindeveloper.MyPetsApp.api.modelos.Provincia
import com.darwindeveloper.MyPetsApp.api.modelos.Sector
import com.darwindeveloper.MyPetsApp.api.responses.ClientResponse
import com.darwindeveloper.MyPetsApp.api.responses.DefaultResponse
import kotlinx.android.synthetic.main.activity_data_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class DataUserActivity : AppCompatActivity() {

    private var preferences: SharedPreferences? = null

    private var user_id: String? = null
    private var est_id: String? = null
    private var api_token: String? = null
    private var est_icono: String? = null
    private var nombre_establecimiento: String? = null

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

    var adapterProvinciaa: ArrayAdapter<String>? = null
    var adapterCiudades: ArrayAdapter<String>? = null
    var adapterSectores: ArrayAdapter<String>? = null


    companion object {
        const val USER_ID = "datau.user_id"
        const val EST_ID = "datau.est_id"
        const val EST_LOGO = "datau.est_logo"
        const val EST_NOMBRE = "datau.est_nombre"
        const val API_TOKEN = "datau.apitoken"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_user)
        user_id = intent.getStringExtra(USER_ID)
        api_token = intent.getStringExtra(API_TOKEN)
        est_id = intent.getStringExtra(EST_ID)
        est_icono = intent.getStringExtra(EST_LOGO)
        nombre_establecimiento = intent.getStringExtra(EST_NOMBRE)

        preferences = PreferenceManager.getDefaultSharedPreferences(this)

        fp_datePicker.maxDate = Date().time




        adapterProvinciaa = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tmp_provincias)
        fp_spinner_provincia.adapter = adapterProvinciaa

        adapterCiudades = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tmp_ciudades)
        fp_spinner_ciudad.adapter = adapterCiudades

        adapterSectores = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tmp_sector)
        fp_spinner_sector.adapter = adapterSectores


        val adapterGenero = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sexos)
        fp_spinner_genero.adapter = adapterGenero
        fp_spinner_genero.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
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


        val adapterPaises = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, paises)
        fp_spinner_pais.adapter = adapterPaises
        fp_spinner_pais.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                pais = paises[p2]
            }

        })


        val adapterNiveles = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, niveles)
        fp_spinner_nivel.adapter = adapterNiveles
        fp_spinner_nivel.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                nivel = niveles[p2]
            }

        })


        initP()

        adu_ok.setOnClickListener {
            saveEdit()
        }
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


    fun init() {
        if (user_id != null && api_token != null) {
            val webService = WebApiClient.client!!.create(WebService::class.java)
            val mcall = webService.cliente(user_id, api_token)
            mcall.enqueue(object : Callback<ClientResponse> {
                override fun onFailure(call: Call<ClientResponse>?, t: Throwable?) {

                }

                override fun onResponse(call: Call<ClientResponse>?, response: Response<ClientResponse>?) {
                    if (response?.body() != null) {
                        val mResponse = response.body();

                        if (mResponse.status == 200) {
                            val cliente = mResponse.client

                            if (cliente != null) {
                                fp_nombres.setText(cliente.nombres)
                                fp_apellidos.setText(cliente.apellidos)
                                fp_cedula.setText(cliente.cedula)
                                fp_email.setText(cliente.email)
                                fp_celular.setText(cliente.celular)
                                fp_telefono.setText(cliente.telefono)
                                fp_direccion.setText(cliente.direccion)

                                if (cliente.nacimiento != null) {
                                    val arr1 = cliente.nacimiento.split("-")

                                    fp_datePicker.updateDate(Integer.parseInt(arr1[0]), Integer.parseInt(arr1[1]) - 1, Integer.parseInt(arr1[2]))

                                }



                                if (cliente.sexo.equals("M")) {
                                    fp_spinner_genero.setSelection(0)
                                } else {
                                    fp_spinner_genero.setSelection(1)
                                }


                                if (cliente.estudio != null) {
                                    val inivel = buscar_nivel(cliente.estudio)
                                    fp_spinner_nivel.setSelection(inivel)
                                }

                                if (cliente.nacionalidad != null) {
                                    val ipais = buscar_pais(cliente.nacionalidad)
                                    fp_spinner_pais.setSelection(ipais)
                                }



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



                                fp_spinner_provincia.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
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

                                        fp_spinner_ciudad.setSelection(0)



                                        sectores.clear()
                                        sectores.addAll(ciudades[0].sectores!!)
                                        tmp_sector.clear()
                                        for (i in sectores.indices) {
                                            tmp_sector.add(sectores[i].sector_id!! + "-" + sectores[i].sector_nombre!!)
                                        }

                                        adapterSectores!!.notifyDataSetChanged()
                                        fp_spinner_sector.setSelection(0)



                                        provincia = provincias[p2].provincia_id + "-" + provincias[p2].provincia_nombre
                                        ciudad = ciudades[0].ciudad_id + "-" + ciudades[0].ciudad_nombre
                                        sector = sectores[0].sector_id + "-" + sectores[0].sector_nombre


                                    }

                                })



                                fp_spinner_ciudad.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
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




                                fp_spinner_sector.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                                    override fun onNothingSelected(p0: AdapterView<*>?) {

                                    }

                                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                                        sector = sectores[p2].sector_id + "-" + sectores[p2].sector_nombre
                                    }

                                })



                                fp_spinner_provincia.setSelection(ip)
                            }
                        }
                    }
                }

            })
        }
    }

    fun initP() {
        val ws = WebApiClient.client!!.create(WebService::class.java)
        val mcall = ws.provincias()

        mcall.enqueue(object : Callback<MutableList<Provincia>> {
            override fun onResponse(call: Call<MutableList<Provincia>>?, response: Response<MutableList<Provincia>>?) {
                val tmpA = response?.body()

                if (tmpA != null) {
                    provincias.addAll(tmpA)
                    init()

                } else {
                    Toast.makeText(this@DataUserActivity, "null p", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MutableList<Provincia>>?, t: Throwable?) {

            }

        })
    }


    private fun saveEdit() {

        val nombres = fp_nombres.text.toString()
        if (nombres.trim().isEmpty()) {

            fp_nombres.setError("ingrese un nombre valido")
            fp_nombres.requestFocus()
            return
        }


        val apellidos = fp_apellidos.text.toString()
        if (apellidos.trim().isEmpty()) {

            fp_apellidos.setError("ingrese un apellido valido")
            fp_apellidos.requestFocus()
            return
        }


        val email = fp_email.text.toString()
        if (email.trim().isEmpty()) {

            fp_email.setError("ingrese un email valido")
            fp_email.requestFocus()
            return
        }


        val cedula = fp_cedula.text.toString()
        if (cedula.trim().isEmpty()) {

            fp_cedula.setError("ingrese una cedula o pasaporte valido")
            fp_cedula.requestFocus()
            return
        }


        val celular = fp_celular.text.toString()
        if (celular.trim().isEmpty()) {

            fp_celular.setError("ingrese un telefono movil valido")
            fp_celular.requestFocus()
            return
        }


        val telefono = fp_telefono.text.toString()
        if (telefono.trim().isEmpty()) {
            fp_telefono.setError("ingrese un telefono fijo valido")
            fp_telefono.requestFocus()
            return
        }


        val direccion = fp_direccion.text.toString()
        if (direccion.trim().isEmpty()) {

            fp_direccion.setError("ingrese una dirección valida")
            fp_direccion.requestFocus()
            return
        }


        val sdf = SimpleDateFormat("yyyy-MM-dd")


        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val date = sdf.parse("$currentYear-$currentMonth-$currentDay")

        val nday = fp_datePicker.dayOfMonth
        val nmonth = fp_datePicker.month
        val nyear = fp_datePicker.year

        val ndate = sdf.parse("$nyear-$nmonth-$nday")


        val nacimiento = "$nyear-${monthToString(nmonth)}-$nday"


        if (!date.after(ndate) && date != ndate) {
            Toast.makeText(this, "La fecha de nacimiento no es valida ", Toast.LENGTH_LONG).show()
            return
        }


        val tmpp = provincia.split("-")
        val tmpc = ciudad.split("-")
        val tmps = sector.split("-")

        Toast.makeText(this, "Actualizando...", Toast.LENGTH_SHORT).show()

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
                Toast.makeText(this@DataUserActivity, "Error intente intente nuevamente", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<DefaultResponse>?, response: Response<DefaultResponse>?) {
                val res = response?.body()
                if (res != null) {
                    Toast.makeText(this@DataUserActivity, res.msg, Toast.LENGTH_SHORT).show()
                    if (res.status == 200) {

                        val editor = preferences!!.edit()
                        editor.putString(Constants.USER_ID, user_id)
                        editor.putString(Constants.USER_NAME, nombres)
                        editor.putString(Constants.USER_LAST_NAME, apellidos)
                        editor.putString(Constants.USER_EMAIL, email)
                        editor.putString(Constants.USER_NUMBRE_IDENTIFICATION, cedula)
                        editor.putString(Constants.USER_API_TOKEN, api_token)
                        editor.putBoolean(Constants.DATA_READY, true)
                        editor.apply()

                        val intent = Intent(this@DataUserActivity, DashboardActivity::class.java)
                        intent.putExtra(DashboardActivity.USER_ID, user_id)
                        intent.putExtra(DashboardActivity.EST_NAME, nombre_establecimiento)
                        intent.putExtra(DashboardActivity.EST_LOGO, est_icono)
                        finishAffinity()
                        startActivity(intent)

                    }
                }
            }

        })

    }


}
