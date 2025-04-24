package com.example.instagram.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.instagram.*
import com.example.instagram.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        startTypingAnimation(" ") {
            fetchAndUpdateUserName()
        }

        // Course buttons
        binding.pythonbtn.setOnClickListener {
            enrollCourse("Python")
            startActivity(Intent(requireContext(), pythondetails::class.java))
        }

        binding.javabtn.setOnClickListener {
            enrollCourse("Java")
            startActivity(Intent(requireContext(), javadetails::class.java))
        }

        binding.cppbtn.setOnClickListener {
            enrollCourse("C++")
            startActivity(Intent(requireContext(), cppdetails::class.java))
        }

        binding.androidbtn.setOnClickListener {
            enrollCourse("Android")
            startActivity(Intent(requireContext(), androiddetails::class.java))
        }

        binding.aiAssistantButton.setOnClickListener {
            startActivity(Intent(requireContext(), AIAssistantActivity::class.java))
        }

        binding.logoutIcon.setOnClickListener {
            showLogoutConfirmation()
        }

        return binding.root
    }

    private fun enrollCourse(courseName: String) {
        val uid = auth.currentUser?.uid ?: return
        val userEmail = auth.currentUser?.email ?: "unknown@example.com"
        val userRef = db.collection("User").document(uid)

// Add course to user's enrolledCourses array
        userRef.update("enrolledCourses", FieldValue.arrayUnion(courseName))
            .addOnSuccessListener {
                sendNotification(courseName)

                // Save individual enrollment to "Enrollments" collection
                val enrollmentData = hashMapOf(
                    "course" to courseName,
                    "userEmail" to userEmail,
                    "userId" to uid,
                    "timestamp" to FieldValue.serverTimestamp()
                )
                db.collection("Enrollments").add(enrollmentData)
            }
    }

    private fun sendNotification(courseName: String) {
        val notification = hashMapOf(
            "message" to "You are now enrolled in $courseName",
            "timestamp" to FieldValue.serverTimestamp()
        )
        db.collection("Notifications").add(notification)
    }


    private fun fetchAndUpdateUserName() {
        val uid = auth.currentUser?.uid ?: return

        db.collection("User").document(uid).get()
            .addOnSuccessListener { document ->
                val name = document.getString("name")
                if (!name.isNullOrEmpty()) {
                    // Check if binding is still valid
                    _binding?.let {
                        startTypingAnimation("Welcome, $name")
                    }
                }
            }
    }

    private fun startTypingAnimation(
        fullText: String,
        onComplete: (() -> Unit)? = null
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            val delayMillis = 50L
            var index = 0
            while (index <= fullText.length && _binding != null) {
                _binding?.welcomeText?.text = fullText.substring(0, index)
                index++
                delay(delayMillis)
            }
            onComplete?.invoke()
        }
    }

    private fun showLogoutConfirmation() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_logout, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel)
        val btnLogout = dialogView.findViewById<Button>(R.id.btn_logout)

        btnCancel.setOnClickListener { dialog.dismiss() }
        btnLogout.setOnClickListener {
            auth.signOut()
            dialog.dismiss()
            val intent = Intent(requireContext(), loginpage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
