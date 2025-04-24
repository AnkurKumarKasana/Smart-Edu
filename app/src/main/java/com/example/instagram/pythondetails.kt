package com.example.instagram
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity


class pythondetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pythondetails)

        val videoBtn = findViewById<LinearLayout>(R.id.pythonvideobtn)
        val pdfBtn = findViewById<LinearLayout>(R.id.pythonpdfbtn) // You must add this id in XML
        val doubt=findViewById<LinearLayout>(R.id.pythdoubtbtn)
        videoBtn.setOnClickListener {
            val intent = Intent(this, PythonVideosActivity::class.java)
            startActivity(intent)
        }

        pdfBtn.setOnClickListener {
            val intent = Intent(this, PythonPdfActivity::class.java)
            startActivity(intent)
        }
        doubt.setOnClickListener {
            val intent = Intent(this, pythondoubt::class.java)
            startActivity(intent)
        }
    }
}
