package com.example.letterboxdapp

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    val authUser: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        Thread.sleep(2000)
        setTheme(R.style.SplashTheme)

        goHome()
    }


    fun goHome() {
        if (authUser.currentUser != null) {
            startActivity(Intent(this, Home::class.java)).apply {

            }
            finish()
        } else {
            startActivity(Intent(this, Ingresar::class.java))
            finish()
        }
    }
}