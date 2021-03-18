package com.example.letterboxdapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class Ingresar : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val analyics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle=Bundle()
        analyics.logEvent("inicio", bundle)
        googleLogin()
        facebookLogin()
        register()
        login()

    }

    private fun register() {
        bRegistrar.setOnClickListener {
            if (temail.text.isNotEmpty() && tcoñtraseña.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(temail.text.toString(), tcoñtraseña.text.toString()).addOnCompleteListener {
                        if (it.isSuccessful) {
                            session()
                            startActivity(Intent(this, Home::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "error en el registro",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(applicationContext, "No dejes campos vacios", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun login() {
        bIngresar.setOnClickListener {
            if (temail.text.isNotEmpty() && tcoñtraseña.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(temail.text.toString(), tcoñtraseña.text.toString()).addOnCompleteListener {
                        if (it.isSuccessful) {
                            session()
                            startActivity(Intent(this, Home::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "prueba otra vez",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(applicationContext, "No dejes campos vacios", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun session(): Boolean {
        val prefs: SharedPreferences =
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val contraseña = prefs.getString("contraseña", null)
        if (email != null && contraseña != null) {
            startActivity(Intent(this, Home::class.java))
            finish()

        }
        return true
    }


    fun googleLogin() {

        val providers = arrayListOf(

            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        bGmail.setOnClickListener {
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setIsSmartLockEnabled(false)
                    .build(),
                RC_SIGN_IN
            )
        }


    }

    fun facebookLogin() {

        val providers = arrayListOf(

            AuthUI.IdpConfig.FacebookBuilder().build()
        )

        bFacebook.setOnClickListener {
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setIsSmartLockEnabled(false)
                    .build(),
                RC_SIGN_IN
            )


        }
    }


    }

