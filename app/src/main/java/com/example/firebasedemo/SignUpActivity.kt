package com.example.firebasedemo

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebasedemo.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : BaseActivity() {

    private var binding: ActivitySignUpBinding? = null  //codigo aumentado

    private lateinit var auth:FirebaseAuth //codigo de firebase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater) //codigo aumentado

        enableEdgeToEdge()

        setContentView(binding?.root)

        // variable auth
        auth = Firebase.auth

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //codigo aumentado - boton para regresar al login
        binding?.tvLoginPage?.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        //codigo aumentado - boton para regitrar mi nuevo usuario
        binding?.btnSignUp?.setOnClickListener { registerUser() }
    }

    //funcion para registrar al nuevo usuario
    private fun registerUser(){
        val name = binding?.etSinUpName?.text.toString()
        val email = binding?.etSinUpEmail?.text.toString()
        val password = binding?.etSinUpPassword?.text.toString()
        if (validateForm(name, email, password)) {
            showProgressBar()

            //crear nuevo usuario y registrarlo en firebase
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {task ->
                    if (task.isSuccessful)
                    {
                        //notificacion breve de que se creo el usuario
                        showToast(this, "Usuario creado exitosamente")
                        hideProgressBar()

                        //envia al usuario a la ventana de juego principal
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    } else {
                        showToast(this,"No se pudo crear el usuario. Vuelva a intentarlo mas tarde")
                        hideProgressBar()
                    }
                }
        }
    }

    fun validateForm(name:String, email:String, password:String):Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                binding?.tilName?.error = "Ingresar el nombre"
                false
            }
            //verifica si el elemento esta vacio y si concide con el patron de una email valida
            TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding?.tilEmail?.error = "Ingresar un correo electronico valido"
                false
            }
            TextUtils.isEmpty(password) -> {
                binding?.tilPassword?.error = "Ingresar la contraseña"
                false
            }
            else -> {true}
        }
    }
}
