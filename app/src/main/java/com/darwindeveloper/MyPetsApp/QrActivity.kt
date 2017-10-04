package com.darwindeveloper.MyPetsApp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.View
import com.darwindeveloper.MyPetsApp.fragments.QrFragment
import kotlinx.android.synthetic.main.activity_qr.*

class QrActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)

        val tr = supportFragmentManager.beginTransaction()
        tr.replace(R.id.aqr_fragment_container, QrFragment())
        tr.commit()
    }

    public fun back(v: View) {
        startActivity(Intent(this, SelectLoginActivity::class.java))
    }
}
