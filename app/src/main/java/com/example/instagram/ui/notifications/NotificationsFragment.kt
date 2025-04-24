package com.example.instagram.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.instagram.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NotificationsFragment : Fragment() {

    private lateinit var listView: ListView
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val messages = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)
        listView = view.findViewById(R.id.notificationListView)

        fetchUserNotifications()
        fetchDoubtReplies()

        return view
    }

    private fun fetchUserNotifications() {
        val currentUserEmail = auth.currentUser?.email ?: return

        db.collection("Enrollments")
            .whereEqualTo("userEmail", currentUserEmail)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val courseName = document.getString("course")
                    // Add to bottom
                    messages.add("✅ You are enrolled in $courseName")
                }
                updateListView()
            }
    }

    private fun fetchDoubtReplies() {
        val currentUserId = auth.currentUser?.uid ?: return

        db.collection("doubts")
            .whereEqualTo("userId", currentUserId)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val reply = document.getString("reply")
                    val course = document.getString("course")
                    if (!reply.isNullOrBlank()) {
                        // Add replies to top
                        messages.add(0, "📢 Reply from Ankur Gurjar $course: $reply")
                    }
                }
                updateListView()
            }
    }

    private fun updateListView() {
        // Prevent crash if fragment is not attached
        if (!isAdded || context == null) return

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            messages
        )
        listView.adapter = adapter
    }
}
