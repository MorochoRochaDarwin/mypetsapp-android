package com.darwindeveloper.MyPetsApp

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.darwindeveloper.MyPetsApp.adapters.SitioAdapter
import com.darwindeveloper.MyPetsApp.api.WebApiClient
import com.darwindeveloper.MyPetsApp.api.WebService
import com.darwindeveloper.MyPetsApp.api.modelos.Ciudad
import com.darwindeveloper.MyPetsApp.api.modelos.Provincia
import com.darwindeveloper.MyPetsApp.api.modelos.Sector
import com.darwindeveloper.MyPetsApp.api.modelos.Sitio

import kotlinx.android.synthetic.main.activity_pet_friendly.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetFriendlyActivity : AppCompatActivity(), SitioAdapter.OnSitioListener {
    override fun onClickSitio(sitio: Sitio) {
        val i = Intent(this, SitiosActivity::class.java)
        i.putExtra(SitiosActivity.NOMBRE, sitio.nombre)
        i.putExtra(SitiosActivity.ICON, icon)
        i.putExtra(SitiosActivity.TIPO, sitio.tipo)
        i.putExtra(SitiosActivity.LAT, sitio.lat)
        i.putExtra(SitiosActivity.LNG, sitio.lng)
        i.putExtra(SitiosActivity.HTML, sitio.html)
        startActivity(i)
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


    var adpater: SitioAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_friendly)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)



        categoria = intent.getStringExtra(CATEGORIA)
        icon = intent.getIntExtra(ICON, R.drawable.ic_pizza)

        supportActionBar!!.setTitle("Pet Friendly $categoria")
        //supportActionBar.setIcon(icon)

        adapterProvinciaa = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tmp_provincias)
        adapterCiudades = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tmp_ciudades)
        adapterSectores = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tmp_sectores)
        apt_spinner_provincias.adapter = adapterProvinciaa
        apt_spinner_ciudades.adapter = adapterCiudades
        apt_spinner_sectores.adapter = adapterSectores

        adpater = SitioAdapter(this, sitios)
        adpater!!.onSitioListener = this
        apt_list.layoutManager = LinearLayoutManager(this)
        apt_list.adapter = adpater


        apt_spinner_provincias.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                posp = p2

                updateCiudades(p2)

            }

        })

        apt_spinner_ciudades.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                posc = p2
                updateSectores(p2)
            }

        })


        apt_spinner_sectores.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                poss = p2

                updateSitios(poss)

            }

        })





        load()


    }


    private fun updateCiudades(pos: Int) {
        ciudades.clear()
        if (provincias[pos].ciudades != null) {
            ciudades.addAll(provincias[pos].ciudades!!)


            if (ciudades.size > 0) {
                tmp_ciudades.clear()
                adapterCiudades!!.notifyDataSetChanged()
                for (i in ciudades.indices) {
                    tmp_ciudades.add(ciudades[i].ciudad_nombre!!)
                }
                adapterCiudades!!.notifyDataSetChanged()
                updateSectores(0)
            }

        }


    }


    private fun updateSectores(pos: Int) {
        sectores.clear()
        sectores.addAll(ciudades[pos].sectores!!)
        tmp_sectores.clear()
        adapterSectores!!.notifyDataSetChanged()
        tmp_sectores.add("todos los sectores")
        for (i in sectores.indices) {
            tmp_sectores.add(sectores[i].sector_nombre!!)
        }
        adapterSectores!!.notifyDataSetChanged()




        sitios.clear()
        adpater?.notifyDataSetChanged()
        updateSitios(0)


    }


    public fun updateSitios(poss: Int) {
        sitios.clear()
        adpater?.notifyDataSetChanged()

        apt_list.visibility = View.GONE
        apt_none.visibility = View.VISIBLE
        val tmp = ciudades[posc].sitios
        if (poss == 0) {
            if (tmp != null) {
                if (!tmp.isEmpty()) {
                    apt_none.visibility = View.GONE
                    apt_list.visibility = View.VISIBLE
                    for (i in tmp.indices) {
                        sitios.add(tmp[i])
                        adpater?.notifyItemInserted(sitios.size - 1)

                    }


                }

            } else {
                apt_list.visibility = View.GONE
                apt_none.visibility = View.VISIBLE
            }


        } else {
            val tmpposs = poss - 1
            if (tmp != null) {
                if (!tmp.isEmpty()) {

                    apt_none.visibility = View.GONE
                    apt_list.visibility = View.VISIBLE
                    val tmp_sector = sectores[tmpposs]



                    for (i in tmp.indices) {

                        if (tmp[i].sector_id == tmp_sector.sector_id) {

                            sitios.add(tmp[i])
                            adpater?.notifyItemInserted(sitios.size - 1)

                        }
                    }
                }

            } else {
                apt_list.visibility = View.GONE
                apt_none.visibility = View.VISIBLE
            }
        }

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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        when (id) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}
