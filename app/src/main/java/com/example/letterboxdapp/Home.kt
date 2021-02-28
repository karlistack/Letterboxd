package com.example.letterboxdapp

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*


class Home : AppCompatActivity() {

    val captura = 1
    val elige = 2

    private val basededatos = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

       /* val myWebView: WebView = findViewById(R.id.progreso)
        myWebView.loadUrl("https://letterboxd.com/director/luis-bunuel/") */

        val bundle = intent.extras
        val email = bundle?.getString("email")
        val contraseña = bundle?.getString("contraseña")
        logout()

        val prefs =
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("contraseña", contraseña)
        prefs.apply()

        userPhoto()


        btInfo.setOnClickListener {

            basededatos.collection("users").document(email.toString()).set(
                hashMapOf(
                    "nombre" to tNombre.text.toString(),
                     "peliculafavorita" to peliculafavorita.text.toString()
                )
            )
        }

        btshowInfo.setOnClickListener {
            basededatos.collection("users").document(email.toString()).get().addOnSuccessListener {
                tNombre.setText(it.get("nombre") as String?)
                peliculafavorita.setText(it.get("pelicula favorita")as String?)

            }
        }

    }

    private fun logout() {

        bSalir.setOnClickListener {

            val prefs =
                getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()

            prefs.clear()
            prefs.apply()
            FirebaseAuth.getInstance().signOut()
            onBackPressed()

        }
    }

    private fun userPhoto() {
        bCamara.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(intent, captura)
            } catch (e: ActivityNotFoundException) {
                e.message
            }
        }

        bGaleria.setOnClickListener {
            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            startActivityForResult(galleryIntent, elige)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == captura && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            vIcono.setImageBitmap(imageBitmap)
        }

        if (requestCode == elige && resultCode == RESULT_OK) {
            vIcono.setImageURI(data?.data)
        }
    }


}