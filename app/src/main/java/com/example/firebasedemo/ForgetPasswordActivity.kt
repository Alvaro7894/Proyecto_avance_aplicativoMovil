package com.example.firebasedemo

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebasedemo.databinding.ActivityForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgetPasswordActivity : BaseActivity() {

    private var binding:ActivityForgetPasswordBinding? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        auth = Firebase.auth

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding?.btnForgotPasswordSubmit?.setOnClickListener { resetPassword()}
    }

    //funcion para reiniciar la constraseña
    private fun resetPassword() {
        val email = binding?.etForgotPasswordEmail?.text.toString()

        if (validateEmail(email)){
            showProgressBar()
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful)
                    {
                        hideProgressBar()
                        binding?.tilEmailForgetPassword?.visibility = View.GONE
                        binding?.tvSubmit?.visibility = View.VISIBLE
                        binding?.btnForgotPasswordSubmit?.visibility = View.GONE
                    }
                    else
                    {
                        hideProgressBar()
                        showToast(this, "Nose puedo reiniciar tu contraseña. Intentelo mas tarde")
                    }
                }
        }
    }
    // revisar codigo
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    //funcion para validar el formulario
    private fun validateEmail(email:String):Boolean {
        return if (TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding?.tilEmailForgetPassword?.error = "Ingresar un correo valido"
                false
            }
            else {
                binding?.tilEmailForgetPassword?.error = null
                true
            }
    }
}