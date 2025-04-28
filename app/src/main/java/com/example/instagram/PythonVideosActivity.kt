package com.example.instagram

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

class PythonVideosActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressText: TextView

    private lateinit var firestore: FirebaseFirestore
    private lateinit var database: DatabaseReference
    private lateinit var userId: String

    private var watchedCount = 0
    private val videoList = mutableListOf<VideoItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_python_videos)

        progressBar = findViewById(R.id.progressBar)
        progressText = findViewById(R.id.progressPercentage)
        recyclerView = findViewById(R.id.videoRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        firestore = FirebaseFirestore.getInstance()
        database = FirebaseDatabase.getInstance().reference
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Step 1: Get progress
        firestore.collection("Users").document(userId).get().addOnSuccessListener { doc ->
            watchedCount = doc.getLong("pythonVideosProgress")?.toInt() ?: 0

            // Step 2: Get videos from Realtime Database
            database.child("videos").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (child in snapshot.children) {
                        val video = child.getValue(VideoItem::class.java)
                        video?.let { videoList.add(it) }
                    }

                    updateProgress()

                    val adapter = VideoAdapter(
                        this@PythonVideosActivity,
                        videoList,
                        watchedCount
                    ) {
                        watchedCount++
                        updateProgress()
                        saveProgress()
                    }
                    recyclerView.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    private fun updateProgress() {
        val progress = if (videoList.isNotEmpty())
            (watchedCount.toFloat() / videoList.size * 100).toInt()
        else 0
        progressBar.progress = progress
        progressText.text = "$progress%"
    }

    private fun saveProgress() {
        val userDoc = firestore.collection("Users").document(userId)
        userDoc.update("pythonVideosProgress", watchedCount)
            .addOnFailureListener {
                userDoc.set(mapOf("pythonVideosProgress" to watchedCount))
            }
    }
}