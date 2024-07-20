package com.example.firebasedemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebasedemo.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : BaseActivity() {

    private var binding: ActivitySignInBinding? = null //codigo aumentado
    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater) //codigo aumentado
        enableEdgeToEdge()

        setContentView(binding?.root) //codigo modificado
        auth = Firebase.auth

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //valor "opcion de inicio de sesion de google" que va guardar una instancia
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)

        //codigo aumentado - me envia a la ventana de registro
        binding?.tvRegister?.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }

        //codigo aumentado - me envia a la ventana de recuperacion de contraseña
        binding?.tvForgotPassword?.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }

        //me envia a la ventana principal del juego
        binding?.btnSignIn?.setOnClickListener {
            userLogin()
        }

        //inicia sesion con google
        binding?.btnSignInWithGoogle?.setOnClickListener{
                signInWithGoogle()
        }
    }

    //funcion para ingresar corroborando que el usuario existe en firebase
    private fun userLogin(){
        val email = binding?.etSinInEmail?.text.toString()
        val password = binding?.etSinInPassword?.text.toString()
        if (validateForm(email,password)) {

            showProgressBar()
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){task ->
                    if (task.isSuccessful)
                    {
                     startActivity(Intent(this,MainActivity::class.java))
                        finish()
                        hideProgressBar()
                    }
                    else {
                        showToast(this,"No se pudo iniciar sesion, intentelo mas tarde")
                        hideProgressBar()
                    }
                }
        }
    }

    //funcion para registrarse con google
    private fun signInWithGoogle () {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    //nuevo valor
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {result ->
        if (result.resultCode == Activity.RESULT_OK)
        {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    //funcion para manejar resultados
    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful)
        {
            val account:GoogleSignInAccount? = task.result
            if (account!= null){
                updateUI(account)
            }
        }
        else
        {
            showToast(this, "Fallo en el inicio de sesion. Vuelva a intentarlo mas tarde")
        }
    }

    private fun updateUI (account: GoogleSignInAccount){
        showProgressBar()
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful)
            {
                startActivity(Intent(this, MainActivity::class.java))
                hideProgressBar()
                finish()
            }
            else
            {
                showToast(this, "No se puedo iniciar sesion. Intentelo mas tarde")
                hideProgressBar()
            }
        }
    }

    //funcion para validar el formulario
    private fun validateForm(email:String,password:String):Boolean {
        return when {
            TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding?.tilEmail?.error = "Ingresar un correo valido"
                false
            }
            TextUtils.isEmpty(password) -> {
                binding?.tilPassword?.error = "Ingresar contraseña"
                false
            }
            else -> { true }
        }
    }
}