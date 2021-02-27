package com.example.letterboxdapp

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*


class Home : AppCompatActivity() {

    val CAPTURA = 1
    val PICK = 2

    private val basededatos = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val myWebView: WebView = findViewById(R.id.paginaweb)
        myWebView.loadUrl("https://letterboxd.com/thisisdrew/list/they-shoot-pictures-dont-they-1000-greatest-1/")
        val myWebView2: WebView = findViewById(R.id.paginaweb2)
        myWebView.loadUrl("https://letterboxd.com/lists/")

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
                     "pelicula favorita" to peliculafavorita.text.toString()
                )
            )
        }

        btshowInfo.setOnClickListener {
            basededatos.collection("users").document(email.toString()).get().addOnSuccessListener {
                tNombre.setText(it.get("name") as String?)
                peliculafavorita.setText(it.get("pelicula favorita")as String?)

            }
        }

    }

    //------------------------------------LogOut-------------------------------------------
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

    //------------------------------------para la foto de perfil-------------------------------------------
    private fun userPhoto() {
        bCamara.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(intent, CAPTURA)
            } catch (e: ActivityNotFoundException) {
                e.message
            }
        }

        bGaleria.setOnClickListener {
            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            startActivityForResult(galleryIntent, PICK)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURA && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            vIcono.setImageBitmap(imageBitmap)
        }

        if (requestCode == PICK && resultCode == RESULT_OK) {
            vIcono.setImageURI(data?.data)
        }
    }


}