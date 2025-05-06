package com.example.instagram

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.instagram.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        binding.navView.setupWithNavController(navController)

        // Optional: listen for new notifications
        listenForNewNotifications()
    }

    // Show badge with count
    fun showNotificationBadge(count: Int) {
        val badge = binding.navView.getOrCreateBadge(R.id.navigation_notifications)
        badge.isVisible = true
        badge.number = count
    }

    // Hide badge
    fun resetNotificationBadge() {
        val badge = binding.navView.getOrCreateBadge(R.id.navigation_notifications)
        badge.isVisible = false
        badge.clearNumber()
    }

    // Optional: Listen for new notifications to update badge count
    private fun listenForNewNotifications() {
        val currentUserId = auth.currentUser?.uid ?: return

        firestore.collection("Notifications")
            .whereEqualTo("userId", currentUserId)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null && !snapshot.isEmpty) {
                    val unreadCount = snapshot.size()
                    showNotificationBadge(unreadCount)
                }
            }
    }
}
