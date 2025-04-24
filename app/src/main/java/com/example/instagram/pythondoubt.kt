package com.example.instagram

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.instagram.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class pythondoubt : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pythondoubt)
        val btnSubmitDoubt=findViewById<Button>(R.id.btnSubmitDoubt)

        btnSubmitDoubt.setOnClickListener {
            val doubtText = findViewById<EditText>(R.id.doubtInput).text.toString().trim()
            val courseName = intent.getStringExtra("courseName") ?: "Unknown"

            if (doubtText.isEmpty()) {
                Toast.makeText(this, "Please enter your doubt", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val doubtData = hashMapOf(
                "userId" to FirebaseAuth.getInstance().currentUser?.uid,
                "userEmail" to FirebaseAuth.getInstance().currentUser?.email,
                "course" to courseName,
                "doubt" to doubtText,
                "reply" to "", // empty initially
                "timestamp" to FieldValue.serverTimestamp()
            )

            FirebaseFirestore.getInstance().collection("doubts")
                .add(doubtData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Doubt submitted ✅", Toast.LENGTH_SHORT).show()
                    findViewById<EditText>(R.id.doubtInput).text.clear()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to submit doubt ❌", Toast.LENGTH_SHORT).show()
                }
        }

    }
    }


