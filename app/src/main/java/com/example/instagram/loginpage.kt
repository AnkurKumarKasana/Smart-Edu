package com.example.instagram

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.instagram.Models.usermodel
import com.example.instagram.databinding.ActivityLoginpageBinding
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class loginpage : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginpageBinding.inflate(layoutInflater)
    }

    private lateinit var user: usermodel
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val text = "<font color=#ff000000>Already have an account?</font> <font color=#1E88E5>Login</font>"
        binding.textView.text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)

        firebaseAuth = FirebaseAuth.getInstance()
        user = usermodel()

        // 🌐 Google Sign-In Options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Replace this with your real web client ID from google-services.json
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // 🟩 Email/Password Sign-Up
        binding.SignUpBtn.setOnClickListener {
            if (binding.name.text?.toString().isNullOrEmpty() ||
                binding.email.text?.toString().isNullOrEmpty() ||
                binding.password.text?.toString().isNullOrEmpty()
            ) {
                Toast.makeText(this@loginpage, "Please fill all the above information", Toast.LENGTH_SHORT).show()
            } else {
                firebaseAuth.createUserWithEmailAndPassword(
                    binding.email.text.toString(),
                    binding.password.text.toString()
                ).addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        user.name = binding.name.text.toString()
                        user.email = binding.email.text.toString()
                        user.password = binding.password.text.toString()

                        Firebase.firestore.collection("User")
                            .document(firebaseAuth.currentUser!!.uid)
                            .set(user)
                            .addOnSuccessListener {
                                startActivity(Intent(this@loginpage, HomeActivity::class.java))
                                finish()
                            }
                    } else {
                        Toast.makeText(this@loginpage, result.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // 🔐 Google Sign-In button click
        binding.Googlebtn.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        // 📲 Go to Login Page
        binding.login.setOnClickListener {
            startActivity(Intent(this@loginpage, AlreadyUserLogin::class.java))
            finish()
        }
    }

    // ✅ Handle Google Sign-In Result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show()
                Log.e("GoogleSignIn", "signInResult:failed code=${e.statusCode}")
            }
        }
    }


    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val firebaseUser = firebaseAuth.currentUser
                    val newUser = usermodel(
                        name = firebaseUser?.displayName ?: "",
                        email = firebaseUser?.email ?: "",
                        password = "GoogleAuth"
                    )
                    Firebase.firestore.collection("User")
                        .document(firebaseUser!!.uid)
                        .set(newUser)
                        .addOnSuccessListener {
                            startActivity(Intent(this@loginpage, HomeActivity::class.java))
                            finish()
                        }
                } else {
                    Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
