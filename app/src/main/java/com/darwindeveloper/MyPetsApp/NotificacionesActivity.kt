package com.darwindeveloper.MyPetsApp

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.darwindeveloper.MyPetsApp.fragments.NotificationsFragment

import kotlinx.android.synthetic.main.activity_notificaciones.*

class NotificacionesActivity : AppCompatActivity() {


    companion object {
        const val TIPO = "na.tipo"
    }

    var tipo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notificaciones)
        setSupportActionBar(toolbar)

        tipo = intent.getStringExtra(TIPO)

        if (tipo == "none") {
            supportActionBar!!.setTitle("Eventos")
        } else {
            supportActionBar!!.setTitle(tipo)
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        val b = Bundle()
        b.putString(NotificationsFragment.TIPO, tipo)
        val fr = NotificationsFragment.newInstance(b)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.an_fragment_container, fr)
        transaction.commit()

    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item!!.itemId


        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }
        }


        return super.onOptionsItemSelected(item)
    }

}
