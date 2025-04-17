package com.example.instagram

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PdfListActivity : AppCompatActivity() {

    private val pdfLinks = listOf(
        "https://drive.google.com/uc?export=download&id=1e_YdOeEzYi6B-kAk9asIQJsftR3_mJi8",
        "https://drive.google.com/uc?export=download&id=1cP_NbGTZziU4_43R99m9aPujo4wzimnq",
        "https://drive.google.com/uc?export=download&id=10GV24ilPkOg_673UA0k5M-78exKlzWsj",
        "https://drive.google.com/uc?export=download&id=1LxzUNL48C7UB1aB644ul3F2r1ecCdmVi"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 20, 20, 20)
        }

        setContentView(layout)

        pdfLinks.forEachIndexed { index, link ->
            val textView = TextView(this).apply {
                text = "Lecture ${index + 1}"
                textSize = 18f
                setPadding(0, 24, 0, 24)
                setOnClickListener {
                    val intent = Intent(this@PdfListActivity, PythonPdfActivity::class.java)
                    intent.putExtra("pdfUrl", link)
                    startActivity(intent)
                }
            }
            layout.addView(textView)
        }
    }
}
