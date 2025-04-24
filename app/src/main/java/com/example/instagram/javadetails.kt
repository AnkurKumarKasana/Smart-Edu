package com.example.instagram

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class javadetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_javadetails)

        val doubt= findViewById<LinearLayout>(R.id.JavaDoubtBtn)

        doubt.setOnClickListener {
            val intent = Intent(this, pythondoubt::class.java)
            startActivity(intent)
        }
    }
}
