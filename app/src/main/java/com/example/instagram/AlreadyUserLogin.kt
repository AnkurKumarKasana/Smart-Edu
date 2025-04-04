package com.example.instagram

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.instagram.Models.usermodel
import com.example.instagram.databinding.ActivityAlreadyUserLoginBinding
import com.google.firebase.auth.FirebaseAuth

class AlreadyUserLogin : AppCompatActivity() {

    private lateinit var binding: ActivityAlreadyUserLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlreadyUserLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.presslogin.setOnClickListener {
            val email = binding.emailEditText.text?.toString()?.trim()
            val password = binding.passwordEditText.text?.toString()?.trim()

            if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            } else {
                val user = usermodel(email, password)

                auth.signInWithEmailAndPassword(user.email!!, user.password!!)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        // 👇 Handle Create an Account click
        binding.CreateAnAccount.setOnClickListener {
            startActivity(Intent(this, loginpage::class.java))
            finish()
        }
    }
}
