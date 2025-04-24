package com.example.instagram

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class cppdetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cppdetails)
        val doubt=findViewById<LinearLayout>(R.id.cppbtn)
        doubt.setOnClickListener{
            val intent = Intent(this, pythondoubt::class.java)
            startActivity(intent)
        }


        // TODO: Add Android course-specific logic here, e.g., button listeners for PDFs, videos, queries, etc.
    }
}
