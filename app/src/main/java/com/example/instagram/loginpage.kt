package com.example.instagram

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.instagram.Models.usermodel
import com.example.instagram.databinding.ActivityLoginpageBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore


class loginpage : AppCompatActivity() {
    val binding by lazy {
        ActivityLoginpageBinding.inflate(layoutInflater)
    }
    private lateinit var user: usermodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val text = "<font color=#ff000000>Already have an account?</font> <font color=#1E88E5>Login</font>"
        binding.textView.text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)

        user=usermodel()
        binding.SignUpBtn.setOnClickListener {
            if(binding.name.text?.toString().isNullOrEmpty() ||
                binding.email.text?.toString().isNullOrEmpty() ||
                binding.password.text?.toString().isNullOrEmpty()) {
                Toast.makeText(this@loginpage, "Please fill all the above information", Toast.LENGTH_SHORT).show()
            } else {
                // Continue with signup logic
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.email.text?.toString() ?: "",
                    binding.password.text?.toString() ?: ""
                ).addOnCompleteListener {
                        result->
                        if(result.isSuccessful){
                            user.name = binding.name.text?.toString() ?: ""
                            user.password = binding.password.text?.toString() ?: ""
                            user.email = binding.email.text?.toString() ?: ""

                            Firebase.firestore.collection("User").document(Firebase.auth.currentUser!!.uid).set(user).addOnSuccessListener {
                                startActivity(Intent(this@loginpage,HomeActivity::class.java))
                                finish()
                            }
                        }else{
                            Toast.makeText(this@loginpage, result.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                }

            }
        }
        binding.login.setOnClickListener {
            startActivity(Intent(this@loginpage,AlreadyUserLogin::class.java))
            finish()
        }
    }
}