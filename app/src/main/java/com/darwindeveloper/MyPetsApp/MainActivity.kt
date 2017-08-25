package com.darwindeveloper.MyPetsApp


import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.darwindeveloper.MyPetsApp.fragments.LoginFragment
import com.darwindeveloper.MyPetsApp.fragments.PasswordResetFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val transaction = supportFragmentManager.beginTransaction()

        when (item.itemId) {
            R.id.navigation_login -> {
                transaction.replace(R.id.fragment_container, LoginFragment())
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_password -> {
                transaction.replace(R.id.fragment_container, PasswordResetFragment())
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_help -> {
                transaction.replace(R.id.fragment_container, LoginFragment())
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }
        }



        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, LoginFragment())
        transaction.commit()


    }
}
