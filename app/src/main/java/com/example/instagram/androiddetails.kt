package com.example.instagram

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
class androiddetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_androiddetails)
        val doubt= findViewById<LinearLayout>(R.id.andrBtn)
        doubt.setOnClickListener{
                val intent = Intent(this, pythondoubt::class.java)
                startActivity(intent)
        }
    }
}
