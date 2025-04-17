package com.example.instagram

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PythonVideosActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressText: TextView

    private lateinit var firestore: FirebaseFirestore
    private lateinit var userId: String

    private var watchedCount = 0
    private val videoUrls = listOf(
        "https://res.cloudinary.com/dg09dxwlv/video/upload/v1744879749/Introduction_to_Python_Course___Python_for_beginners_tpuw1f.mp4",
        "https://res.cloudinary.com/dg09dxwlv/video/upload/v1744884277/Writing_First_Python_Program___Printing_to_Console_in_Python___Python_Tutorials_for_Beginners_lec4_xwavhm.mp4",
        "https://res.cloudinary.com/dg09dxwlv/video/upload/v1744884840/Introduction_to_Python_Programming___Python_for_Beginners_lec1_k4la8z.mp4"
    )

    private val descriptions = listOf(
        "Introduction to Python",
        "Variables and Data Types",
        "Control Structures"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_python_videos)

        progressBar = findViewById(R.id.progressBar)
        progressText = findViewById(R.id.progressPercentage)
        recyclerView = findViewById(R.id.videoRecyclerView)

        firestore = FirebaseFirestore.getInstance()
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Step 1: Load progress from Firestore
        firestore.collection("Users").document(userId).get().addOnSuccessListener { doc ->
            watchedCount = doc.getLong("pythonVideosProgress")?.toInt() ?: 0
            updateProgress()

            // Step 2: Set up adapter with saved watchedCount
            val adapter = VideoAdapter(videoUrls, descriptions, watchedCount) {
                watchedCount++
                updateProgress()
                saveProgress()
            }
            recyclerView.adapter = adapter
        }
    }

    private fun updateProgress() {
        val progress = (watchedCount.toFloat() / videoUrls.size * 100).toInt()
        progressBar.progress = progress
        progressText.text = "$progress%"
    }

    private fun saveProgress() {
        val userDoc = firestore.collection("Users").document(userId)
        userDoc.update("pythonVideosProgress", watchedCount)
            .addOnFailureListener {
                userDoc.set(mapOf("pythonVideosProgress" to watchedCount)) // if user doc doesn't exist
            }
    }
}
