package com.example.firebasedemo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebasedemo.databinding.ActivityGetStartedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class GetStartedActivity : AppCompatActivity() {
    private var binding:ActivityGetStartedBinding? = null  //nuevo codigo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGetStartedBinding.inflate(layoutInflater) //nuevo codigo
        enableEdgeToEdge()

        setContentView(binding?.root) //nuevo codigo

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //nuevo codigo
        binding?.cvGetStarted?.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
            finish()
        }

        val auth = Firebase.auth
        //condicion en caso de que el usuario ya tenga la sesion iniciada
        if (auth.currentUser!= null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}