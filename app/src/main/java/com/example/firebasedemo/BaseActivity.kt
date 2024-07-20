package com.example.firebasedemo

import android.app.Activity
import android.app.Dialog  //clase para crear dialogos personalisados
import android.os.Bundle
import android.widget.Toast //clase para mostrar mensajes cortos en la pantalla
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

open class BaseActivity : AppCompatActivity() {

    private lateinit var pb:Dialog //nuevo codigo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_base)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    //nuevo codigo - funcion para mostrar la barra de progreso
    fun showProgressBar (){
        pb = Dialog(this)
        pb.setContentView(R.layout.progress_bar)
        pb.setCancelable(false)
        pb.show()
    }

    //nuevo codigo - funcion para ocultar la barra de progreso
    fun hideProgressBar (){
        pb.hide()
    }

    //Toast = al español es "Mensaje emergente"
    fun showToast(activity:Activity, msg:String){
        Toast.makeText(activity,msg, Toast.LENGTH_SHORT).show()
    }
}