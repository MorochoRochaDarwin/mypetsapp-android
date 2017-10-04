package com.darwindeveloper.MyPetsApp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View

class SelectLoginActivity : AppCompatActivity() {

    private val REQUEST_CAMERA_PERMISSION = 1002
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_login)
    }


    override fun onResume() {
        super.onResume()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            if (result2 != PackageManager.PERMISSION_GRANTED) {
                val per = arrayOf(Manifest.permission.CAMERA)
                requestPermissions(per, REQUEST_CAMERA_PERMISSION)
                return
            }

        }


    }

    public fun startQR(v: View) {
        startActivity(Intent(this,QrActivity::class.java))
    }
    public fun startNOQR(v: View) {
        startActivity(Intent(this,MainActivity::class.java))
    }
}
